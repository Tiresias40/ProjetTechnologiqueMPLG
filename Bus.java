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
    int sender_id = jsonObj.getInt("sender_id");

    if (type.equals("register")) {
      jsonObj = bus_register(sender_class, sender_name);
    } else if (type.equals("deregister")) {
      jsonObj = bus_deregister(sender_id);
    } else if (type.equals("list")) {
      jsonObj = list(sender_class, sender_name);

    } else if (type.equals("send")) {
      jsonObj = receive(sender_id, jsonObj.getJsonObject("contents"));
    } else {
      // Erreur
    }

    return jsonObj; // A modifier, comment l'envoyer au capteur ? Même objet
    // que celui reçu, problème ?
  }

  public JsonObject bus_register(String sender_class, String sender_name) {
    // if sender déjà enregistré ?
    // Gére les id des différents types de capteurs
    Sender new_sender = new Sender(sender_class, sender_name, null);
    if(tabSender.contains(new_sender)) {
      //return error
    }
    else {
      int i = 0;
      while (i < tabSender.size()
          || tabSender.get(++i).getSender_id() != -1
          && tabSender.get(i).getSender_class().equals(new_sender.getSender_class())) 
      {;}
      new_sender.setSender_id(i);
      tabSender.add(new_sender);
    
    
      JsonObjectBuilder answerBuild = Json.createObjectBuilder();
      answerBuild.add("type", "register");

      JsonObjectBuilder ackBuild = Json.createObjectBuilder();
      ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
      JsonObject ack = ackBuild.build();
      answerBuild.add("ack", ack);

      answerBuild.add("sender_id", i);
      JsonObject answer = answerBuild.build();
    
      return answer;
    }//à corriger
  }

  public JsonObject bus_deregister(int sender_id) {
    // Gérer cas d'erreur, est-ce que l'on supprime aussi les messages ?
    tabSender.get(sender_id).setSender_id(-1);
    tabSender.get(sender_id).setSender_name("");

    JsonObjectBuilder answerBuild = Json.createObjectBuilder();
    answerBuild.add("type", "deregister");

    JsonObjectBuilder ackBuild = Json.createObjectBuilder();
    ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
    JsonObject ack = ackBuild.build();
    answerBuild.add("ack", ack);

    JsonObject answer = answerBuild.build();

    return answer;
  }

  public JsonObject list(String sender_class, String sender_name) {
    ArrayList<Sender> results = new ArrayList<Sender>(); // ArrayList contenant
                                                         // tous les capteurs
                                                         // concernés par la
                                                         // requête
    for (int count = 0; count < tabSender.size(); count++) {
      if (sender_class != null
          && tabSender.get(count).getSender_class().equals(sender_class)) {
        if (sender_name != null
            && tabSender.get(count).getSender_name().equals(sender_name)) { // Les
                                                                            // deux
          results.add(tabSender.get(count));
        } else { // Que class
          results.add(tabSender.get(count));
        }
      } else {
        if (sender_name != null
            && tabSender.get(count).getSender_name().equals(sender_name)) { // Que
                                                                            // name
          results.add(tabSender.get(count));
        } else { // Tout envoyer (aucun des deux attributs)
          results.add(tabSender.get(count));
        }
      }
    }
    // Conversion ArrayList<Sender> en JsonObject (Array ?)
    JsonObjectBuilder answerBuild = Json.createObjectBuilder();
    answerBuild.add("type", "list");

    JsonObjectBuilder ackBuild = Json.createObjectBuilder();
    ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
    JsonObject ack = ackBuild.build();
    answerBuild.add("ack", ack);

    JsonArray result = Json.createArrayBuilder().build(); // Crée un JsonArray
                                                          // vide
    JsonObjectBuilder buildArray;
    Sender s;
    for (int i = 0; i < results.size(); i++) {
      buildArray = Json.createObjectBuilder(); // Crée un nouveau buildArray à
                                               // chaque itération (pas sûr)
      s = tabSender.get(i);
      buildArray.add("sender_id", s.getSender_id());
      buildArray.add("sender_class", s.getSender_class());
      buildArray.add("sender_name", s.getSender_name());
      buildArray.add("last_message_id", s.getLast_message_id()); // Faire
                                                                 // attribut
                                                                 // last_message_id
                                                                 // dans
                                                                 // Sender.java
      result.add(buildArray.build());
    }
    answerBuild.add("results", result);
    JsonObject answer = answerBuild.build();

    return answer;
  }

  public JsonObject receive(int sender_id, JsonObject contents) {
    // Réponse à send() de Sender.java
    // Penser à enregistrer la date avec le message, cf java.util.Date
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
    ackBuild.add("resp", "ok"); // Penser à coder le cas d'erreur !
    JsonObject ack = ackBuild.build();
    answerBuild.add("ack", ack);

    JsonObject answer = answerBuild.build();
    return answer;
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
    return get(sender_id, tabSender.get(sender_id).getLast_message_id());
  }
}
