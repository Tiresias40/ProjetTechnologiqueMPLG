package projet;

import javax.json.*;

public class Sensor {
	protected String sender_class;
	protected String sender_name;
	protected int sender_id;

	protected int last_message_id;
	private Bus bus; // permet l'appel à la méthode request() de Bus

	public Sensor() {
		this.sender_class = null;
		this.sender_name = null;
		this.sender_id = -1;
	}
	public Sensor(String sender_class, String sender_name, Bus bus) {
		this.sender_class = sender_class;
		this.sender_name = sender_name;
		this.sender_id = -1;

		this.last_message_id = -1;
		this.bus = bus;
	}
	//getter and setters
	public String getSender_class() {
		return sender_class;
	}

	public void setSender_class(String sender_class) {
		this.sender_class = sender_class;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public int getSender_id() {
		return sender_id;
	}

	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}
	
	public int getLast_message_id() {
		return last_message_id;
	}

	public void setLast_message_id(int last_message_id) {
		this.last_message_id = last_message_id;
	}
	
	//méthode de renvoi d'erreur selon le code d'erreur
	public void printError(int error) {
		switch (error) {
		case 400:
			System.out.println("Bad Request - Your request sucks.");
			break;
		case 401:
			System.out.println("Unauthorized - Your API key is wrong.");
		case 403:
			System.out
					.println("Forbidden - The kitten requested is hidden for administrators only.");
		case 404:
			System.out
					.println("Not Found - The specified kitten could not be found.");
		case 405:
			System.out
					.println("Method Not Allowed - You tried to access a kitten with an invalid method.");
		case 406:
			System.out
					.println("Not Acceptable - You requested a format that isn't json.");
		case 410:
			System.out
					.println("Gone - The kitten requested has been removed from our servers.");
		case 418:
			System.out.println("I'm a teapot.");
		case 429:
			System.out
					.println("Too many requests - You're requesting too many kittens ! Slow down !");
		case 500:
			System.out
					.println("Internal server error - We had a problem with our server. Try again later.");
		case 503:
			System.out
					.println("Service Unavailable - We're temporarily offline for maintenance. Please try again later.");
		}
	}
	
	//enregistrement d'un capteur
	public void register() {
		// initialisation du jsonObject à envoyer
		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "register");
		requestBuild.add("sender_class", this.sender_class);
		requestBuild.add("sender_name", this.sender_name);
		JsonObject request = requestBuild.build();
		// retour du serveur + mise à jour du champ sender_id
		JsonObject answer = bus.request(request);
		String ack = answer.getString("resp");
		if (ack.equals("ok"))
			this.sender_id = answer.getInt("sender_id");
		else {
			int error = answer.getInt("error_id");
			printError(error);
		}
	}
	
	// déenregistrement d'un capteur
	public String deregister() {
		// initialisation du json à envoyer
		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "deregister");
		requestBuild.add("sender_id", this.sender_id);
		JsonObject request = requestBuild.build();
		// retour serveur
		JsonObject answer = bus.request(request);
		String ack = answer.getString("resp");
		if (ack.equals("error")) {
			int error = answer.getInt("error_id");
			printError(error);
		}
		return ack;
	}
	
	//message à envoyer sur le bus
	public void send(JsonObject content) {
		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "send");
		requestBuild.add("sender_id", this.sender_id);
		requestBuild.add("content", content);
		JsonObject request = requestBuild.build();
		// retour serveur
		JsonObject answer = bus.request(request);
		String ack = answer.getString("resp");
		if (ack.equals("error")) {
			int error = answer.getInt("error_id");
			printError(error);
		}
	}
}
