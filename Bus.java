package projet;

import java.io.*;
import java.net.*;

import javax.json.*;

public class Bus {
	private ServerSocket socketBus;
	private PrintWriter printW; // Facultatif
	private BufferedReader reader; // Facultatif
	// Tableau contenant toutes les infos sur les capteurs connectés, regroupant tous les messages
	// Matrice : sender_id et msg_id -> facile à retrouver
	private JsonObject[][] tabMsg = new JsonObject[10][100]; // Passer en Array, ArrayList, List ? JsonObject particulier ?
	private Sender[] tabSender = new Sender[10]; // Passer en Array, ArrayList, List ?

	// num de port 7182
	public Bus(int numPort) throws IOException {
		this.socketBus = new ServerSocket(numPort);
	}

	public Bus() throws IOException {
		this(7182);
	}

	JsonObject request(JsonObject jsonObj) {		
		String type = jsonObj.getString("type");
		// Faire des appels de fonction pour chaque cas -> désengorge code, pas trop grosse fonction request
		// Switch case ou if ? String, compliqué ...
		// Plan A : série if pour convertir String en int -> if(==register) entier=1, if(==deregister) entier=2, ...
		// Plan B : série if pour remplacer switch :
		if(type.equals("register")){
			// Enregistrement
		}else if(type.equals("deregister")){
			// Désenregistrement
		}else if(type.equals("list")){
			list(jsonObj.getString("sender_class"), jsonObj.getString("sender_name"));
		}else if(type.equals("send")){
			// Fonction send (receive ?)
		}else{
			// Erreur
		}
		
		return jsonObj; // A modifier, comment l'envoyer au capteur ?
	}
	
	// Fonctions register, deregister à faire version Bus

	public JsonObject list(String sender_class, String sender_name) {
		Sender[] results = new Sender[tabSender.length];
		for (int count = 0; count < tabSender.length; count++) {
			if (sender_class != null
					&& tabSender[count].getSender_class().equals(sender_class)) {
				if (sender_name != null
						&& tabSender[count].getSender_name()
								.equals(sender_name)) { // Les deux
					results[count] = tabSender[count];
				} else { // Que class
					results[count] = tabSender[count];
				}
			} else {
				if (sender_name != null
						&& tabSender[count].getSender_name()
								.equals(sender_name)) { // Que name
					results[count] = tabSender[count];
				} else { // Tout envoyer (aucun des deux attributs)
					results[count] = tabSender[count];
				}
			}
		}
		// Convertir Sender[] en JsonObject (Array ?)
		return ;
	}
	
	public void receive(){
		// Réponse à send() de Sender.java
	}
	
	public JsonObject get(int sender_id, int msg_id) {
		// Chercher le message dans tableau messages du bus
		JsonObject msg = tabMsg[sender_id][msg_id]; // A modifier
		
		JsonObjectBuilder requestBuild = Json.createObjectBuilder();
		requestBuild.add("type", "get");
		//requestBuild.add("ack", ""); // jsonobject resp ; ok
		requestBuild.add("msg_id", msg_id);
		// requestBuild.add("date", date); // comment avoir la date du message ? L'enregistrer au même moment ?
		// requestBuild.add("contents", msg); // extraire contents du msg/JsonObject
		JsonObject request = requestBuild.build();

		return request;
	}
	
	public JsonObject get_last(int sender_id){
		// Prendre indice dernier message tableau concernant sender_id
	    int idLastMessage = 1; // Aller au dernier objet de la liste tabMsg
		return get(sender_id, idLastMessage); // idLastMessage récupérer avant
	}
}
