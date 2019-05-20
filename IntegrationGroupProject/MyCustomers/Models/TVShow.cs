using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MyCustomers.Models
{
    public class TVShow
    {
        public int ID { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public string Genre { get; set; }
        public int NbSeasons { get; set; }
        public IEnumerable<Episode> Episodes { get; set; }
        public double Rating { get; set; }
        public string Director { get; set; }
        public string Audience { get; set; }
        public DateTime? ReleaseDate { get; set; }
    }
}
