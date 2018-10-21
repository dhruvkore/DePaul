/* 2018-01-14:
Blockchain.java for Blockchain
Dhruv Kore for CSC435

This is some quick sample code giving a simple framework for coordinating multiple processes in a Blockchain group.

INSTRUCTIONS:

Set the numProceses class variable (e.g., 1,2,3), and use a batch file to match

AllStart.bat:

REM for three procesess:
start java Blockchain 0
start java Blockchain 1
java Blockchain 2

You might want to start with just one process to see how it works.

Thanks: http://www.javacodex.com/Concurrency/PriorityBlockingQueue-Example

Some code provided by Professor Clark Elliott from DePaul

Notes to CDE:
Optional: send public key as Base64 XML along with a signed string.
Verfy the signature with public key that has been restored.

*/

import sun.jvm.hotspot.opto.Block;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.bind.annotation.*;

import java.io.StringWriter;
import java.io.StringReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.StringTokenizer;

/* CDE Some other uitilities: */

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.text.*;


// Would normally keep a process block for each process in the multicast group:
/* class ProcessBlock{
  int processID;
  PublicKey pubKey;
  int port;
  String IPAddress;
  } */

// Ports will incremented by 1000 for each additional process added to the multicast group:
class Ports{
    public static int KeyServerPortBase = 6050;
    public static int UnverifiedBlockServerPortBase = 6051;
    public static int BlockchainServerPortBase = 6052;

    public static int KeyServerPort;
    public static int UnverifiedBlockServerPort;
    public static int BlockchainServerPort;

    public void setPorts(){
        KeyServerPort = KeyServerPortBase + (Blockchain.PID * 1000);
        UnverifiedBlockServerPort = UnverifiedBlockServerPortBase + (Blockchain.PID * 1000);
        BlockchainServerPort = BlockchainServerPortBase + (Blockchain.PID * 1000);
    }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class BlockLedger
{
    @XmlElement(name = "BlockRecord")
    private List<BlockRecord> BlockLedger = null;

    public List<BlockRecord> getEmployees() {
        return BlockLedger;
    }

    public void setBlockRecord(List<BlockRecord> blockLedger) {
        this.BlockLedger = blockLedger;
    }
}

@XmlRootElement
class BlockRecord implements Comparable{
    /* Examples of block fields: */
    String SHA256String;
    String SignedSHA256;
    String BlockID;
    String VerificationProcessID;
    String CreatingProcess;
    String PreviousHash;
    String Fname;
    String Lname;
    String SSNum;
    String DOB;
    String Diag;
    String Treat;
    String Rx;
    Date CreationDate;

  /* Examples of accessors for the BlockRecord fields. Note that the XML tools sort the fields alphabetically
     by name of accessors, so A=header, F=Indentification, G=Medical: */

    public String getASHA256String() {return SHA256String;}
    @XmlElement
    public void setASHA256String(String SH){this.SHA256String = SH;}

    public String getASignedSHA256() {return SignedSHA256;}
    @XmlElement
    public void setASignedSHA256(String SH){this.SignedSHA256 = SH;}

    public String getACreatingProcess() {return CreatingProcess;}
    @XmlElement
    public void setACreatingProcess(String CP){this.CreatingProcess = CP;}

    public String getAVerificationProcessID() {return VerificationProcessID;}
    @XmlElement
    public void setAVerificationProcessID(String VID){this.VerificationProcessID = VID;}

    public String getABlockID() {return BlockID;}
    @XmlElement
    public void setABlockID(String BID){this.BlockID = BID;}

    public String getFSSNum() {return SSNum;}
    @XmlElement
    public void setFSSNum(String SS){this.SSNum = SS;}

    public String getFFname() {return Fname;}
    @XmlElement
    public void setFFname(String FN){this.Fname = FN;}

    public String getFLname() {return Lname;}
    @XmlElement
    public void setFLname(String LN){this.Lname = LN;}

    public String getFDOB() {return DOB;}
    @XmlElement
    public void setFDOB(String DOB){this.DOB = DOB;}

    public String getGDiag() {return Diag;}
    @XmlElement
    public void setGDiag(String D){this.Diag = D;}

    public String getGTreat() {return Treat;}
    @XmlElement
    public void setGTreat(String D){this.Treat = D;}

    public String getGRx() {return Rx;}
    @XmlElement
    public void setGRx(String D){this.Rx = D;}

    public Date getCreationDate() { return CreationDate; }
    @XmlElement
    public void setCreationDate(Date creationDate) { CreationDate = creationDate; }

