
import java.io.*;
import java.net.*;
import java.util.Scanner;

import Interface.Clientinterface;
import Interface.Serverinterface;

class TCPServer extends Serverinterface implements Runnable {
	// class TCPServer extends Serverinterface implements Runnable {
	String name;
	int option_update;//1.Immediate 2.Deferred 
	int option_type;//1.Wound and wait 2.Wait And Die 3.Cautious Waiting
	int c = 0;
	int[] commited = new int[11];
	int[] earliestLSN = new int[11];
	int logline = 1;
	

	public TCPServer(String na, int opt_up, int opt_ty) {
		super(na);
		name = na;
		option_update = opt_up;
		option_type = opt_ty;
	}

	@Override
	public void run() {

		try {
			setLocation(0, Clientinterface.client_windowY);
			// to be shown down of the Clients

			setVisible(true);
			String clientSentence;
			String capitalizedSentence;
			ServerSocket welcomeSocket = new ServerSocket(6789);

			// String buffer = "This is the content to write into file";
			File log_file = new File("log.txt");

			// if file doesn't exists, then create it
			if (!log_file.exists()) {
				log_file.createNewFile();
			}

			while (true) {
				// TODO Auto-generated method stub
				Socket connectionSocket = welcomeSocket.accept();

				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));

				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();

				System.out.println("Received: " + clientSentence);// T1 + COMMAND + A + 2
				
				if (option_update == 1){

					FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(), true);
					logfw.write(clientSentence);

					if (!clientSentence.contains("WRITE")) {
						logfw.write("\n");
						logfw.close();
					}


					String[] comm = clientSentence.split(" ");
					int transaction_num = Integer.parseInt(comm[0].substring(1));
					
					if (earliestLSN[transaction_num] == 0){
						earliestLSN[transaction_num] = logline;
					}
					
					if (comm[1].equals("COMMIT")){
						
					}
					else if (comm[1].equals("ABORT")){
						//UNDO AT ABORT
						int trans_num = Integer.parseInt(comm[0].substring(1));
						int start_line = earliestLSN[trans_num];
						Scanner undo = new Scanner(log_file);
						int i = 1;
						while (i != start_line){
							i++;
							undo.nextLine();
						}
						while (i <= logline){
							String command = undo.nextLine();
							String[] comm2 = command.split(" ");
							if (comm2[1].equals("WRITE")){
								Item item = OurMain.items.get((int)(Character.toLowerCase(comm2[2].charAt(0)) - 'a'));
								if (item.touched == false){
									item.touched = true;
									File valuefile = new File(comm2[2] + ".txt");
									FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
									valuefw.write(comm2[4]);
									valuefw.close();
								}
							}
							i++;
						}
						undo.close();
						System.out.println("here we go again");
						Item item;
						for (int k=0; k<26; k++){
							item = OurMain.items.get(k);
							item.touched = false;
						}
					}
					else if (!comm[1].equals("END")) {
						File valuefile = new File(comm[2] + ".txt");

						if (comm[1].equals("WRITE")) {
							Scanner valuefw1 = new Scanner(valuefile);

							String temp = valuefw1.next();
							valuefw1.close();

							FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
							logfw.write(" " + temp);
							logfw.write("\n");
							valuefw.write(comm[3]);


							valuefw.close();
							valuefw1.close();
							logfw.close();
						}
						if (comm[1].equals("READ")) {
							//NA STELOUME TO VALUE PISW STON CLIENT
							Scanner valuefw = new Scanner(valuefile);
							String temp = valuefw.next();
							System.out.println(comm[2] + "=" + temp);
							valuefw.close();

						}

					}
					logline++;
				}
				else if (option_update == 2){

					FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(), true);
					logfw.write(clientSentence);
					logfw.write("\n");
					logfw.close();
					String[] comm = clientSentence.split(" ");
					int transaction_num = Integer.parseInt(comm[0].substring(1));
					//CHECKPOINT PHASE
					if (logline % 10 == 0){
						int trans_num;
						int start_line = Integer.MAX_VALUE;
						for (int i=0; i<10; i++)
							if (commited[i] == 1)
								if (earliestLSN[i] < start_line)
									start_line = earliestLSN[i];
						Scanner redo = new Scanner(log_file);
						int i = 1;
						while (i != start_line){
							i++;
							redo.nextLine();
						}
						while (i <= logline){
							String command = redo.nextLine();
							String[] comm2 = command.split(" ");
							trans_num = Integer.parseInt(comm2[0].substring(1));
							if (comm2[1].equals("WRITE")){
								if (commited[trans_num] == 1){
									File valuefile = new File(comm2[2] + ".txt");
									FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
									valuefw.write(comm2[3]);
									valuefw.close();
								}
							}
							i++;
						}
						redo.close();
						for (int k=0; k<10; k++)
							commited[k] = 0;	
					}
					
					if (earliestLSN[transaction_num] == 0){
						earliestLSN[transaction_num] = logline;
					}
					if (comm[1].equals("COMMIT")){
						commited[transaction_num] = 1;
					}
					else if (comm[1].equals("ABORT")){
						
					}
					else if (!comm[1].equals("END")) {
						File valuefile = new File(comm[2] + ".txt");
						if (comm[1].equals("READ")) {
							//NA STELOUME TO VALUE PISW STON CLIENT
							Scanner valuefw = new Scanner(valuefile);
							String temp = valuefw.next();
							System.out.println(comm[2] + "=" + temp);
							valuefw.close();

						}

					}
					logline++;
				}

				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);

			}
		} catch (Exception e) {
			System.out.println("Provlima sto SERVER");
		}
	}

	/*
	 * @Override public int Click_Strict() { //Must be implement at child c++;
	 * if (c % 2 == 1) return 1; return 0; }
	 */
}
