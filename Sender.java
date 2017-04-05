package projet;

import java.io.*;
import javax.json.*;

public class Sender {
	private String sender_class;
	private String sender_name;
	private int sender_id;
	private Bus bus; // Peut-être à supprimer ?
	private int last_id; // Id du dernier message

	public Sender(String sender_class, String sender_name, Bus bus) {
		this.sender_class = sender_class;
		this.sender_name = sender_name;
		this.sender_id = -1;
		this.bus = bus;
	}

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

	public void register() {
		// initialisation du json à envoye printError(error);r
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

	public void get(int sender_id, int msg_id) {
		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "get");
		requestBuild.add("sender_id", sender_id);
		requestBuild.add("msg_id", msg_id);
		JsonObject request = requestBuild.build();
		// retour serveur
		JsonObject answer = bus.request(request);
		String ack = answer.getString("resp");
		if (ack.equals("error")) {
			int error = answer.getInt("error_id");
			printError(error);
		} else {
			// Afficher contenu message
			// Temporaire pour GPS
			int lat = answer.getInt("lat");
			int lng = answer.getInt("lng");
			System.out.println("lat : " + lat + " lng : " + lng);
		}
	}
	
	public void get_last(int sender_id){
		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "get_last");
		requestBuild.add("sender_id", sender_id);
		JsonObject request = requestBuild.build();
		// retour serveur
		JsonObject answer = bus.request(request);
		String ack = answer.getString("resp");
		if (ack.equals("error")) {
			int error = answer.getInt("error_id");
			printError(error);
		} else {
			// Afficher contenu message
			// Temporaire pour GPS
			int lat = answer.getInt("lat");
			int lng = answer.getInt("lng");
			System.out.println("lat : " + lat + " lng : " + lng);
		}
	}
}
