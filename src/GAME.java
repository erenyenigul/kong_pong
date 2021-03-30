


import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GAME extends GraphicsProgram {
	
	private final int BALL_SIZE = 10;
	
	private GOval ball;
	private int ball_speed= -2;
	public GRect paddle_1;
	public GRect paddle_2;
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	private static final int PADDLE_Y_OFFSET = 30;
	private static double PADDLE_1_SPEED = 0;
	private static double PADDLE_2_SPEED = 0;	
	private Socket SOCK;
	private Dimension d;
	private int score_1 = 0;
	private int score_2 = 0;
	private boolean isStarted = false;
	
//	private AudioClip sound = MediaTools.loadAudioClip("sound_hit.wav");
//	private SoundClip startSound =  new SoundClip(new File("/src/start.wav"));
	
	
	private GLabel score_1_l = new GLabel("");
	private GLabel score_2_l = new GLabel("");
	private int direction [] = new int[2]  ;
	private PrintStream PS;
	private BufferedReader BR;
	private GameDataHandler data ;
	public void run() {
		

		
		ServerSocket SRVSOCK;
		Socket SOCK ;
		
		
		try {
			SRVSOCK = new ServerSocket(444);
			SOCK =  SRVSOCK.accept();
			data = new GameDataHandler(SOCK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		startScreen();		
		setObjects();

		direction[0] = ball_speed;
		direction[1] = ball_speed;
		
		
		while(true) {
			
			setSize(d);		
			
			
			ball.move(direction[0], direction[1]);
	        pause(15);
	      
	        int isGoal = isGoal();
	        
	       
            RandomGenerator rg = new RandomGenerator() ;
            if(isGoal ==1) {
            	direction[0] *= rg.nextInt(-1,1)==0?1:-1;
            	direction[1] *= rg.nextInt(-1,1)==0?1:-1;
            	data.sendData((int) ball.getX(),(int)  ball.getY(), (int) paddle_1.getX(),0,isGoal,score_1,score_2);
            	resetAndScore(1);
            	
      
            }
            else if(isGoal == 2) {
            	direction[0] *= rg.nextInt(-1,1)==0?1:-1;
            	direction[1] *= rg.nextInt(-1,1)==0?1:-1;
            	data.sendData((int) ball.getX(),(int)  ball.getY(), (int) paddle_1.getX(),0,isGoal,score_1,score_2+1);
            	resetAndScore(2);
            	
            }
            
            else
            	data.sendData((int) ball.getX(),(int)  ball.getY(), (int) paddle_1.getX(),0,isGoal,score_1,score_2);
            
            String x_location = data.getPaddleX();
	        if(x_location != null) {
	        	paddle_2.setLocation( Integer.parseInt(x_location), getHeight()-30);
	        	println("PADDLE_LOC:"+x_location);
	        }
	        
	       
	        
	        if(getHeight()<ball.getY()+BALL_SIZE || 0 >ball.getY() ) {
	         	direction[0] *= 1 ;
	    		direction[1] *= -1 ;
	    		
	    	//	sound.play();
	    		
	        }
	        if(getWidth()<ball.getX()+BALL_SIZE|| 0 > ball.getX() ) {
	        	direction[0] *= -1;
	    		direction[1] *= 1;
	    	
	    	//	sound.play();
	        
	        }
	        if(ball.getBounds().intersects(paddle_1.getBounds())) {
	        	direction[0] *= 1;
	    		direction[1] *= -1;
	    	
	    	//	sound.play();
	        }
	        if(ball.getBounds().intersects(paddle_2.getBounds())) {
	        	direction[0] *= 1;
	    		direction[1] *= -1;
	    	//	sound.play();
	        }
	       
	        if( paddle_1.getX()+paddle_1.getWidth()>getWidth() || paddle_1.getX()<=0 )
	        	PADDLE_1_SPEED = 0;
	        if( paddle_2.getX()+paddle_2.getWidth()>getWidth() || paddle_2.getX()<=0 )
	        	PADDLE_2_SPEED = 0;
	        paddle_1.move(PADDLE_1_SPEED, 0);
	        paddle_2.move(PADDLE_2_SPEED, 0);
	        PADDLE_1_SPEED = 0;
	        PADDLE_2_SPEED = 0;
	               
		}
	
		
		
	}
	public void keyReleased(KeyEvent e) {
		if(KeyEvent.getKeyText(e.getKeyCode()).equals("D") || KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
				PADDLE_1_SPEED = 0;
		}
		if(KeyEvent.getKeyText(e.getKeyCode()).equals("M") ||KeyEvent.getKeyText(e.getKeyCode()).equals("B")) {
				PADDLE_2_SPEED = 0;
			
		}
		
	
		
		
		
		
	}
	
	public void keyPressed(KeyEvent e) {
		
		isStarted = true;
		//this method is called when any key is pressed
		if(KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {
			if (!(paddle_1.getX()+paddle_1.getWidth()+10>getWidth())) {
				PADDLE_1_SPEED = getWidth()/20;
				
			}
			
		}
		
		//this method is called when any key is pressed
		if(KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
			if (paddle_1.getX()-10>0) {
				PADDLE_1_SPEED = -getWidth()/20;
				
			}
			
		}
					
	}
	

	private int isGoal() {
		if(ball.getY()<0) {
			return 1;
		}else if(getHeight()<ball.getY()+BALL_SIZE) {
			return 2;
		}else {
			return 0;
		}
	}
	
	
	public void resetAndScore(int scorer) {
		paddle_1.setLocation(getWidth()/2,30);
		paddle_2.setLocation(getWidth()/2,getHeight()-30);
		GLabel score_text = new GLabel("Player "+scorer+" scored!");
		score_text.setFont("CosmicAlien-30");
		score_text.setColor(Color.GREEN);
		add(score_text,getWidth()/2-score_text.getWidth()/2,getHeight()/2-30);
		pause(1500);
		remove(score_text);
		ball.setLocation(getWidth()/2,getHeight()/2);
		setScoreBoard();
		if(scorer == 1)
			score_1++;
		else if(scorer==2)
			score_2++;
		
	}
	
	public void setScoreBoard() {
		
	
		remove(score_1_l);
		remove(score_2_l);
		score_1_l.setLabel("Player 1:"+ score_1);
		score_2_l.setLabel("Player 2:"+ score_2);
		score_1_l.setColor(Color.GREEN);
		score_2_l.setColor(Color.GREEN);
		score_1_l.setFont("CosmicAlien-20");
		score_2_l.setFont("CosmicAlien-20");
		add(score_1_l, getWidth()/2-score_1_l.getWidth()/2, getHeight()/2);
		add(score_2_l, getWidth()/2-score_2_l.getWidth()/2, getHeight()/2+40);
	}
	
	public void startScreen() {
		d = new Dimension(800, 500);
		setMaximumSize(d);
		setMinimumSize(d);
		
		setSize(d);
		setBackground(Color.BLACK);
		pause(100);
	

		GLabel start_text = new GLabel("THE KONG-PONG EXPERIENCE");
		start_text.setFont("CosmicAlien-30");
		start_text.setColor(Color.GREEN);
		add(start_text,getWidth()/2-start_text.getWidth()/2,getHeight()/2-30);

				
		GLabel small_text = new GLabel("PRESS ANY KEY TO START");
		small_text.setFont("CosmicAlien-20");
		small_text.setColor(Color.GREEN);
		add(small_text,getWidth()/2-small_text.getWidth()/2,getHeight()/2+80);
	
		
		addKeyListeners();
		
	
		
		

		while(!isStarted) {
			
			small_text.setColor(Color.WHITE);
			pause(100);
			small_text.setColor(Color.GREEN);
			pause(100);
		
		}

		remove(start_text);
		remove(small_text);
	}
	
	public void setObjects() {
		//Adding the ball
        ball = new GOval(10,10,10,10);
		
		
		ball.setSize(BALL_SIZE, BALL_SIZE);	
		ball.setFilled(true);
		ball.setColor(Color.GREEN);
		ball.setLocation(getWidth()/2,getHeight()/2);
		
		add(ball);	
		    
	          //movement
		
		direction[0] = ball_speed;
		direction[1] = ball_speed;
	
		paddle_1 =  new GRect(10,30,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle_1.setFillColor(Color.GREEN);
		paddle_1.setFilled(true);
		
		
		paddle_2 =  new GRect(10,getHeight()-30,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle_2.setFillColor(Color.GREEN);
		paddle_2.setFilled(true);
		
		
		add(paddle_1);
		add(paddle_2);
		
		
	}
	
	
	
	

	
	
	}
	
	