    @Override
    public int compareTo(Object o) {
        return this.getCreationDate().compareTo(((BlockRecord)o).getCreationDate());
    }
}

class PublicKeyWorker extends Thread { // Class definition
    Socket sock; // Class member, socket, local to Worker.
    PublicKeyWorker (Socket s) {sock = s;} // Constructor, assign arg s to local sock
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String data = in.readLine ();
            System.out.println("Got key: " + data);
            sock.close();
        } catch (IOException x){x.printStackTrace();}
    }
}

class PublicKeyServer implements Runnable {
    //public ProcessBlock[] PBlock = new ProcessBlock[3]; // One block to store info for each process.

    public void run(){
        int q_len = 6;
        Socket sock;
        System.out.println("Starting Key Server input thread using " + Integer.toString(Ports.KeyServerPort));
        try{
            ServerSocket servsock = new ServerSocket(Ports.KeyServerPort, q_len);
            while (true) {
                sock = servsock.accept();
                new PublicKeyWorker (sock).start();
            }
        }catch (IOException ioe) {System.out.println(ioe);}
    }
}

class UnverifiedBlockServer implements Runnable {
    BlockingQueue<BlockRecord> queue;
    UnverifiedBlockServer(BlockingQueue<BlockRecord> queue){
        this.queue = queue; // Constructor binds our prioirty queue to the local variable.
    }

  /* Inner class to share priority queue.
  We are going to place the unverified blocks into this queue in the order we get
     them, but they will be retrieved by a consumer process sorted by blockID. */

    class UnverifiedBlockWorker extends Thread { // Class definition
        Socket sock; // Class member, socket, local to Worker.
        UnverifiedBlockWorker (Socket s) {sock = s;} // Constructor, assign arg s to local sock
        public void run(){
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String data = in.readLine ();

                JAXBContext jaxbContext = JAXBContext.newInstance(BlockRecord.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                BlockRecord br = (BlockRecord) jaxbUnmarshaller.unmarshal(new StringReader(data));

                System.out.println("Put in priority queue: " + data + "\n");
                queue.put(br);
                sock.close();
            } catch (Exception x){x.printStackTrace();}
        }
    }

    public void run(){
        int q_len = 6; /* Number of requests for OpSys to queue */
        Socket sock;
        System.out.println("Starting the Unverified Block Server input thread using " +
                Integer.toString(Ports.UnverifiedBlockServerPort));
        try{
            ServerSocket servsock = new ServerSocket(Ports.UnverifiedBlockServerPort, q_len);
            while (true) {
                sock = servsock.accept(); // Got a new unverified block
                new UnverifiedBlockWorker(sock).start(); // So start a thread to process it.
            }
        }catch (IOException ioe) {System.out.println(ioe);}
    }
}

/* We have received unverified blocks into a thread-safe concurrent access queue. Just for the example, we retrieve them
in order according to their blockID. Normally we would retreive the block with the lowest time stamp first, or? This
is just an example of how to implement such a queue. It must be concurrent safe because two or more threads modifiy it
"at once," (mutiple worker threads to add to the queue, and consumer thread to remove from it).*/

class UnverifiedBlockConsumer implements Runnable {
    BlockingQueue<BlockRecord> queue;
    int PID;
    UnverifiedBlockConsumer(BlockingQueue<BlockRecord> queue){
        this.queue = queue; // Constructor binds our prioirty queue to the local variable.
    }

