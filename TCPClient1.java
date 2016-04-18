import java.io.*;
import java.net.*;

class TCPClient1 implements Runnable {	
	
	String inputfile;
	
	TCPClient1(String in){
		inputfile = in;
	}
	
	@Override
	public void run() {
		try {
			String sentence;
			String modifiedSentence;
			BufferedReader inFromUser = new BufferedReader( new FileReader(inputfile));
			Socket clientSocket = new Socket("localhost", 6789);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
		}
		catch(Exception e){
			System.out.println("Provlima sto Client1");
		}

	}
}