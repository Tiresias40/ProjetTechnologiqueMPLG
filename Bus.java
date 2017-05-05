package projet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.json.*;

public class Bus {
	private ServerSocket socketBus;
	private PrintWriter printW;
	private BufferedReader reader;
	// Tableau contenant toutes les infos sur les capteurs connectés, regroupant
	// tous les messages
	// Matrice : sender_id et msg_id -> facile à retrouver
	private ArrayList<ArrayList<JsonObject>> tabMsg = new ArrayList<ArrayList<JsonObject>>();
	private ArrayList<Sensor> tabSender = new ArrayList<Sensor>();

	public Bus() throws IOException {
		this(7182);
	}

	// num de port 7182
	public Bus(int numPort) throws IOException {
		this.socketBus = new ServerSocket(numPort);
	}

	// permet de construire la réponse est de l'envoyer
	JsonObject request(JsonObject jsonObj) {
		String type = jsonObj.getString("type");
		String sender_class = jsonObj.getString("sender_class");
		String sender_name = jsonObj.getString("sender_name");
		int sender_id = jsonObj.getInt("sender_id");
		int msg_id = jsonObj.getInt("last_message_id");

		if (type.equals("register")) {// demande de type register
			jsonObj = bus_register(sender_class, sender_name);
		} else if (type.equals("deregister")) { // demande de type deregister
			jsonObj = bus_deregister(sender_id);
		} else if (type.equals("list")) { // demande de type list
			jsonObj = list(sender_class, sender_name);

		} else if (type.equals("send")) { // demande de type send
			jsonObj = receive(sender_id, jsonObj.getJsonObject("contents"));
		} else if (type.equals("get")) { // demande de type get
			jsonObj = get(sender_id, msg_id);
		} else if (type.equals("get_last")) { // demande de type get_last
			jsonObj = get_last(sender_id);
		} else { // type de la demande non existant
			JsonObjectBuilder answerBuild = Json.createObjectBuilder();
			answerBuild.add("type", type);
			JsonObjectBuilder ackBuild = Json.createObjectBuilder();
			ackBuild.add("resp", "error");
			ackBuild.add("error_id", 400);
			JsonObject ack = ackBuild.build();
			jsonObj = answerBuild.build();
		}

		return jsonObj;
	}

	// construit la réponse pour une demande de type register
	public JsonObject bus_register(String sender_class, String sender_name) {
		Sensor new_sender = new Sensor(sender_class, sender_name, null);
		JsonObject answer = null;
		JsonObjectBuilder answerBuild = Json.createObjectBuilder();
		answerBuild.add("type", "register");
		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		if (tabSender.contains(new_sender)) { // le capteur est déjà présent sur le bus
			ackBuild.add("resp", "error");
			ackBuild.add("error_id", 400);
		} else {
			int i = 0;
			while (i < tabSender.size() || tabSender.get(++i).getSender_id() != -1
					&& tabSender.get(i).getSender_class().equals(new_sender.getSender_class())) {
				;
			} // on le place à la suite si le tableau n'est pas plein
				// ou à la première case vide qui avait le même type de capteur
			new_sender.setSender_id(i);
			tabSender.add(new_sender);

			ackBuild.add("resp", "ok");
			JsonObject ack = ackBuild.build();
			answerBuild.add("ack", ack);

			answerBuild.add("sender_id", i);
			answer = answerBuild.build();
		}
		return answer;
	}

	// construit la réponse pour une demande de type deregister
	public JsonObject bus_deregister(int sender_id) {
		// on vide la case où il était stocké
		tabSender.get(sender_id).setSender_id(-1);
		tabSender.get(sender_id).setSender_name("");

		JsonObjectBuilder answerBuild = Json.createObjectBuilder();
		answerBuild.add("type", "deregister");

		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		ackBuild.add("resp", "ok");
		JsonObject ack = ackBuild.build();
		answerBuild.add("ack", ack);

		JsonObject answer = answerBuild.build();

		return answer;
	}

	// construit la réponse pour une demande de type list
	public JsonObject list(String sender_class, String sender_name) { //arguments facultatifs
		// ArrayList contenant tous les capteurs concernés par la requête
		ArrayList<Sensor> results = new ArrayList<Sensor>(); 
		for (int count = 0; count < tabSender.size(); count++) {
			if (sender_class != null && tabSender.get(count).getSender_class().equals(sender_class)) {
				if (sender_name != null && tabSender.get(count).getSender_name().equals(sender_name)) {
					// la requête demande tous les capteurs par classe et par nom
					results.add(tabSender.get(count));
				} else { // la requête demande tous les capteurs par classe
					results.add(tabSender.get(count));
				}
			} else { // par de demande pour la classe
				if (sender_name != null && tabSender.get(count).getSender_name().equals(sender_name)) { 
				    // la requête demande tous les capteurs par nom																					
					results.add(tabSender.get(count));
				} else { //la requête demande d'envoyer tous les capteurs enregistrés (aucun des arguments de la fonction )
					results.add(tabSender.get(count));
				}
			}
		}
		// Conversion ArrayList<Sender> en JsonObject
		JsonObjectBuilder answerBuild = Json.createObjectBuilder();
		answerBuild.add("type", "list");

		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		ackBuild.add("resp", "ok");
		JsonObject ack = ackBuild.build();
		answerBuild.add("ack", ack);

		JsonArray result = Json.createArrayBuilder().build(); 
		// Crée un JsonArray vide
		JsonObjectBuilder buildArray;
		Sensor s;
		for (int i = 0; i < results.size(); i++) {
			buildArray = Json.createObjectBuilder(); 
			// Crée un nouveau build Array à chaque itération
			s = tabSender.get(i);
			buildArray.add("sender_id", s.getSender_id());
			buildArray.add("sender_class", s.getSender_class());
			buildArray.add("sender_name", s.getSender_name());
			buildArray.add("last_message_id", s.getLast_message_id()); 
			result.add(buildArray.build());
		}
		answerBuild.add("results", result);
		JsonObject answer = answerBuild.build();

		return answer;
	}
	
	// construit la réponse pour une demande de type send
	public JsonObject receive(int sender_id, JsonObject contents) {
		// Date non ajouté au message
		if (!tabMsg.contains(sender_id)) {
			ArrayList<JsonObject> sender_msg_list = new ArrayList<JsonObject>();
			tabMsg.add(sender_msg_list);
		}
		JsonObjectBuilder msgBuild = Json.createObjectBuilder();
		msgBuild.add("sender_id", sender_id);
		msgBuild.add("contents", contents);
		JsonObject msg = msgBuild.build();
		tabMsg.get(sender_id).add(msg);

		JsonObjectBuilder answerBuild = Json.createObjectBuilder();
		answerBuild.add("type", "send");

		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		JsonObject ack = ackBuild.build();
		answerBuild.add("ack", ack);

		JsonObject answer = answerBuild.build();
		return answer;
	}
	// construit la réponse pour une demande de type send
	public JsonObject get(int sender_id, int msg_id) {
		// Chercher le message dans tableau messages du bus
		JsonObject msg = tabMsg.get(sender_id).get(msg_id); // A modifier !!!

		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "get");

		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
		JsonObject ack = ackBuild.build();
		requestBuild.add("ack", ack);

		requestBuild.add("msg_id", msg_id);
		requestBuild.add("contents", msg.getJsonObject("contents"));
		JsonObject request = requestBuild.build();

		return request;
	}
	// construit la réponse pour une demande de type send
	public JsonObject get_last(int sender_id) {
		// Prendre indice dernier message tableau concernant sender_id
		return get(sender_id, tabSender.get(sender_id).getLast_message_id());
	}
}
