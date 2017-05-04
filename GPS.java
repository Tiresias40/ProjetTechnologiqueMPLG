package projet;

import javax.json.*;

public class GPS extends Sensor {
	private double latitude;
	private double longitude;

	public GPS() {
		super();
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.sender_class = "GPS";
	}

	public GPS(String name) {
		super();
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.sender_name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public JsonObject initContentToSend() {
		JsonObjectBuilder contentBuilder = Json.createObjectBuilder();
		contentBuilder.add("lat", getLatitude());
		contentBuilder.add("lng", getLongitude());
		JsonObject content = contentBuilder.build();
		return content;
	}
}
