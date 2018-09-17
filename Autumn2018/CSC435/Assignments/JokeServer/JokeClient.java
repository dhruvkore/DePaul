import java.io.*;
import java.net.*;

public class InetClient{
	public static void main(String args[]){
		String serverName;
		if (args.length < 1) serverName = "localhost";
		else serverName = args[0];

		System.out.println("Dhruv's Joke Client, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 30000");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //Input stream object
		try{
			String name;
			String userName;
			
			System.out.print("Enter a Username, (quit) to end: ");
			System.out.flush();
			userName = in.readLine();

			do{
				System.out.print("Enter a hostname or an IP address, (quit) to end: ");
				System.out.flush();
				name = in.readLine(); //Read in
				if(name.indexOf("quit") < 0)
					;
					//getRemoteAddress(name, serverName); //Call helper function
			} while(name.indexOf("quit") < 0); //If quit
			System.out.println("Cancelled by user request.");
		}
		catch (IOException x) { x.printStackTrace();} //Catches exception
	}
}
