using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MyCustomers.Models
{
    public class Review
    {
        public int ID { get; set; }

        public int MovieId { get; set; }

        public String Content { get; set; }

        public int Rating { get; set; }
    }
}
