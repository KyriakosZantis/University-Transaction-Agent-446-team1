
public class OurMain {

	static int num_of_clients = 3;
	static Thread[] clients = new Thread[num_of_clients];
	static char items[] = new char[20];
	static int status_of_items[] = new int[20];
	static int locker_of_items[] = new int[20];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inputfile;
		String name;
		for (int i=0; i<20; i++)
			items[i] = (char)('a'+i);
		// TODO Auto-generated method stub
		Thread t1 = new Thread(new TCPServer());
		t1.setDaemon(true);
		t1.start();
		for (int i=0; i<num_of_clients; i++){
			inputfile="input"+(i+1)+".txt";
			name ="Client"+(i+1);
			clients[i] = new Thread(new TCPClient(inputfile, name, (i+1)));
			clients[i].start();
		}
	}

}
