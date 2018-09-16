import java.io.*;
import java.net.*;

class Worker extends Thread {
	Socket sock;
	Worker (Socket s) {sock = s;}

	public void run(){
		PrintStream out = null;
		BufferedReader in = null;
		try{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream())); //New input object
			out = new PrintStream(sock.getOutputStream()); //New output object

			try{
				String name;
				name = in.readLine(); //Read line from client
				System.out.println("Looking up " + name);
				printRemoteAddress(name, out); //Call Helper for remote address
			}
			catch(IOException x){
				System.out.println("Server read error");
				x.printStackTrace();
			}

			sock.close();
		}
		catch(IOException ioe) {System.out.println(ioe);}
	}

	static void printRemoteAddress (String name, PrintStream out){
		try{
			out.println("Looking up " + name + "...");
			InetAddress machine = InetAddress.getByName(name); //Get InetAddress object by name
			out.println("Host name : " + machine.getHostName()); //Print Host Name
			out.println("Host IP : " + toText (machine.getAddress ())); //Print Host IP
		}
		catch(UnknownHostException ex){ //Catches exception if try fails
			out.println("Failed in attempt to look up " + name);
		}
	}

	static String toText (byte ip[]){
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < ip.length; ++i){
			if(i > 0) result.append(".");
			result.append(0xff & ip[i]);
		}
		return result.toString();
	}
}

public class InetServer {
	public static void main(String a[]) throws IOException{
		int q_len = 6;
		int port = 30000;
		Socket sock;

		ServerSocket servsock = new ServerSocket(port, q_len); // Creates Server socket object

		System.out.println("Clark Elliott's Inet server 1.8 starting up, listening at port 30000");
		while(true){
			sock = servsock.accept(); //Accepts client connections
			new Worker(sock).start(); //Starts thread for each client connection.
		}
	}
}