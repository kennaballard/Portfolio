﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using MyCustomers.Data;

namespace MyCustomers.Migrations
{
    [DbContext(typeof(CustomerContext))]
    [Migration("20181207182914_initial3")]
    partial class initial3
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.1.1-rtm-30846")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("MyCustomers.Models.Customer", b =>
                {
                    b.Property<int>("ID")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime?>("DateOfBirth");

                    b.Property<string>("Gender");

                    b.Property<bool>("IsSubscribedToNewsletter");

                    b.Property<string>("Membership");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasMaxLength(100);

                    b.Property<string>("Password");

                    b.Property<double>("Probability");

                    b.HasKey("ID");

                    b.ToTable("Customer");
                });

            modelBuilder.Entity("MyCustomers.Models.Movie", b =>
                {
                    b.Property<int>("ID")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Audience");

                    b.Property<string>("Description");

                    b.Property<string>("Director");

                    b.Property<string>("Genre");

                    b.Property<double>("Length");

                    b.Property<int>("NumberOfRatings");

                    b.Property<string>("PosterFile");

                    b.Property<double>("Rating");

                    b.Property<DateTime?>("ReleaseDate");

                    b.Property<string>("Title");

                    b.HasKey("ID");

                    b.ToTable("Movies");
                });

            modelBuilder.Entity("MyCustomers.Models.RentedMovies", b =>
                {
                    b.Property<int>("ID")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("CustomerID");

                    b.Property<int>("MovieID");

                    b.HasKey("ID");

                    b.ToTable("RentedMovies");
                });

            modelBuilder.Entity("MyCustomers.Models.SuggestedMovies", b =>
                {
                    b.Property<int>("ID")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("CustomerID");

                    b.Property<int>("MovieID");

                    b.HasKey("ID");

                    b.ToTable("SuggestedMovies");
                });
#pragma warning restore 612, 618
        }
    }
}
