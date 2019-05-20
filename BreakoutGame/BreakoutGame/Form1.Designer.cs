namespace BreakoutGame
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.menuStrip = new System.Windows.Forms.MenuStrip();
            this.tsMenuItem_file = new System.Windows.Forms.ToolStripMenuItem();
            this.tsMenuItem_exit = new System.Windows.Forms.ToolStripMenuItem();
            this.tsMenuItem_scores = new System.Windows.Forms.ToolStripMenuItem();
            this.tsMenuItem_display = new System.Windows.Forms.ToolStripMenuItem();
            this.tsMenuStrip_reset = new System.Windows.Forms.ToolStripMenuItem();
            this.panel1 = new System.Windows.Forms.Panel();
            this.lbl_level = new System.Windows.Forms.Label();
            this.cb_levels = new System.Windows.Forms.ComboBox();
            this.btn_stop = new System.Windows.Forms.Button();
            this.btn_start = new System.Windows.Forms.Button();
            this.pnl_bottom = new System.Windows.Forms.Panel();
            this.lbl_score = new System.Windows.Forms.Label();
            this.pnl_main = new System.Windows.Forms.Panel();
            this.paddle = new BreakoutGame.Paddle();
            this.ball = new BreakoutGame.Ball();
            this.lbl_countdown = new System.Windows.Forms.Label();
            this.bricksEasy = new BreakoutGame.BricksEasy();
            this.bricksHard = new BreakoutGame.BricksHard();
            this.bricksMedium = new BreakoutGame.BricksMedium();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.timer_countdown = new System.Windows.Forms.Timer(this.components);
            this.menuStrip.SuspendLayout();
            this.panel1.SuspendLayout();
            this.pnl_bottom.SuspendLayout();
            this.pnl_main.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.paddle)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.ball)).BeginInit();
            this.SuspendLayout();
            // 
            // menuStrip
            // 
            this.menuStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.tsMenuItem_file,
            this.tsMenuItem_scores});
            this.menuStrip.Location = new System.Drawing.Point(0, 0);
            this.menuStrip.Name = "menuStrip";
            this.menuStrip.Size = new System.Drawing.Size(754, 24);
            this.menuStrip.TabIndex = 0;
            this.menuStrip.Text = "menuStrip1";
            this.menuStrip.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // tsMenuItem_file
            // 
            this.tsMenuItem_file.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.tsMenuItem_exit});
            this.tsMenuItem_file.Name = "tsMenuItem_file";
            this.tsMenuItem_file.Size = new System.Drawing.Size(37, 20);
            this.tsMenuItem_file.Text = "File";
            // 
            // tsMenuItem_exit
            // 
            this.tsMenuItem_exit.Name = "tsMenuItem_exit";
            this.tsMenuItem_exit.Size = new System.Drawing.Size(92, 22);
            this.tsMenuItem_exit.Text = "Exit";
            this.tsMenuItem_exit.Click += new System.EventHandler(this.tsMenuItem_exit_Click);
            // 
            // tsMenuItem_scores
            // 
            this.tsMenuItem_scores.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.tsMenuItem_display,
            this.tsMenuStrip_reset});
            this.tsMenuItem_scores.Name = "tsMenuItem_scores";
            this.tsMenuItem_scores.Size = new System.Drawing.Size(53, 20);
            this.tsMenuItem_scores.Text = "Scores";
            // 
            // tsMenuItem_display
            // 
            this.tsMenuItem_display.Name = "tsMenuItem_display";
            this.tsMenuItem_display.Size = new System.Drawing.Size(112, 22);
            this.tsMenuItem_display.Text = "Display";
            this.tsMenuItem_display.Click += new System.EventHandler(this.tsMenuItem_display_Click);
            // 
            // tsMenuStrip_reset
            // 
            this.tsMenuStrip_reset.Name = "tsMenuStrip_reset";
            this.tsMenuStrip_reset.Size = new System.Drawing.Size(112, 22);
            this.tsMenuStrip_reset.Text = "Reset";
            this.tsMenuStrip_reset.Click += new System.EventHandler(this.tsMenuStrip_reset_Click);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.lbl_level);
            this.panel1.Controls.Add(this.cb_levels);
            this.panel1.Controls.Add(this.btn_stop);
            this.panel1.Controls.Add(this.btn_start);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(0, 24);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(754, 48);
            this.panel1.TabIndex = 1;
            this.panel1.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // lbl_level
            // 
            this.lbl_level.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lbl_level.AutoSize = true;
            this.lbl_level.Location = new System.Drawing.Point(588, 10);
            this.lbl_level.Name = "lbl_level";
            this.lbl_level.Size = new System.Drawing.Size(36, 13);
            this.lbl_level.TabIndex = 3;
            this.lbl_level.Text = "Level:";
            this.lbl_level.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // cb_levels
            // 
            this.cb_levels.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.cb_levels.BackColor = System.Drawing.SystemColors.InactiveBorder;
            this.cb_levels.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cb_levels.FormattingEnabled = true;
            this.cb_levels.Items.AddRange(new object[] {
            "Easy",
            "Medium",
            "Hard"});
            this.cb_levels.Location = new System.Drawing.Point(630, 7);
            this.cb_levels.Name = "cb_levels";
            this.cb_levels.Size = new System.Drawing.Size(121, 21);
            this.cb_levels.TabIndex = 2;
            this.cb_levels.SelectedIndexChanged += new System.EventHandler(this.cb_levels_SelectedIndexChanged);
            this.cb_levels.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // btn_stop
            // 
            this.btn_stop.Location = new System.Drawing.Point(108, 4);
            this.btn_stop.Name = "btn_stop";
            this.btn_stop.Size = new System.Drawing.Size(98, 41);
            this.btn_stop.TabIndex = 1;
            this.btn_stop.Text = "Stop";
            this.btn_stop.UseVisualStyleBackColor = true;
            this.btn_stop.Click += new System.EventHandler(this.btn_stop_Click);
            this.btn_stop.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // btn_start
            // 
            this.btn_start.Location = new System.Drawing.Point(4, 4);
            this.btn_start.Name = "btn_start";
            this.btn_start.Size = new System.Drawing.Size(98, 41);
            this.btn_start.TabIndex = 0;
            this.btn_start.Text = "Start";
            this.btn_start.UseVisualStyleBackColor = true;
            this.btn_start.Click += new System.EventHandler(this.btn_start_Click);
            this.btn_start.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // pnl_bottom
            // 
            this.pnl_bottom.Controls.Add(this.lbl_score);
            this.pnl_bottom.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.pnl_bottom.Location = new System.Drawing.Point(0, 577);
            this.pnl_bottom.Name = "pnl_bottom";
            this.pnl_bottom.Size = new System.Drawing.Size(754, 32);
            this.pnl_bottom.TabIndex = 2;
            // 
            // lbl_score
            // 
            this.lbl_score.AutoSize = true;
            this.lbl_score.Location = new System.Drawing.Point(3, 10);
            this.lbl_score.Name = "lbl_score";
            this.lbl_score.Size = new System.Drawing.Size(47, 13);
            this.lbl_score.TabIndex = 0;
            this.lbl_score.Text = "Score: 0";
            this.lbl_score.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // pnl_main
            // 
            this.pnl_main.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.pnl_main.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.pnl_main.Controls.Add(this.paddle);
            this.pnl_main.Controls.Add(this.ball);
            this.pnl_main.Controls.Add(this.lbl_countdown);
            this.pnl_main.Controls.Add(this.bricksEasy);
            this.pnl_main.Controls.Add(this.bricksHard);
            this.pnl_main.Controls.Add(this.bricksMedium);
            this.pnl_main.Location = new System.Drawing.Point(0, 72);
            this.pnl_main.Name = "pnl_main";
            this.pnl_main.Size = new System.Drawing.Size(754, 499);
            this.pnl_main.TabIndex = 3;
            this.pnl_main.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // paddle
            // 
            this.paddle.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.paddle.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("paddle.BackgroundImage")));
            this.paddle.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.paddle.Location = new System.Drawing.Point(0, 469);
            this.paddle.Name = "paddle";
            this.paddle.Size = new System.Drawing.Size(100, 30);
            this.paddle.TabIndex = 6;
            this.paddle.TabStop = false;
            this.paddle.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // ball
            // 
            this.ball.BackColor = System.Drawing.Color.Transparent;
            this.ball.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("ball.BackgroundImage")));
            this.ball.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.ball.Location = new System.Drawing.Point(12, 216);
            this.ball.Name = "ball";
            this.ball.Size = new System.Drawing.Size(28, 28);
            this.ball.TabIndex = 5;
            this.ball.TabStop = false;
            this.ball.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // lbl_countdown
            // 
            this.lbl_countdown.AutoSize = true;
            this.lbl_countdown.Font = new System.Drawing.Font("Microsoft Sans Serif", 20.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_countdown.Location = new System.Drawing.Point(151, 240);
            this.lbl_countdown.Name = "lbl_countdown";
            this.lbl_countdown.Size = new System.Drawing.Size(0, 31);
            this.lbl_countdown.TabIndex = 4;
            this.lbl_countdown.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // bricksEasy
            // 
            this.bricksEasy.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.bricksEasy.Location = new System.Drawing.Point(4, 4);
            this.bricksEasy.Margin = new System.Windows.Forms.Padding(7);
            this.bricksEasy.Name = "bricksEasy";
            this.bricksEasy.Size = new System.Drawing.Size(747, 206);
            this.bricksEasy.TabIndex = 0;
            this.bricksEasy.Visible = false;
            this.bricksEasy.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // bricksHard
            // 
            this.bricksHard.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.bricksHard.Location = new System.Drawing.Point(3, 0);
            this.bricksHard.Name = "bricksHard";
            this.bricksHard.Size = new System.Drawing.Size(748, 210);
            this.bricksHard.TabIndex = 2;
            this.bricksHard.Visible = false;
            this.bricksHard.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // bricksMedium
            // 
            this.bricksMedium.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.bricksMedium.Location = new System.Drawing.Point(3, 0);
            this.bricksMedium.Name = "bricksMedium";
            this.bricksMedium.Size = new System.Drawing.Size(751, 199);
            this.bricksMedium.TabIndex = 1;
            this.bricksMedium.Visible = false;
            this.bricksMedium.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            // 
            // timer1
            // 
            this.timer1.Interval = 15;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // timer_countdown
            // 
            this.timer_countdown.Interval = 1000;
            this.timer_countdown.Tick += new System.EventHandler(this.timer_countdown_Tick);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(754, 609);
            this.Controls.Add(this.pnl_main);
            this.Controls.Add(this.pnl_bottom);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.menuStrip);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MainMenuStrip = this.menuStrip;
            this.Margin = new System.Windows.Forms.Padding(2);
            this.MaximumSize = new System.Drawing.Size(770, 648);
            this.MinimumSize = new System.Drawing.Size(770, 648);
            this.Name = "Form1";
            this.Text = "Breakout Game";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.PreviewKeyDown += new System.Windows.Forms.PreviewKeyDownEventHandler(this.Suppress_Arrow_Keys);
            this.menuStrip.ResumeLayout(false);
            this.menuStrip.PerformLayout();
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.pnl_bottom.ResumeLayout(false);
            this.pnl_bottom.PerformLayout();
            this.pnl_main.ResumeLayout(false);
            this.pnl_main.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.paddle)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.ball)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip;
        private System.Windows.Forms.ToolStripMenuItem tsMenuItem_file;
        private System.Windows.Forms.ToolStripMenuItem tsMenuItem_exit;
        private System.Windows.Forms.ToolStripMenuItem tsMenuItem_scores;
        private System.Windows.Forms.ToolStripMenuItem tsMenuItem_display;
        private System.Windows.Forms.ToolStripMenuItem tsMenuStrip_reset;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button btn_stop;
        private System.Windows.Forms.Button btn_start;
        private System.Windows.Forms.ComboBox cb_levels;
        private System.Windows.Forms.Label lbl_level;
        private System.Windows.Forms.Panel pnl_bottom;
        private System.Windows.Forms.Label lbl_score;
        private System.Windows.Forms.Panel pnl_main;
        private BricksEasy bricksEasy;
        private BricksMedium bricksMedium;
        private BricksHard bricksHard;
        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.Label lbl_countdown;
        private Ball ball;
        private Paddle paddle;
        private System.Windows.Forms.Timer timer_countdown;
    }
}

