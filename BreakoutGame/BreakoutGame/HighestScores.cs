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
    public partial class HighestScores : Form
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        public HighestScores()
        {
            InitializeComponent();
            Setup();
        }

        // CHANGE LABEL TO DISPLAY THE LIST OF TOP SCORES \\
        public void Setup()
        {
            lbl_top.Text = "";
            TopScore.topScores.Sort((ts1, ts2) => ts2.Score.CompareTo(ts1.Score));
            foreach(TopScore score in TopScore.topScores)
            {
                lbl_top.Text = lbl_top.Text + "\n" + score.Name + "    :    " + score.Score;
            }
        }
    }
}
