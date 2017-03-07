package exempleJson;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.json.*;

public class Client {
  
  public static void main(String [] args) throws Exception {
    //port utilisé par tout le monde : 7182 
    Socket client = new Socket(InetAddress.getLocalHost(), 7182);      
    
    //on crée les buffers
    PrintWriter print = new PrintWriter(client.getOutputStream());
    
    //on crée l'objet json
    JsonObjectBuilder request = Json.createObjectBuilder();
    request.add("object_class", "GPS");
    request.add("object_name", "toto");
    request.build();
    System.out.println("coucou");
    //on l'envoie
    print.println(request.toString());
    print.flush();
    System.out.println("[CLIENT]: "+ request.toString());
   
    print.close();
    client.close();
  }
}
