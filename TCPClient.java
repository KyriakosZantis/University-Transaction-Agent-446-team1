import java.io.*;
import java.net.*;
import java.util.Scanner;

class TCPClient implements Runnable {	
	
	String inputfile;
	String name;
	int id;
	Scanner useless = new Scanner(System.in);
	
	TCPClient(String in, String na, int i){
		inputfile = in;
		name = na;
		id = i;
	}
	
	@Override
	public void run() {
		try {
			String sentence = null;
			String modifiedSentence;
			BufferedReader inFromUser = new BufferedReader( new FileReader(inputfile));
			
			while (true){
				char item = '\0';
				int position = -1;
				System.out.println("Press any button to continue");
				useless.next();
				Socket clientSocket = new Socket("localhost", 6789);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				sentence = inFromUser.readLine();
				//Strict 2-Phase Commit
				/*if (sentence.equals("Commit") || sentence.equals("Abort"))
					for (int i=0; i<20; i++)
						if (OurMain.locker_of_items[i] == id)	 //Did i lock item 'i'?
							if (OurMain.status_of_items[i] == 2) //Exclusive lock
								OurMain.status_of_items[i] = 0;      //No lock
				if (sentence.contains("Read") || sentence.contains("Write")){
					for (int i=0; i<20; i++){
						if (sentence.contains(" " + OurMain.items[i])){
							item = OurMain.items[i];
							position = i;
							break;
						}
					}
					while (sentence.contains("Read"))
						if (OurMain.status_of_items[position] != 0){
							OurMain.status_of_items[position] = 1;
							OurMain.locker_of_items[position] = this.id;
							break;
						}
						else Thread.sleep(500);
					while (sentence.contains("Write"))
						if (OurMain.status_of_items[position] != 0){
							OurMain.status_of_items[position] = 2;
							OurMain.locker_of_items[position] = this.id;
							break;
						}
						else Thread.sleep(500);
				}*/
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + modifiedSentence);
				clientSocket.close();
				if (sentence.equals("End")) break;
			}
		
			
			inFromUser.close();
			
		}
		catch(Exception e){
			System.out.println("Provlima sto Client1");
		}

	}
}