/*--------------------------------------------------------

1. Dhruv Kore / Oct 7, 2018 :

2. Java used

3. Precise command-line compilation examples / instructions:
 > javac JokeServer.java

4. Precise examples / instructions to run this program:

In separate shell windows:
> java MyWebServer

In internet Browser:
localhost:2540/

Directory is navigated with through browser.
Web service exposes restful api to do so.

5. List of files needed for running the program.
 a. MyWebServer.java

5. Notes:

Everything should work except file names with spaces or names with spaces add a '+'

----------------------------------------------------------*/

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
				while(true){
					name = in.readLine(); //Read line from client
					if(name == null || name.length() == 0)
						break;
					//System.out.println(name); //To retrieve HTTP-streams
					if(name.startsWith("GET") && name.endsWith("HTTP/1.1")){ //If HTTP GET request
						String path = name.substring(4, name.length() - 9).trim(); //Remove GET and HTTP/1.1
						String extension = "";
						String output = "";
						File file = new File("./" + path); // Use current directory as root
						if(path.endsWith(".html")){
							extension = "text/html";
						}
						else if(path.endsWith(".txt")){
							extension = "text/plain";
						}
						else if(path.endsWith(".ico")){ // Used for returning Favicon
							extension = "image/x-icon";
						}
						else if(path.startsWith("/cgi/addnums.fake-cgi")){ // Used for CGI
							extension = "text/html";
							String[] parameters = path.substring(path.indexOf("?"), path.length()).split("&", 0); // Parameters from CGI GET request
							// Split key from value for parameters
							String userName = parameters[0].substring(parameters[0].indexOf("=") + 1, parameters[0].length());
							String num1 = parameters[1].substring(parameters[1].indexOf("=") + 1, parameters[1].length());
							String num2 = parameters[2].substring(parameters[2].indexOf("=") + 1, parameters[2].length());

							// Do computation
							Integer sum = Integer.valueOf(num1) + Integer.valueOf(num2);

							System.out.println("addnums.fake-cgi");
							System.out.println("Name: " + userName + " Num1: " + num1 + " Num2: " + num2);
							System.out.println("");

							// HTML formatted result of number addition
							output += "<h1> Dear " + userName + 
								", the output of the sum of " + num1 + " and "
								+ num2 + " is " + sum + "</h1>";
						}
						else if(path.endsWith("/")){ // If Directory
							extension = "text/html";
							System.out.println("");
							System.out.println("Index of ." + path);
							output += "<h1> Index of " + path + "</h1> <pre>";

							String parentName = file.getName();
							if(path.length() > 1) // Link to parent path. (Removes last directory in path if not at root)
								output += "<a href=\"" + path.substring(0, path.length() - parentName.length() - 1) + "\">Parent Directory</a>\n";
							File[] contents = file.listFiles();
							if(contents != null){
								// Link for each file and directory in current directory
								for(int i = 0; i < contents.length; i++){
									System.out.println(contents[i]);
									output += "<a href=\"" + path + contents[i].getName();
									if(contents[i].isDirectory()){
										output += "/\">";
										output += "Directory: " + contents[i].getName() + "/";
									}
									else if (contents[i].isFile()){
										output += "\">";
										output += "File: " + contents[i].getName() + " (" + contents[i].length() + ") ";
									}
									output += "</a>\n";
								}
							}
							output += "</pre>\n";
						}
						else{
							extension = "text/plain";
						}

						if(path.endsWith("/") || path.startsWith("/cgi/addnums.fake-cgi")){ // For Directory or CGI output
							out.println("HTTP/1.1 200 OK");
							out.println("Content-Length: " + output.length());
							out.println("Content-Type: " + extension + "\r\n\r\n");
							out.println(output);
						}
						else{
							// For file output.
							out.println("HTTP/1.1 200 OK");
							out.println("Content-Length: " + file.length());
							out.println("Content-Type: " + extension + "\r\n\r\n");
							System.out.println("Sending File" + file);

							// Write stream to output
							InputStream fileinput = new FileInputStream(file);
							byte[] buffer = new byte[1000];
							while(fileinput.available() > 0){
								out.write(buffer, 0, fileinput.read(buffer));
							}
						}
					}
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

public class MyWebServer {
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