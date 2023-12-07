# import other necessary libaries
from utils import create_line, create_mask
from skimage import io, feature
import matplotlib.pyplot as plt
import numpy as np
import math

# load the input image
img = io.imread('road.jpg', as_gray=True)

# run Canny edge detector to find edge points
edges = feature.canny(img)

# create a mask for ROI by calling create_mask
mask = create_mask(img.shape[0], img.shape[1])

# extract edge points in ROI by multipling edge map with the mask
edge_points = np.multiply(mask, edges)

# perform Hough transform
degrees = 180
diag = math.ceil(math.sqrt(img.shape[0]**2 + img.shape[1]**2))
hough_acc = np.zeros((degrees, 2*diag))

(rows, cols) = edge_points.shape
for x in range(cols):
    for y in range(rows):
        if edge_points[y][x] != 0:
            for theta in range(degrees):
                rad = math.pi/180*theta
                projection = x*math.cos(rad) + y*math.sin(rad)
                hough_acc[theta][int(projection)+diag] += 1


# find the right lane by finding the peak in hough space
peak = np.unravel_index(hough_acc.argmax(), hough_acc.shape)
print(peak)

line = create_line(peak[1]-diag, math.pi/180*peak[0], img)

# zero out the values in accumulator around the neighborhood of the peak
nbhd_size = 45
x = -math.floor(nbhd_size/2)
y = -math.floor(nbhd_size/2)
for i in range(nbhd_size):
    for j in range(nbhd_size):       
        hough_acc[peak[0]+x+i][peak[1]+y+j] = 0

# find the left lane by finding the peak in hough space
local = feature.peak_local_max(hough_acc, min_distance=40)
newpeak = (local[0][0], local[0][1])
newline = create_line(newpeak[1]-diag, math.pi/180*newpeak[0], img)

# plot the results
plt.imshow(edges, cmap='gray')
plt.show()

plt.imshow(mask, cmap='gray')
plt.show()

plt.imshow(edge_points, cmap='gray')
plt.show()

plt.imshow(img, cmap='gray')
plt.scatter(line[0],line[1], s=2)
plt.scatter(newline[0],newline[1], s=2)
plt.show()

