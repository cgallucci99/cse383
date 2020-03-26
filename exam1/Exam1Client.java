import java.net.*;
import java.io.*;
import java.util.logging.*;

/*
 * Cam Gallucci
 * Client for first exam for cse383
 * 
 * I certify this is my own work. I used the lab as starter code.
 */
public class Exam1Client {
	int port;
	String fqdn;
	Socket socket = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;

	public static void main(String[] args) {
		int port = 3000;
		String fqdn= "10.33.4.81";

		Exam1Client client = new Exam1Client(port,fqdn);
		client.Main();
	}

	//Construtor
	public Exam1Client(int port, String fqdn) {

		// Set up logging
		try {
			fh = new FileHandler("client.log");
			LOGGER.addHandler(fh);
			LOGGER.setUseParentHandlers(false);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  

		} catch (IOException err) {
			System.err.println("Error - can't open log file");
			//continue here since this is not a blocking error
		}

		LOGGER.info("ClientMAIN - Port = " + port + " fqdn=" + fqdn);
		this.fqdn = fqdn;
		this.port = port;
	}

	public void Main() {
		boolean result = false;
		String greeting = "";
		String response = "";

		for (int retry = 5; retry > 0 && !result; retry --) {
			try {
				LOGGER.info("Connecting");
				connect();
				int int1 = dis.readInt();
				int int2 = dis.readInt();
				int mult = int1*int2;
				String res = mult+" galluccs";
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos));
				bw.write(res);
				bw.newLine();
				bw.flush();
				response = readResponse();
				result = true;
				socket.close();
			} catch (IOException err) {
				System.err.println("Error during protocol " + err.toString());
				LOGGER.log(Level.SEVERE,"error during connection", err);
			    try {   //in error condition try to close socket
			        socket.close();
			    } catch (IOException err1) {}//can ignore error in this case since this is just best effort.
			    
			}
		}
		if (result) {
			System.out.println("Success");
			System.out.println("Response => " + response);
		}
		else {
			System.out.println("Failed");
		}
	}

	//connects to server
	public void connect() throws IOException {
		socket = new Socket(fqdn, port);

		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
	}

	public String readResponse() throws IOException {
		String response="";

		response = dis.readUTF();
		return response;
	}
}

