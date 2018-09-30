/*--------------------------------------------------------

1. Name Dhruv Kore / Date 9/23/2018:

2. Java version used is the official version for the class.


3. Precise command-line compilation examples / instructions:

> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java


4. Precise examples / instructions to run this program:

In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

For Joke Server:
N/A

For JokeClient:
Enter UserName (Cannot be blank).
Press Enter for Joke or Proverb.

For JokeClientAdmin
Enter "J" for Joke Mode or "P" for Proverb Mode

5. List of files needed for running the program.

 a. JokeServer.java
 b. JokeClient.java
 c. JokeClientAdmin.java

5. Notes:

I did not implement the random selection.
It only sends Joke or proverb in sequential order.
Only one port for the client connection and only one port for admin connection has been implemented.
----------------------------------------------------------*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class JokeClientAdmin {
	public static ArrayList<String> jokes = new ArrayList<>();
	public static ArrayList<String> proverbs = new ArrayList<>();

	public static void main(String args[]){
		String serverName;
		if (args.length < 1) serverName = "localhost";
		else serverName = args[0];


		System.out.println("Dhruv's Joke Client Admin, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 4546");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //Input stream object
		try{
			String name;
			String userName;

			do{
				System.out.print("Enter J for Joke mode or P for Proverb Mode, (quit) to end: ");
				System.out.flush();
				name = in.readLine(); //Read in
				if(name.indexOf("quit") < 0)
					changeMode(name, serverName); //Call helper function
			} while(name.indexOf("quit") < 0); //If quit
			System.out.println("Cancelled by user request.");
		}
		catch (IOException x) { x.printStackTrace();} //Catches exception
	}

	static void changeMode(String userName, String serverName){
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		String toSend = "";

		try{
			sock = new Socket(serverName, 4546); //Create socket object

			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //Input from Server
			toServer = new PrintStream(sock.getOutputStream()); //Output to Server

			toSend = userName;

			toServer.println(toSend);
			toServer.flush();

			textFromServer = fromServer.readLine(); //Read text from input stream from server

			if(textFromServer != null) System.out.println(textFromServer); //Print text

			sock.close(); //Close connection
		}
		catch(IOException x){ //Catch Exception in case of errors
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}
}
