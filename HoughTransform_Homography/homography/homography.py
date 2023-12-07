from skimage.feature import match_descriptors, SIFT
from skimage import transform
import numpy as np
import random

def matchPics(I1, I2):
    # Given two images I1 and I2, perform SIFT matching to find candidate match pairs

    ### YOUR CODE HERE
    ### You can use skimage or OpenCV to perform SIFT matching
    extractor = SIFT()
    extractor.detect_and_extract(I1)
    locs1 = extractor.keypoints
    desc1 = extractor.descriptors
    extractor.detect_and_extract(I2)
    locs2 = extractor.keypoints
    desc2 = extractor.descriptors
    matches = match_descriptors(desc1,desc2, max_ratio=0.6)

    ### END YOUR CODE
    
    return matches, locs1, locs2

def computeH_ransac(matches, locs1, locs2):

    # Compute the best fitting homography using RANSAC given a list of matching pairs
    
    ### YOUR CODE HERE
    ### You should implement this function using Numpy only
    iterations = 2000
    num_points = 4
    inliers = []
    bestH = None

    for iter in range(iterations):
        tmp_inliers = []
        rand_points = []

        # Get random matches
        selections = random.choices(range(matches.shape[0]), k=num_points)
        for i in selections:
            rand_points.append(matches[i])

        # Setup Matrix
        matrix = []
        for point in rand_points:
            y1 = locs1[point[0]][0]
            x1 = locs1[point[0]][1]
            y2 = locs2[point[1]][0]
            x2 = locs2[point[1]][1]

            matrix.append([x1, y1, 1, 0, 0, 0, x1*-x2, y1*-x2, -x2])
            matrix.append([0, 0, 0, x1, y1, 1, x1*-y2, y1*-y2, -y2])

        matrix = np.array(matrix)

        # SVD and reshape
        u, s, v = np.linalg.svd(matrix)
        homography = np.reshape(v[-1], (3, 3))

        # normalize
        homography = (1/homography.item(8))*homography

        # Compute inliers
        for idx, match in enumerate(matches):
            # SSD
            point1 = np.array([locs1[match[0]][0], locs1[match[0]][1], 1])
            point2 = np.array([locs2[match[1]][0], locs2[match[1]][1], 1])

            tmp = np.dot(homography, np.transpose(point1))
            point2_est = (1/tmp[2])*tmp

            ssd = np.linalg.norm(np.transpose(point2)-point2_est)
            
            # Add to inlier list if in range 
            if ssd < 300:
                tmp_inliers.append(idx)

        # check vs best inliers to date
        if len(tmp_inliers) > len(inliers):
            bestH = homography
            inliers = tmp_inliers
            if len(inliers) > len(matches)*0.65:
                break
    ### END YOUR CODE

    return bestH, inliers

def compositeH(H, template, img):

    # Create a compositie image after warping the template image on top
    # of the image using homography


    #Create mask of same size as template
    mask = np.zeros(template.shape)
    #Warp mask by appropriate homography
    warped_mask = transform.warp(mask, H)
    #Warp template by appropriate homography
    composite_img = warped_template = transform.warp(template, H)
    #Use mask to combine the warped template and the image
    # does not work
    # composite_img = np.multiply(warped_mask, img)
    return composite_img
