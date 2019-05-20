using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BreakoutGame
{
    public partial class BricksMedium : UserControl
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        static List<Brick> medBricks = new List<Brick>();
        static Random rnd = new Random();
        int brickColour;
        public BricksMedium()
        {
            InitializeComponent();
            Setup();
        }

        // GOES THROUGH TABLE LAYOUT PANEL \\
        // CREATES A BRICK FOR EACH COLUMN AND EACH ROW \\
        public void Setup()
        {
            for (int r = 0; r < tlp_bricks.RowCount; r++)
            {
                for (int c = 0; c < tlp_bricks.ColumnCount; c++)
                {
                    // SETS UP BRICK \\
                    Brick position = new Brick();
                    DecideColour(ref position);
                    position.BackgroundImageLayout = ImageLayout.Stretch;
                    position.Dock = DockStyle.Fill;

                    // ADDS BRICK TO TLP \\
                    tlp_bricks.Controls.Add(position, c, r);
                }
            }
        }

        // DECIDES COLOUR OF EACH INDIVIDUAL BRICK \\
        public void DecideColour(ref Brick current)
        {
            for (int i = 0; i < current.whichBrick.Length; i++)
            {
                current.whichBrick[i] = false;
            }

            brickColour = rnd.Next(1, 6);
            switch (brickColour)
            {
                case 1:
                    current.BackgroundImage = Properties.Resources.Brick1;
                    current.whichBrick[0] = true;
                    break;
                case 2:
                    current.BackgroundImage = Properties.Resources.Brick2;
                    current.whichBrick[1] = true;
                    break;
                case 3:
                    current.BackgroundImage = Properties.Resources.Brick3;
                    current.whichBrick[2] = true;
                    break;
                case 4:
                    current.BackgroundImage = Properties.Resources.Brick4;
                    current.whichBrick[3] = true;
                    break;
                case 5:
                    current.BackgroundImage = Properties.Resources.Brick5;
                    current.whichBrick[4] = true;
                    break;
            }
        }

        public List<Brick> getBricks()
        {
            // ADDS THE BRICKS TO LIST \\
            for (int r = 0; r < tlp_bricks.RowCount; r++)
            {
                for (int c = 0; c < tlp_bricks.ColumnCount; c++)
                {
                    Brick current = tlp_bricks.GetControlFromPosition(c, r) as Brick;
                    // MAKES ALL BRICKS VISIBLE \\
                    current.Visible = true;
                    medBricks.Add(current);
                }
            }
            return medBricks;
        }
    }
}
