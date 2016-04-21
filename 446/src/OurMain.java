import java.util.ArrayList;

import Interface.Menu;

public class OurMain extends Menu {

	static int num_of_clients = 3;
	static Thread[] clients = new Thread[num_of_clients];
	static ArrayList<Item> items = new ArrayList<Item>(20);
	/**
	 * @param args
	 */
	
	@Override
	public int doButtonAction() {

		String inputfile;
		String name;
		for (int i=0; i<20; i++)
			items.add(new Item((char)('a'+i)));
		Thread t1 = new Thread(new TCPServer("Server"));
		t1.setDaemon(true);
		t1.start();
		for (int i=0; i<num_of_clients; i++){
			inputfile="input"+(i+1)+".txt";
			name ="Client"+(i+1);
			clients[i] = new Thread(new TCPClient(inputfile, name, (i+1)));
			clients[i].start();
		}
	

		return 0;
	}

	
	public static void main(String[] args) {
		OurMain o=new OurMain();
		o.setVisible(true);
	}
}
