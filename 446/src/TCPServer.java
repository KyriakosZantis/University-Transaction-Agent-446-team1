
import java.io.*;
import java.net.*;

import Interface.Serverinterface;


class TCPServer  implements Runnable {
//	class TCPServer extends Serverinterface implements Runnable {
	String name;
	int c = 0;
	
	public TCPServer(String na) {
		//super(na);
		name = na;
	}

	@Override
	public void run() {

		try {
			//setVisible(true);
			String clientSentence;
			String capitalizedSentence;
			ServerSocket welcomeSocket = new ServerSocket(6789);

			//String buffer = "This is the content to write into file";
			File log_file = new File("log.txt");
			File memory_file = new File("memory.txt");
			// if file doesn't exists, then create it
			if (!log_file.exists()) {
				log_file.createNewFile();
			}
			if (!memory_file.exists()) {
				memory_file.createNewFile();
			}
			while(true){
				// TODO Auto-generated method stub
				Socket connectionSocket = welcomeSocket.accept();
				
				BufferedReader inFromClient =
						new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine(); 
				System.out.println("Received: " + clientSentence);
				FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(),true);
				logfw.write(clientSentence);
				logfw.write("\n");
				logfw.close();
				FileWriter memoryfw = new FileWriter(memory_file.getAbsoluteFile(),true);
				memoryfw.write(clientSentence);
				memoryfw.write("\n");
				memoryfw.close();
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);
				
				
			}
		}
		catch(Exception e){
			System.out.println("Provlima sto SERVER");
		}
	}
	
/*	@Override
	public int Click_Strict() {
		//Must be implement at child
		c++;
		if (c % 2 == 1) return 1;
		return 0;
	}*/
}
	