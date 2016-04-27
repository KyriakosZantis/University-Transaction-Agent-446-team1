import java.util.ArrayList;

public class Item {
	String name;
	//int value;
	//int before_value;
	int status; // 0=unlocked 1=readlock 2=writelock
	ArrayList<Integer> list = new ArrayList<Integer>();
	boolean touched;
	
	public Item(char na){
		name = Character.toString(na);
		//value = 0;
		//before_value = 0;
		status = 0;
		touched = false;
	}
}

