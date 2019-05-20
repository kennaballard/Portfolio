using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BreakoutGame
{
    class Paddle : PictureBox
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        int vx = 12;

        public Paddle()
        {
            this.BackgroundImage = Properties.Resources.Paddle;
            this.BackgroundImageLayout = ImageLayout.Stretch;
            this.Height = 30;
            this.Width = 100;
        }

        // MOVEMENT --> LEFT \\
        public void MoveLeft()
        {
            int x = this.Location.X;
            this.Location = new System.Drawing.Point(x - vx, Location.Y);
        }

        // MOVEMENT --> RIGHT \\
        public void MoveRight()
        {
            int x = this.Location.X;
            this.Location = new System.Drawing.Point(x + vx, Location.Y);
        }
    }
}
