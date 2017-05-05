package projet;

import javax.json.*;

public class Accelerometer extends Sensor {

	private double valueX;
	private double valueY;
	private double ValueZ;

	public Accelerometer() {
		super();
		this.valueX = 0.0;
		this.valueY = 0.0;
		this.ValueZ = 0.0;
		this.sender_class = "Accelerometer";
	}

	public Accelerometer(String name) {
		super();
		this.valueX = 0.0;
		this.valueY = 0.0;
		this.ValueZ = 0.0;
		this.sender_name = name;
	}
	//getter and setters
	public double getValueX() {
		return valueX;
	}

	public void setValueX(double valueX) {
		this.valueX = valueX;
	}

	public double getValueY() {
		return valueY;
	}

	public void setValueY(double valueY) {
		this.valueY = valueY;
	}

	public double getValueZ() {
		return ValueZ;
	}

	public void setValueZ(double valueZ) {
		ValueZ = valueZ;
	}
	
	//initialise les données à envoyer par la méthode send()
	public JsonObject initContentToSend() {
		JsonObjectBuilder contentBuilder = Json.createObjectBuilder();
		contentBuilder.add("x", getValueX());
		contentBuilder.add("y", getValueY());
		contentBuilder.add("z", getValueZ());
		JsonObject content = contentBuilder.build();
		return content;
	}
}
