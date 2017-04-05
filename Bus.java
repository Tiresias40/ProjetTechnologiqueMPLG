package projet;

import java.io.*;
import java.net.*;

import javax.json.*;

public class Bus {
	private ServerSocket socketBus = null;
	private PrintWriter printW;
	private BufferedReader reader;
	// Tableau contenant toutes les infos sur les capteurs connectés
	private Sender[] tabSender = new Sender[10];

	// num de port 7182
	public Bus(int numPort) throws IOException {
		socketBus = new ServerSocket(numPort);
	}

	public Bus() throws IOException {
		this(7182);
	}

	JsonObject request(JsonObject jsonObj) {
		printW.println(jsonObj);
		JsonReader jreader = Json.createReader(reader);
		jsonObj = (JsonObject) jreader.read();
		jreader.close();
		return jsonObj;
	}

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
		return;
	}
}
