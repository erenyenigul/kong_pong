import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class GameDataHandler {
	
	private Socket SOCK;
	private PrintStream PS;
	private BufferedReader BR;

	public GameDataHandler(Socket SOCK) {
		
		this.SOCK = SOCK;
		InputStreamReader IR;
		try {
			IR = new InputStreamReader(SOCK.getInputStream());
			BR = new BufferedReader(IR);
	        PS = new PrintStream(SOCK.getOutputStream());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void sendData(int ball_x,int ball_y, int paddle_x, int isEnded, int isGoal, int score_1, int score_2) {
		PS.println(ball_x+"#"+ball_y+"#"+paddle_x+"€"+isEnded+"€"+score_1+"#"+score_2+"€"+isGoal);
	}
	
	public String getPaddleX() {
		String data = null;  
		try {
				if(BR.ready()) {
					data = BR.readLine();
					
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return data;
		
	}
	
}
