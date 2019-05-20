using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace BreakoutGame
{
    public class Brick : PictureBox
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        private int _maxHitsAllowed = 2;
        public bool InPlay
        {
            get { return this.Visible; }
            set { this.Visible = value; }
        }

        public int MaxHitsAllowed { get { return _maxHitsAllowed; } }

        public int NumOfHits { get; set; }

        // TO KEEP TRACK OF WHICH BRICK COLOUR \\
        public bool[] whichBrick = new bool[] { false, false, false, false, false };

        // CONSTRUCTOR \\
        public Brick()
        {   
            this.BackgroundImageLayout = ImageLayout.Stretch;
            NumOfHits = 0;
        }

        // CHECKS IF THE BRICK HAS REACHED ITS MAX NUM OF HITS \\
        public bool ReachedMax()
        {
            if (this.NumOfHits == MaxHitsAllowed)
                return true;
            else
            return false;
        }

        // CHANGES THE BACKGROUND IMAGE TO BROKEN BRICK \\
        public void ChangeImage()
        {
            if (this.NumOfHits == 1)
            {
                if (whichBrick[0])
                    this.BackgroundImage = Properties.Resources.Brick1_broken;

                else if (whichBrick[1])
                    this.BackgroundImage = Properties.Resources.Brick2_broken;

                else if (whichBrick[2])
                    this.BackgroundImage = Properties.Resources.Brick3_broken;

                else if (whichBrick[3])
                    this.BackgroundImage = Properties.Resources.Brick4_broken;

                else if (whichBrick[4])
                    this.BackgroundImage = Properties.Resources.Brick5_broken;
            }
        }

        // RESETS THE BRICKS FOR A NEW GAME
        public void Reset()
        {
            // CHANGES THE BACKGROUND IMAGE BACK TO UNBROKEN BRICK \\
            if (whichBrick[0])
                this.BackgroundImage = Properties.Resources.Brick1;

            else if (whichBrick[1])
                this.BackgroundImage = Properties.Resources.Brick2;

            else if (whichBrick[2])
                this.BackgroundImage = Properties.Resources.Brick3;

            else if (whichBrick[3])
                this.BackgroundImage = Properties.Resources.Brick4;

            else if (whichBrick[4])
                this.BackgroundImage = Properties.Resources.Brick5;

            // RESET THE HITS AND MAKE VISIBLE \\
            this.Visible = true;
            this.NumOfHits = 0;
        }
    }
}
