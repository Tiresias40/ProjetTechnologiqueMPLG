package projet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.json.*;

public class Bus {
	private ServerSocket socketBus;
	private PrintWriter printW; // Facultatif ?
	private BufferedReader reader; // Facultatif ?
	// Tableau contenant toutes les infos sur les capteurs connectés, regroupant
	// tous les messages
	// Matrice : sender_id et msg_id -> facile à retrouver
	private ArrayList<ArrayList<JsonObject>> tabMsg = new ArrayList<ArrayList<JsonObject>>();
	private ArrayList<Sender> tabSender = new ArrayList<Sender>();

	// num de port 7182
	public Bus(int numPort) throws IOException {
		this.socketBus = new ServerSocket(numPort);
	}

	public Bus() throws IOException {
		this(7182);
	}

	JsonObject request(JsonObject jsonObj) {
		String type = jsonObj.getString("type");
		String sender_class = jsonObj.getString("sender_class");
		String sender_name = jsonObj.getString("sender_name");
		
		if (type.equals("register")) {
			jsonObj = bus_register(sender_class, sender_name);
		} else if (type.equals("deregister")) {
			// Désenregistrement
		} else if (type.equals("list")) {
			jsonObj = list(sender_class, sender_name);
		} else if (type.equals("send")) {
			jsonObj = receive(jsonObj.getInt("sender_id"), jsonObj.getJsonObject("contents"));
		} else {
			// Erreur
		}

		return jsonObj; // A modifier, comment l'envoyer au capteur ? Même objet que celui reçu, problème ?
	}

	// Fonctions register, deregister à faire version Bus
	
	public JsonObject bus_register(String sender_class, String sender_name){
		// if sender déjà enregistré ?
		Sender new_sender = new Sender(sender_class, sender_name, null);
		tabSender.add(new_sender);
		
		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
		JsonObject ack = ackBuild.build();
		
		return ack;
	}

	public JsonObject list(String sender_class, String sender_name) {
		ArrayList<Sender> results = new ArrayList<Sender>();
		for (int count = 0; count < tabSender.size(); count++) {
			if (sender_class != null
					&& tabSender.get(count).getSender_class().equals(sender_class)) {
				if (sender_name != null
						&& tabSender.get(count).getSender_name()
								.equals(sender_name)) { // Les deux
					results.add(tabSender.get(count));
				} else { // Que class
					results.add(tabSender.get(count));
				}
			} else {
				if (sender_name != null
						&& tabSender.get(count).getSender_name()
								.equals(sender_name)) { // Que name
					results.add(tabSender.get(count));
				} else { // Tout envoyer (aucun des deux attributs)
					results.add(tabSender.get(count));
				}
			}
		}
		// Convertir Sender[] en JsonObject (Array ?)
		JsonObjectBuilder buildArray = Json.createObjectBuilder();
    JsonObjectBuilder answerBuild = Json.createObjectBuilder();
      answerBuild.add("type", "list");
      answerBuild.add("ack", "ok");
      JsonArrayBuilder result = Json.createArrayBuilder();
        for(int i = 0; i < tabSender.length; i++) {
          Sender s;
          s = tabSender[i]; 
          result.add(buildArray);
            buildArray.add("sender_id", s.getSender_id());
            buildArray.add("sender_class", s.getSender_class());
            buildArray.add("sender_name", s.getSender_name());
            buildArray.add("last_message_id", s.getLast_id());
        }
		return answer;
	}

	public JsonObject receive(int sender_id, JsonObject contents) {
		// Réponse à send() de Sender.java
		// Penser à enregistrer la date avec le message, cf java.util.Date
		if(!tabMsg.contains(sender_id)){
			ArrayList<JsonObject> sender_msg_list = new ArrayList<JsonObject>();
			tabMsg.add(sender_msg_list);
		}
		JsonObjectBuilder msgBuild = Json.createObjectBuilder();
		msgBuild.add("sender_id", sender_id);
		msgBuild.add("contents", contents);
		JsonObject msg = msgBuild.build();
		tabMsg.get(sender_id).add(msg);
		
		JsonObjectBuilder ackBuild = Json.createObjectBuilder();
		ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
		JsonObject ack = ackBuild.build();
		
		return ack;
	}

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
		// requestBuild.add("date", date); // comment avoir la date du message ?
		// L'enregistrer au même moment ?
		requestBuild.add("contents", msg.getJsonObject("contents"));
		JsonObject request = requestBuild.build();

		return request;
	}

	public JsonObject get_last(int sender_id) {
		// Prendre indice dernier message tableau concernant sender_id
		return get(sender_id, tabMsg.get(sender_id).get(tabMsg.size()).getInt("msg_id"));
	}
}
