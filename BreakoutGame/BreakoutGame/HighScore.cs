using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BreakoutGame
{
    public partial class HighScore : Form
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        private TopScore _current;
        public TopScore Current
        {
            get { return _current; }
            set { _current = value; }
        }
        public HighScore(TopScore _ts)
        {
            InitializeComponent();
            Current = _ts;
        }

        // CHNAGES PLAYERS NAME TO USER INPUT --> NO INPUT, STAYS ANONYMOUS \\
        private void btn_OK_Click(object sender, EventArgs e)
        {            
            if(txt_name.Text != "")
            {
                Current.Name = txt_name.Text;
            }
            this.Close();
        }

        // ADDS TOP SCORE TO LIST, OPENS THE HIGHEST SCORES WINDOW \\
        private void HighScore_FormClosed(object sender, FormClosedEventArgs e)
        {
            // REMOVES LOWEST SCORE IF NECESSARY \\
            if(TopScore.topScores.Count >= Form1.MAX_TOP_SCORES)
            {
                int lowest = TopScore.topScores.Min(i => i.Score);
                TopScore lowestScore = TopScore.topScores.Find(i => i.Score == lowest);
                TopScore.topScores.Remove(lowestScore);
            }           

            // ADDS NEW HIGH SCORE \\
            TopScore.topScores.Add(Current);
            HighestScores highestscrs = new HighestScores();
            highestscrs.ShowDialog();
        }
    }
}
