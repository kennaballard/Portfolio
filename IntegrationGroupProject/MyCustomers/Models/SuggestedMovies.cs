using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MyCustomers.Models
{
    public class SuggestedMovies
    {
        public int ID { get; set; }
        public int CustomerID { get; set; }
        public int MovieID { get; set; }
    }
}
