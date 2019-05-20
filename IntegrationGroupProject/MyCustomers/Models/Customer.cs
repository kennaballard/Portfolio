using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace MyCustomers.Models
{
    public class Customer
    {
        public int ID { get; set; }
        [Required(ErrorMessage = "Please enter the customer's name")]
        [MaxLength(100)]
        public string Name { get; set; }
        public bool IsSubscribedToNewsletter { get; set; }

        [Display(Name = "Date of Birth")]
        public DateTime? DateOfBirth { get; set; }
        public string Gender { get; set; }
        public double Probability { get; set; }

        public string Password { get; set; }

        public string Membership { get;set;}
    }
}
