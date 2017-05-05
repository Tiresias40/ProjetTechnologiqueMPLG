package driver;

import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import projet.Bus;
import projet.Sensor;

public class DriverGPS {
	  private CommPortIdentifier id;
	  private SerialPort port;
	  private InputStream in;
	  private OutputStream out;
	  private String name;
	  private String url;
	  private Sensor sensor = null; 
	  private Bus bus = null;
	  
	  public DriverGPS(String url, String name) {
		  this.url = url;
		  this.name = name;
	  }
	  
	  public void initialize() throws Exception {
			this.id = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");
			this.port = (SerialPort) id.open("ecouteur", 1000);
			port.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			this.in = port.getInputStream(); 
			this.out = port.getOutputStream();
			this.bus = new Bus(7182);
			this.sensor = new Sensor("GPS",name, bus);
	  }
	  
	  public static void main(String[] args) throws Exception {
		  DriverGPS driver = new DriverGPS(args[0], args[1]);
		  
		  driver.initialize();
		  driver.sensor.register();
		  //TODO : récupérer les informations et les transmettre via send()
	  }
}
