using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BreakoutGame
{
    public partial class Form1 : Form
    {
        // ----------------------------- \\
        // NAME: Kennedy Ballard
        // STUDENT ID: 1649618
        // ----------------------------- \\

        List<Brick> bricks = new List<Brick>();

        const int POINTS_PER_BRICK = 10;
        const string SCORE_FILE = "../../theScores.txt";
        const int NAME = 0;
        const int SCORE = 1;
        const int COUNTDOWN = 3;
        public const int MAX_TOP_SCORES = 5;

        int score = 0;
        int count = COUNTDOWN;
        bool win;
          
        public Form1()
        {
            InitializeComponent();
            ReadTopScores();

            // SELECTS MEDIUM LEVEL BY DEFAULT \\
            cb_levels.SelectedIndex = 1;
            bricksMedium.Visible = true;
        }

        #region READ AND WRITE FILES

        // READS IN THE TOP SCORES FROM FILE \\
        private void ReadTopScores()
        {
            try
            {
                if (File.Exists(SCORE_FILE))
                {
                    using (StreamReader read = new StreamReader(SCORE_FILE))
                    {
                        while (!read.EndOfStream)
                        {
                            string[] scoreInfo = read.ReadLine().Split('|');
                            TopScore current = new TopScore(int.Parse(scoreInfo[SCORE]), scoreInfo[NAME]);

                            TopScore.topScores.Add(current);
                        }
                    }
                }
            } catch(Exception err)
            {
                MessageBox.Show(err.Message);
            }
        }

        // WRITES THE TOP SCORES TO THE FILE \\
        private void WriteTopScores()
        {
            try
            {
                using (StreamWriter write = new StreamWriter(SCORE_FILE, false))
                {
                    foreach (TopScore current in TopScore.topScores)
                    {
                        write.WriteLine($"{current.Name}|{current.Score.ToString()}");
                    }
                }
            }
            catch (Exception err)
            {
                MessageBox.Show(err.Message);
            }

        }
        #endregion

        #region MENUSTRIP CLICKS
        // RESETS THE TOP SCORES \\
        private void tsMenuStrip_reset_Click(object sender, EventArgs e)
        {
            TopScore.topScores.Clear();
        }

        // DISPLAYS THE CURRENT TOP SCORES \\
        private void tsMenuItem_display_Click(object sender, EventArgs e)
        {
            HighestScores highest = new HighestScores();
            highest.ShowDialog();
        }

        // CLOSES THE WINDOW ON CHOOSING FILE --> EXIT \\
        private void tsMenuItem_exit_Click(object sender, EventArgs e)
        {
            this.Close();
        }
        #endregion

        #region SETUP SELECTED LEVEL
        private void cb_levels_SelectedIndexChanged(object sender, EventArgs e)
        {
            bricks.Clear();
            // DECIDES WHICH SET OF BRICKS TO MAKE VISIBLE \\
            // HIDES ALL AND THEN DISPLAYS ONE CORRESPONDING TO LEVEL SELECTED \\
            bricksMedium.Visible = false;
            bricksHard.Visible = false;
            bricksEasy.Visible = false;

            int current = cb_levels.SelectedIndex;

            switch (current)
            {
                case 0:
                    bricksEasy.Visible = true;
                    bricks = bricksEasy.getBricks();
                    timer1.Interval = 21;
                    break;
                case 1:
                    bricksMedium.Visible = true;
                    bricks = bricksMedium.getBricks();
                    timer1.Interval = 20;
                    break;
                case 2:
                    bricksHard.Visible = true;
                    bricks = bricksHard.getBricks();
                    timer1.Interval = 17;
                    break;
                default:
                    break;
            }
        }
        #endregion

        #region TIMER CONTROL
        // COUNTDOWN TO A NEW GAME \\
        private void timer_countdown_Tick(object sender, EventArgs e)
        {
            count -= 1;
            lbl_countdown.Text = count.ToString();
            if (count == 0)
            {
                timer_countdown.Enabled = false;
                timer1.Enabled = true;
                lbl_countdown.Text = "";
                count = COUNTDOWN;
            }  
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            // MOVE THE BALL
            ball.MoveBall();

            // CHECK IF THE BALL HITS SOMETHING
            CheckIntersection();
        }
        #endregion

        #region COLLISIONS
        // CHECKS IF BALL AND BRICK/WALL/PADDLE INTERSECT \\
        private void CheckIntersection()
        {
            // BOTTOM --> GAME OVER \\
            if ((ball.Location.X > paddle.Location.X + paddle.Width || ball.Location.X + ball.Width < paddle.Location.X) && ball.Location.Y + ball.Height > paddle.Location.Y)
            {
                if(ball.Location.Y + ball.Height >= pnl_main.Height)
                {
                    ball.BackgroundImage = Properties.Resources.Ball_flat;
                    EndGame();
                }                
            }

            // TOP \\
            else if (ball.Location.Y <= 0)
            {
                ball.BounceVertical();
            }

            // WALL \\
            else if (ball.Bounds.Location.X <= pnl_main.Location.X || ball.Bounds.Location.X + ball.Width >= pnl_main.Location.X + pnl_main.Width)
            {
                ball.BackgroundImage = Properties.Resources.Ball_squished;
                ball.BounceHorizontal();
            }

            // PADDLE \\
            else if (ball.Bounds.IntersectsWith(paddle.Bounds))
            {
                score -= 1;
                ball.BackgroundImage = Properties.Resources.Ball_flat;
                ball.BounceVertical();
            }

            else
                ball.BackgroundImage = Properties.Resources.Ball;

            // BRICK \\
            foreach (Brick brick in bricks)
            {
                if (ball.Bounds.IntersectsWith(brick.Bounds) && brick.Visible)
                {
                    score += POINTS_PER_BRICK;

                    // IF A BALL HITS A BRICK MAKE IT BOUNCE \\
                    ball.BounceVertical();
                    brick.NumOfHits++;

                    // CHECK TO SEE THE NUMBER OF HITS \\
                    // If one hit: replace with broken brick
                    brick.ChangeImage();

                    // If two hits: remove brick from list, make it disappear
                    if (brick.ReachedMax())
                    {
                        brick.Visible = false;;
                    }
                }
            }                    
          
            lbl_score.Text = "Score: " + score;
            CheckForWin();
           
        }
        #endregion

        #region START/END GAME
        // STARTS A NEW GAME \\
        private void btn_start_Click(object sender, EventArgs e)
        {
            // RESETS THE BALL AND PADDLE POSITIONS \\
            ball.BackgroundImage = Properties.Resources.Ball;
            ball.Location = new Point(12, 216);
            paddle.Location = new Point(0, paddle.Location.Y);

            // STOPS PLAYER FROM CHANGING LEVEL DURING GAME \\
            cb_levels.Enabled = false;

            // STARTS COUNTDOWN \\
            timer_countdown.Enabled = true;
            lbl_countdown.Text = count.ToString();
          
            // RESETS CURRENT SCORE \\
            score = 0;
            lbl_score.Text = "Score: 0";         
        }

        // STOP CURRENT GAME \\
        private void btn_stop_Click(object sender, EventArgs e)
        {
            EndGame();
        }

        // CHECKS TO SEE IF ALL BRICKS HAVE BEEN BROKEN \\
        public void CheckForWin()
        {
            win = true;
            foreach (Brick brick in bricks)
            {
                win = win && !brick.Visible;
            }

            if (win)
                EndGame();
        }

        // ENDS THE GAME \\
        private void EndGame()
        {
            // STOPS BOTH TIMERS \\
            timer1.Enabled = false;
            timer_countdown.Enabled = false;
          
            // ALLOWS PLAYER TO CHOOSE A NEW LEVEL \\
            cb_levels.Enabled = true;

            // RESETS THE BRICKS FOR A NEW GAME \\
            ReplaceBricks();

            if(score < 0)
            {
                score = 0;
                lbl_score.Text = "Score: 0";
            }

            // DISPLAY GAME OVER \\
            MessageBox.Show(win ? $"Congratulations, you won!\nYour final score was: {score.ToString()}" : $"Game Over!\nYour final score was: {score.ToString()}");

            // CHECK IF CURRENT SCORE IS A TOP SCORE \\
            if(TopScore.topScores.Count < MAX_TOP_SCORES || score >= TopScore.topScores.Min(i => i.Score))
            {
                AddNewScore();
            }
        }

        // RESETS THE BRICKS FOR A NEW GAME \\
        private void ReplaceBricks()
        {
            foreach (Brick brick in bricks)
            {
                brick.Reset();
            }
        }
        #endregion

        #region SCORES
        // ADD THE CURRENT SCORE TO TOP SCORES \\
        private void AddNewScore()
        {
            TopScore currentScore = new TopScore(score, "anonymous");

            HighScore highScr = new HighScore(currentScore);
            highScr.ShowDialog();

        }
        #endregion

        #region KEYS
        private void Suppress_Arrow_Keys(object sender, PreviewKeyDownEventArgs e)
        {
            if (e.KeyCode == Keys.Right || e.KeyCode == Keys.Left)
            {
                // SUPPRESS TAB MOVEMENT \\
                e.IsInputKey = true;

                // MOVES THE PADDLE \\
                if (e.KeyCode == Keys.Right)
                {
                    paddle.MoveRight();
                }
                else
                {
                    paddle.MoveLeft();
                }
                
            }
        }
        #endregion

        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            WriteTopScores();
        }
    }
}
