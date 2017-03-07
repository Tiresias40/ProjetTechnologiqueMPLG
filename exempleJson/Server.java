package exempleJson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Server {
  
 public static void main(String args[]) throws Exception {
    //on crée les sockets
    ServerSocket server = new ServerSocket(7182);
    Socket socket = server.accept();
    
    //on crée les buffers
    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
    //permet de lire le json
    JsonReader req = Json.createReader(reader);
   
    //crée l'objet json 
    JsonObject jsonObj = req.readObject();
    
    //on crée une instance d'un Object que l'on initialise
    Object obj = new Object();
    obj.setClass(jsonObj.getString("sender_class"));
    obj.setName(jsonObj.getString("sender_name"));
    
    //on affiche l'instance de Object que l'on a obtenu
    System.out.println(obj.toString());
    
    //on ferme les buffers et le socket
    reader.close();
    server.close();
  }

}
