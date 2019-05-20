using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using MyCustomers.Data;
using MyCustomers.Models;

namespace MyCustomers.Controllers
{

    public class AdminController : Controller
    {
        /// /////////////////////////////////////////////////////////////////
        /// DATABASE
        /// /////////////////////////////////////////////////////////////////

        private readonly CustomerContext _context;
        public AdminController(CustomerContext db)
        {
            _context = db;
        }



        /// /////////////////////////////////////////////////////////////////
        /// LOGIN FUNCTIONS
        /// /////////////////////////////////////////////////////////////////

        //REGISTER A NEW USER
        [HttpPost]
        public async Task<IActionResult> NewUser(string username, string password, string verifyPass)
        {
            if (string.IsNullOrEmpty(username))
            {
                return RedirectToAction("Register", new { errorCode = 1 });
            }
            if (!password.Equals(verifyPass)) {
                return RedirectToAction("Register", new { errorCode = 2 });
            }

            _context.Users.Add(new User() { Username = username, Password = password, Role = "User", DateOfBirth = new DateTime() });
            _context.SaveChanges();

            return RedirectToAction(nameof(Index));
        }

        //REGISTER
        public IActionResult Register(int errorCode = 0)
        {
            return View(errorCode);
        }

        //LOGIN
        [HttpPost]
        public async Task<IActionResult> Login(string username, string password)
        {
            var role_collected = ValidateCredentials(username, password);
            if (string.IsNullOrEmpty(username) || role_collected == "none")
            {
                return RedirectToAction(nameof(Index));
            }

            User user = _context.Users.SingleOrDefault(c => c.Username == username);
            if (user == null)
            {
                return NotFound();
            }


            var identity = new ClaimsIdentity(new[] {
            new Claim(ClaimTypes.Name, username),
            new Claim(ClaimTypes.Role, role_collected),
            new Claim(ClaimTypes.Role, user.ID.ToString()),
        }, CookieAuthenticationDefaults.AuthenticationScheme);

            //represent user
            var principal = new ClaimsPrincipal(identity);
            await HttpContext.SignInAsync(
                CookieAuthenticationDefaults.AuthenticationScheme,
                principal);

            return RedirectToAction(nameof(Index));
        }

        //LOGOUT
        [HttpPost]
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(
                CookieAuthenticationDefaults.AuthenticationScheme);
            return RedirectToAction(nameof(Index));
        }

        //GET USERS
        public IEnumerable<User> GetUsers()
        {
            return _context.Users.ToList();
        }

        //CHECK CREDENTIALS
        public string ValidateCredentials(string username, string password)
        {
            var users = GetUsers();
            foreach (var user in users)
                if (username == user.Username && password == user.Password)
                    return user.Role;
            return "none";
        }


        /// /////////////////////////////////////////////////////////////////
        /// PAGES
        /// /////////////////////////////////////////////////////////////////

        public IActionResult Index()
        {
            return View();
        }
        [Authorize(policy: "MustBeAdmin")]
        public IActionResult Customers()
        {
            var customers = _context.Users.Where(u => !u.Role.Equals("Admin")).ToList();
            return View("Customers", customers);
        }

        [Authorize(policy: "MustBeAdmin")]
        public IActionResult UpdateMovies()
        {
            IEnumerable<Movie> movies = _context.Movies;
            return View("Movies", movies);
        }
        public IActionResult ErrorForbidden() => View();
        public IActionResult ErrorNotLoggedIn() => View();









        /// /////////////////////////////////////////////////////////////////
        /// USER FUNCTIONS
        /// /////////////////////////////////////////////////////////////////
        [HttpPost]
        public IActionResult UpdateUser(int id, string gender, string birth, string current, string password, string confirm)
        {
            User user = _context.Users.SingleOrDefault(c => c.ID == id);
            if (user == null)
            {
                return NotFound();
            }
            if (string.IsNullOrEmpty(gender) || string.IsNullOrEmpty(birth))
            {
                return RedirectToAction("Account");
            }
            if (!string.IsNullOrEmpty(current) && !string.IsNullOrEmpty(password) && !string.IsNullOrEmpty(confirm))
            {
                if (!user.Password.Equals(current) || !password.Equals(confirm))
                    return RedirectToAction("Account");
                else
                    user.Password = confirm;
            }
            try
            {
                user.Gender = gender;
                user.DateOfBirth = DateTime.Parse(birth);
            }
            catch
            {
                return RedirectToAction("Account");
            }
            
            _context.Users.Update(user);
            _context.SaveChanges();

            return RedirectToAction("Index");
        }


