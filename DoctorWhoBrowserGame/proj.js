/*************************************************************/
/******************* DOCTOR WHO GAME *************************/
// Kennedy Ballard
// Notes: Game works best in FireFox
// November 2017
/*************************************************************/

/********************/
/***** VARIABLES ****/
/********************/
var WIDTH;
var HEIGHT;
var MENU_OFFSET = 100;
var BIG_TEXT = "32pt";
var SMALL_TEXT = "24pt";

var FRAMERATE = 30;
var BETWEEN_LVLS = 8000;

var dalekSound;
var shadowSound;
var angelSound;

// PLAYER CONSTANTS
var PLAYER_HEIGHT = 60;
var PLAYER_WIDTH = 60;

var LIVES = 4;
var P_SPEED = 4.5;
var bonus = 0;

// DALEK CONSTANTS
var X = 80;
var Y = 50;
var DAL_NUM = 9;
var PERIMETER = 1;
var VERTICAL = 2;
var HORIZONTAL = 3;
var D_SPEED = 6;

// DALEK ARRAY
var daleks;
var exterminate;

// SHADOW CONSTANTS
var NUM_SHADOWS = 4;
var INTERVAL = 15;
var X_LIMIT = 900;
var Y_LIMIT = 700;
var START_POINT = 50;

// SHADOWS ARRAY
var shadows;

// ANGEL CONSTANTS
var NUM_ANGELS = 3;
var A_SPEED = 1;
var TICK = 150;
var TIME_ALLOWED = 20000;

var angels;
var endAngel;
var angelDeath;

// KEYCODES
var UP = 38;
var DOWN = 40;
var LEFT = 37;
var RIGHT = 39;
var ENTER = 13;
var INSTRUCT = 73;


// MOVEMENT CONTROL CONSTANTS
var ACCEL_RATE = 1;
var TO_RADIANS = Math.PI / 180;
var ROTATION_INCR = 2;

// CANVAS AND DIV/MENU VARIABLES
var c;
var ctx;
var can;
var background;
var divEl;
var intervalID;
var btnPosTop = MENU_OFFSET;

// USER LEVEL
var level = 1;

// PORTAL GATEWAY
var exit;

// END LEVEL --> USER HAS WON
var MAX_LEVEL = 2;

// ENDING CONDITION 
var gameOver = true;
var playerWon;

// STEERING VARIABLES
var rotationFactor = 0;
var me;

/*************************************************/
/************ CLASS SOUND ELEMENTS ***************/
/*************************************************/
class Sound {
  constructor(src_) {
    this._sound = document.createElement("audio");
    this._sound.src = src_;
    this._sound.volume = 0.1;
    document.body.appendChild(this._sound);
  }

  Play(){
    this._sound.play();
  }

  Stop(){
    this._sound.pause();
  }
}

/*********************************************/
/************ CLASS FOR PORTAL ***************/
/*********************************************/
class Portal {
  constructor() {
    var portal = document.createElement("img");
    portal.setAttribute("src", "portal.png");

    this._myimg = portal;
    this._width = portal.width / 2;
    this._height = portal.height / 2;

    this._x = WIDTH - this._width/2;
    this._y = HEIGHT - this._height/2;

    this._speed = 0.75;
    this._rotation = 0;
    this._rotationIncr = ROTATION_INCR;
  }

  Move(){
      this._x -= (Math.cos((this._rotation - 90) * TO_RADIANS) * this._speed) / 5;
      this._y -= (Math.sin((this._rotation - 90) * TO_RADIANS) * this._speed) / 5;
  }

  Rotate(){
    rotationFactor += this._rotationIncr;
    this._rotation = rotationFactor;
  }

  Draw(){
    //IMPLEMENT PROPER MOVEMENT --> NON LINEAR (with trig)
    ctx.save();
    ctx.translate(this._x, this._y);
    ctx.rotate(this._rotation * TO_RADIANS)
    ctx.translate(-(can.width/2), -(can.height/2));
    ctx.drawImage(this._myimg, can.width/2 - this._width/2, can.height/2 - this._height/2, this._width, this._height);
    ctx.restore();
  }
}

/*********************************************/
/******** CLASS FOR USER'S 'PLAYER' **********/
/*********************************************/
class Player {

  constructor() {
    // APPEARANCE
    // SETUP IMG ELEMENT FOR PLAYER
    var img = document.createElement("img");
    img.setAttribute("src", "tardis.png");

    this._myimg = img;
    this._width = img.width / 3;
    this._height = img.height / 3;
    this._x = 0;
    this._y = 0;

    this._lives = LIVES;
    this._time = 0;
    this._score = 0;

    // MOVEMENT  
    this._speed = P_SPEED;
    this._dir;
  }

