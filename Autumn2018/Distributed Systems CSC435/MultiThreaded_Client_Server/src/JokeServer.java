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


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class Worker extends Thread {
	Socket sock;
	StatementType statementType;

	public static final HashMap<String, String> allJokes = new HashMap<String, String>();
	static {
		allJokes.put("JA", "What do Italian ghosts have for dinner? Spook-hetti!");
		allJokes.put("JB", "Where are fish in orbit? In trout-er space.");
		allJokes.put("JC", "I'm so good at sleeping. I can do it with my eyes closed.");
		allJokes.put("JD", "Why is Peter Pan always flying? He neverlands.");
	}

	public static final HashMap<String, String> allProverb = new HashMap<String, String>();
	static {
		allProverb.put("PA", "A cat may look at a king.");
		allProverb.put("PB", "A bad penny always turns up.");
		allProverb.put("PC", "A friend in need is a friend indeed.");
		allProverb.put("PD", "A man is known by his friends.");
	}

	Worker (Socket s, StatementType sT) {sock = s; statementType = sT;}

	public void run(){
		PrintStream out = null;
		BufferedReader in = null;
		try{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream())); //New input object
			out = new PrintStream(sock.getOutputStream()); //New output object

			try{
				String name;
				name = in.readLine(); //Read line from client
				System.out.println("From Client: " + name);
				sendJokeOrProverb(name, out); //Call Helper for remote address
			}
			catch(IOException x){
				System.out.println("Server read error");
				x.printStackTrace();
			}

			sock.close();
		}
		catch(IOException ioe) {System.out.println(ioe);}
	}

	private void sendJokeOrProverb(String name, PrintStream out){
		// From client : "UserName|Jokes|Proverbs"
		String[] sent = name.split("\\|");
		HashSet<String> jokes = new HashSet<>();
		HashSet<String> proverb = new HashSet<>();

		if(sent.length > 1) {
			String[] sendJokes = sent[1].split(",");
			if(!sendJokes[0].equalsIgnoreCase("NONE")) {
				for (String s :
						sendJokes) {
					jokes.add(s);
				}
			}
		}

		if(sent.length > 2) {
			String[] sendProverbs = sent[2].split(",");
			if(!sendProverbs[0].equalsIgnoreCase("NONE")) {
				for (String s :
						sendProverbs) {
					proverb.add(s);
				}
			}
		}

		if(statementType == StatementType.JOKE){
			for (String s :
					allJokes.keySet()) {
				if(!jokes.contains(s)){
					out.println(s + "| " + sent[0] + " | " + allJokes.get(s));
					System.out.println("Sending: " + s + "| " + sent[0] + " | " + allJokes.get(s));
					return;
				}
			}
			out.println("Reset,JA" + "| " + sent[0] + " | " + allJokes.get("JA"));
			System.out.println("Sending: JA" + "| " + sent[0] + " | " + allJokes.get("JA"));
			return;
		}
		else{
			for (String s :
					allProverb.keySet()) {
				if(!proverb.contains(s)){
					out.println(s + "| " + sent[0] + " | " + allProverb.get(s));
					System.out.println("Sending: " + s + "| " + sent[0] + " | " + allProverb.get(s));
					return;
				}
			}
			out.println("Reset,PA" + "| " + sent[0] + " | " + allProverb.get("PA"));
			System.out.println("Sending: PA" + "| " + sent[0] + " | " + allProverb.get("PA"));
			return;
		}
	}
}

class AdminWorker extends Thread {
	Socket sock;
	StatementType statementType;

	public AdminWorker(Socket s) {sock = s;}

	public void run(){
		PrintStream out = null;
		BufferedReader in = null;
		try{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream())); //New input object
			out = new PrintStream(sock.getOutputStream()); //New output object

			try{
				String name;
				name = in.readLine(); //Read line from client
				System.out.println("From Client: " + name);
				if(name.equalsIgnoreCase("J")){
					JokeServer.statementType = StatementType.JOKE;
					out.println("Mode: JOKE Mode");
					System.out.println("Mode: JOKE Mode");
				}
				else if (name.equalsIgnoreCase("P")){
					JokeServer.statementType = StatementType.PROVERB;
					out.println("Mode: PROVERB Mode");
					System.out.println("Mode: PROVERB Mode");
				}
			}
			catch(IOException x){
				System.out.println("Server read error");
				x.printStackTrace();
			}

			sock.close();
		}
		catch(IOException ioe) {System.out.println(ioe);}
	}
}

class AdminLooper implements Runnable{
	public static boolean adminControlSwitch = true;

	public void run(){
		System.out.println("Dhruv's Admin Joke Server 1.8 starting up, listening at port 4546");
		int q_len = 6;
		int port = 4546;
		Socket adminSock;

		try{
			ServerSocket servsock = new ServerSocket(port, q_len);
			System.out.println("Dhruv's Admin Joke Server 1.8 starting up, listening at port 4546");
			while(adminControlSwitch){
				adminSock = servsock.accept();
				new AdminWorker(adminSock).start();
			}
		}
		catch(IOException ioe) { System.out.println(ioe);}
	}
}

public class JokeServer {
	public static StatementType statementType;

	public static void main(String a[]) throws IOException{
		int q_len = 6;
		int port = 4545;
		Socket sock;

		statementType = StatementType.JOKE;

		ServerSocket servsock = new ServerSocket(port, q_len); // Creates Server socket object

		System.out.println("Dhruv's Joke Server 1.8 starting up, listening at port 4545");


		AdminLooper AL = new AdminLooper();

		Thread adminThread = new Thread(AL);
		adminThread.start();

		while(true){
			sock = servsock.accept(); //Accepts client connections
			new Worker(sock, statementType).start(); //Starts thread for each client connection.
		}
	}
}

//Enum for Server State
enum StatementType {
	JOKE,
	PROVERB
}