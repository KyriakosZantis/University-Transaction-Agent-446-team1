import java.io.*;
import java.net.*;
import java.util.Scanner;

import Interface.Clientinterface;

class TCPClient extends Clientinterface implements Runnable {	
	
	String inputfile;
	String name;
	int id;
	Socket clientSocket ;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	String sentence = null;
	String modifiedSentence;
	BufferedReader inFromUser;
	
	
	
	TCPClient(String in, String na, int i){
		super(na);
		inputfile = in;
		name = na;
		id = i;
	}
	
	@Override
	public int doButtonAction() {
	//Must be implement at child
		try {
		clientSocket = new Socket("localhost", 6789);
		 outToServer = new DataOutputStream(clientSocket.getOutputStream());
		 inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
		if (sentence.equals("End")) {
			
			inFromUser.close();
			return 0;
			}
	

	
	
		}
		catch(Exception e){
			System.out.println("Error at Button at Transaction "+id);
			return 1;
		}
		return 1;
	}
	
	@Override
	public void run() {
		try {
			inFromUser = new BufferedReader( new FileReader(inputfile));
			setVisible(true);
		
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
		}
		catch(Exception e){
			System.out.println("Error at Transaction "+id);
		}

	}
}