  Move(){
    // EXECUTE MOVEMENT --> IF OUT OF BOUNDS, PLAYER WILL BOUNCE OFF WALLS
    switch(this._dir){
      case "W":
        this._x -= this._speed;

        if(this._x < 0)
          this._dir = "E";
        break;
      case "S":
        this._y += this._speed;

        if(this._y + this._height > HEIGHT)
          this._dir = "N";
        break;
      case "E":
        this._x += this._speed;

        if(this._x + this._width > WIDTH)
          this._dir = "W";
        break;
      case "N":
        this._y -= this._speed;

        if(this._y < 0)
          this._dir = "S";
        break;
      default:
        break;
    }
  }

  // DRAW PLAYER
  Draw(){
    ctx.drawImage(this._myimg, this._x, this._y, this._width, this._height);
  }
}

/*********************************************/
/********** CLASS FOR DALEK ENEMIES **********/
/*********************************************/
class Dalek {
  constructor(x, y, type){
    // APPEARANCE
    // SETUP IMG ELEMENT FOR DALEK
    var img = document.createElement("img");
    img.setAttribute("src", "dalek1.png");

    this._myimg = img;
    this._x = x;
    this._y = y;
    this._width = img.width/10;
    this._height = img.height/10;

    // DESCRIBES WHAT TYPE OF MOVEMENT THE DALEK EXECUTES:
    // 1. PERIMETER
    // 2. VERTICAL
    // 3. HORIZONTAL
    this._type = type;

    // MOVEMENT
    this._speed = 4;
    switch(this._type){
      case PERIMETER:
        this._dir = "S";
        break;
      case VERTICAL:
        this._dir = "N";
        break;
      case HORIZONTAL:
        this._dir = "E";
        break;
    }
  }

  Move(){
    // CHECK DALEKS OUT OF BOUNDS
    switch(this._type){
      case PERIMETER:
        this.CheckPerimBounds();
        break;
      case VERTICAL:
        this.CheckVertBounds();
        break;
      case HORIZONTAL:
        this.CheckHorizBounds();
        break;
    }

    // EXECUTE MOVEMENT
    switch(this._dir){
      case "W":
        this._x -= this._speed;
        break;
      case "S":
        this._y += this._speed;
        break;
      case "E":
        this._x += this._speed;
        break;
      case "N":
        this._y -= this._speed;
        break;
    }
  }
  
  // DALEKS GOING AROUND THE PERIMETER
  CheckPerimBounds(){
    // CHANGE DALEK DIRECTION TO STAY IN BOUNDS
    if(this._x < X){
      this._x = X;
      this._dir = "S"; 
    }   
    else if(this._y + this._height > HEIGHT - Y){
      this._y = (HEIGHT - Y) - this._height;
      this._dir = "E";
    }
    else if(this._x + this._width > WIDTH - X){
      this._x = (WIDTH - X) - this._width;
      this._dir = "N";
    }
    else if(this._y < Y){
      this._y = Y;
      this._dir = "W";
    }
  }
   
  // DALEKS MOVING TOP TO BOTTOM
  CheckVertBounds(){
    if(this._y < Y){
      this._y = Y;
      this._dir = "S"; 
    }  
    else if(this._y + this._height > HEIGHT - Y){
      this._y = (HEIGHT - Y) - this._height;
      this._dir = "N";
    }
  }

  // DALEKS MOVING LEFT TO RIGHT
  CheckHorizBounds(){
    if(this._x + this._width > WIDTH - X){
      this._x = (WIDTH - X) - this._width;
      this._dir = "W";
    }
    else if(this._x < X){
      this._x = X;
      this._dir = "E";
    }
  }
  // DRAW DALEKS
  Draw(){
    ctx.drawImage(this._myimg, this._x, this._y, this._width, this._height);
  }
}

/************************************************/
/********** CLASS FOR SHADOW ENEMIES ************/
/************************************************/

class Shadow {
  constructor(x, y, xTar, yTar){
    // APPEARANCE
    var img = document.createElement("img");
    img.setAttribute("src", "shadow2.png");

    this._myimg = img;
    this._x = x;
    this._y = y;

    this._height = this._myimg.height/1.5;
    this._width = this._myimg.width/1.5;

    // MOVEMENT
    this._xTarget = xTar;
    this._yTarget = yTar;
    this._tick = 0;
  }

