
import java.io.*;
import java.net.*;


class TCPServer implements Runnable {
	
	@Override
	public void run() {

		try {
			/*static TCPClient1 client1;
			static TCPClient2 client2;
			static TCPClient3 client3;*/
			/*client1 = new TCPClient("input1.txt");
			client1.main(null);
			client2 = new TCPClient("input2.txt");
			client2.main(null);
			client3 = new TCPClient("input3.txt");
			client3.main(null);*/
			String clientSentence;
			String capitalizedSentence;
			ServerSocket welcomeSocket = new ServerSocket(6789);
			/*client1.main(argv);
	          client2.main(argv);
	          client3.main(argv);*/

			while(true){
				// TODO Auto-generated method stub
				Socket connectionSocket = welcomeSocket.accept();
				
				BufferedReader inFromClient =
						new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine(); 
				System.out.println("Received: " + clientSentence);
				clientSentence = clientSentence + " hi";
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);
				
				
			}
		}
		catch(Exception e){
			System.out.println("Provlima sto SERVER");
		}
	}
}
	