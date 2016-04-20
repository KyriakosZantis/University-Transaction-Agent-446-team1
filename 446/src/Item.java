
public class Item {
	String name;
	int value;
	int before_value;
	int status;
	
	public Item(char na){
		name = Character.toString(na);
		value = 0;
		before_value = 0;
		status = 0;
	}
}

