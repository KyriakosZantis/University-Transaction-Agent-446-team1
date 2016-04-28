
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import Interface.Clientinterface;
import Interface.Serverinterface;

class TCPServer extends Serverinterface implements Runnable {
	// class TCPServer extends Serverinterface implements Runnable {
	String name;
	int option_update;// 1.Immediate 2.Deferred
	int option_type;// 1.Wound and wait 2.Wait And Die 3.Cautious Waiting
	int c = 0;
	int[] commited = new int[11];
	int[] earliestLSN = new int[11];
	int logline = 1;
	int num_of_clients;
	static ArrayList<Item> items = new ArrayList<Item>(26);
	File log_file ;

	private void write_to_log(String clientSentence) throws IOException {

		FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(), true);
		logfw.write(clientSentence);
		logfw.write("\n");
		logfw.close();
	}

	public TCPServer(String na, int opt_up, int opt_ty, int num_oc) {
		super(na);
		name = na;
		option_update = opt_up;
		option_type = opt_ty;
		num_of_clients = num_oc;
	}

	@Override
	public void run() {
		log_file = new File("log.txt");
		try {
	
			log_file.delete();
			log_file.createNewFile();
				
			    /*  if (log_file.createNewFile()){
				        System.out.println("File is created!");
				      }else{
				        System.out.println("File already exists.");
				      }*/
			
	
			for (int i = 0; i < 26; i++) {
				items.add(new Item((char) ('a' + i)));
				try {
					File valuefile = new File((char) ('A' + i) + ".txt");

					valuefile.delete();
					valuefile.createNewFile();
					
				     /* if (valuefile.createNewFile()){
					        System.out.println("File is created!");
					      }else{
					        System.out.println("File already exists.");
					      }*/
				

					FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
					valuefw.write("0");
					valuefw.close();

				} catch (Exception e) {
					System.out.println("Error creating .txt file");
				}

			}

			setLocation(0, Clientinterface.client_windowY);
			// to be shown down of the Clients

			setVisible(true);
			String clientSentence;
			
			String serverResponse = "";
			ServerSocket welcomeSocket = new ServerSocket(6789);

			// String buffer = "This is the content to write into file";

			while (true) {
				// TODO Auto-generated method stub
				Socket connectionSocket = welcomeSocket.accept();

				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));

				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();

				System.out.println("Received: " + clientSentence);// T1 +
																	// COMMAND +
													// A + 2

				if (option_update == 1) {

					FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(), true);

					logfw.write(clientSentence);

					if (!clientSentence.contains("WRITE")) {
						logfw.write("\n");
						logfw.close();
					}

					String[] comm = clientSentence.split(" ");
					int transaction_num = Integer.parseInt(comm[0].substring(1));

					if (earliestLSN[transaction_num] == 0) {
						earliestLSN[transaction_num] = logline;
					}

					if (comm[1].equals("COMMIT")) {

					} else if (comm[1].equals("ABORT")) {
						// UNDO AT ABORT
						int trans_num = Integer.parseInt(comm[0].substring(1));
						int start_line = earliestLSN[trans_num];
						Scanner undo = new Scanner(log_file);
						int i = 1;
						while (i != start_line) {
							i++;
							undo.nextLine();
						}
						while (i <= logline) {
							String command = undo.nextLine();
							String[] comm2 = command.split(" ");
							if (comm2[1].equals("WRITE")) {
								Item item = items.get((int) (Character.toLowerCase(comm2[2].charAt(0)) - 'a'));
								if (item.touched == false) {
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
						for (int k = 0; k < 26; k++) {
							item = items.get(k);
							item.touched = false;
						}
					} else if (!comm[1].equals("END")) {
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
							// NA STELOUME TO VALUE PISW STON CLIENT
							Scanner valuefw = new Scanner(valuefile);
							String temp = valuefw.next();
							System.out.println(comm[2] + "=" + temp);
							valuefw.close();

						}

					}
					logline++;
				} else if (option_update == 2) {

				
					// step1
					String[] comm = clientSentence.split(" ");
					int transaction_num = Integer.parseInt(comm[0].substring(1));

					if (earliestLSN[transaction_num] == 0) {
						earliestLSN[transaction_num] = logline;
					}

					if (comm[1].equals("END")) {
						num_of_clients--;
						System.out.println(num_of_clients);
						write_to_log(clientSentence);
						//outToClient.writeBytes("OK");
						serverResponse="OK";
					}

					else if (comm[1].equals("COMMIT")) {
						commited[transaction_num] = 1;
						write_to_log(clientSentence);
						//outToClient.writeBytes("OK");
						serverResponse="OK";
					} else if (comm[1].equals("ABORT")) {
						write_to_log(clientSentence);
						//outToClient.writeBytes("OK");
						serverResponse="OK";
					} else if (comm[1].equals("READLOCK")) {
					
						Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
						if (item.status != 2) {
							item.status = 1;
							item.list.add(transaction_num);		
							//outToClient.writeBytes("OK");
							serverResponse="OK";
						} else {
							//outToClient.writeBytes("NOT ALLOWED");
							serverResponse="NOT ALLOWED";
						}
						
					
					} else if (comm[1].equals("WRITELOCK")) {
						Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
						if (item.status != 2) {
							if (item.status == 0 || (item.status == 1 && item.list.size() == 1 && item.list.contains(transaction_num)))
							item.status = 2;
							if(	!item.list.contains(transaction_num))
							item.list.add(transaction_num);
							//outToClient.writeBytes("OK");
							serverResponse="OK";
						} else {
							serverResponse="NOT ALLOWED";
							//outToClient.writeBytes("NOT ALLOWED");
						}
					} else if (comm[1].equals("UNLOCK")) {
						Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
						if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num))
							item.status = 0;
						else if (item.status == 1 && item.list.size() == 1 && item.list.contains(transaction_num))
							item.status = 0;
						else {
							item.list.remove(transaction_num);
						}
						serverResponse="OK";
						//outToClient.writeBytes("OK");
					} else {
						File valuefile = new File(comm[2] + ".txt");
						Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
						if (comm[1].equals("READ")) {
								Scanner valuefw = new Scanner(valuefile);
								String temp = valuefw.next();
								System.out.println(comm[2] + "=" + temp);
								valuefw.close();
								// STELOUME TO VALUE PISW STON CLIENT
								
								//outToClient.writeBytes(temp);
								serverResponse=temp;
								
								write_to_log(clientSentence);
						//WRITE COMMAND
						} else {
								System.out.println("OK");
								// STELOUME TO VALUE PISW STON CLIENT
								//outToClient.writeBytes("OK");
								serverResponse="OK";
								write_to_log(clientSentence);
						}
					}
					
					
					// step2

					// CHECKPOINT PHASE
					System.out.println("Log line is "+logline);
					if (logline % 10 == 0 || num_of_clients == 0) {
						int trans_num;
						int start_line = Integer.MAX_VALUE;
						for (int i = 0; i < 10; i++){
							if (commited[i] == 1){
								if (earliestLSN[i] < start_line){
									start_line = earliestLSN[i];
								}
							}
						}
						if(start_line==Integer.MAX_VALUE){
							System.out.println("Provlima");
							
						}else {
							System.out.println(" start_line "+start_line);
							
						}
						Scanner redo = new Scanner(log_file);
						int i = 1;
						while (i != start_line) {
							i++;
							redo.nextLine();
						}
						while (i <= logline) {
							String command = redo.nextLine();
							String[] comm2 = command.split(" ");
							trans_num = Integer.parseInt(comm2[0].substring(1));
							if (comm2[1].equals("WRITE")) {
								if (commited[trans_num] == 1) {
									File valuefile = new File(comm2[2] + ".txt");
									FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
									valuefw.write(comm2[3]);
									valuefw.close();
									
								}
							}
							i++;
						}
						redo.close();
						for (int k = 0; k < 10; k++)
							commited[k] = 0;
						
						System.out.println("FLUSHED");
					}


					logline++;
				}
							outToClient.writeBytes(serverResponse+'\n');
								
			}
		} catch (IOException e1) {
			System.out.println("Provlima ston server");
			e1.printStackTrace();
		} 
	}

	/*
	 * @Override public int Click_Strict() { //Must be implement at child c++;
	 * if (c % 2 == 1) return 1; return 0; }
	 */
}
