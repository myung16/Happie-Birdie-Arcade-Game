/* 
 * HappyBirdie.java
 * 6/5/2016
 */

//CREDIT TO:
//http://sweetclipart.com/landscape-scene-hills-bushes-and-sky-166
//http://opengameart.org/content/game-character-blue-flappy-bird-sprite-sheets
//http://www.appszoom.com/android_games/arcade_and_action/flappy-wings-not-flappy-bird_jozrb.html
//https://itch.io/jam/flappyjam
//http://gabustudio.com/tutorials/creating-html5-platform-game-tutorial-part-3
//http://www.growthengineering.co.uk/awesome-aligning-gamification-business-objectives/
//http://www.1001fonts.com/arcadeclassic-font.html
//http://forums.indiegamer.com/threads/vfx-artist-effects-paid.44538/
//http://www.softicons.com/social-media-icons/twitter-vector-icons-by-iconshock/twitter-blue-birdie-icon
//https://dribbble.com/shots/1451359-Eagle-Sprite-Sheet
//https://dribbble.com/shots/1579815-Game-Asset-Stylish-Bird-Game-Character-Sprite-Sheets

//we chose to import individual libraries as we need to save memory

//also this is alphabetically sorted
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class HappyBirdie implements ActionListener, MouseListener, KeyListener, WindowListener{
  //instantiators and global variable declarations and initializations
  public static HappyBirdie happyBirdie;
  private ArrayList<Rectangle> columns;
  private boolean gameOver, inputAlready, invinciblePower, smallPower, slowMoPower, flapWings;
  private double cloudSmallMove, cloudBigMove;
  private File highScoreFile;
  private final int WIDTH = 800, HEIGHT = 600;
  private Font customFont, customFont2;
  private Image gameIcon, birdImage, birdPowerImage, birdFlapImage, birdFlapPowerImage, powerUpFXImage, powerUpBarImage, powerUpBarOverLayImage,
    bgImage, cloudImage, groundImage, pipeImage, pipeHeadImage, playButtonImage, titleImage, menuBird, menuBird2, menuBird3;
  private int ticks, yMotion, score, scoreTick, speed, bgMove, bgMovePower, groundMove, currentPowerUpCount, powerTimeLeft, gameScreen;
  private int speedPower = 10, powerUp = -1, maxPowerUpCount = 5, diffChoice = 1;
  private int[] highScoresEasy, highScoresMed, highScoresHard;
  private java.util.Timer powerUpTimer;
  private javax.swing.Timer timer;
  private PrintWriter out;
  private Random rand;
  private Rectangle bird, playButton, exitButton, instructionButton, highScoreButton, highScoreToggleButton,
    char1Button, char2Button, char3Button, diffEasyButton, diffMedButton, diffHardButton;
  private Render render;
  private String name;
  private String birdChoice = "blueBird";
  private String[] highNamesEasy, highNamesMed, highNamesHard;
  private URL arcadeClassic;
  
  /*
   * Constructor that creates random, frame and timer objects for use in other methods. This method also sets up the frame,
   * creates the bird and column objects and calls the addColumn method thrice so that there can be columns on and off screen at all times.
   */
  public HappyBirdie(){
    //object instantiating
    JFrame frame = new JFrame();
    timer = new javax.swing.Timer(25, this);
    render = new Render();
    rand = new Random();
    highScoresEasy = new int[3];
    highNamesEasy = new String[highScoresEasy.length];
    highScoresMed = new int[3];
    highNamesMed = new String[highScoresMed.length];
    highScoresHard = new int[3];
    highNamesHard = new String[highScoresHard.length];
    
    //menu assets
    playButtonImage = new ImageIcon("Assets//playButton.png").getImage();
    titleImage = new ImageIcon("Assets//titleImage.png").getImage();
    menuBird = new ImageIcon("Assets//blueBird.png").getImage();
    menuBird2 = new ImageIcon("Assets//whiteBird.png").getImage();
    menuBird3 = new ImageIcon("Assets//eagleBird.png").getImage();
    
    //game assets
    birdChoiceModifiers();
    powerUpFXImage = new ImageIcon("Assets//powerUpFX.gif").getImage();
    bgImage = new ImageIcon("Assets//background.png").getImage();
    cloudImage = new ImageIcon("Assets//cloud.png").getImage();
    groundImage = new ImageIcon("Assets//ground.png").getImage();
    pipeImage = new ImageIcon("Assets//pipe.png").getImage();
    pipeHeadImage = new ImageIcon("Assets//pipeHead.png").getImage();
    powerUpBarImage = new ImageIcon("Assets//powerUpBar.png").getImage();
    powerUpBarOverLayImage = new ImageIcon("Assets//powerUpBarOverLay.png").getImage();
    
    //other assets
    gameIcon = new ImageIcon("Assets//gameIcon.png").getImage();
    
    //highscore text file and reader/out instantiating
    highScoreFile = new File("Assets//happyBirdieHS.txt");
    if(highScoreFile.exists()){
      try{
        Scanner readerTemp = new Scanner(highScoreFile);
        if(!readerTemp.hasNextInt()){
          PrintWriter outTemp = new PrintWriter(new FileWriter(highScoreFile));
          for(int i = 0; i < highScoresEasy.length + highScoresMed.length + highScoresHard.length; i++){
            outTemp.println(0);
            outTemp.println("empty");
          }
          outTemp.close();
        }
        readerTemp.close();
        Scanner reader = new Scanner(highScoreFile);
        for(int i = 0; i < highScoresEasy.length; i++){
          highScoresEasy[i] = reader.nextInt();
          reader.nextLine();
          highNamesEasy[i] = reader.nextLine();
        }
        for(int i = 0; i < highScoresMed.length; i++){
          highScoresMed[i] = reader.nextInt();
          reader.nextLine();
          highNamesMed[i] = reader.nextLine();
        }
        for(int i = 0; i < highScoresHard.length; i++){
          highScoresHard[i] = reader.nextInt();
          reader.nextLine();
          highNamesHard[i] = reader.nextLine();
        }
        reader.close();
      }
      catch(IOException e){}
    }
    
    //font import and registering
    try{
      arcadeClassic = getClass().getClassLoader().getResource("Assets//arcadeClassic.ttf");
      customFont = Font.createFont(Font.TRUETYPE_FONT, arcadeClassic.openStream());
      customFont = customFont.deriveFont(1, 100);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(customFont);
    }catch (Exception e) {}
    
    //frame properties
    frame.setTitle("Happy Birdie");
    frame.setSize(WIDTH, HEIGHT);
    frame.setIconImage(gameIcon);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//closes on close button
    frame.setResizable(false); //unresizable
    frame.setLocationRelativeTo(null); //centers windows
    frame.add(render);
    frame.addMouseListener(this);
    frame.addWindowListener(this);
    frame.addKeyListener(this);
    frame.setVisible(true);
    
    bird = new Rectangle(WIDTH / 2, HEIGHT / 2 - 10, 50, 35);//creates the rectangle object(bird)
    columns = new ArrayList<Rectangle>();
    
    //button rectangles on menu
    playButton = new Rectangle(635, 420, 140, 135);
    exitButton = new Rectangle(15, 463, 90, 90);
    instructionButton = new Rectangle(595, 225, 150, 70);
    highScoreButton = new Rectangle(595, 300, 150, 70);
    highScoreToggleButton = new Rectangle(5, 0, 200, 50);
    char1Button = new Rectangle(WIDTH / 2 - 100, 200, 110, 75); //original bluebird
    char2Button = new Rectangle(WIDTH / 2 - 100, 300, 110, 70); //white bird sunglasses
    char3Button = new Rectangle(WIDTH / 2 - 100, 400, 110, 70); //eagle bird
    diffEasyButton = new Rectangle(25, 200, 100, 40);
    diffMedButton = new Rectangle(25, 250, 100, 40);
    diffHardButton = new Rectangle(25, 300, 100, 40);
    
    slowMoModifiers(false);
    
    //add it three times so that there's 2 on screen and one on side that peeks in
    addColumn(true);
    addColumn(true);
    addColumn(true);
    
    timer.start();
  }
  
  /*
   * Adds 2 rectangles when called, randomly selecting the positions within a certain criteria
   * @param start is a boolean that is used to determine if the program should print the columns coming up or the columns far off the screen
   */
  private void addColumn(boolean start){
    int space = 250;//space inbetween rectangles
    int width = 100;//width of rectangles
    int height = 50 + rand.nextInt(250);//no less than 50 and no greater than 300
    
    if(start){
      //adds two rectangles(top and bottom ones) to the array list
      columns.add(new Rectangle(WIDTH + width + columns.size() * 250,//from the right plus width of rectangle plus the size of the array times 300
                                HEIGHT - height - 120,//from the bottom minus the height of columns minus 120(ground length)
                                width, height));
      columns.add(new Rectangle(WIDTH + width + ((columns.size() - 1) * 250),//from the right plus width of rectangle plus the size of the array times 300
                                0, width,
                                HEIGHT - height - space));//from the bottom minus the height of columns minus the space between the rectangles
    }
    else{
      //adds two rectangles(top and bottom ones) to the array list
      //this is different from the one above as this one is the next column that is off-screen
      //therefore it is here to keep 2 columns in the array at all times
      columns.add(new Rectangle(columns.get(columns.size() - 1).x + 400,//take last column to get its position and add 600 for the bottom column
                                HEIGHT - height - 120,
                                width, height));
      columns.add(new Rectangle(columns.get(columns.size() - 1).x,//take last column to get its position for the top column
                                0, width,
                                HEIGHT - height - space));
    }
  }
  
  /*
   * Paints the column that was passed into as a paramater
   * @param g is the instance of Graphics. It is used to set the color and fill a rectangle
   * @param column is a rectangle that is to be drawn
   */
  private void paintColumn(Graphics g, Rectangle column){
    if(column.y <= 50){
      g.drawImage(pipeImage, column.x, column.y + column.height, column.width, -column.height, null);
      g.drawImage(pipeHeadImage, column.x, column.y + column.height, pipeHeadImage.getWidth(null), -pipeHeadImage.getHeight(null), null);//45 is the pipe head height
    }
    else{
      g.drawImage(pipeImage, column.x, column.y, column.width, column.height, null);
      g.drawImage(pipeHeadImage, column.x, column.y, pipeHeadImage.getWidth(null), pipeHeadImage.getHeight(null), null);//45 is the pipe head height
    }
  }
  
  /*
   * Checks if the game has not started yet or if the game is over. If the game is over, start the game, place bird
   * in middle, reset score and add columns. If the game has not started, start it. If the game is already started and 
   * the jump method is called, then make the bird jump.
   */
  private void jump(){
    scoreTick++;
    if(gameOver){
      bird = new Rectangle(WIDTH / 2, HEIGHT / 2 - 10, 50, 35);//creates the rectangle object(bird)
      columns.clear();
      yMotion = 0;
      score = 0;
      addColumn(true);
      addColumn(true);
      addColumn(true);
      inputAlready = false;
      gameOver = false;
    }
    if(yMotion > 0)
      yMotion = 0;
    yMotion -= 10;
  }
  
  /*
   * This method's only purpose is to be called to fill in the spot of 1 line instead of two.
   * It enables slow motion modifiers(so the speed at which everything moves)
   * 
   * @param slowMoOn is the boolean indication whether to turn of the slow motion indicators or on
   */
  private void slowMoModifiers(boolean slowMoOn){
    if(slowMoOn){
      speedPower = 6;//speed the columns move at
      bgMovePower = 1;
    }
    else{
      if(diffChoice == 3)
        speedPower = 13;//speed the columns move at
      else
        speedPower = 10;//speed the columns move at
      bgMovePower = 2;
    }
  }
  
  /*
   * This method's only purpose is to set all the modifications to off/false when game is over
   */
  private void gameOverModifiers(){
    flapWings = false;
    currentPowerUpCount = 0;
    powerUp = -1;
    invinciblePower = false;
    smallPower = false;
    slowMoPower = false;
    bird = new Rectangle(bird.x, bird.y, 50, 35);
    slowMoModifiers(false);
  }
  
  /*
   * This method's only purpose is to be called to grab all the assets when the user changes birds
   */
  public void birdChoiceModifiers(){
    birdImage = new ImageIcon("Assets//" + birdChoice + ".png").getImage();
    birdFlapImage = new ImageIcon("Assets//" + birdChoice + "Flap.gif").getImage();
    birdPowerImage = new ImageIcon("Assets//" + birdChoice + "Power.png").getImage();
    birdFlapPowerImage = new ImageIcon("Assets//" + birdChoice + "FlapPower.gif").getImage();
  }
  
  /*
   * finds the index of the sorting of the scores array then uses that index to sort the name array as sorted with the scores array
   * @param names which is an array that is sorted based on the sorting of the scores array
   * @param scores which is an array that is sorted based on the higher next number of the iteration (for loop)
   */
  public void scoreSort(String[] names, int[] scores) {
    for(int i = scores.length - 1; i >= 1; i--) {
      String temp;
      int currentMax = scores[0];
      int currentMaxIndex = 0;
      for(int j = 1; j <= i; j++) {
        if (currentMax > scores[j]) {
          currentMax = scores[j];
          currentMaxIndex = j;
        }
      }
      if (currentMaxIndex != i) {
        temp = names[currentMaxIndex];
        names[currentMaxIndex] = names[i];
        names[i] = temp;
        scores[currentMaxIndex] = scores[i];
        scores[i] = currentMax;
      }
    }
  }
  
  /*
   * Inherited method from ActionListener. Used to move the columns,
   * delete columns that have passed, cause the bird to jump, and check for collisions.
   * Calls for a repaint when done.
   * @param e is the variable for the ActionEvent. Unused in this method.
   */
  @Override
  public void actionPerformed(ActionEvent e){
    speed = speedPower;//speed the columns move at
    ticks++;//ticks are implemented into the program because we dont want the birdie to start falling down right after you click
    
    //moves the columns if the game has started
    if(gameScreen == 1){
      for(int index = 0; index < columns.size();index++){
        Rectangle column = columns.get(index);
        column.x -= speed;
      }
      
      if(ticks % 2 == 0 && yMotion < 15)
        yMotion += 2;
      
      //iterator through all columns to check if they passed the screen to remove them from the list
      for(int index = 0; index < columns.size();index++){
        Rectangle column = columns.get(index);
        if(column.x + column.width < 0){
          columns.remove(column);
          if(column.y == 0)
            addColumn(false);
        }
      }
      
      bird.y += yMotion;
      
      for(Rectangle column : columns){
        //checks if the bird is in the exact space between the rectangles
        if(column.y == 0 && !gameOver && !invinciblePower && !slowMoPower && scoreTick >= 1 && bird.y + bird.height > column.y + column.height && bird.y + bird.height < column.y + column.height + 250 
             && bird.x + bird.width > column.x + column.width - 5 && bird.x + bird.width < column.x + column.width + 5){
          if(powerUp < 0)
            currentPowerUpCount++;
          score++;
          scoreTick = 0;
        }
        //this is for invincibility
        else if(column.y == 0 && !gameOver && invinciblePower && bird.x + bird.width > column.x + column.width - 5 && bird.x + bird.width < column.x + column.width + 5 && bird.y < HEIGHT ){
          score++;
          scoreTick = 0;
        }
        //this is for small power
        else if(column.y == 0 && !gameOver && slowMoPower && scoreTick >= 2 && bird.y + bird.height > column.y + column.height && bird.y + bird.height < column.y + column.height + 250 
                  && bird.x + bird.width > column.x + column.width - 5 && bird.x + bird.width < column.x + column.width + 5){
          score += 3;
          scoreTick = 0;
        }
        
        if(column.intersects(bird) && !invinciblePower){
          gameOver = true;
          if(bird.x < column.x)
            bird.x = column.x - bird.width;//column moves the bird when it dies
          else{
            //to make it slide over the columns
            if(column.y != 0)//if it is not the top column that its intersecting
              bird.y = column.y - bird.height;
            else if(bird.y < column.height)
              bird.y = column.height;
          }
        }
      }
      
      if((bird.y > HEIGHT - 120 + 5 - bird.height || bird.y <= 0) && !invinciblePower)//if it goes below or above the limits then it is game over
        gameOver = true;
      
      if(bird.y <= 0){//dont let it go over the top
        bird.y = 0;
        yMotion = 0;
      }
      if(bird.y + yMotion >= HEIGHT - 120)//if it hits the ground, it will stay there
        bird.y = HEIGHT - 120 + 5 - bird.height;
      
      if(gameOver){
        gameOverModifiers();
        //copy array to "current" array
        int[] highScoresCurrent = new int[highScoresEasy.length];
        String[] highNamesCurrent = new String[highNamesEasy.length];
        String tempDiff = "";
        if(diffChoice == 1){
          tempDiff = "Easy";
          System.arraycopy(highScoresEasy, 0, highScoresCurrent, 0, highScoresEasy.length);
          System.arraycopy(highNamesEasy, 0, highNamesCurrent, 0, highNamesEasy.length);
        }
        else if(diffChoice == 2){
          tempDiff = "Medium";
          System.arraycopy(highScoresMed, 0, highScoresCurrent, 0, highScoresMed.length);
          System.arraycopy(highNamesMed, 0, highNamesCurrent, 0, highNamesMed.length);
        }
        else if(diffChoice == 3){
          tempDiff = "Hard";
          System.arraycopy(highScoresHard, 0, highScoresCurrent, 0, highScoresHard.length);
          System.arraycopy(highNamesHard, 0, highNamesCurrent, 0, highNamesHard.length);
        }
        for(int i = 0; i < highScoresCurrent.length; i++){
          if(score > highScoresCurrent[i] && !inputAlready){
            String tempName = highNamesCurrent[i];
            boolean validInput = false;
            do{
              if(highNamesCurrent[i].equals("empty")){
                name = JOptionPane.showInputDialog("Please input a name to set a new high score of " + score +
                                                   " at position number " + (i + 1) + " on " + tempDiff + " difficulty" +
                                                   ". Should not exceed 10 characters.");
              }
              else
                name = JOptionPane.showInputDialog("Please input a name for the new high score of " + score + " that " +
                                                   "will replace " + tempName + "'s score of " + highScoresCurrent[i] + 
                                                   " at position number " + (i + 1) + " on " + tempDiff + " difficulty" +
                                                   ". Should not exceed 10 characters.");
              //checks for a valid input for name or else it wont let the user go through
              if(name == null)
                validInput = false;
              else{
                validInput = true;
                if(name.length() > 10 || name.length() == 0)
                  validInput = false;
              }
            }while(!validInput);
            
            //copies temp arrays to use in sorting with the new scores then copies back into the original arrays, cutting out the last position
            int[] highScoresTemp = new int[4];
            System.arraycopy(highScoresCurrent, 0, highScoresTemp, 0, highScoresCurrent.length);
            String[] highNamesTemp = new String[4];
            System.arraycopy(highNamesCurrent, 0, highNamesTemp, 0, highNamesCurrent.length);
            highScoresTemp[3] = score;
            highNamesTemp[3] = name;
            scoreSort(highNamesTemp, highScoresTemp);
            System.arraycopy(highScoresTemp, 0, highScoresCurrent, 0, highScoresCurrent.length);
            System.arraycopy(highNamesTemp, 0, highNamesCurrent, 0, highNamesCurrent.length);
            
            //copy "current" array to the gamemode array
            if(diffChoice == 1){
              System.arraycopy(highScoresCurrent, 0, highScoresEasy, 0, highScoresEasy.length);
              System.arraycopy(highNamesCurrent, 0, highNamesEasy, 0, highNamesEasy.length);
            }
            else if(diffChoice == 2){
              System.arraycopy(highScoresCurrent, 0, highScoresMed, 0, highScoresMed.length);
              System.arraycopy(highNamesCurrent, 0, highNamesMed, 0, highNamesMed.length);
            }
            else if(diffChoice == 3){
              System.arraycopy(highScoresCurrent, 0, highScoresHard, 0, highScoresHard.length);
              System.arraycopy(highNamesCurrent, 0, highNamesHard, 0, highNamesHard.length);
            }
            
            inputAlready = true;
            break;
          }
        }
      }
      else{
        flapWings = true;
        //power-ups
        if(currentPowerUpCount == maxPowerUpCount){
          currentPowerUpCount = 0;
          powerTimeLeft = 6;//length of powerup ability (5 sec)
          powerUp = rand.nextInt(3) + 1;//randomly generates a power up
          powerUpTimer = new java.util.Timer(true);//pass true because we want it to be a daemon timer so that it wont continuously run past the games stopping point
          powerUpTimer.scheduleAtFixedRate(new TimerTask() {
            public void run(){
              powerTimeLeft--;
              if(powerTimeLeft == 0){
                gameOverModifiers();
                powerUpTimer.cancel();
              }
            }
          }, (long) 0, (long) 1000);
          
          if(powerUp == 1)//invincibility
            invinciblePower = true;
          else if(powerUp == 2){//smaller bird
            smallPower = true;
            bird = new Rectangle(bird.x, bird.y, 30, 20);
          }
          else if(powerUp == 3){
            slowMoPower = true;
            slowMoModifiers(true);
          }
        }
      }
    }
    render.repaint();//paints everything again in another class so that the program can run while painting
  }
  
  /*
   * Repaints all assests in the frame(background, ground, grass, bird, columns and strings[game over, start click, score and highscore]).
   * @param g is used as a Graphics instance to call methods pertaining to Graphics(drawImage, setColor, etc.)
   */
  public void repaint(Graphics g){
    g.setClip(0, 0, WIDTH, HEIGHT);//only renders what is on screen
    g.setColor(Color.black);
    
    bgMove -= bgMovePower;//speed the background moves at
    cloudBigMove -= bgMovePower / 2.0;//speed the clouds move at
    cloudSmallMove -= bgMovePower / 4.0;//speed the clouds move at
    groundMove -= speed;//speed the ground moves at
    
    //paints infinite background (scrolling)
    if(bgMove <= -WIDTH)
      bgMove = 0;
    g.drawImage(bgImage, bgMove, 0, WIDTH, HEIGHT, null);
    g.drawImage(bgImage, WIDTH + bgMove, 0, WIDTH, HEIGHT, null);
    
    //paints infinite big clouds (scrolling)
    if(cloudBigMove <= -WIDTH)
      cloudBigMove = 0;
    g.drawImage(cloudImage, (int)cloudBigMove, 52, WIDTH, 100, null);
    g.drawImage(cloudImage, WIDTH + (int)cloudBigMove, 52, WIDTH, 100, null);
    
    //paints infinite small clouds (scrolling)
    if(cloudSmallMove <= -(WIDTH / 3))
      cloudSmallMove = 0;
    for(int count = 0; count <= 3; count++)
      g.drawImage(cloudImage, WIDTH / 3 * count + (int)cloudSmallMove, 0, WIDTH / 3, 35, null);
    
    //paints infinite ground (scrolling)
    if(groundMove <= -groundImage.getWidth(null))
      groundMove = 0;
    for(int count = 0; count <= 2; count++)
      g.drawImage(groundImage, groundImage.getWidth(null) * count + groundMove, HEIGHT - 130, null);//paints the ground
    
    if(gameScreen == 0){//menu
      //Title Image
      g.drawImage(titleImage, 30, -5, 730, 200, null);
      customFont = customFont.deriveFont(1, 100);
      g.setFont(customFont);
      g.drawString("Happy Birdie", WIDTH / 2 - 350, HEIGHT / 2 - 140);
      
      g.drawImage(playButtonImage, playButton.x, playButton.y, playButton.width, playButton.height, null);//Play Button Image
      
      //Character Select images (credit to yonatan for idea for selection :D)(credit to momin for being dat boi)
      if(birdChoice.equals("blueBird"))
        g.drawImage(menuBird, char1Button.x - 10, char1Button.y - 10, char1Button.width + 10, char1Button.height + 10, null);
      else
        g.drawImage(menuBird, char1Button.x, char1Button.y, char1Button.width, char1Button.height, null);
      if(birdChoice.equals("whiteBird"))
        g.drawImage(menuBird2, char2Button.x - 10, char2Button.y - 10, char2Button.width + 10, char2Button.height + 10, null);
      else
        g.drawImage(menuBird2, char2Button.x, char2Button.y, char2Button.width, char2Button.height, null);
      if(birdChoice.equals("eagleBird"))
        g.drawImage(menuBird3, char3Button.x - 10, char3Button.y - 10, char3Button.width + 10, char3Button.height + 10, null);
      else
        g.drawImage(menuBird3, char3Button.x, char3Button.y, char3Button.width, char3Button.height, null);
      
      customFont = customFont.deriveFont(0, 20);
      g.setFont(customFont);
      
      //difficulty selection
      if(diffChoice == 1){//easy
        g.drawImage(titleImage, diffEasyButton.x - 5, diffEasyButton.y, diffEasyButton.width + 5, diffEasyButton.height + 10, null);
        g.drawString("Easy", diffEasyButton.x + 10, diffEasyButton.y + diffEasyButton.height);
      }
      else{
        g.drawImage(titleImage, diffEasyButton.x, diffEasyButton.y, diffEasyButton.width, diffEasyButton.height, null);
        g.drawString("Easy", diffEasyButton.x + 10, diffEasyButton.y + diffEasyButton.height - 7);
      }
      if(diffChoice == 2){//medium
        g.drawImage(titleImage, diffMedButton.x - 5, diffMedButton.y, diffMedButton.width + 5, diffMedButton.height + 10, null);
        g.drawString("Medium", diffMedButton.x + 10, diffMedButton.y + diffMedButton.height);
      }
      else{
        g.drawImage(titleImage, diffMedButton.x, diffMedButton.y, diffMedButton.width, diffMedButton.height, null);
        g.drawString("Medium", diffMedButton.x + 10, diffMedButton.y + diffMedButton.height - 7);
      }
      if(diffChoice == 3){//hard
        g.drawImage(titleImage, diffHardButton.x - 5, diffHardButton.y, diffHardButton.width + 5, diffHardButton.height + 10, null);
        g.drawString("Hard", diffHardButton.x + 10, diffHardButton.y + diffHardButton.height);
      }
      else{
        g.drawImage(titleImage, diffHardButton.x - 5, diffHardButton.y, diffHardButton.width, diffHardButton.height, null);
        g.drawString("Hard", diffHardButton.x + 10, diffHardButton.y + diffHardButton.height - 7);
      }
      
      //Instruction Button
      g.drawImage(titleImage, instructionButton.x + instructionButton.width, instructionButton.y, -instructionButton.width, instructionButton.height - 15, null);
      g.drawString("Instructions", instructionButton.x + 10, instructionButton.y + instructionButton.height - 27);
      
      //Highscores button
      g.drawImage(titleImage, highScoreButton.x + highScoreButton.width, highScoreButton.y, -highScoreButton.width, highScoreButton.height - 15, null);
      g.drawString("High Scores", highScoreButton.x + 10, highScoreButton.y + highScoreButton.height - 27);
      
      //credits at bottom
      g.drawString("Pravinthan  Prabagaran", 0, HEIGHT - 44);
      g.drawString("Marcus  Yung", 0, HEIGHT - 30);
    }
    else if (gameScreen == 1){//game itself
      //paints each column
      for(Rectangle column : columns)
        paintColumn(g, column);
      
      //paint the powerup meter
      g.fillRect(50, (HEIGHT - 135 - 425) / 2 + 5 + 95, 35, 305);    
      if(!gameOver || currentPowerUpCount < maxPowerUpCount){
        g.setColor(new Color(212, 175, 55));
        if(!invinciblePower && !smallPower && !slowMoPower)
          g.fillRoundRect(52, 422 - 300 / maxPowerUpCount * currentPowerUpCount, 20, (HEIGHT - 135 - 425) / 2 + 300 / maxPowerUpCount * currentPowerUpCount, 30, 30);
        else
          g.fillRoundRect(52, 122, 20, 320, 30, 30);
      }
      g.drawImage(powerUpBarImage, 5, (HEIGHT - 137 - 425) / 2 + 5, 115, 425, null);
      g.drawImage(powerUpBarOverLayImage, 5, (HEIGHT - 137 - 425) / 2 + 5, 115, 106, null);
      
      g.setColor(Color.black);
      
      //paints the bird on power meter and normal flying-bird
      if(currentPowerUpCount >= maxPowerUpCount / 2 + 1 || invinciblePower || smallPower || slowMoPower){
        if (invinciblePower || smallPower || slowMoPower)
          g.drawImage(powerUpFXImage, bird.x - 40 / 2, bird.y - 40 / 2, bird.width + 40, bird.height + 40, null);
        g.drawImage(birdPowerImage, 15, (HEIGHT - 137 - 387) / 2, 105, 75, null);
        if(flapWings)
          g.drawImage(birdFlapPowerImage, bird.x, bird.y, bird.width, bird.height, null);
        else
          g.drawImage(birdPowerImage, bird.x, bird.y, bird.width, bird.height, null);
      }
      else{
        g.drawImage(birdImage, 15, (HEIGHT - 137 - 387) / 2, 105, 75, null);
        if(flapWings)
          g.drawImage(birdFlapImage, bird.x, bird.y, bird.width, bird.height, null);
        else
          g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);
      }
      
      //paints the game over
      customFont2 = customFont.deriveFont(1, 100);
      g.setFont(customFont2);
      if(gameOver)
        g.drawString("Game Over!", WIDTH / 2 - 250, HEIGHT / 2 - 50);
      
      //copy array to "current" array for score outputs
      int[] highScoresCurrent = new int[highScoresEasy.length];
      String[] highNamesCurrent = new String[highNamesEasy.length];
      if(diffChoice == 1){
        System.arraycopy(highScoresEasy, 0, highScoresCurrent, 0, highScoresEasy.length);
        System.arraycopy(highNamesEasy, 0, highNamesCurrent, 0, highNamesEasy.length);
      }
      else if(diffChoice == 2){
        System.arraycopy(highScoresMed, 0, highScoresCurrent, 0, highScoresMed.length);
        System.arraycopy(highNamesMed, 0, highNamesCurrent, 0, highNamesMed.length);
      }
      else if(diffChoice == 3){
        System.arraycopy(highScoresHard, 0, highScoresCurrent, 0, highScoresHard.length);
        System.arraycopy(highNamesHard, 0, highNamesCurrent, 0, highNamesHard.length);
      }
      
      //paints score dynamically with score value
      if(score < 10){
        g.drawString(String.valueOf(score), WIDTH / 2 - 100, 100);//score print
        g.drawString(String.valueOf(highScoresCurrent[0]), WIDTH / 2 + 100, 100);//highscore print
        g.fillRect(WIDTH / 2 - 25 + 28, 65, (WIDTH / 2 + 25) - (WIDTH / 2 - 25), 10);
      }
      else{
        g.drawString(String.valueOf(score), WIDTH / 2 - 150, 100);//score print
        g.drawString(String.valueOf(highScoresCurrent[0]), WIDTH / 2 + 150, 100);//highscore print
        g.fillRect(WIDTH / 2 - 38 + 53, 65, (WIDTH / 2 + 38) - (WIDTH / 2 - 38), 10);
      }
      
      //power up time left drawing on screen
      customFont = customFont.deriveFont(1, 24);
      g.setFont(customFont);
      if(invinciblePower){
        //invincibility time left print
        g.drawString("Invincibility!", WIDTH / 2 - 100, 475);
        g.setFont(customFont2);
        g.drawString(String.valueOf(powerTimeLeft), WIDTH / 2 + 100, 485);
      }
      else if(smallPower){
        //Reduced Size time left print
        g.drawString("Reduced Size!", WIDTH / 2 - 100, 475);
        g.setFont(customFont2);
        g.drawString(String.valueOf(powerTimeLeft), WIDTH / 2 + 70, 485);
      }
      else if(slowMoPower){
        //Slow Motion time left print
        g.drawString("Slow Motion Bonus Points!", WIDTH / 2 - 180, 475);
        g.setFont(customFont2);
        g.drawString(String.valueOf(powerTimeLeft), WIDTH / 2 + 150, 485);
      }
    }
    else if (gameScreen == 2) {//instuction screen
      customFont = customFont.deriveFont(1, 80);
      g.setFont(customFont);
      g.drawString("Instructions", WIDTH / 2 - 295, HEIGHT / 2 - 200);
      customFont = customFont.deriveFont(1, 24);
      g.setFont(customFont);
      g.drawString("Click the mouse or space bar to keep the bird up in the air!", WIDTH / 2 - 350, 150);
      g.drawString("Pass through the columns to score points!", WIDTH / 2 - 350, 200);
      g.drawString("You get a random power up every couple points!", WIDTH / 2 - 350, 250);
      g.drawString("The top scores are saved on 3 different leaderboards!", WIDTH / 2 - 350, 300);
      g.drawString("Invincibility     Reduced Size     Slow Motion", WIDTH / 2 - 350, 410);
      customFont = customFont.deriveFont(1, 48);
      g.setFont(customFont);
      g.drawString("POWER UPS", WIDTH / 2 - 350, 380);
    }
    else if(gameScreen == 3){//Easy high score screen
      customFont = customFont.deriveFont(1, 80);
      g.setFont(customFont);
      //1st
      g.drawString(highNamesEasy[0], WIDTH / 2 - 250, HEIGHT / 2 - 100);
      g.drawString(String.valueOf(highScoresEasy[0]), WIDTH - 125, HEIGHT / 2 - 100);
      //2nd
      g.drawString(highNamesEasy[1], WIDTH / 2 - 250, HEIGHT / 2);
      g.drawString(String.valueOf(highScoresEasy[1]), WIDTH - 125, HEIGHT / 2);
      //3rd
      g.drawString(highNamesEasy[2], WIDTH / 2 - 250, HEIGHT / 2 + 100);
      g.drawString(String.valueOf(highScoresEasy[2]), WIDTH - 125, HEIGHT / 2 + 100);
    }
    else if(gameScreen == 4){//Medium high score screen
      customFont = customFont.deriveFont(1, 80);
      g.setFont(customFont);
      //1st
      g.drawString(highNamesMed[0], WIDTH / 2 - 250, HEIGHT / 2 - 100);
      g.drawString(String.valueOf(highScoresMed[0]), WIDTH - 125, HEIGHT / 2 - 100);
      //2nd
      g.drawString(highNamesMed[1], WIDTH / 2 - 250, HEIGHT / 2);
      g.drawString(String.valueOf(highScoresMed[1]), WIDTH - 125, HEIGHT / 2);
      //3rd
      g.drawString(highNamesMed[2], WIDTH / 2 - 250, HEIGHT / 2 + 100);
      g.drawString(String.valueOf(highScoresMed[2]), WIDTH - 125, HEIGHT / 2 + 100);
    }
    else if(gameScreen == 5){//Hard high score screen
      customFont = customFont.deriveFont(1, 80);
      g.setFont(customFont);
      //1st
      g.drawString(highNamesHard[0], WIDTH / 2 - 250, HEIGHT / 2 - 100);
      g.drawString(String.valueOf(highScoresHard[0]), WIDTH - 125, HEIGHT / 2 - 100);
      //2nd
      g.drawString(highNamesHard[1], WIDTH / 2 - 250, HEIGHT / 2);
      g.drawString(String.valueOf(highScoresHard[1]), WIDTH - 125, HEIGHT / 2);
      //3rd
      g.drawString(highNamesHard[2], WIDTH / 2 - 250, HEIGHT / 2 + 100);
      g.drawString(String.valueOf(highScoresHard[2]), WIDTH - 125, HEIGHT / 2 + 100);
    }
    if(gameScreen >= 3){//all high score screens
      customFont = customFont.deriveFont(1, 80);
      g.setFont(customFont);
      if(gameScreen == 3)
        g.drawString("High Scores Easy", WIDTH / 2 - 350, HEIGHT / 2 - 200);
      else if(gameScreen == 4)
        g.drawString("High Scores Medium", WIDTH / 2 - 400, HEIGHT / 2 - 200);
      else if(gameScreen == 5)
        g.drawString("High Scores Hard", WIDTH / 2 - 350, HEIGHT / 2 - 200);
      g.drawString("1", WIDTH / 2 - 350, HEIGHT / 2 - 100);
      g.drawString("2", WIDTH / 2 - 350, HEIGHT / 2);
      g.drawString("3", WIDTH / 2 - 350, HEIGHT / 2 + 100);
      g.drawImage(titleImage, highScoreToggleButton.x, highScoreToggleButton.y, highScoreToggleButton.width, highScoreToggleButton.height, null);//high score toggle button
      customFont = customFont.deriveFont(0, 20);
      g.setFont(customFont);
      g.drawString("Toggle High Scores", highScoreToggleButton.x + 5, highScoreToggleButton.y + 40);   
    }
    if(gameScreen >= 1)
      g.drawImage(playButtonImage, exitButton.x + exitButton.width, exitButton.y, -exitButton.width, exitButton.height, null);//Exit Button Image
  }
  
  /*
   * Runs the jump method if a button on the mouse is pressed
   * @param e is the variable of MouseEvent. It is not used
   */
  @Override
  public void mouseClicked(MouseEvent e){
    if(playButton.contains(e.getX(), e.getY() - 20) && gameScreen == 0){
      gameScreen = 1;
      jump();
    }
    else if(instructionButton.contains(e.getX(), e.getY() - 20) && gameScreen == 0)
      gameScreen = 2;
    else if(highScoreButton.contains(e.getX(), e.getY() - 20) && gameScreen == 0)
      gameScreen = 3;
    //high score difficulty buttons
    else if(highScoreToggleButton.contains(e.getX(), e.getY() - 20) && gameScreen == 3)
      gameScreen = 4;
    else if(highScoreToggleButton.contains(e.getX(), e.getY() - 20) && gameScreen == 4)
      gameScreen = 5;
    else if(highScoreToggleButton.contains(e.getX(), e.getY() - 20) && gameScreen == 5)
      gameScreen = 3;
    else if(exitButton.contains(e.getX(), e.getY() - 20) && gameScreen >= 1){//for exit button on game and instruction screen
      gameOver = true;
      gameOverModifiers();
      gameScreen = 0;
    }
    //char select buttons
    else if(char1Button.contains(e.getX(), e.getY() - 20) && gameScreen == 0)
      birdChoice = "blueBird";
    else if(char2Button.contains(e.getX(), e.getY() - 20) && gameScreen == 0)
      birdChoice = "whiteBird";
    else if(char3Button.contains(e.getX(), e.getY() - 20) && gameScreen == 0)
      birdChoice = "eagleBird";
    else if(gameScreen == 1)
      jump();
    //difficulty level buttons
    else if(diffEasyButton.contains(e.getX(), e.getY() - 20) && gameScreen == 0){
      maxPowerUpCount = 5;
      speedPower = 10;
      diffChoice = 1;
    }
    else if(diffMedButton.contains(e.getX(), e.getY() - 20) && gameScreen == 0){
      maxPowerUpCount = 7;
      speedPower = 10;
      diffChoice = 2;
    }
    else if(diffHardButton.contains(e.getX(), e.getY() - 20) && gameScreen == 0){
      maxPowerUpCount = 10;
      speedPower = 13;
      diffChoice = 3;
    }
    birdChoiceModifiers();
  }
  
  /*
   * If the key pertaining to VK_SPACE (space bar) is pressed, run the jump method
   * @param e is the variable of KeyEvent. It is used to find the keycode.
   */
  @Override
  public void keyReleased(KeyEvent e){
    if (e.getKeyCode() == KeyEvent.VK_SPACE && gameScreen == 1)
      jump();
  }
  
  /*
   * If the frame is being closed then output the highscore
   * @param e is the variable of WindowEvent. Unused
   */
  @Override
  public void windowClosing(WindowEvent e){
    try{
      out = new PrintWriter(new FileWriter(highScoreFile));
      for(int i = 0; i < highScoresEasy.length; i++){
        out.println(highScoresEasy[i]);
        out.println(highNamesEasy[i]);
      }
      for(int i = 0; i < highScoresMed.length; i++){
        out.println(highScoresMed[i]);
        out.println(highNamesMed[i]);
      }
      for(int i = 0; i < highScoresHard.length; i++){
        out.println(highScoresHard[i]);
        out.println(highNamesHard[i]);
      }
      out.close();
    }
    catch(IOException g){}
    System.exit(0);
  }
  
  /////////////////UNUSED//////////////////////
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){}
  public void windowOpened(WindowEvent e){}
  public void windowClosed(WindowEvent e){}  
  public void windowIconified(WindowEvent e){}
  public void windowDeiconified(WindowEvent e){}
  public void windowActivated(WindowEvent e){}
  public void windowDeactivated(WindowEvent e){}
  /////////////////UNUSED///////////////////////
  
  public static void main(String[] args){
    //sets the look and feel of the program
    try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    catch(Exception e){}
    happyBirdie = new HappyBirdie();//instantiates the HappyBirdie constructor
  }
}