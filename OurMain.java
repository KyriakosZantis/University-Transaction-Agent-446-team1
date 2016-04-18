
public class OurMain {

	static int num_of_clients = 3;
	static Thread[] clients = new Thread[num_of_clients];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t1 = new Thread(new TCPServer());
		t1.setDaemon(true);
		t1.start();
		for (int i=0; i<num_of_clients; i++){
			String inputfile="input"+(i+1)+".txt";
			clients[i] = new Thread(new TCPClient1(inputfile));
			clients[i].start();
		}
	}

}