  // MOVES SHADOW TO A RANDOM POINT ON CANVAS
  Move(){

    if(this._tick > 80){
      // GENERATE NEW RANDOM POINT
      this._xTarget = Math.ceil(Math.random() * X_LIMIT) - START_POINT;
      this._yTarget = Math.ceil(Math.random() * Y_LIMIT);
      this._tick = 0;
    }
    this._tick += 1.5;

    // MOVE
    this._x += (this._xTarget - this._x)/INTERVAL;
    this._y += (this._yTarget - this._y)/INTERVAL;
  }

  // DRAW SHADOW
  Draw(){
    ctx.drawImage(this._myimg, this._x, this._y, this._width, this._height);
  }
}

/************************************************/
/******* CLASS FOR WEEPING ANGEL ENEMIES ********/
/************************************************/
class Angel{
  constructor(x, y, image, ticker){
    // APPEARANCE
    var img = document.createElement("img");
    img.setAttribute("src", image);

    this._myimg = img
    this._x = x;
    this._y = y;
    this._width = this._myimg.width/3;
    this._height = this._myimg.height/3;

    // MOVEMENT
    this._speedx = A_SPEED;
    this._speedy = A_SPEED;
    this._tick = ticker;
  }

  // DRAW ANGEL
  Draw(){
  // CHANGE Y POSITION BASED ON TICKER
   if(this._tick < TICK/3)
      this._y = HEIGHT/4 - this._height/2;  
    else if(this._tick < 2 * TICK/3)
      this._y = 3*HEIGHT/4 - this._height/2;
    else
      this._y = HEIGHT/2 - this._height/2;

    // DRAW ANGEL
    ctx.drawImage(this._myimg, this._x, this._y, this._width, this._height);
  }
}

/****************************/
/****** LOAD EVERYTHING *****/
/****************************/
$(document).ready(Setup);

/**************************************************/
/************* FUNCTION FOR ONLOAD ****************/
/**************************************************/

function Setup() {
  /******************************/
  /* GENERATE CANVAS & ELEMENTS */
  /******************************/
  HEIGHT = window.innerHeight-20;
  WIDTH = window.innerWidth-20;
  // CREATE AND ADD CANVAS
  c = document.createElement("canvas");
  c.setAttribute("id", "mycanvas");
  c.setAttribute("height", HEIGHT);
  c.setAttribute("width", WIDTH);

  document.body.appendChild(c);
  $("#mycanvas").css("background-image", "url(main.jpg)");

  can = document.getElementById("mycanvas");
  ctx = can.getContext("2d");

  ctx.font = (BIG_TEXT + " Orbitron");
  ctx.fillStyle = 'white';
  ctx.textAlign = "center";

  // SETUP INTRO SOUND
  intro = new Sound("intro.mp3");
  intro._sound.loop = true;
  intro._sound.volume = 0.05;

  
  shadowSound = new Sound("whispers.mp3");
  angelSound = new Sound("echo.mp3");

  // INITIALIZE ENEMIES
  InitializeEnemies();
 
  /**************************/
  /* EVENT LISTENER SECTION */
  /**************************/
  $(document).keydown(KeyHandler);

  /*********************/
  /* DRAW START SCREEN */
  /*********************/
  intro.Play();
  DisplayStart();
}

/**********************************/
/**** INITIALIZE ENEMY ARRAYS *****/
/**********************************/
function InitializeEnemies(){
  /***** DALEKS *****/
  daleks = [];
  // INITIALIZE RANDOMLY PLACED DALEKS
  for(i=0; i < DAL_NUM; i++){
    type = (i >= 2*HORIZONTAL) ? (i - 2*HORIZONTAL) + 1 : ((i >= HORIZONTAL) ? (i - HORIZONTAL) + 1: i + 1);
    dx = Math.floor((Math.random() * (WIDTH - 2*X)) + X);
    dy = Math.floor((Math.random() * (HEIGHT - 2*Y)) + Y);
    d = new Dalek(dx, dy, type);
    daleks.push(d);
  }

  /***** SHADOW MONSTERS *****/
  shadows = [];
  // SETUP SHADOWS
  for(i = 1; i <= NUM_SHADOWS; i++){
    var shdw = new Shadow((i*WIDTH/NUM_SHADOWS), i*HEIGHT/NUM_SHADOWS, Math.random() * X_LIMIT, Math.random() * Y_LIMIT);
    shadows.push(shdw);
  }

  /***** WEEPING ANGELS *****/
  var image = "angel1.png";
  
    // SETUP RANDOMLY PLACED ANGELS
    angels = [];
    for(i = 0; i < NUM_ANGELS; i++){
      angel = new Angel(WIDTH/4 *(i+1), HEIGHT/4 * (i+1), image, TICK/3*i);
      angels.push(angel);
  
      // CHANGE IMAGES 
      image = i==0 ? "angel2.png" : "angel3.png";
    }
  
  /***** DEATH ANGEL *****/
  // SET UP IMAGE
  endAngel = new Angel(0, 0, "deathAngel.png", 0);
  endAngel._width *= 3;
  endAngel._height *=3;
  endAngel._x = WIDTH/2 - endAngel._width/2;
}

