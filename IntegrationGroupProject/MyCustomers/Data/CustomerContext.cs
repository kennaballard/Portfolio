using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MyCustomers.Data
{
    public class CustomerContext : DbContext
    {
        public CustomerContext(DbContextOptions<CustomerContext> options)
            : base(options)
        {
        }

        public DbSet<MyCustomers.Models.Customer> Customer { get; set; }

        public DbSet<MyCustomers.Models.Movie> Movies { get; set; }

        public DbSet<MyCustomers.Models.RentedMovies> RentedMovies { get; set; }

        public DbSet<MyCustomers.Models.SuggestedMovies> SuggestedMovies { get; set; }

        public DbSet<Models.Review> Reviews { get; set; }

        public DbSet<MyCustomers.Models.User> Users { get; set; }
    }
}
