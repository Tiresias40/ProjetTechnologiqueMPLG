package projet;

import javax.json.*;

public class Sender {
	private String sender_class;
	private String sender_name;
	private int sender_id;
	private Bus bus;
	
	public Sender(String sender_class, String sender_name, Bus bus) {
		this.sender_class = sender_class;
		this.sender_name = sender_name;
		this.sender_id = -1;
		this.bus = bus;
	}
	
	public void printError(int error){
	  switch(error){
	  case 400:
	    System.out.println("Bad Request - Your request sucks.");
	    break;
	  case 401:
	    System.out.println("Unauthorized - Your API key is wrong.");
	  case 403:
	    System.out.println("Forbidden - The kitten requested is hidden for administrators only.");
	  case 404:
	    System.out.println("Not Found - The specified kitten could not be found.");
	  case 405:
	    System.out.println("Method Not Allowed - You tried to access a kitten with an invalid method.");
	  case 406:
	    System.out.println("Not Acceptable - You requested a format that isn't json.");
	  case 410:
	    System.out.println("Gone - The kitten requested has been removed from our servers.");
	  case 418:
	    System.out.println("I'm a teapot.");
	  case 429:
	    System.out.println("Too many requests - You're requesting too many kittens ! Slow down !");
	  case 500:
	    System.out.println("Internal server error - We had a problem with our server. Try again later.");
	  case 503:
	    System.out.println("Service Unavailable - We're temporarily offline for maintenance. Please try again later.");
	  }
	}
	
	public void register(String sender_class, String sender_name) { 
	  //initialisation du json à envoye printError(error);r
	  JsonObjectBuilder requestBuild = Json.createObjectBuilder();
	  requestBuild.add("type", "register");
	  requestBuild.add("sender_class", sender_class);
	  requestBuild.add("sender_name", sender_name);
	  JsonObject request = requestBuild.build(); 
	  //retour du serveur + mise à jour du champ sender_id
		JsonObject answer = bus.request(request);
		String ack = answer.getString("resp");
		if(ack.equals("ok")) 
		  sender_id = answer.getInt("sender_id");
		else {
		  int error = answer.getInt("error_id");
		  printError(error);
		}
	}
	
	public String deregister(int sender_id) {
	  //initialisation du json à envoyer
	  JsonObjectBuilder requestBuild = Json.createObjectBuilder();
	  requestBuild.add("type", "deregister");
	  requestBuild.add("sender_id", sender_id);
	  JsonObject request = requestBuild.build();
	  //retour serveur
	  JsonObject answer = bus.request(request);
	  String ack = answer.getString("resp");
	  if(ack.equals("ok")){
	  }
	  else {
	    int error = answer.getInt("error_id");
	    printError(error);
	  }
	  return ack;
	}
}