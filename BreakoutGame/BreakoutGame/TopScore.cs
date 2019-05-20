using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BreakoutGame
{
    public class TopScore
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        public static List<TopScore> topScores = new List<TopScore>();
        private int _score;
        private string _name;

        public int Score
        {
            get { return _score; }
            set { _score = value; }
        }

        public string Name
        {
            get { return _name; }
            set { _name = value; }
        }

        public TopScore()
        {
            _score = 0;
            _name = "";
        }

        public TopScore(int score_, string name_)
        {
            Score = score_;
            Name = name_;
        }
    }
}
