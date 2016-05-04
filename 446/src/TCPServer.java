
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Interface.Clientinterface;
import Interface.LogfileView;
import Interface.Serverinterface;

class TCPServer extends Serverinterface implements Runnable {
	// class TCPServer extends Serverinterface implements Runnable {
	String name;
	int option_update;// 1.Immediate 2.Deferred
	int option_type;// 1.Wound and wait 2.Wait And Die 3.Cautious Waiting
	int c = 0;
	int numofclient = 11;
	int[] commited = new int[numofclient];
	boolean[] compens = new boolean[numofclient];
	int[] earliestLSN = new int[numofclient];
	int[] timestamp = new int[numofclient];
	boolean[] waitgraph = new boolean[numofclient];
	int TS = 0;
	boolean[] shrinking_phase = new boolean[numofclient];
	int logline = 1;
	int num_of_clients;
	static ArrayList<Item> items = new ArrayList<Item>(26);
	File log_file;
	String Logfile = "";
	LogfileView frame = new LogfileView(Logfile);

	private void write_to_log(String clientSentence) throws IOException {
		if (frame.isVisible()) {
			// System.out.println("Is visible");
			frame.setVisible(false);
		}

		FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(), true);
		logfw.write(clientSentence);
		logfw.write("\n");
		logfw.close();
		// for interface
		Logfile += logline + ") " + clientSentence + "<br>";
		frame = new LogfileView(Logfile);
		frame.setLocation(0, Clientinterface.client_windowY + server_windowY);
		frame.setVisible(true);

	}

	public TCPServer(String na, int opt_up, int opt_ty, int num_oc) {
		super(na);
		name = na;
		option_update = opt_up;
		option_type = opt_ty;
		num_of_clients = num_oc;
	}

	@Override
	public int doButtonActionCrash() {

		try {
			write_to_log("T0 CRASH");
			logline++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int doButtonActionCheckpoint() {

		try {
			write_to_log("T0 CHECKPOINT");
			logline++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public void free_locks(int id) {
		for (int i = 0; i < items.size(); i++) {
			Item temp = items.get(i);
			if (temp.list.contains(id)) {
				temp.list.remove((Integer) id);
				if (temp.status == 2) {
					temp.status = 0;

				} else if (temp.status == 1) {

					if (temp.list.size() == 0) {
						temp.status = 0;
					}
				}

			}

		}

	}

	// rtrn 0 for Continue,
	// 1 for wait,
	// 2 for restart
	// i is the demanding transaction timestamp and j the holders timestamp
	public int waitType(int i, int j, int n, int t, Item item) throws IOException {

		System.out.println(
				"T" + t + " with " + i + " timestamp " + " try to catch" + "T" + n + " with " + j + " timestamp ");
		// recourse earliest,trans earliest
		if (option_type == 1) {// 1.Wound and wait 2.Wait And Die 3.Cautious
			if (i == j) {
				System.out.println("Return Ok");
				return 0;
			} // Waiting
			if (i > j) {
				System.out.println("Return wait");
				return 1;
			}
			String trans = "T" + n;
			if (option_update == 1)
				abort_trans(trans);
			write_to_log(trans + " ABORT");
			free_locks(n);
			compens[n] = true;
			logline++;
			item.list.remove((Integer) n);
			System.out.println("Return Ok");
			return 0;

		} else if (option_type == 2) {// 1.Wound and wait 2.Wait And Die
			if (i == j) {
				System.out.println("Return Ok");
				return 0;
			} // 3.Cautious Waiting
			if (i < j) {
				System.out.println("Return wait");
				return 1;
			}

			String trans = "T" + t;
			if (option_update == 1)
				abort_trans(trans);
			logline++;
			write_to_log(trans + " ABORT");
			compens[t] = true;
			free_locks(t);
			item.list.remove((Integer) t);
			System.out.println("Return restart");
			return 2;

		} else if (option_type == 3) {// 1.Wound and wait 2.Wait And Die

			if (i == j) {
				System.out.println("Return Ok");
				return 0;
			} // 3.Cautious Waiting

			if (!waitgraph[t]) {
				if (waitgraph[n]) {
					String trans = "T" + t;
					if (option_update == 1)
						abort_trans(trans);
					logline++;
					write_to_log(trans + " ABORT");
					compens[t] = true;
					free_locks(t);
					item.list.remove((Integer) t);
					System.out.println("Return restart");
					return 2;

				} else {
					waitgraph[t] = true;
					System.out.println("Return wait");
					return 1;
				}

			} else {
				System.out.println("Return wait");
				return 1;
			}

		}

		return 0;
	}

	public void abort_trans(String comm) throws IOException {
		int trans_num = Integer.parseInt(comm.substring(1));
		int start_line = earliestLSN[trans_num];
		if (start_line == 0) {
			return;
		}
		Scanner undo = new Scanner(log_file);
		int i = 1;
		while (i != start_line) {
			i++;
			undo.nextLine();
		}
		System.out.println("logline is " + logline);
		while (i < logline) {
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
		Item item;
		for (int k = 0; k < 26; k++) {
			item = items.get(k);
			item.touched = false;
		}
	}

	@Override
	public void run() {
		log_file = new File("log.txt");
		try {

			log_file.delete();
			log_file.createNewFile();

			/*
			 * if (log_file.createNewFile()){ System.out.println(
			 * "File is created!"); }else{ System.out.println(
			 * "File already exists."); }
			 */

			for (int i = 0; i < 26; i++) {
				items.add(new Item((char) ('a' + i)));
				try {
					File valuefile = new File((char) ('A' + i) + ".txt");

					valuefile.delete();
					valuefile.createNewFile();

					/*
					 * if (valuefile.createNewFile()){ System.out.println(
					 * "File is created!"); }else{ System.out.println(
					 * "File already exists."); }
					 */

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
				System.out.println("Log will write the next entr at line : " + logline);
				boolean log_flag = false;
				// TODO Auto-generated method stub
				Socket connectionSocket = welcomeSocket.accept();

				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));

				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();

				System.out.println("Received: " + clientSentence);// T1 +

				// COMMAND +
				// A + 2

				String[] comm = clientSentence.split(" ");
				int transaction_num = Integer.parseInt(comm[0].substring(1));

				if (compens[transaction_num]) {
					serverResponse = "RESTART";
					compens[transaction_num] = false;
				} else {
					if (option_update == 1) {
						/// IMMEDIATE UPDATE
						// step1

						if (timestamp[transaction_num] == 0)
							timestamp[transaction_num] = ++TS;

						if (comm[1].equals("END")) {
							num_of_clients--;
							System.out.println(num_of_clients);
							write_to_log(clientSentence);
							// outToClient.writeBytes("OK");
							serverResponse = "OK";
							log_flag = true;
						}

						else if (comm[1].equals("COMMIT")) {
							commited[transaction_num] = 1;
							write_to_log(clientSentence);
							// outToClient.writeBytes("OK");
							serverResponse = "OK";
							log_flag = true;
						} else if (comm[1].equals("ABORT")) {
							commited[transaction_num] = 2;
							// UNDO AT ABORT
							write_to_log(clientSentence);
							abort_trans(comm[0]);
							log_flag = true;
						} else if (comm[1].equals("READLOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU AREN'T ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
								if (item.status != 2) {
									item.status = 1;
									item.list.add(transaction_num);
									// outToClient.writeBytes("OK");
									waitgraph[transaction_num] = false;
									serverResponse = "OK";
								} else {
									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										item.list.add(transaction_num);
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";
										// item.list.remove(0);

									}

								}
							}

						} else if (comm[1].equals("WRITELOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU AREN'T ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
								if (item.status != 2) {
									if (item.status == 0 || (item.status == 1 && item.list.size() == 1
											&& item.list.contains(transaction_num))) {
										item.status = 2;
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										if (!item.list.contains(transaction_num))
											item.list.add(transaction_num);
									} else {

										for (int inter = 0; inter < item.list.size(); inter++) {

											int t = item.list.get(inter);
											int i = timestamp[transaction_num];
											int j = timestamp[t];
											int temp = waitType(i, j, t, transaction_num, item);
											if (temp == 0) {
												waitgraph[transaction_num] = false;
												serverResponse = "OK";
											} else if (temp == 1) {
												serverResponse = "WAIT";
												break;

											} else if (temp == 2) {
												serverResponse = "RESTART";
												break;

											}
										}
									}
									// outToClient.writeBytes("OK");
								} else {
									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										serverResponse = "OK";
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";
										// item.list.remove(0);

									}
									// outToClient.writeBytes("WAIT");
								}
							}
						} else if (comm[1].equals("UNLOCK")) {
							shrinking_phase[transaction_num] = true;
							Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
							if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num)) {
								if (commited[transaction_num] == 0) {
									System.out.println("YOU AREN'T ALLOWED TO UNLOCK A WRITE-LOCK BEFORE COMMIT");
									System.exit(0);
								} else {
									item.status = 0;
									item.list.remove((Integer) transaction_num);
								}
							} else if (item.status == 1 && item.list.size() == 1
									&& item.list.contains(transaction_num)) {
								item.status = 0;
								item.list.remove((Integer) transaction_num);
							} else {
								item.list.remove((Integer) transaction_num);
							}
							serverResponse = "OK";
							// outToClient.writeBytes("OK");
						} else {
							File valuefile = new File(comm[2] + ".txt");
							Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
							// READ COMMAND
							if (comm[1].equals("READ")) {
								Scanner valuefw = new Scanner(valuefile);
								String temp = valuefw.next();

								System.out.println(comm[2] + "=" + temp);
								valuefw.close();
								// STELOUME TO VALUE PISW STON CLIENT

								// outToClient.writeBytes(temp);
								serverResponse = temp;

								write_to_log(clientSentence);
								log_flag = true;
								// WRITE COMMAND
							} else if (comm[1].equals("WRITE")) {
								Scanner valuefw1 = new Scanner(valuefile);
								String temp = valuefw1.next();
								valuefw1.close();
								FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
								valuefw.write(comm[3]);
								valuefw.close();
								valuefw1.close();

								// STELOUME TO VALUE PISW STON CLIENT
								// outToClient.writeBytes("OK");
								serverResponse = "OK";
								write_to_log(clientSentence + " " + temp);
								log_flag = true;
							} else {
								Exception e = null;
								throw e;
							}
						}

						// if the first line is readlock dont count
						if (log_flag) {// if Unlock or lock dont change the line

							if (earliestLSN[transaction_num] == 0)
								earliestLSN[transaction_num] = logline;
							// if Unlock or lock dont change the line
							logline++;
						}

					} else if (option_update == 2) {

						/// DEFERRED UPDATE
						// step1

						if (timestamp[transaction_num] == 0)
							timestamp[transaction_num] = ++TS;

						if (comm[1].equals("END")) {
							num_of_clients--;
							System.out.println(num_of_clients);
							write_to_log(clientSentence);
							// outToClient.writeBytes("OK");
							serverResponse = "OK";
							log_flag = true;
						}

						else if (comm[1].equals("COMMIT")) {
							commited[transaction_num] = 1;
							write_to_log(clientSentence);
							// outToClient.writeBytes("OK");
							serverResponse = "OK";
							log_flag = true;
						} else if (comm[1].equals("ABORT")) {
							commited[transaction_num] = 2;
							write_to_log(clientSentence);
							// outToClient.writeBytes("OK");
							serverResponse = "OK";
							log_flag = true;
						} else if (comm[1].equals("READLOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU AREN'T ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
								if (item.status != 2) {
									item.status = 1;
									item.list.add(transaction_num);
									// outToClient.writeBytes("OK");
									waitgraph[transaction_num] = false;
									serverResponse = "OK";
								} else {
									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										item.list.add(transaction_num);
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";
										// item.list.remove(0);

									}
								}
							}

						} else if (comm[1].equals("WRITELOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU AREN'T ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
								if (item.status != 2) {
									if (item.status == 0 || (item.status == 1 && item.list.size() == 1
											&& item.list.contains(transaction_num))) {
										item.status = 2;
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										if (!item.list.contains(transaction_num))
											item.list.add(transaction_num);
									} else {

										for (int inter = 0; inter < item.list.size(); inter++) {

											int t = item.list.get(inter);
											int i = timestamp[transaction_num];
											int j = timestamp[t];
											int temp = waitType(i, j, t, transaction_num, item);
											if (temp == 0) {
												waitgraph[transaction_num] = false;
												serverResponse = "OK";
											} else if (temp == 1) {
												serverResponse = "WAIT";
												break;

											} else if (temp == 2) {
												serverResponse = "RESTART";
												break;

											}
										}

									}
									// outToClient.writeBytes("OK");
								} else {

									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										serverResponse = "OK";
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";
										// item.list.remove(0);

									}
									// outToClient.writeBytes("WAIT");

								}
							}
						} else if (comm[1].equals("UNLOCK")) {
							shrinking_phase[transaction_num] = true;
							Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
							if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num)) {
								if (commited[transaction_num] == 0) {
									System.out.println("YOU AREN'T ALLOWED TO UNLOCK A WRITE-LOCK BEFORE COMMIT");
									System.exit(0);
								} else {
									item.status = 0;
									item.list.remove((Integer) transaction_num);
								}
							} else if (item.status == 1 && item.list.size() == 1
									&& item.list.contains(transaction_num)) {
								item.status = 0;
								item.list.remove((Integer) transaction_num);
							} else {
								item.list.remove((Integer) transaction_num);
							}
							serverResponse = "OK";
							// outToClient.writeBytes("OK");
						} else {
							File valuefile = new File(comm[2] + ".txt");
							Item item = items.get((int) (Character.toLowerCase(comm[2].charAt(0)) - 'a'));
							// READ COMMAND
							if (comm[1].equals("READ")) {
								Scanner valuefw = new Scanner(valuefile);
								String temp = valuefw.next();

								System.out.println(comm[2] + "=" + temp);
								valuefw.close();
								// STELOUME TO VALUE PISW STON CLIENT

								// outToClient.writeBytes(temp);
								serverResponse = temp;

								write_to_log(clientSentence);
								log_flag = true;
								// WRITE COMMAND
							} else if (comm[1].equals("WRITE")) {
								System.out.println("OK");
								// STELOUME TO VALUE PISW STON CLIENT
								// outToClient.writeBytes("OK");
								serverResponse = "OK";
								write_to_log(clientSentence);
								log_flag = true;
							} else {
								Exception e = null;
								throw e;
							}
						}

						// if the first line is readlock dont count
						if (log_flag) {// if Unlock or lock dont change the line

							if (earliestLSN[transaction_num] == 0) {
								earliestLSN[transaction_num] = logline;
							}
						}

						// step2

						// CHECKPOINT PHASE

						if (logline % 10 == 0 || num_of_clients == 0) {
							int loglinecheck = logline;
							if (log_flag == false)
								loglinecheck--;
							int trans_num;
							int start_line = loglinecheck;
							for (int i = 0; i < 10; i++) {// number of
															// transactions
								if (commited[i] == 1) {
									if (earliestLSN[i] < start_line) {
										start_line = earliestLSN[i];
									}
								}
							}

							// System.out.println("start_line "+start_line);

							Scanner redo = new Scanner(log_file);
							int i = 1;
							while (i < start_line) {
								i++;
								redo.nextLine();
							}
							while (i <= loglinecheck) {
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
						if (log_flag) {// if Unlock or lock dont change the line

							logline++;
						}
					}
				}

				outToClient.writeBytes(serverResponse + '\n');

			}
		} catch (IOException e1) {
			System.out.println("Provlima ston server");
			e1.printStackTrace();
		} catch (Exception e1) {

			System.err.println("Wrong Input");
			e1.printStackTrace();
		}
	}

	/*
	 * @Override public int Click_Strict() { //Must be implement at child c++;
	 * if (c % 2 == 1) return 1; return 0; }
	 */
}
