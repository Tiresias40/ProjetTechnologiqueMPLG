package Bus;

public class Sender {
	String sender_class;
	String sender_name;
	int sender_id;
	
	public Sender(String sender_class, String sender_name){
		this.sender_class = sender_class;
		this.sender_name = sender_name;
		this.sender_id = -1;
	}
	
	public int register(String sender_class, String sender_name){ // return id
		return 0;
	}
	public int deregister(int sender_id){ // entier de confirmation
		return 0;
	}
}
