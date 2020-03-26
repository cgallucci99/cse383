import java.net.*;
import java.io.*;
import java.util.logging.*;

/*
 * My Name
 * Client for first programming project for cse383
 * 
 * This client will:
 * Normally description would go here.
 * 
 * I certify this is my own work
 */
public class Exam1Client {
	int port;
	String fqdn;
	Socket socket = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;



	//Main method - DO NO WORK IN PSVM, just invoke other classes
	//invocation java <PGM> serverName serverPort Values...
	public static void main(String[] args) {
		int port = 3000;
		String fqdn= "10.33.4.81";
		
		// if (args.length < 6)
		// {
		// 	System.err.println("Invalid usage - FQDN port VALUES (at least 2 values required)");
		// }
		//copy over values from command line
		//java.util.ArrayList<String> values = new java.util.ArrayList<String>();
		// for (int i=2;i<args.length;i++) {
		// 	values.add(args[i]);
		// }

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

	//main - takes an arraylist of arguments -> each argument is type, number. 
	public void Main() {
		boolean result = false;
		String greeting = "";
		String response = "";

		for (int retry = 5; retry > 0 && !result; retry --) {
			System.out.println("Loop "+retry);
			try {
				LOGGER.info("Connecting");
				connect();
				//greeting = readGreeting();
				//System.out.println("greeting: " + greeting);
				//sendValues(values);
				//dos.writeInt(0);
				//dos.flush();
				System.out.println("Next line: int int1 = dis.readInt()");
				int int1 = dis.readInt();
				System.out.println("int1 = "+int1+"; Next line: int int2 = dis.readInt()");

				int int2 = dis.readInt();
				System.out.println("ints: " + int1+ " "+ int2);
				int mult = int1*int2;
				String res = mult+" galluccs";
				System.out.println("res: " + res);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos));
				bw.write(res);
				bw.newLine();
				bw.flush();
				response = readResponse();
				System.out.println("Response: " + response);
				result = true;
				socket.close();
				System.out.println("Socet is closed");
			} catch (IOException err) {
				System.err.println("Error during protocol " + err.toString());
				LOGGER.log(Level.SEVERE,"error during connection", err);
			    try {   //in error condition try to close socket
			        socket.close();
			    } catch (IOException err1) {}//can ignore error in this case since this is just best effort.
			    
			}
		}
		System.out.println("result: " + result);
		if (result) {
			System.out.println("Success");
			System.out.println("Greeting = " + greeting);
			System.out.println("Response => " + response);
		}
		else {
			System.out.println("Failed");
		}
	}

	//connects to server
	public void connect() throws IOException {
		//
		// Make the socket connection and set DataInputStream and DataOutputStream
		// Set the timeout to 5 seconds
		//
		socket = new Socket(fqdn, port);

		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
System.out.println("Connected in connect()");
	}

	//server sends initial greeting that should contain students uniqueid
	public String readGreeting() throws IOException {

		String greet="";
		// Get the greeting from the server
		greet = dis.readUTF();


		return greet;
	}

	//send values to server
	// public void sendValues(java.util.ArrayList<String> values) throws IOException {
	// 	System.out.println("In sendvalues");
	// 	java.util.Iterator<String> itr = values.iterator();
	// 	while (itr.hasNext()) {
	// 		String type = itr.next();
	// 		if (type.equals("i")) {
	// 			dos.writeInt(1);
	// 			int val = Integer.parseInt(itr.next());
	// 			dos.writeInt(val);
	// 		} else if (type.equals("d")) {
	// 			dos.writeInt(2);
	// 			double val = Double.parseDouble(itr.next());
	// 			dos.writeDouble(val);
	// 		}


	// 		// Write your code here

	// 	}
	// 	dos.writeInt(0);	//signifies end of values
	// 	dos.flush();
	// }

	//read response - first string should be "OK" or "Error - and error message"  Then it should send back SUM of numbers as a double
	public String readResponse() throws IOException {
		String response="";

			// Write your code here
		response = dis.readUTF();
		//response += dis.readDouble();
System.out.println("in readResponse");
		return response;
	}
}

