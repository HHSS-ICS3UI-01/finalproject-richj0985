/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

/**
 *
 * @author richj0985
 */
public class SoccerFootFight extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 800 + 200;
    static final int HEIGHT = 600;
    static final int FIELD_LEVEL = 460;
    
    // Ball travel speeds
    static final int BALL_GRAVITY       = 1;
    static final int BALL_SPEED         = 10;
    static final int BALL_DY_MULTIPLIER = 2;
//    static final int BALL_GRAVITY       = 3;
//    static final int BALL_SPEED         = 15;
//    static final int BALL_DY_MULTIPLIER = 3;
    

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;

    // soccer ball
    SoccerBall ball = new SoccerBall();

    // players
    SoccerPlayer player1 = new SoccerPlayer();
    SoccerPlayer player2 = new SoccerPlayer();

    // nets
    Rectangle net1 = new Rectangle(0, 300, 40, 200);
    Rectangle net2 = new Rectangle(760 + 200, 300, 40, 200);
    Rectangle crossBar1 = new Rectangle(0, 300, 40, 4);
    Rectangle crossBar2 = new Rectangle(760 + 200, 300, 40, 4);

    // key count
    int keyDownCount = 0;
    int keyUpCount = 0;
    int screen = 1;
    
    boolean menuUp = false;
    boolean menuDown = false;
    boolean menuEnter = false;
    boolean menuChange = false;

    // store variable for if a goal is being scored
    boolean score = false;
    
    int redBoxX = HEIGHT / 2;
    
    public void initializeGame() {
        // Initialize the players positions
        player2.playerNum = 2;
        player2.x = 50;
        player2.y = 500 - 80;
        player2.width = 60;
        player2.height = 80;
        player2.direction = 1;
        player2.score = 0;

        player1.playerNum = 1;
        player1.x = WIDTH - 50 - 60;
        player1.y = 500 - 80;
        player1.width = 60;
        player1.height = 80;
        player1.direction = -1;
        player1.score = 0;

        // Initialize the ball
        ball.x = 470;
        ball.y = FIELD_LEVEL;
        ball.width = 40;
        ball.height = 40;
    }

    public void drawPlayer(Graphics g, SoccerPlayer player) {      
        // skin color
        Color whiteSkin = new Color(247, 238, 188);

        // hair color
        Color brown = new Color(207, 136, 80);

        // player body
        if (player.playerNum == 1) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.RED);
        }
        g.fillOval(player.x + 15, player.y - 35 + 80, 30, 30);

        // face
        g.setColor(whiteSkin);
        g.fillOval(player.x, player.y - 80 + 80, 60, 60);

        // arms
        g.setColor(whiteSkin);
        g.fillOval(player.x + 2, player.y - 28 + 80, 15, 15);
        g.fillOval(player.x + 2 + 40, player.y - 28 + 80, 15, 15);

        // shoes
        g.setColor(Color.BLACK);
        g.fillOval(player.x + 5 + player.foot1X + player.footRun2, player.y - 15 + player.foot1Y + player.feetJump + 80, 25, 15);
        g.fillOval(player.x + 25 + player.foot2X + player.footRun1, player.y - 15 + player.foot2Y + player.feetJump + 80, 25, 15);

        // hair
        g.setColor(brown);
        g.fillOval(player.x - 5, player.y - 80 + 80, 20, 20);
        g.fillOval(player.x, player.y - 85 + 80, 20, 20);
        g.fillOval(player.x + 10, player.y - 87 + 80, 20, 20);
        g.fillOval(player.x + 20, player.y - 90 + 80, 20, 20);
        g.fillOval(player.x + 30, player.y - 87 + 80, 20, 20);
        g.fillOval(player.x + 40, player.y - 85 + 80, 20, 20);
        g.fillOval(player.x + 45, player.y - 80 + 80, 20, 20);

        // Draw face based on direction moving
        if (player.direction == 1) {
            // eyes
            // layer 1
            g.setColor(Color.WHITE);
            g.fillOval(player.x + 15, player.y - 60 + 80, 20, 20);
            g.fillOval(player.x + 40, player.y - 60 + 80, 20, 20);

            // layer 2
            g.setColor(Color.BLACK);
            g.fillOval(player.x + 25, player.y - 55 + 80, 12, 12);
            g.fillOval(player.x + 45, player.y - 55 + 80, 12, 12);

            // mouth
            g.setColor(Color.BLACK);
            g.drawLine(player.x + 30, player.y - 32 + 80, player.x + 50, player.y - 32 + 80);

            // nose
            g.setColor(Color.BLACK);
            g.drawLine(player.x + 40, player.y - 40 + 80, player.x + 40, player.y - 40 + 80);
        } else {
            // layer 1
            g.setColor(Color.WHITE);
            g.fillOval(player.x + 15 - 14, player.y - 60 + 80, 20, 20);
            g.fillOval(player.x + 40 - 14, player.y - 60 + 80, 20, 20);

            // layer 2
            g.setColor(Color.BLACK);
            g.fillOval(player.x + 25 - 20, player.y - 55 + 80, 12, 12);
            g.fillOval(player.x + 45 - 20, player.y - 55 + 80, 12, 12);

            // mouth
            g.setColor(Color.BLACK);
            g.drawLine(player.x + 30 - 20, player.y - 32 + 80, player.x + 50 - 20, player.y - 32 + 80);

            // nose
            g.setColor(Color.BLACK);
            g.drawLine(player.x + 40 - 22, player.y - 40 + 80, player.x + 40 - 22, player.y - 40 + 80);
        }
    }
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE
        if(screen == 1){
            
//            BufferedImage img = null;
//
//            //  Load the background picture
//            try {
//                img = ImageIO.read( new File("soccer game home screen.jpg") );
//            } catch (IOException e) {
//            }
//
//            g.drawImage(img, 0, 0, null);
            
            Font titleFont = new Font("Impact", Font.BOLD, 100);
            g.setFont(titleFont);
            g.setColor(Color.BLUE);
            g.drawString("Soccer Foot Fight", WIDTH / 10, HEIGHT / 2 - 150);
            
            Font byFont = new Font("Times New Roman", Font.BOLD, 20);
            g.setFont(byFont);
            g.setColor(Color.BLACK);
            g.drawString("JR Sports", WIDTH / 2 - 50, HEIGHT / 2 - 100);
            
            Font tabs = new Font("Arial", Font.BOLD, 40);
            g.setFont(tabs);
            if(redBoxX == HEIGHT / 2){
                g.setColor(Color.BLACK);
            }else{
                g.setColor(Color.RED);
            }
            g.drawString("PLAY NOW", WIDTH / 2 - 100, HEIGHT / 2 + 40);
            if(redBoxX == HEIGHT / 2 + 60){
                g.setColor(Color.BLACK);
            }else{
                g.setColor(Color.RED);
            }
            g.drawString("CONTROLS", WIDTH / 2 - 100 - 10, HEIGHT / 2 + 40 + 60);
            if(redBoxX == HEIGHT / 2 + 60 + 60){
                g.setColor(Color.BLACK);
            }else{
                g.setColor(Color.RED);
            }
            g.drawString("INSTRUCTIONS", WIDTH / 2 - 100 - 50, HEIGHT / 2 + 40 + 60 + 60);
            
            if(menuUp && !menuChange && redBoxX != HEIGHT / 2){
                redBoxX = redBoxX - 60;
                menuChange = true;
            } else if(menuDown && !menuChange && redBoxX != HEIGHT / 2 + 60 + 60){
                redBoxX = redBoxX + 60;
                menuChange = true;
            }
            
            g.setColor(Color.BLACK);
            g.drawRect(WIDTH / 2 - 200, HEIGHT / 2, 400, 50);
            
            g.drawRect(WIDTH / 2 - 200, HEIGHT / 2 + 60, 400, 50);
            
            g.drawRect(WIDTH / 2 - 200, HEIGHT / 2 + 60 + 60, 400, 50);
            
            g.setColor(Color.RED);
            g.drawRect(WIDTH / 2 - 200, redBoxX, 400, 50);
            
            if(redBoxX == HEIGHT / 2 && menuEnter){
                screen = 2;
            }
        }else if(screen == 2){
        // Colors
        // grass
        Color grass = new Color(22, 196, 16);

        // sky
        Color sky = new Color(175, 240, 236);

        // sky
        g.setColor(sky);
        g.fillRect(0, 0, 800 + 200, 600);

        // soccer field
        g.setColor(grass);
        g.fillRect(0, 400, 800 + 200, 200);

        // detail on field
        g.setColor(Color.WHITE);
        g.fillRect(WIDTH / 2 - 20, 400, 20, 200);
        g.drawOval(365 + 100, 475, 50, 50);

        // ball
        g.setColor(Color.BLACK);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
        g.setColor(Color.WHITE);
        g.fillOval(ball.x + 1, ball.y + 1, ball.width - 2, ball.height - 2);

        // draw both nets
        g.setColor(Color.WHITE);
        for (int index = 1; index <= 4; index = index + 1) {
            g.drawLine(net1.x + index, net1.y, net1.x + index, net1.y + 200);
            g.drawLine(net1.x + index + net1.width - 4, net1.y, net1.x + index + net1.width - 4, net1.y + 200);
            g.drawLine(net2.x + index, net2.y, net2.x + index, net2.y + 200);
            g.drawLine(net2.x + index + net2.width - 4, net2.y, net2.x + index + net2.width - 4, net2.y + 200);
        }
        for (int index = 1; index <= 4; index = index + 1) {
            g.drawLine(net1.x, net1.y + index, net1.x + net1.width, net1.y + index);
            g.drawLine(net1.x, net1.y + index + net1.height - 4, net1.x + net1.width, net1.y + index + net1.height - 4);
            g.drawLine(net2.x, net2.y + index, net2.x + net2.width, net2.y + index);
            g.drawLine(net2.x, net2.y + index + net1.height - 4, net2.x + net2.width, net2.y + index + net1.height - 4);
        }
        for (int index = 0; index <= net1.width; index = index + 10) {
            g.drawLine(net1.x + index, net1.y, net1.x + index, net1.y + 200);
            g.drawLine(net2.x + index, net2.y, net2.x + index, net2.y + 200);
        }
        for (int index = 0; index <= net1.height; index = index + 10) {
            g.drawLine(net1.x, net1.y + index, net1.x + net1.width, net1.y + index);
            g.drawLine(net2.x, net2.y + index, net2.x + net2.width, net2.y + index);
        }

        // draw the player
        drawPlayer(g, player1);
        drawPlayer(g, player2);

        // scoreboard
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(WIDTH / 2 - 400, 0, 800, 60);
        g.setColor(Color.BLACK);
        g.drawRect(WIDTH / 2 - 400, 0, 800, 60);

        Font scoreFont = new Font("Impact", Font.BOLD, 40);
        g.setFont(scoreFont);
        g.setColor(Color.BLACK);
        g.drawString("Soccer Foot Fight", WIDTH / 2 - 150, 40);
        g.setFont(scoreFont);
        g.setColor(Color.BLUE);
        g.drawString("USA: " + player1.score, WIDTH / 2 + 200, 40);
        g.setFont(scoreFont);
        g.setColor(Color.RED);
        g.drawString("CANADA: " + player2.score, WIDTH / 2 - 350, 40);
        }
        // GAME DRAWING ENDS HERE
    }
    
    public void movePlayer(SoccerPlayer player, SoccerBall ball) {
        // player collides with player
        if (player1.intersects(player2) && (player1.y + 60 >= player2.y && player2.y + 60 >= player1.y)) {
            if (player1.x > player2.x) {
                player1.left = false;
                player2.right = false;
            } else {
                player1.right = false;
                player2.left = false;
            }
        }
        
        // moving
        // turn right
        if (player.right) {
            player.x = player.x + 5;
            player.footRun1 = 4;
            player.direction = 1;
        } else {
            player.footRun1 = 0;
        }
        // turn left
        if (player.left) {
            player.x = player.x - 5;
            player.footRun2 = -4;
            player.direction = -1;
        } else {
            player.footRun2 = 0;
        }
        
        // player collides with wall - boundaries - (ensure player is not allowed to move off the field)
        if (player.x <= 0) {
            player.x = 0;
        } else if (player.x + 60 >= WIDTH) {
            player.x = WIDTH - 60;
        }
        
        // jumping
        if (player.jump && !player.jumping) {
            player.dy = -18;
            player.jumping = true;
        }
        player.dy = player.dy + player.gravity;
        if (player.dy > 18) {
            player.dy = 18;
        }
        player.y = player.y + player.dy;
        if (player.y > 500 - 80) {
            player.y = 500 - 80;
            player.jumping = false;
        }
        if (player.jumping) {
            player.feetJump = 5;
        } else {
            player.feetJump = 0;
        }
        
        // player collides with ball        
        // check to see if the player and ball are hitting each other   
        if (player.intersects(ball)) {
            // ball hit the player, allow the ball to rise into the air after
            // hitting the player
            ball.gravity = 0;
            // players head (top 25% of player)
//            Rectangle playersHead = new Rectangle();
//            playersHead.x          = player.x;
//            playersHead.y          = player.y;
//            playersHead.width      = player.width;
//            playersHead.height     = player.height / 4;
            // set the balls X direction based on side the player the ball hit
            Rectangle playersLeftSide = new Rectangle();
            playersLeftSide.x      = player.x;
            playersLeftSide.y      = player.y;
            playersLeftSide.width  = player.width / 3;
            playersLeftSide.height = player.height / 3;
            // Set the ball direction based on where th ball hits the player on the head
            if (playersLeftSide.intersects(ball)) {
                // if hit left side and ball is traveling right then change it's direction
                if (ball.speed > 0) {
                    ball.speed *= -1;

                    // increase the ball speed due to conflict
                    ball.speed -= 1;
                }
            } else {
                // ball hit the right side of player, 
                // check to see if the ball is traveling left and if it is then change it's direction
                // if hit left side and ball is traveling right then change it's direction
                if (ball.speed < 0) {
                    
                    ball.speed *= -1;
                    // increase the ball speed due to conflict
                    ball.speed += 1;
                }
            }
            // push the ball ahead of the player to simulate the player carrying the ball up the field
            if (player.x + player.width / 2 <= ball.x) {
                ball.x += 5;
                // if player contacted the ball, default to controlling the ball versus enertia
                // if in the corner of field, don't allow player to control the ball
//                if (ball.x > ball.width | ball.x < WIDTH - ball.width) {
//                    ball.speed = 0;
//                }
            } else if (player.x >= ball.x + ball.width / 2) {
                ball.x -= 5;
                // if player contacted the ball, default to controlling the ball versus enertia
                // if in the corner of field, don't allow player to control the ball
                //               if (ball.x > ball.width || ball.x < WIDTH - ball.width) {
                //                   ball.speed = 0;
                //               }
            }
        }

        // kicking - determine if the player is within half a ball width from the ball
        // if then all the player to kick the ball.  The easiest way to detemine
        // within half ball distance from the ball is to make a new rectangle
        // that is 50% bigger
        Rectangle playerFootRect = new Rectangle();
        if (player.direction == 1) {
            playerFootRect.x = player.x + player.width - ball.width / 4;
        } else {
            playerFootRect.x = player.x - ball.width / 2;
        }
        playerFootRect.y = player.y + player.height - ball.width / 4;
        playerFootRect.width = ball.width + ball.width / 2;
        playerFootRect.height = ball.height + ball.width / 2;

        // If player is atempting to kick the ball, determine if the ball is close enough to the player
        // if it is then set the ball in motion using the speed and dx
        if (player.kick && playerFootRect.intersects(ball)) {
            // ball has been kicked, set it's speed and direction
            ball.speed = player.direction * BALL_SPEED;
            ball.x += ball.speed;

            // set the power of the kick based on random value
            ball.dy = (int) (Math.random() * (12 - 5 + 1) + 5) * BALL_DY_MULTIPLIER;
//            if (ball.dy > 100) {
//                ball.dy = 100;
//            }
        }
        if (player.kick) {
            if (player.x + player.width / 2 <= ball.x) {
                player.foot2X = 15;
                player.foot2Y = - 5;
            } else if (player.x >= ball.x + ball.width / 2) {
                player.foot1X = - 15;
                player.foot1Y = - 5;
            }
        } else {
            player.foot1X = 0;
            player.foot2X = 0;
            player.foot1Y = 0;
            player.foot2Y = 0;
        }
        }

    public void moveBall(SoccerBall ball) {
        if(screen == 2){
        // use variable to represent the drag from the ball to the ground
        int drag;

        // move the ball and apply both gravity to ball height
        // and apply a "drag" to the ball speed. 
        ball.x = ball.x + ball.speed;
        ball.y = ball.y - ball.dy + ball.gravity;
        // accumulate the gravity on the ball's flight
        ball.gravity = ball.gravity + BALL_GRAVITY;

        // bounce the ball off the field surface
        // reduce the bounce height of the ball each time it hits the ground
        if (ball.y >= FIELD_LEVEL) {
            ball.y = FIELD_LEVEL;
            // reduce the size of the bounce by 40%
            // set gravity strength back to zero to allow the ball to begin to 
            // bounce up. 
            ball.dy -= (float) ball.dy * .20;
            ball.gravity = 0;
        }

        // if the ball moving and is rolling on the ground, apply some drag to the ball speed
        if (ball.speed != 0 && ball.y >= FIELD_LEVEL) {
            // reduce the ball speed by 33% each frame
            drag = ball.speed / 5;
            // ensure we apply a drag value
            if (drag == 0) {
                if (ball.speed > 0) {
                    drag = 1;
                } else {
                    drag = -1;
                }
            }
            ball.speed = ball.speed - drag;
        }

        // check the ball against the field boundaries   If the ball hits the edge of 
        // the field then change it's direction and take 50% of it's speed away 
        if (ball.x < 0) {
            ball.x = 0;
            ball.speed = ball.speed / 2 * -1;
        } else if (ball.x > WIDTH - 40) {
            ball.speed = ball.speed / 2 * -1;
            ball.x = WIDTH - 40;
        }
        
        // ball collides with the cross bar of the net
        if(ball.intersects(crossBar1) || ball.intersects(crossBar2)) {
            ball.gravity = 0;
            if(ball.intersects(crossBar1)){
                if (ball.speed < 0) {
                    ball.speed *= -1;
                    ball.speed += 1;
                }
            }else{
                if (ball.speed > 0) {
                    ball.speed *= -1;
                    ball.speed -= 1;
                }
            }
        }
        
        // ball is scored
        if (score == false && ball.intersects(net1) && !ball.intersects(crossBar1)) {
            player1.score = player1.score + 1;
            score = true;
        } else if (score == false && ball.intersects(net2) && !ball.intersects(crossBar2)) {
            player2.score = player2.score + 1;
            score = true;
        } else if (!ball.intersects(net1) && !ball.intersects(net2)) {
            score = false;
        }
        }
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        
        // Initialize the goal objects of the game
        initializeGame();
        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            // move players in game
            if(screen == 2){
                movePlayer(player1, ball);
                movePlayer(player2, ball);

                // move the ball in game
                moveBall(ball);

                // test
                System.out.println("P1X:" + player1.x + " P1Y:" + player1.y + " P2X:" + player2.x + " P2Y:" + player2.y + " P1Gravity:" + player1.gravity + " BX:" + ball.x + " BY:" + ball.y + " BSpeed:" + ball.speed + " BGravity:" + ball.gravity + " KD:" + keyDownCount + " KU:" + keyUpCount + " SPC:" + player1.kick + " Score:" + score + " BIN1:" + ball.intersects(net1) + " BIN2:" + ball.intersects(net2));
            }
            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("Soccer Foot Fight");

        // creates an instance of my game
        SoccerFootFight game = new SoccerFootFight();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyDownCount++;
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_RIGHT) {
            player1.right = true;
        } else if (key == KeyEvent.VK_LEFT) {
            player1.left = true;
        } else if (key == KeyEvent.VK_UP) {
            player1.jump = true;
            menuUp = true;
        } else if (key == KeyEvent.VK_SPACE) {
            player1.kick = true;
            menuEnter = true;
        } else if (key == KeyEvent.VK_A) {
            player2.left = true;
        } else if (key == KeyEvent.VK_D) {
            player2.right = true;
        } else if (key == KeyEvent.VK_W) {
            player2.jump = true;
        } else if (key == KeyEvent.VK_SHIFT) {
            player2.kick = true;
        } else if (key == KeyEvent.VK_DOWN){
            menuDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyUpCount++;
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT) {
            player1.right = false;
        } else if (key == KeyEvent.VK_LEFT) {
            player1.left = false;
        } else if (key == KeyEvent.VK_UP) {
            player1.jump = false;
            menuUp = false;
            menuChange = false;
        } else if (key == KeyEvent.VK_SPACE) {
            player1.kick = false;
            menuEnter = false;
        } else if (key == KeyEvent.VK_A) {
            player2.left = false;
        } else if (key == KeyEvent.VK_D) {
            player2.right = false;
        } else if (key == KeyEvent.VK_W) {
            player2.jump = false;
        } else if (key == KeyEvent.VK_SHIFT) {
            player2.kick = false;
        } else if(key == KeyEvent.VK_DOWN){
            menuDown = false;
            menuChange = false;
        }
    }
}