        /// /////////////////////////////////////////////////////////////////
        /// ACCOUNT
        /// /////////////////////////////////////////////////////////////////
        
        //DISPLAY ACCOUNT INFORMATION OF CURRENT ACCOUNT
        public ActionResult Account()
        {
            var roles = ((ClaimsIdentity)User.Identity).Claims
                .Where(c => c.Type == ClaimTypes.Role)
                .Select(c => c.Value);

            User customer = _context.Users.SingleOrDefault(c => c.ID.ToString() == roles.ElementAt(1));
            if (customer == null)
            {
                return NotFound();
            }

            return View(customer);

        }

        //DISPLAY RENTED MOVIE FOR CURRENT ACCOUNT
        public ActionResult RentedMovies()
        {
            var roles = ((ClaimsIdentity)User.Identity).Claims
                .Where(c => c.Type == ClaimTypes.Role)
                .Select(c => c.Value);

            User customer = _context.Users.SingleOrDefault(c => c.ID.ToString() == roles.ElementAt(1));
            if (customer == null)
            {
                return NotFound();
            }

            IEnumerable<RentedMovies> movieIDs = _context.RentedMovies.Where(c => c.CustomerID == customer.ID);

            List<Movie> movies = new List<Movie>();
            foreach (RentedMovies anID in movieIDs)
            {
                movies.Add(_context.Movies.SingleOrDefault(c => c.ID == anID.MovieID));
            }

            IEnumerable<Movie> SuggestedMovies = new List<Movie>();

            if (movies.Count() > 0)
                SuggestedMovies = GetSuggested(customer, movies);

            Tuple<User, IEnumerable<Movie>> userMovies = new Tuple<User, IEnumerable<Movie>>(customer, movies);
            Tuple<Tuple<User, IEnumerable<Movie>>, IEnumerable<Movie>> dataSet = new Tuple<Tuple<User, IEnumerable<Movie>>, IEnumerable<Movie>>(userMovies, SuggestedMovies);

            return View(dataSet);
        }



        /// /////////////////////////////////////////////////////////////////
        /// ADMIN FUNCTIONS
        /// /////////////////////////////////////////////////////////////////
        
        //DISPLAY ACCOUNT INFO OF CUSTOMER
        [Authorize(policy: "MustBeAdmin")]
        public ActionResult Customer(int id)
        {
            User customer = _context.Users.SingleOrDefault(c => c.ID == id);
            if (customer == null)
            {
                return NotFound();
            }

            return View("Account", customer);

        }

        //DELETE CUSTOMER 
        [Authorize(policy: "MustBeAdmin")]
        public IActionResult CustomerDelete(int id)
        {
            User user = _context.Users.SingleOrDefault(c => c.ID == id);
            if (user == null)
            {
                return NotFound();
            }
            _context.Users.Remove(user);
            _context.SaveChanges();

            return RedirectToAction("Customers");
        }


        //DISPLAY MOVIE EDITOR
        [Authorize(policy: "MustBeAdmin")]
        public ActionResult Edit(int id)
        {
            Movie movie = _context.Movies.SingleOrDefault(c => c.ID == id);
            if (movie == null)
            {
                return NotFound();
            }

            return View(movie);

        }

        //DISPLAY CUSTOMERS RENTED MOVIES
        [Authorize(policy: "MustBeAdmin")]
        public ActionResult RentedMoviesAdmin(int id)
        {
            User customer = _context.Users.SingleOrDefault(c => c.ID == id);
            if (customer == null)
            {
                return NotFound();
            }

            IEnumerable<RentedMovies> movieIDs = _context.RentedMovies.Where(c => c.CustomerID == customer.ID);
            List<Movie> movies = new List<Movie>();
            foreach (RentedMovies anID in movieIDs)
            {
                movies.Add(_context.Movies.SingleOrDefault(c => c.ID == anID.MovieID));
            }

            IEnumerable<Movie> SuggestedMovies = new List<Movie>();

            if (movies.Count() > 0)
                SuggestedMovies = GetSuggested(customer, movies);

            Tuple<User, IEnumerable<Movie>> userMovies = new Tuple<User, IEnumerable<Movie>>(customer, movies);
            Tuple<Tuple<User, IEnumerable<Movie>>, IEnumerable<Movie>> dataSet = new Tuple<Tuple<User, IEnumerable<Movie>>, IEnumerable<Movie>>(userMovies, SuggestedMovies);

            return View("RentedMovies", dataSet);
        }

