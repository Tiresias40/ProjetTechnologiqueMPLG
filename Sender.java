package projet;

import javax.json.*;

public class Sender {
	private String sender_class;
	private String sender_name;
	private int sender_id;
	
	public Sender(String sender_class, String sender_name) {
		this.sender_class = sender_class;
		this.sender_name = sender_name;
		this.sender_id = -1;
	}
	
	public void register(String sender_class, String sender_name) { 
	  JsonObjectBuilder request = Json.createObjectBuilder();
	  request.add("sender_class", sender_class);
	  request.add("sender_name", sender_name);
	  request.build();
		
	}
	
	public int deregister(int sender_id) { // entier de confirmation
		return 0;
	}
}