/*****************************************************/
/**************** EVENT LISTENERS ********************/
/*****************************************************/
// KEYCODES
function KeyHandler(e){
  switch(e.which){
    case UP:
      me._dir = "N";
      break;
    case DOWN:
      me._dir = "S";
      break;
    case LEFT:
      me._dir = "W";
      break;
    case RIGHT:
      me._dir = "E";
      break;
    case INSTRUCT:
      DisplayInstructions();
      break;
    case ENTER:
      if(gameOver)
        StartGame();     
      break;
    default:
      break;
  }
}

// BEGIN GAME
function StartGame(){
  /**********************/
  /**** INITIALIZE  *****/
  /**********************/
  playerWon = false;
  gameOver = false;
  level = 1;

  ctx.clearRect(0, 0, WIDTH, HEIGHT);

  // PLAYER
  me = new Player();

  // PORTAL
  exit = new Portal();
  $("#start").css("display", "none");
  LevelOneSetup();

  // SET GAME INTERVAL
  intervalID = setInterval(Draw, FRAMERATE);
}

/****************************************************/
/********** DISPLAY INSTRUCTIONS ********************/
/****************************************************/
function DisplayInstructions(){
  // STOP SOUNDS AND INTERVAL IF GAME HAS BEGUN
  if(!gameOver){
    angelSound.Stop();
    shadowSound.Stop();
    clearTimeout(angelDeath);
    clearInterval(dalekSound);
    clearInterval(intervalID);
    
    gameOver = true;
  }

  // DISPLAY INSTRUCTIONS
  $("#mycanvas").css("background-image", "url(main.jpg)");

  ctx.clearRect(0,0,WIDTH,HEIGHT);
  ctx.font = (BIG_TEXT + " Orbitron");
  ctx.fillText("HOW TO PLAY", WIDTH/2 , HEIGHT/5);

  ctx.font = (SMALL_TEXT + " Orbitron");
  ctx.fillText("Move the tardis using the arrow keys", WIDTH/3 , HEIGHT/3);
  ctx.fillText("You must avoid the enemies!", WIDTH/3 , HEIGHT/3 + MENU_OFFSET);
  ctx.fillText("Get to the portal to move to the next planet", WIDTH/3 , HEIGHT/3 + 2*MENU_OFFSET);
  
  ctx.fillText("Daleks & Weeping Angels will kill you if they hit you", 3*WIDTH/4 , HEIGHT/3 + 2*MENU_OFFSET);
  ctx.fillText("The Vashta Nerada will kill you if the tardis is enveloped in them", 3*WIDTH/4 , HEIGHT/3 + 3*MENU_OFFSET);
  ctx.fillText("Remember; don't blink!", WIDTH/5 , HEIGHT/3 + 4*MENU_OFFSET);
  
  ctx.font = (BIG_TEXT + " Orbitron");
  ctx.fillText("PRESS ENTER TO START", WIDTH/2 , 4*HEIGHT/5 + MENU_OFFSET);
}

/****************************************************/
/********** DISPLAY START SCREEN ********************/
/****************************************************/
function DisplayStart(){
  ctx.font = (BIG_TEXT + " Orbitron");
  ctx.fillText("DOCTOR WHO GAME", WIDTH/2 , HEIGHT/4);

  ctx.font = (SMALL_TEXT + " Orbitron");
  ctx.fillText("Press enter to play!", WIDTH/2 , HEIGHT/2);
  ctx.fillText("To see the instructions, press 'i'", WIDTH/2 , HEIGHT/2 + MENU_OFFSET);
}

/****************************************************/
/************* SETUP LEVEL ONE **********************/
/****************************************************/
function LevelOneSetup(){
  $("#mycanvas").css("background-image", "url(brick.jpg)");

  // DALEK SOUND --> EVERY FIVE SECONDS
  exterminate = new Sound("Exterminate.mp3");
  dalekSound = setInterval(function(){
    exterminate.Play();
  }, 5000);
}

