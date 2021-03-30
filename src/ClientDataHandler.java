import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

class ClientDataHandler {
	
	private int PADDLE_SPEED =0;
	private Socket SOCK;
	private String rawMessage;
	private BufferedReader BR;
	private PrintStream PS;
	
	public ClientDataHandler(Socket SOCK) {
		this.SOCK = SOCK;	
	}
	
	public String getRawData() {
		
		try {
			
			InputStreamReader IR  = new InputStreamReader(SOCK.getInputStream());
			BufferedReader BR = new BufferedReader(IR);
			
			rawMessage = null;
			if(BR.ready()) {
				  rawMessage = BR.readLine();
			}
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return rawMessage;
	
	}
	
	
	public String getElement(int a,int b) {
		String body;
		String element;
		body = rawMessage.split("€")[a];
		element = body.split("#")[b];
		System.out.println(body + "" + element);
		return element;
	}
	
	
	
	
	public int getBallX() {
	      return Integer.parseInt(getElement(0,0));
	}
	
	
	public int getBallY() {
		
		   return Integer.parseInt(getElement(0,1));
	}
	
	public int getPaddle() {
		
		   return Integer.parseInt(getElement(0,2));
	}
	
	public int[] getScoreboard() {
		int [] scores = new int[2];
		scores[0] =  Integer.parseInt(getElement(2,0));
		scores[1] =  Integer.parseInt(getElement(2,1));
		return scores;
	}
	
	public int isGoal() {
		
		return Integer.parseInt(getElement(3,0));
		
	}

	
	
	public boolean isEnded() {
		return (Integer.parseInt(getElement(1,0))==0)?false:true;
	}
	
	public void sendPaddleLocation(int x) {
		try {
			PS = new PrintStream(SOCK.getOutputStream());
			PS.println(x);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
