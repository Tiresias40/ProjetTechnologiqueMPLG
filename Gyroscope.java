package projet;

import javax.json.*;

public class Gyroscope extends Sensor {

	private double posX;
	private double posY;
	private double posZ;

	public Gyroscope() {
		super();
		this.posX = 0.0;
		this.posY = 0.0;
		this.posZ = 0.0;
		this.sender_class = "Gyroscope";
	}

	public Gyroscope(String name) {
		super();
		this.posX = 0.0;
		this.posY = 0.0;
		this.posZ = 0.0;
		this.sender_name = name;
	}
	//getter and setters
	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	//initialise les données à envoyer par la méthode send()
	public JsonObject initContentToSend() {
		JsonObjectBuilder contentBuilder = Json.createObjectBuilder();
		contentBuilder.add("x", getPosX());
		contentBuilder.add("y", getPosY());
		contentBuilder.add("z", getPosZ());
		JsonObject content = contentBuilder.build();
		return content;
	}
}