/****************************************************/
/************* SETUP LEVEL TWO **********************/
/****************************************************/
function LevelTwoSetup(){
  // SETUP CANVAS
  $("#mycanvas").css("background-image", "url(wood1.jpg)");

  // SETUP PLAYER
  me._x = 0;
  me._y = 0;
  me._dir = "";

  shadowSound._sound.loop = true;
  shadowSound.Play();

  intervalID = setInterval(Draw, FRAMERATE);
}

/****************************************************/
/************* SETUP LEVEL THREE ********************/
/****************************************************/
function LevelThreeSetup(){
  //SETUP CANVAS
  $("#mycanvas").css("background-image", "url(cloth.jpg)");

  // SETUP PLAYER
  me._x = 0;
  me._y = 0;
  me._dir = "";

  angelDeath = setTimeout(AngelGameOver, TIME_ALLOWED);

  angelSound._sound.loop = true;
  angelSound.Play();

  intervalID = setInterval(Draw, FRAMERATE);
}

/****************************************************/
/************ RESET PLAYER --> LOST LIFE ************/
/****************************************************/
function ResetPlayerPos(x, y){
  me._x = x;
  me._y = y;
  me._dir = "None";
  me._lives -= 1;

  if(me._lives == 0)
    gameOver = true;
}
/****************************************************/
/*************** COLLISION DETECTION ****************/
/****************************************************/

  // CHECKS IF A DALEK OR ANGEL HAS COLLIDED WITH PLAYER
  function CheckCollision(enemy){
	  // CHECK COLLISION FROM EACH POSSIBLE APPROACH
	  var right = (enemy._x < me._x + me._width && enemy._x > me._x);
    var left = (enemy._x + enemy._width > me._x && enemy._x + enemy._width < me._x + me._width);
    var below = (enemy._y < me._y + me._height && enemy._y > me._y);
    var above = (enemy._y + enemy._height > me._y && enemy._y + enemy._height < me._y + me._height);
	
  	if((right && below) || (right && above) || (left && below) || (left && above)){
      ResetPlayerPos(0, 0);
	  }
  }

  // CHECKS IF A SHADOW CLOUD HAS ENGLUFED THE PLAYER
  function CheckIntersect(enemy){
    // CHECK COLLISION --> COVERS BOTH SIDES AND AT LEAST HALF THE HEIGHT OF PLAYER
    var leftAndRight = (enemy._x < me._x && enemy._x + enemy._width > me._x + me._width);
    var top = (enemy._y < me._y + me._height/2 && enemy._y + enemy._height > me._y + me._height);
    var bottom = (enemy._y < me._y + me._height/2 && enemy._y > me._y + me._height);

    if((leftAndRight && top) || (leftAndRight && bottom)){
      ResetPlayerPos(0, 0);
    }
  }

/****************************************************/
/***************** REACHED PORTAL *******************/
/****************************************************/
function CheckPortal(){
  if(me._x + me._width >= exit._x && me._y + me._height >= exit._y){
    clearInterval(intervalID);

    me._x = 0;
    me._y = 0;
    me._dir = "";

    var tele = new Sound("teleport.mp3");
    tele._sound.volume = 0.4;
    tele.Play();

    level ++;
    ctx.clearRect(0, 0, WIDTH, HEIGHT);
    switch(level){
      case 2:
        clearInterval(dalekSound);
        // DISPLAY THE SCORE
        ShowScore();
        // SETUP NEXT LEVEL
        setTimeout(LevelTwoSetup, BETWEEN_LVLS);
        break;
      case 3:
        shadowSound.Stop();
        // DISPLAY THE SCORE
        ShowScore();
        // SETUP NEXT LEVEL
        setTimeout(LevelThreeSetup, BETWEEN_LVLS)
        break;
      case 4:
        angelSound.Stop();
        // REMOVE TIME TRAP
        clearTimeout(angelDeath);
        gameOver = true; 
        playerWon = true;
        DisplayEndScreen();
        break;
    }
  }
}

