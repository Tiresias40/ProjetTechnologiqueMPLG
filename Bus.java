package projet;
import java.io.*;
import java.net.*;

import javax.json.*;

public class Bus {
		ServerSocket socketBus = null; 
		PrintWriter printW;
		BufferedReader reader;
		int last_id; // Id du dernier message
		// Tableau contenant toutes les infos sur les capteurs connectés
		
		
		//num de port 7182
		public Bus(int numPort) throws IOException {
		  socketBus = new ServerSocket(numPort);
    }
		
		public Bus() throws IOException {
		  this(7182);
		}
		
		JsonObject request (JsonObject jsonObj) {
		  printW.println(jsonObj);
		  JsonReader jreader = Json.createReader(reader);
		  jsonObj = (JsonObject) jreader.read();
		  return jsonObj;
		}
		
		public void list(String sender_class, String sender_name) {
		  // A FAIRE
		}
}
