using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static System.Net.Mime.MediaTypeNames;

namespace MyCustomers.Models
{
    public class Movie
    {
        public int ID { get; set; }
        public string Title { get; set; }
        public string Genre { get; set; }
        public double Length { get; set; }
        public double Rating { get; set; }
        public int NumberOfRatings { get; set; }
        public string Director { get; set; }
        public string Audience { get; set; }
        public string Description { get; set; }
        public DateTime? ReleaseDate { get; set; }
        public string PosterFile { get; set; }
    }
}
