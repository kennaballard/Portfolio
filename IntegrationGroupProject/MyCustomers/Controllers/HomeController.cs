using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MyCustomers.Data;
using MyCustomers.Models;

using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace MyCustomers.Controllers
{
    public class HomeController : Controller
    {
        private readonly CustomerContext _context;
        public HomeController(CustomerContext db)
        {
            _context = db;
        }

        protected override void Dispose(bool disposing)
        {
            _context.Dispose();
        }

        public IActionResult Index(string genre = "")
        {
            IEnumerable<Movie> movies = _context.Movies;

            if (genre != "")
            {
                movies = movies.Where(c => c.Genre == genre);
            }
            return View(movies);
        }

        public IActionResult Details(int id)
        {
            Movie movie = _context.Movies.SingleOrDefault(m => m.ID == id);
            if (movie == null)
            {
                return NotFound();
            }
            Tuple<Movie, IEnumerable<Review>> info = new Tuple<Movie, IEnumerable<Review>>(movie, _context.Reviews.Where(c => c.MovieId == id));
            return View(info);
        }

        public IActionResult About()
        {
            ViewData["Message"] = "Your application description page.";

            return View();
        }

        public IActionResult Contact()
        {
            ViewData["Message"] = "Your contact page.";

            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }

        // ADD A REVIEW AND RATING
        [HttpPost]
        public async Task<IActionResult> updateRating(int movie, int newRating, String review_txt)
        {        
            Movie mv = _context.Movies.SingleOrDefault(c => c.ID == movie);
          
            // Change rating info - rating total and the # of ratings
            double total = mv.Rating * mv.NumberOfRatings;
            mv.NumberOfRatings++;
            total += newRating;
            mv.Rating = total / mv.NumberOfRatings;

            // Save the changes
            _context.Reviews.Add(new Review() { Content = review_txt, MovieId=movie, Rating=newRating });
            _context.Movies.Update(mv);
            _context.SaveChanges();

            return RedirectToAction("Details", new { id = movie });
        }

        // RENT A MOVIE
        public IActionResult rentMovie(String user, int movie)
        {
            int userId = _context.Users.First(u => u.Username.Equals(user)).ID;
            _context.RentedMovies.Add(new RentedMovies() { CustomerID = userId, MovieID = movie });
            _context.SaveChanges();

            return RedirectToAction("Details", new { id = movie });
        }

        public IActionResult mostViewed()
        {
            // Get all Genres
            List<String> genres = _context.Movies.GroupBy(m => m.Genre).Select(grp => grp.First().Genre).ToList();
            var results = _context.RentedMovies.GroupBy(m => m.MovieID,
                               (key, g) => new { ID = key, Rentals = g.ToList() });

            var ids = results.OrderByDescending(r => r.Rentals.Count()).ToList();        
            List<Movie> top5 = new List<Movie>();

            for(int i=0; i< 5; i++)
            {
                int id = ids.ElementAt(i).ID;
                top5.Add(_context.Movies.First(m => m.ID == id));
            }
            

            return View(top5);
        }


        //DELETE REVIEW
        public IActionResult DeleteReview(int id, int mid)
        {
            Review review = _context.Reviews.SingleOrDefault(m => m.ID == id);
            if (review == null)
            {
                return NotFound();
            }
            Movie movie = _context.Movies.SingleOrDefault(x => x.ID == mid);
            if (movie == null)
            {
                return NotFound();
            }

            _context.Reviews.Remove(review);
            _context.SaveChanges();

            return RedirectToAction("Details", new { id = movie.ID });
        }

        public IActionResult ErrorForbidden() => View();
        public IActionResult ErrorNotLoggedIn() => View();
    }
}
