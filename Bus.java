package Bus;
import java.io.*;
import java.net.*;
import javax.json.*;

public class Bus {
	public void main(String args[]) throws Exception{
		ServerSocket serverSocket = new ServerSocket(8080); // SE METTRE D'ACCORD !!!
		
		while(true){
			Socket socket = serverSocket.accept();
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			String[] sender_tab = new String[5]; // A modifier : peut-être un tableau pour chaque type de device.
			
			int int_sender_class = in.read();
			int int_sender_name = in.read();
			
			// A convertir !!!
			
			/*			
			if (entier < table.length && entier >= 0 ){ // on vérifie que l'id du client
				if(in_out==0){ // Si le client entre :
					if (table[entier] == 0){ // Si le client n'est pas présent
						table[entier] = 1;
						sortie.write(1);
					}else // Si le client est déjà présent.
						sortie.write(0);
				}else // Si le client sort
					table[entier] = 0;
			}
			*/
			
			serverSocket.close();
			socket.close();
		}
	}
}
