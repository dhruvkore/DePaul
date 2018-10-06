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
				System.out.println(name);
				out.println("Server has recieved your request.")
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

public class InetServer {
	public static void main(String a[]) throws IOException{
		int q_len = 6;
		int port = 2540;
		Socket sock;

		ServerSocket servsock = new ServerSocket(port, q_len); // Creates Server socket object

		System.out.println("Dhruv Kore's MyWebServer starting up, listening at port 2540");
		while(true){
			sock = servsock.accept(); //Accepts client connections
			new Worker(sock).start(); //Starts thread for each client connection.
		}
	}
}