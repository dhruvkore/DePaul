import java.io.*;
import java.net.*;

public class InetClient{
	public static void main(String args[]){
		String serverName;
		if (args.length < 1) serverName = "localhost";
		else serverName = args[0];

		System.out.println("Clark Elliott's Inet Client, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 30000");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //Input stream object
		try{
			String name;
			do{
				System.out.print("Enter a hostname or an IP address, (quit) to end: ");
				System.out.flush();
				name = in.readLine(); //Read in
				if(name.indexOf("quit") < 0)
					getRemoteAddress(name, serverName); //Call helper function
			} while(name.indexOf("quit") < 0); //If quit
			System.out.println("Cancelled by user request.");
		}
		catch (IOException x) { x.printStackTrace();} //Catches exception
	}

	static String toText (byte ip[]){
		StringBuffer result = new StringBuffer();
		for (int i = 0; i< ip.length; ++i){
			if (i > 0) result.append(".");
			result.append (0xff & ip[i]);
		}
		return result.toString();
	}

	static void getRemoteAddress(String name, String serverName){
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;

		try{
			sock = new Socket(serverName, 30000); //Create socket object

			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //Input from Server
			toServer = new PrintStream(sock.getOutputStream()); //Output to Server

			toServer.println(name);
			toServer.flush();
			for(int i = 1; i <= 3; i++){
				textFromServer = fromServer.readLine(); //Read text from input stream from server
				if(textFromServer != null) System.out.println(textFromServer); //Print text
			}
			sock.close(); //Close connection
		}
		catch(IOException x){ //Catch Exception in case of errors
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}
}
