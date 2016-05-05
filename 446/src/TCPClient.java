
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Interface.Clientinterface;

class TCPClient extends Clientinterface implements Runnable {

	String inputfile;
	String name;
	int id;
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	String sentence = null;
	String modifiedSentence;
	BufferedReader inFromUser;
	int sleep = 0;
	ArrayList<String> array = new ArrayList<String>(100);
	int arraylength = 0;
	int line = 0;
	boolean notactive=false;

	TCPClient(String in, String na, int i) throws FileNotFoundException {
		super(na);
		inputfile = in;
		name = na;
		id = i;
		inFromUser = new BufferedReader(new FileReader(inputfile));
	}

	// Every time the button is clicked, open new socket connection and send a
	// sentence
	@Override
	public int doButtonAction() {
		// Must be implement at child
		int position = -1;
		if (sleep == 0) {
			try {
				clientSocket = new Socket("localhost", 6789);
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				sentence = array.get(line);
				

				// Strict 2-Phase Lock
				/*
				 * if (sentence.contains("Commit") ||
				 * sentence.contains("Abort")) for (int i=0; i<20; i++) if
				 * (OurMain.locker_of_items[i] == id) //Did i lock item 'i'? if
				 * (OurMain.status_of_items[i] == 2) //Exclusive lock
				 * OurMain.status_of_items[i] = 0; //No lock if
				 * (sentence.contains("Read") || sentence.contains("Write")){
				 * for (int i=0; i<20; i++){ if (sentence.contains(" " +
				 * OurMain.items[i])){ position = i; break; } } while
				 * (sentence.contains("Read")) if
				 * (OurMain.status_of_items[position] == 0){
				 * OurMain.status_of_items[position] = 1;
				 * OurMain.locker_of_items[position] = this.id; break; } else
				 * Thread.sleep(500); while (sentence.contains("Write")) if
				 * (OurMain.status_of_items[position] == 0){
				 * OurMain.status_of_items[position] = 2;
				 * OurMain.locker_of_items[position] = this.id; break; } else
				 * Thread.sleep(500); }
				 */
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + modifiedSentence);
				clientSocket.close();
				
				 if (modifiedSentence.equals("KILL")){
						return 0;
					}
				if (!modifiedSentence.equals("WAIT") && !modifiedSentence.equals("RESTART"))
					line++;
				else if (!modifiedSentence.equals("RESTART")){
					sleep = 3;
				}
				else {
					line=0;
				}

				if (line == arraylength) {
					return 0;
				} else {
					lblNextInstruction.setText(array.get(line));
				}
			} catch (Exception e) {
				System.out.println("Error at Button at Transaction " + id);
				return 1;
			}
		}
		else sleep--;
		return 1;
	}

	// Once transaction is initialized, a button appears
	@Override
	public void run() {
		try {
			inFromUser = new BufferedReader(new FileReader(inputfile));
			do {
				sentence = inFromUser.readLine();
				array.add("T"+this.id+" "+sentence);
				arraylength++;
			} while (!sentence.contains("END"));
			inFromUser.close();
			/*
			 * for (int i=0; i<arraylength; i++)
			 * System.out.println(array.get(i));
			 */
			lblNextInstruction.setText(array.get(0));
			setLocation((id - 1) * client_windowX + 1, 0);// to shown one side
															// by the other
			setVisible(true);
		} catch (Exception e) {
			System.out.println("Error at Transaction " + id);
			boolean notactive=true;
		}

	}
}