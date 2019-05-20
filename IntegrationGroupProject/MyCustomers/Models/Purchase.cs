using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MyCustomers.Models
{
    public class Purchase
    {
        public int ID { get; set; }
        public Movie MovieRented { get; set; }
        public TVShow ShowRented { get; set; }      
        public double Total { get; set; }
        public DateTime TransactionDate { get; set; }
    }
}