        /// /////////////////////////////////////////////////////////////////
        /// MOVIE FUNCTIONS
        /// /////////////////////////////////////////////////////////////////

        //ADD NEW MOVIE
        [HttpPost]
        public IActionResult New(string title, string genre, double length, string director, string audience, string description, string release, string poster)
        {
            if (string.IsNullOrEmpty(title) || string.IsNullOrEmpty(genre) || 
                string.IsNullOrEmpty(director) || string.IsNullOrEmpty(audience) || 
                string.IsNullOrEmpty(description) || string.IsNullOrEmpty(release) || 
                string.IsNullOrEmpty(poster))
            {
                return RedirectToAction("UpdateMovies");
            }
            try
            {
                DateTime dateValue = DateTime.Parse(release);
                var record = new Movie() { Title = title, Genre = genre, Length = length, Director = director, Audience = audience, Description = description, ReleaseDate = dateValue, PosterFile = poster };
                _context.Movies.Add(record);
                _context.SaveChanges();
            }
            catch
            {
                return RedirectToAction("UpdateMovies");
            }
            return RedirectToAction("UpdateMovies");
        }

        //UPDATE MOVIE
        [HttpPost]
        public IActionResult Updated(int id, string title, string genre, double length, string director, string audience, string description, string release, string poster)
        {
            Movie movie = _context.Movies.SingleOrDefault(c => c.ID == id);
            if (movie == null)
            {
                return NotFound();
            }

            if (string.IsNullOrEmpty(title) || string.IsNullOrEmpty(genre) ||
                string.IsNullOrEmpty(director) || string.IsNullOrEmpty(audience) ||
                string.IsNullOrEmpty(description) || string.IsNullOrEmpty(release) ||
                string.IsNullOrEmpty(poster))
            {
                return View("Edit", movie);
            }

            try
            {
                DateTime dateValue = DateTime.Parse(release);
                movie.Title = title;
                movie.Genre = genre;
                movie.Length = length;
                movie.Director = director;
                movie.Audience = audience;
                movie.Description = description;
                movie.ReleaseDate = dateValue;
                movie.PosterFile = poster;

                _context.Movies.Update(movie);
                _context.SaveChanges();

            }
            catch
            {
                return View("Edit", movie);
            }


            return RedirectToAction("UpdateMovies");
        }

        //REMOVE MOVIE
        public IActionResult Deleted(int id)
        {
            Movie movie = _context.Movies.SingleOrDefault(c => c.ID == id);
            if (movie == null)
            {
                return NotFound();
            }
            _context.Movies.Remove(movie);
            _context.SaveChanges();

            return RedirectToAction("UpdateMovies");
        }

        public IEnumerable<Movie> GetSuggested(User customer, IEnumerable<Movie> rented)
        {
            List<Movie> suggested = new List<Movie>();
            Dictionary<String, Int32> rentedGenres = new Dictionary<String, Int32>();
            // Get users most watched genres
            foreach (Movie movie in rented)
            {
                string genre = movie.Genre;
                if (rentedGenres.ContainsKey(genre))
                    // up the count 
                    rentedGenres[genre]++;
                else
                    // add to list, count starts at 1
                    rentedGenres.Add(genre, 1);
            }

            IEnumerable<Movie> totalMovies = _context.Movies;

            // get most watched genre
            string mostPopularGenre = rentedGenres.Keys.Aggregate((x, y) => rentedGenres[x] > rentedGenres[y] ? x : y);

            // Get movies of the same genre
            foreach (Movie movie in totalMovies)
            {
                if (movie.Genre == mostPopularGenre && !rented.Contains(movie))
                    suggested.Add(movie);
            }
            customer.Probability = (double)rentedGenres[mostPopularGenre] / (double)rented.Count() * 100;

            // Update probability in database
            _context.Users.Update(customer);
            _context.SaveChanges();

            return suggested;
        }
    }
}