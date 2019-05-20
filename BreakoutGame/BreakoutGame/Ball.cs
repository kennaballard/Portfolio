using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BreakoutGame
{
    public class Ball : PictureBox
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        static Random rnd = new Random();
        private int vx;
        private int vy;

        private int velocity = 5;

        public Ball()
        {
            this.BackgroundImage = Properties.Resources.Ball;
            this.BackgroundImageLayout = ImageLayout.Stretch;
            this.Height = 28;
            this.Width = 28;

            // MAKES BALL START IN DIFFERENT DIRECTION \\
            int angle = rnd.Next(30, 75);
            double rad = Math.PI * angle / 180;
            vx = (int)(velocity * Math.Cos(rad));
            vy = (int)(velocity * Math.Sin(rad));
        }

        // MOVES BALL \\
        public void MoveBall()
        {
            int x = this.Location.X;
            int y = this.Location.Y;

            this.Location = new System.Drawing.Point(x + vx, y + vy);
        }

        // CHANGES VERTICAL DIRECTION \\
        public void BounceVertical()
        {
            vy = -vy;
            this.MoveBall();
        }

        // CHANGES HORIZONTAL DIRECTION \\
        public void BounceHorizontal()
        {
            vx = -vx;
            this.MoveBall();
        }
    }
}