    public void run(){
        BlockRecord br;
        PrintStream toServer;
        Socket sock;
        String newBlockchain;
        String fakeVerifiedBlock;

        System.out.println("Starting the Unverified Block Priority Queue Consumer thread.\n");
        try{
            while(true){ // Consume from the incoming queue. Do the work to verify. Mulitcast new Blockchain
                br = queue.take(); // Will blocked-wait on empty queue
                System.out.println("Consumer got unverified: " + br.toString());



                // Ordindarily we would do real work here, based on the incoming data.
                int j; // Here we fake doing some work (That is, here we could cheat, so not ACTUAL work...)
                for(int i=0; i< 100; i++){ // put a limit on the fake work for this example
                    j = ThreadLocalRandom.current().nextInt(0,10);
                    try{Thread.sleep(500);}catch(Exception e){e.printStackTrace();}
                    if (j < 3) break; // <- how hard our fake work is; about 1.5 seconds.
                }

	/* With duplicate blocks that have been verified by different procs ordinarily we would keep only the one with
           the lowest verification timestamp. For the example we use a crude filter, which also may let some dups through */
                if(Blockchain.blockchain.indexOf(br.BlockID) < 0){ // Crude, but excludes most duplicates.
                    System.out.println("Block [" + br.BlockID + "] verified by P" + Blockchain.PID);

                    JAXBContext jaxbContext = JAXBContext.newInstance(BlockRecord.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    StringWriter sw = new StringWriter();
                    jaxbMarshaller.marshal(br, sw);

                    String fullBlock = sw.toString();
                    String XMLHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
                    String blockHeader = "<BlockLedger>";
                    String blockTail = "</BlockLedger>";
                    String cleanBlock = fullBlock.replace(XMLHeader, "");
                    String cleanBlockchain = Blockchain.blockchain.replace(XMLHeader, "");
                    cleanBlockchain = cleanBlockchain.replace(blockHeader, "");
                    cleanBlockchain = cleanBlockchain.replace(blockTail, "");
                    // Show the string of concatenated, individual XML blocks:
                    String XMLBlockchain = XMLHeader + "\n<BlockLedger>" + cleanBlock + cleanBlockchain + "</BlockLedger>";
                    String tempBlockchain = XMLBlockchain; // add the verified block to the chain
                    for(int i=0; i < Blockchain.numProcesses; i++){ // send to each process in group, including us:
                        sock = new Socket(Blockchain.serverName, Ports.BlockchainServerPortBase + (i * 1000));
                        toServer = new PrintStream(sock.getOutputStream());
                        toServer.println(tempBlockchain); toServer.flush(); // make the multicast
                        sock.close();
                    }
                }
                Thread.sleep(1500); // For the example, wait for our Blockchain to be updated before processing a new block
            }
        }catch (Exception e) {System.out.println(e);}
    }
}

// Incoming proposed replacement Blockchains. Compare to existing. Replace if winner:

class BlockchainWorker extends Thread { // Class definition
    Socket sock; // Class member, socket, local to Worker.
    BlockchainWorker (Socket s) {sock = s;} // Constructor, assign arg s to local sock
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String data = "";
            String data2;
            while((data2 = in.readLine()) != null){
                data = data + data2;
            }
            Blockchain.blockchain = data; // Would normally have to check first for winner before replacing.
            if(Blockchain.PID == 0) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("BlockchainLedger.xml"));
                    writer.write(data);
                    writer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("         --NEW Blockchain--\n" + Blockchain.blockchain + "\n\n");

            sock.close();
        } catch (IOException x){x.printStackTrace();}
    }
}

class BlockchainServer implements Runnable {
    public void run(){
        int q_len = 6; /* Number of requests for OpSys to queue */
        Socket sock;
        System.out.println("Starting the Blockchain server input thread using " + Integer.toString(Ports.BlockchainServerPort));
        try{
            ServerSocket servsock = new ServerSocket(Ports.BlockchainServerPort, q_len);
            while (true) {
                sock = servsock.accept();
                new BlockchainWorker (sock).start();
            }
        }catch (IOException ioe) {System.out.println(ioe);}
    }
}

// Class Blockchain for Blockchain
public class Blockchain {
    public static String serverName = "localhost";
    public static String blockchain =   "\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>\"" +
                                        "<BlockLedger>" +
                                            "<blockRecord>" +
                                                "<ABlockID>1b97a338-2603-4f9c-86aa-ebdb400759a4</ABlockID>" +
                                                "<ACreatingProcess>Process0</ACreatingProcess>" +
                                                "<ASHA256String>SHA string goes here...</ASHA256String>" +
                                                "<ASignedSHA256>Signed SHA string goes here...</ASignedSHA256>" +
                                                "<AVerificationProcessID>To be set later...</AVerificationProcessID>" +
                                                "<creationDate>2018-10-20T21:39:46.953-05:00</creationDate>" +
                                                "<FDOB>1996.03.07</FDOB><FFname>Fake</FFname>" +
                                                "<FLname>Block</FLname>" +
                                                "<FSSNum>123-45-6789</FSSNum><GDiag>Fake</GDiag>" +
                                                "<GRx>Stuff</GRx><GTreat>Other</GTreat>" +
                                                "</blockRecord>" +
                                                "</BlockLedger>"; //Fake first Block
    public static int numProcesses = 3; // Set this to match your batch execution file that starts N processes with args 0,1,2,...N
    public static int PID = 0; // Our process ID

    private static final int iFNAME = 0;
    private static final int iLNAME = 1;
    private static final int iDOB = 2;
    private static final int iSSNUM = 3;
    private static final int iDIAG = 4;
    private static final int iTREAT = 5;
    private static final int iRX = 6;
    private static final int iDATE = 7;
    public static String FILENAME = "";

