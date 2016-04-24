import java.util.ArrayList;

import Interface.Menu;

public class OurMain extends Menu {

	
	Thread[] clients = new Thread[num_of_clients];
	static ArrayList<Item> items = new ArrayList<Item>(20);
	/**
	 * @param args
	 */
	
	@Override
	public int doButtonAction() {

		String inputfile;
		String name;

		switch(option_update){
			case 1:{
					
					System.out.println("The Update is  Immediate");
					break;
					}
			 case 2:{
				
				System.out.println("The Update is  Deferred");
				break;
				}
			 default:{System.out.println("ERROR.You didnt Choose");}
		}
		
		switch(option_type){
		case 1:{
				
				System.out.println("The Type is  Wound and wait");
				break;
				}
		 case 2:{
			
			System.out.println("The Type is  Wait And Die");
			break;
			}
		 case 3:{
				
			System.out.println("The Type is  Cautious Waiting");
			break;
			}
		 default:{System.out.println("ERROR.You didnt Choose");}
		
	}
		
		System.out.println("The Number of Clients are "+num_of_clients);
		
		
		for (int i=0; i<20; i++)
			items.add(new Item((char)('a'+i)));
		Thread t1 = new Thread(new TCPServer("Server"));
		t1.setDaemon(true);
		t1.start();
		for (int i=0; i<num_of_clients; i++){
			inputfile="input"+(i+1)+".txt";
			name ="Client"+(i+1);
			try{
			clients[i] = new Thread(new TCPClient(inputfile, name, (i+1)));
			clients[i].start();
			}catch (Exception e){
				System.out.println("There is no file with name "+inputfile );
			}
		
		}
	

		return 0;
	}

	
	public static void main(String[] args) {
		OurMain o=new OurMain();
		o.setVisible(true);
	}
}