/****************************************************/
/******** DISPLAY WEEPING ANGEL AT GAMEOVER *********/
/****************************************************/
// IF PLAYER TAKES TOO LONG TO COMPLETE LEVEL --> GAMEOVER
function AngelGameOver(){
  angelSound.Stop();
  ctx.clearRect(0,0,WIDTH, HEIGHT);
  clearInterval(intervalID);
  gameOver=true;
  me._lives = 0;
  //  SET UP SOUND
  var screech = new Sound("screech.mp3");
  screech._sound.volume = 1;

  // DISPLAY IMAGE AND PLAY SOUND
  ctx.drawImage(endAngel._myimg, endAngel._x, endAngel._y , endAngel._width, endAngel._height);
  screech.Play();

  
  // SHOW GAMEOVER
  setTimeout(DisplayEndScreen, 5000);
}

/****************************************************/
/********** DISPLAY END SCREEN AT GAMEOVER **********/
/****************************************************/
function DisplayEndScreen(){
  // STOP ALL SOUNDS
  clearInterval(dalekSound);

  // DISPLAY GAMEOVER
  $("#mycanvas").css("background-image", "url(main.jpg)");
  ctx.font = (BIG_TEXT + " Orbitron");
  var over = "GAME OVER";
  bonus = playerWon ? 100 : 0;

  if(playerWon){
    over += " --> YOU WON!!!!";
    me._score += bonus;
  }

  ctx.fillText(over, WIDTH/2, HEIGHT/6);
  ctx.fillText("Final Scores:", WIDTH/2, HEIGHT/6 + MENU_OFFSET);

  ShowScore();

  ctx.fillText("Press ENTER to play again!", WIDTH/2, HEIGHT/2 + 4*MENU_OFFSET);
}

/****************************************************/
/******************* DISPLAY SCORE ******************/
/****************************************************/
// IN BETWEEN LEVELS AND AT END OF GAME
function ShowScore(){
  $("#mycanvas").css("background-image", "url(main.jpg)");

  ctx.font = (SMALL_TEXT + " Orbitron");

  ctx.fillText("Bonus: " + bonus, WIDTH/2, HEIGHT/2 - MENU_OFFSET);
  // DISPLAY TIME
  var time = "Your time: " + me._time.toFixed(3) + " seconds";
  ctx.fillText(time, WIDTH/2, HEIGHT/2);
  ctx.fillText("Lives remaining: " + me._lives, WIDTH/2, HEIGHT/2 + MENU_OFFSET);
   ctx.fillText("Level reached: " + level, WIDTH/2, HEIGHT/2 + 2*MENU_OFFSET);
  // CALCULATE AND DISPLAY SCORE
  var timeScr = 1/me._time * 1000;
  me._score += Math.round(((timeScr * me._lives) + (timeScr * level))* level);
  var scoreString = "Score: " + me._score;

  ctx.fillText(scoreString, WIDTH/2, HEIGHT/2 + 3*MENU_OFFSET);
}

/****************************************************/
/****************** MAIN GAME LOOP ******************/
/****************************************************/
function Draw(){
  if(!gameOver){
    ctx.clearRect(0,0,WIDTH, HEIGHT);
    // DRAW & ROTATE PORTAL
    exit.Rotate();
    exit.Move();
    exit.Draw();

    // MOVE AND DRAW PLAYER
    me.Move();
    me.Draw();

    // INCREASE PLAYER TIME 
    // --> SECONDS = FRAMERATE/1 SECOND
    me._time += FRAMERATE/1000;

    switch(level){
      case 1:
        // MOVE AND DRAW DALEKS
        for(i = 0; i < DAL_NUM; i++){
          daleks[i].Move();
          daleks[i].Draw();

		      CheckCollision(daleks[i]);
        }
        break;
      case 2:
        // MOVE AND DRAW SHADOWS
        for(i = 0; i < NUM_SHADOWS; i++){
          shadows[i].Draw();
          shadows[i].Move();
  
          CheckIntersect(shadows[i]);
        }
        break;
      case 3:
        // DRAW AND CHANGE ANGEL POSITION
        for(i = 0; i < NUM_ANGELS; i++){    
          angels[i]._tick += 1.5;
          if(angels[i]._tick < TICK){
            angels[i].Draw();
            CheckCollision(angels[i]);
          }
          // RESET TICKER
          else if(angels[i]._tick > 2*TICK){
            angels[i]._tick = 0;
          }
        }
        break;
    }   
    CheckPortal();
  }
  else{
    // GAMEOVER
    clearInterval(intervalID);
    ctx.clearRect(0, 0, WIDTH, HEIGHT);
    clearTimeout(angelDeath);
    DisplayEndScreen();

    // STOP SOUNDS
    angelSound.Stop();
    clearInterval(dalekSound);
    shadowSound.Stop();
  }
}