    public void MultiSend (){ // Multicast some data to each of the processes.
        Socket sock;
        PrintStream toServer;

        try{
            for(int i=0; i< numProcesses; i++){// Send our key to all servers.
                sock = new Socket(serverName, Ports.KeyServerPortBase + (i * 1000));
                toServer = new PrintStream(sock.getOutputStream());
                toServer.println("FakeKeyProcess" + Blockchain.PID); toServer.flush();
                sock.close();
            }
            Thread.sleep(1000); // wait for keys to settle, normally would wait for an ack

            try {
                try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
                    String[] tokens = new String[10];
                    String stringXML;
                    String InputLineStr;
                    String suuid;
                    UUID idA;

                    BlockRecord[] blockArray = new BlockRecord[20];

                    JAXBContext jaxbContext = JAXBContext.newInstance(BlockRecord.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    StringWriter sw = new StringWriter();

                    // CDE Make the output pretty printed:
                    //jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    int n = 0;
                    while ((InputLineStr = br.readLine()) != null) {
                        blockArray[n] = new BlockRecord();

                        blockArray[n].setASHA256String("SHA string goes here...");
                        blockArray[n].setASignedSHA256("Signed SHA string goes here...");

                        /* CDE: Generate a unique blockID. This would also be signed by creating process: */
                        idA = UUID.randomUUID();
                        suuid = new String(UUID.randomUUID().toString());
                        blockArray[n].setABlockID(suuid);
                        blockArray[n].setACreatingProcess("Process" + Integer.toString(PID));
                        blockArray[n].setAVerificationProcessID("To be set later...");
                        /* CDE put the file data into the block record: */
                        tokens = InputLineStr.split(" +"); // Tokenize the input
                        blockArray[n].setFSSNum(tokens[iSSNUM]);
                        blockArray[n].setFFname(tokens[iFNAME]);
                        blockArray[n].setFLname(tokens[iLNAME]);
                        blockArray[n].setFDOB(tokens[iDOB]);
                        blockArray[n].setGDiag(tokens[iDIAG]);
                        blockArray[n].setGTreat(tokens[iTREAT]);
                        blockArray[n].setGRx(tokens[iRX]);
                        blockArray[n].setCreationDate(new Date());
                        n++;
                    }
                    System.out.println(n + " records read.");
                    System.out.println("Names from input:");
                    for(int i=0; i < n; i++){
                        System.out.println("  " + blockArray[i].getFFname() + " " +
                                blockArray[i].getFLname());
                    }
                    System.out.println("\n");


                    for(int j=0; j < n; j++){
                        for(int i=0; i< numProcesses; i++) {
                            sw = new StringWriter();
                            jaxbMarshaller.marshal(blockArray[i], sw);
                            sock = new Socket(serverName, Ports.UnverifiedBlockServerPortBase + (i * 1000));
                            toServer = new PrintStream(sock.getOutputStream());
                            stringXML = sw.toString();
                            toServer.println(stringXML);
                            toServer.flush();
                            sock.close();
                        }
                    }
                } catch (IOException e) {e.printStackTrace();}
            } catch (Exception e) {e.printStackTrace();}
        }catch (Exception x) {x.printStackTrace ();}
    }

    public static void main(String args[]){
        int q_len = 6; /* Number of requests for OpSys to queue. Not interesting. */
        PID = (args.length < 1) ? 0 : Integer.parseInt(args[0]); // Process ID
        System.out.println("Dhruv Kore's BlockFramework control-c to quit.\n");
        System.out.println("Using processID " + PID + "\n");

        switch(PID){
            case 1: FILENAME = "BlockInput1.txt"; break;
            case 2: FILENAME = "BlockInput2.txt"; break;
            default: FILENAME= "BlockInput0.txt"; break;
        }

        System.out.println("Using input file: " + FILENAME);

        final BlockingQueue<BlockRecord> queue = new PriorityBlockingQueue<>(); // Concurrent queue for unverified blocks
        new Ports().setPorts(); // Establish OUR port number scheme, based on PID

        new Thread(new PublicKeyServer()).start(); // New thread to process incoming public keys
        new Thread(new UnverifiedBlockServer(queue)).start(); // New thread to process incoming unverified blocks
        new Thread(new BlockchainServer()).start(); // New thread to process incoming new Blockchains
        try{Thread.sleep(3000);}catch(Exception e){} // Wait for servers to start.
        if(PID == 0){
            try {
                Socket sock;
                PrintStream toServer;

                for (int i = 0; i < Blockchain.numProcesses; i++) { // send to each process in group, including us:
                    sock = new Socket(Blockchain.serverName, Ports.BlockchainServerPortBase + (i * 1000));
                    toServer = new PrintStream(sock.getOutputStream());
                    toServer.println(blockchain);
                    toServer.flush(); // make the multicast
                    sock.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        new Blockchain().MultiSend(); // Multicast some new unverified blocks out to all servers as data
        try{Thread.sleep(3000);}catch(Exception e){} // Wait for multicast to fill incoming queue for our example.

        new Thread(new UnverifiedBlockConsumer(queue)).start(); // Start consuming the queued-up unverified blocks
    }
}
