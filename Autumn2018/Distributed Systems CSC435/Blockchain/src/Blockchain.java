/*

Blockchain.java for Blockchain
Dhruv Kore for CSC435

An attempt to create a workable blockchain program.
Runs with multiple nodes accross three processes.

REM for three procesess:
start java Blockchain 0
start java Blockchain 1
java Blockchain 2

---
The web sources:
-
http://www.java2s.com/Code/Java/Security/SignatureSignAndVerify.htm
https://www.mkyong.com/java/java-digital-signatures-example/ (not so clear)
https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
https://www.programcreek.com/java-api-examples/index.php?api=java.security.SecureRandom
https://www.mkyong.com/java/java-sha-hashing-example/
https://stackoverflow.com/questions/19818550/java-retrieve-the-actual-value-of-the-public-key-from-the-keypair-object

XML validator:
https://www.w3schools.com/xml/xml_validator.asp

XML / Object conversion:
https://www.mkyong.com/java/jaxb-hello-world-example/

http://www.javacodex.com/Concurrency/PriorityBlockingQueue-Example

https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html  @author JJ
https://dzone.com/articles/generate-random-alpha-numeric  by Kunal Bhatia  ·  Aug. 09, 12 · Java Zone

---

Some code provided by Professor Clark Elliott from DePaul

*/

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.bind.annotation.*;

import java.io.StringWriter;
import java.io.StringReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Date;
import java.util.UUID;


// Static relative ports
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

// Contains BlockChainLedger
// Used to Unmarshal and marshal of Ledger
@XmlRootElement(name = "BlockLedger")
@XmlAccessorType (XmlAccessType.FIELD)
class BlockLedger
{
    @XmlElement(name = "blockRecord")
    private BlockRecord[] BlockLedger;

    public BlockRecord[]  getBlockLedger() {
        return BlockLedger;
    }
    @XmlElement
    public void setBlockLedger(BlockRecord[]  blockLedger) {
        this.BlockLedger = blockLedger;
    }
}

// Contains BlockRecord
// Used to Unmarshal and marshal of BlockRecord
@XmlRootElement(name = "blockRecord")
class BlockRecord implements Comparable{
    /* Examples of block fields: */
    int blockNumber;
    String SHA256String;
    byte[] SignedSHA256;
    String BlockID;
    byte[] BlockIDSignedByProcess;
    int VerificationProcessID;
    int CreatingProcess;
    String PreviousHash;
    String Fname;
    String Lname;
    String SSNum;
    String DOB;
    String Diag;
    String Treat;
    String Rx;
    Date CreationDate;

    //Data is of Medical Records

    public String getASHA256String() {return SHA256String;}
    @XmlElement
    public void setASHA256String(String SH){this.SHA256String = SH;}

    public byte[] getASignedSHA256() {return SignedSHA256;}
    @XmlElement
    public void setASignedSHA256(byte[] SH){this.SignedSHA256 = SH;}

    public int getACreatingProcess() {return CreatingProcess;}
    @XmlElement
    public void setACreatingProcess(int CP){this.CreatingProcess = CP;}

    public int getAVerificationProcessID() {return VerificationProcessID;}
    @XmlElement
    public void setAVerificationProcessID(int VID){this.VerificationProcessID = VID;}

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

    public byte[] getBlockIDSignedByProcess() {
        return BlockIDSignedByProcess;
    }
    @XmlElement
    public void setBlockIDSignedByProcess(byte[] blockIDSignedByProcess) {
        BlockIDSignedByProcess = blockIDSignedByProcess;
    }

    public String getPreviousHash() {
        return PreviousHash;
    }
    @XmlElement
    public void setPreviousHash(String previousHash) {
        PreviousHash = previousHash;
    }

    public int getBlockNumber() {
        return blockNumber;
    }
    @XmlElement
    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    @Override
    public int compareTo(Object o) {
        return this.getCreationDate().compareTo(((BlockRecord)o).getCreationDate());
    }
}

// Worker that processes Public Keys sent to this process
class PublicKeyWorker extends Thread {
    Socket sock; //Local socket
    PublicKeyWorker (Socket s) {sock = s;}
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String data = in.readLine ();
            int pid = Integer.parseInt(data.substring(0, data.indexOf(" ")));
            String publicKeyStr = data.substring(2, data.length());
            System.out.println("Got key: " + publicKeyStr + " From PID: " + pid);

            Blockchain.nodePublicKeys[pid] = Blockchain.strToPublicKey(publicKeyStr);

            sock.close();
        } catch (IOException x){x.printStackTrace();}
    }
}

// Server that accepts Public Keys sent to this process
class PublicKeyServer implements Runnable {
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

// Server that accepts Unverified Blocks sent to this process
class UnverifiedBlockServer implements Runnable {
    BlockingQueue<BlockRecord> queue;
    UnverifiedBlockServer(BlockingQueue<BlockRecord> queue){
        this.queue = queue; // Constructor binds our prioirty queue to the local variable.
    }

    // Worker that processes Unverified Blocks sent to this process
    class UnverifiedBlockWorker extends Thread {
        Socket sock;
        UnverifiedBlockWorker (Socket s) {sock = s;}
        public void run(){
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String data = in.readLine ();

                System.out.println("Received unverified block: " + data);

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
        int q_len = 6;
        Socket sock;
        System.out.println("Starting the Unverified Block Server input thread using " +
                Integer.toString(Ports.UnverifiedBlockServerPort));
        try{
            ServerSocket servsock = new ServerSocket(Ports.UnverifiedBlockServerPort, q_len);
            while (true) {
                sock = servsock.accept(); //Accepts new unverified blocks
                new UnverifiedBlockWorker(sock).start();
            }
        }catch (IOException ioe) {System.out.println(ioe);}
    }
}


// Processes unverified blocks from the Queue
class UnverifiedBlockConsumer implements Runnable {
    BlockingQueue<BlockRecord> queue;
    UnverifiedBlockConsumer(BlockingQueue<BlockRecord> queue){
        this.queue = queue;
    }

    public void run(){
        BlockRecord br;
        PrintStream toServer;
        Socket sock;

        System.out.println("Starting the Unverified Block Priority Queue Consumer thread.\n");
        try{
            while(true){ //infinite loop for consumption of blocks
                br = queue.take(); // Takes from queue based on priority (time created)
                System.out.println("Consumer got unverified: " + br.toString());

                if(Blockchain.blockchain.contains(br.getABlockID())){ //If block is already in the chain, then disregard.
                    System.out.println("Block already in Chain: " + br.getABlockID());
                    continue;
                }

                //Verifies creator of the Block and Checks signed Hash
                if(!Blockchain.verifySig(br.getABlockID().getBytes(),
                        Blockchain.nodePublicKeys[br.getACreatingProcess()],
                        br.getBlockIDSignedByProcess())
                        || !Blockchain.verifySig(br.getASHA256String().getBytes(),
                        Blockchain.nodePublicKeys[br.getACreatingProcess()],
                        br.getASignedSHA256())){
                    System.out.println("Cannot Verify Block: " + br.getABlockID() + "  :(");
                    continue;
                }

                //Set Sequential Block Number
                br.setBlockNumber(Blockchain.blocksInChain); //This number is incremented when new blockchain is received

                //Set Verification Process ID
                br.setAVerificationProcessID(Blockchain.PID);



                // Fake work
                int j;
                for(int i=0; i< 100; i++){ // put a limit on the fake work for this example
                    j = ThreadLocalRandom.current().nextInt(0,10);
                    try{Thread.sleep(500);}catch(Exception e){e.printStackTrace();}
                    if (j < 3) break; // <- how hard our fake work is; about 1.5 seconds.
                }

                // If block ID is not in chain, then continue
                if(Blockchain.blockchain.indexOf(br.BlockID) < 0){ //Removes Most Duplicates
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
                    String tempBlockchain = XMLBlockchain;

                    //Check if winner before sending
                    if(Blockchain.blockchain.contains(br.getABlockID())){
                        continue; //Block is already in chain so this process is not the winner.
                    }

                    for(int i=0; i < Blockchain.numProcesses; i++){ //Send to all other Nodes/Process
                        sock = new Socket(Blockchain.serverName, Ports.BlockchainServerPortBase + (i * 1000)); // Loop through all relative port + offset
                        toServer = new PrintStream(sock.getOutputStream());
                        toServer.println(tempBlockchain);
                        toServer.flush();
                        sock.close();
                    }
                }
                Thread.sleep(1500); // Wait for chain to update on all processes
            }
        }catch (Exception e) {e.printStackTrace();}
    }
}


// Check if block already in chain and if not then send new chain
class BlockchainWorker extends Thread {
    Socket sock;
    BlockchainWorker (Socket s) {sock = s;}
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String data = "";
            String data2;
            while((data2 = in.readLine()) != null){
                data = data + data2;
            }

            //String cleanerBlockledger = data.replace("<BlockLedger>", "");
            //cleanerBlockledger = cleanerBlockledger.replace("</BlockLedger>", "");

            try {
                //Crude way to get last Hash
                String previousHash = data.substring(data.indexOf("<ASHA256String>") + 15, data.indexOf("</ASHA256String>"));
                System.out.println("This is the previous Hash: " + previousHash);
                Blockchain.previousHash = previousHash; //Parse for last Hash
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            Blockchain.blockchain = data; // Checked winner by checking if Block already in blockchain before sending verified block
            Blockchain.blocksInChain = Blockchain.blocksInChain + 1; //Increment blocks in the chain
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
        int q_len = 6;
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

// Blockchain Program
public class Blockchain {
    public static String serverName = "localhost";


    public static String blockchain = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                        "<BlockLedger>" +
                                        "<blockRecord>" +
                                        "<ABlockID>b3b307ba-e2a3-4f19-84e1-0f79f24e627a</ABlockID>" +
                                        "<ACreatingProcess>0</ACreatingProcess>" +
                                        "<AVerificationProcessID>0</AVerificationProcessID>" +
                                        "<blockIDSignedByProcess>sY5T+vv6XCb+OVme+zZiiwA8s8SzlLGb9AC0uozBK3LsWNLoYT7BM64cf5bb9iLkOtzU2aunAkO1zbrPg/K2GbTdydOlFqBZQxIdleqbp0wF/151BZGzl5anoeMWdcqvS8gdMZidF/zKe5iLLSdFh3rWVVWOeGj/X5dv9UxPe2w=</blockIDSignedByProcess>" +
                                        "<blockNumber>0</blockNumber>" +
                                        "<creationDate>2018-10-21T16:38:11.335-05:00</creationDate>" +
                                        "<FDOB>1111.11.11</FDOB>" +
                                        "<FFname>Dummy</FFname>" +
                                        "<FLname>Data</FLname>" +
                                        "<FSSNum>111-11-1111</FSSNum>" +
                                        "<GDiag>Something</GDiag>" +
                                        "<GRx>Other</GRx>" +
                                        "<GTreat>Somethingelse</GTreat>" +
                                        "<ASHA256String>DummyHash</ASHA256String>" +
                                        "</blockRecord>" +
                                        "</BlockLedger>"; //First Dummy Block
    public static int numProcesses = 3; // 3 processes
    public static int PID = 0; // Default process ID
    public static String previousHash = ""; // Holds previous Hash of verified block
    public static int blocksInChain = 1; // Used for Naming block Number in chain

    private static final int iFNAME = 0;
    private static final int iLNAME = 1;
    private static final int iDOB = 2;
    private static final int iSSNUM = 3;
    private static final int iDIAG = 4;
    private static final int iTREAT = 5;
    private static final int iRX = 6;
    public static String FILENAME = "";
    public static KeyPair keyPair;
    public static PublicKey[] nodePublicKeys = new PublicKey[20];

    //Signs data using privatekey
    public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
        Signature signer = Signature.getInstance("SHA1withRSA");
        signer.initSign(key);
        signer.update(data);
        return (signer.sign());
    }

    //Verifies data using public key
    public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception {
        Signature signer = Signature.getInstance("SHA1withRSA");
        signer.initVerify(key);
        signer.update(data);

        return (signer.verify(sig));
    }

    //Generates key pair
    public static KeyPair generateKeyPair(long seed) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");
        rng.setSeed(seed);
        keyGenerator.initialize(1024, rng);

        return (keyGenerator.generateKeyPair());
    }

    public static PublicKey strToPublicKey(String publicKeyStr) {
        byte[] publicByte = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicByte);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        }
        catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        catch (InvalidKeySpecException ikse){
            ikse.printStackTrace();
        }
        return null;
    }

    // Block br.getPreviousHash() + br.getABlockID() + data; to string
    public static String getSHAfromBlock(BlockRecord br) {
        String data = br.getFFname() + br.getFLname() + br.getFSSNum() + br.getFDOB()
                + br.getGDiag() + br.getGTreat() + br.getCreationDate();
        String toHash = br.getPreviousHash() + br.getABlockID() + data;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(toHash.getBytes());
            byte byteData[] = md.digest();

            //Byte to Hex
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        }
        catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }

        return "";
    }

    // Read from file and send unverified blocks
    public static void readFromFileAndMulticast() {
        Socket sock;
        PrintStream toServer;

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

                // Better Visual Cosemetic Format of XML
                //jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                int n = 0;
                while ((InputLineStr = br.readLine()) != null) {
                    blockArray[n] = new BlockRecord();

                    idA = UUID.randomUUID();
                    suuid = UUID.randomUUID().toString();
                    blockArray[n].setABlockID(suuid);

                    // UUID signed by creating Process Private key
                    byte[] signedUUID = signData(suuid.getBytes(), Blockchain.keyPair.getPrivate());
                    blockArray[n].setBlockIDSignedByProcess(signedUUID);

                    blockArray[n].setACreatingProcess(PID);
                    tokens = InputLineStr.split(" +"); // Split file input
                    blockArray[n].setFSSNum(tokens[iSSNUM]);
                    blockArray[n].setFFname(tokens[iFNAME]);
                    blockArray[n].setFLname(tokens[iLNAME]);
                    blockArray[n].setFDOB(tokens[iDOB]);
                    blockArray[n].setGDiag(tokens[iDIAG]);
                    blockArray[n].setGTreat(tokens[iTREAT]);
                    blockArray[n].setGRx(tokens[iRX]);
                    blockArray[n].setCreationDate(new Date()); // Current Date and Time

                    //This is hash of PreviousHash + Random Seed + data
                    String SHA256String = Blockchain.getSHAfromBlock(blockArray[n]);
                    blockArray[n].setASHA256String(SHA256String);

                    // SHA256 of PreviousHash + Random Seed + data is Signed with CreatingProcess Private Key
                    byte[] signedSHA256 = Blockchain.signData(SHA256String.getBytes(), Blockchain.keyPair.getPrivate());
                    blockArray[n].setASignedSHA256(signedSHA256);

                    n = n + 1;
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
                        jaxbMarshaller.marshal(blockArray[j], sw);
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
    }

    public void MultiSend (){ // Send initial data to every process on appropriate ports
        Socket sock;
        PrintStream toServer;

        try{
            for(int i=0; i< numProcesses; i++){
                sock = new Socket(serverName, Ports.KeyServerPortBase + (i * 1000));
                toServer = new PrintStream(sock.getOutputStream());
                String publicKey = Base64.getEncoder().encodeToString(Blockchain.keyPair.getPublic().getEncoded());
                toServer.println(PID + " " + publicKey);
                System.out.println("Sent Key: " + publicKey);
            }
            Thread.sleep(1000); // Wait for processes to accept keys

            readFromFileAndMulticast(); // Reads from File and multicasts unverified blocks

        }catch (Exception x) {x.printStackTrace ();}
    }



    public static void main(String args[]){
        int q_len = 6;
        PID = (args.length < 1) ? 0 : Integer.parseInt(args[0]); // Gets process ID from commandline argument
        System.out.println("Dhruv Kore's BlockFramework control-c to quit.\n");
        System.out.println("Using processID " + PID + "\n");

        switch(PID){
            case 1: FILENAME = "BlockInput1.txt"; break;
            case 2: FILENAME = "BlockInput2.txt"; break;
            default: FILENAME= "BlockInput0.txt"; break;
        }

        System.out.println("Using input file: " + FILENAME);

        //Create KeyPair
        try {
            Blockchain.keyPair = generateKeyPair(999);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        final BlockingQueue<BlockRecord> queue = new PriorityBlockingQueue<>(); // Queue that uses Priority as time
        new Ports().setPorts(); // Sets port numbers based on relative port numbers and PID

        new Thread(new PublicKeyServer()).start(); // Thread accepts Public Keys
        new Thread(new UnverifiedBlockServer(queue)).start(); // Thread accepts unverified blocks
        new Thread(new BlockchainServer()).start(); // Thread accepts new blockchains
        try{Thread.sleep(3000);}catch(Exception e){} // Sleep until other process/servers start

        // Process 0 is the main process. It create the initial blockchain
        if(PID == 0){
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(BlockLedger.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                StringWriter sw = new StringWriter();

                Socket sock;
                PrintStream toServer;

                for (int i = 0; i < Blockchain.numProcesses; i++) { // send to each process in group, including us:
                    sock = new Socket(Blockchain.serverName, Ports.BlockchainServerPortBase + (i * 1000));
                    toServer = new PrintStream(sock.getOutputStream());
                    toServer.println(blockchain);
                    toServer.flush();
                    sock.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        new Blockchain().MultiSend(); // Multicasts unverified blocks
        try{Thread.sleep(3000);}catch(Exception e){} // Sleep for servers to accept unverified blocks

        new Thread(new UnverifiedBlockConsumer(queue)).start(); // Starts Thread that starts to process queued unverified blocks

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); // User Input stream
        try{
            System.out.print("Enter a Argument or Ctrl-C to exit): ");
            System.out.flush();
            String input;

            do{
                System.out.print("Press enter to request joke/proverb, (quit) to end: ");
                System.out.flush();
                input = in.readLine(); //Read in
                if(input.startsWith("C")){
                    printCreditTally();
                }
                else if(input.startsWith("R")){
                    String[] arguments = input.split(" ");
                    FILENAME = arguments[1];
                    Blockchain.readFromFileAndMulticast();
                }
                else if(input.startsWith("V")){
                    verifyFullBlockchain();
                }
                else if(input.startsWith("L")){
                    printAllRecords();
                }

            } while(input.indexOf("quit") < 0); // If argument has "quit". Then exits taking commandline arguments (Still need to Ctrl-C)
            System.out.println("Cancelled by user request.");
        }
        catch (IOException x) { x.printStackTrace(); }
        catch (Exception ex){ ex.printStackTrace(); }

    }

    // Tries to verify full blockchain (Does not work)
    private static void verifyFullBlockchain() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BlockLedger.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            System.out.println();
            BlockLedger bl = (BlockLedger) jaxbUnmarshaller.unmarshal(new StringReader(blockchain));
            BlockRecord[] blr = bl.getBlockLedger();

            String previousHashForValidation = blr[0].SHA256String;

            for(int i = 1; i < blr.length; i++) {
                BlockRecord br = blr[i];

                //This is hash of PreviousHash + Random Seed + data
                String SHA256String = Blockchain.getSHAfromBlock(blr[i]);

                String data = br.getFFname() + br.getFLname() + br.getFSSNum() + br.getFDOB()
                        + br.getGDiag() + br.getGTreat() + br.getCreationDate();
                String toHash = previousHashForValidation + br.getABlockID() + data;

                //Verifies creator of the Block and Checks signed Hash
                if(!Blockchain.verifySig(br.getABlockID().getBytes(),
                        Blockchain.nodePublicKeys[br.getACreatingProcess()],
                        br.getBlockIDSignedByProcess())
                        || !Blockchain.verifySig(br.getASHA256String().getBytes(),
                        Blockchain.nodePublicKeys[br.getACreatingProcess()],
                        br.getASignedSHA256())){
                    System.out.println("Blockchain no good: " + br.getABlockID() + "  :(");
                    return;
                }

                System.out.println("Chain Verified: " + br.getABlockID());
                previousHashForValidation = SHA256String;
            }
            System.out.println("Blockchain has been Verified!");
        }
        catch (Exception ex)
        {
           ex.printStackTrace();
        }

    }

    // Prints full blockchain ledger
    private static void printAllRecords() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BlockLedger.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            System.out.println();
            BlockLedger bl = (BlockLedger) jaxbUnmarshaller.unmarshal(new StringReader(blockchain));
            BlockRecord[] blr = bl.getBlockLedger();

            System.out.println("Block Num | Creation Time | Fname | Lname | SSNum | DOB | Diag | Treat | Rx");
            for(int i = 0; i < blr.length; i++){
                System.out.println(blr[i].getBlockNumber() + " | "
                        + blr[i].getCreationDate() + " | "
                        + blr[i].getFFname() + " | "
                        + blr[i].getFLname() + " | "
                        + blr[i].getFSSNum() + " | "
                        + blr[i].getFDOB() + " | "
                        + blr[i].getGDiag() + " | "
                        + blr[i].getGTreat() + " | "
                        + blr[i].getGRx());
            }
            System.out.println("");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Gets tally of verifications per process based on full blockchain ledger
    private static void printCreditTally() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BlockLedger.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            int[] verificationCredit = new int[20];

            BlockLedger bl = (BlockLedger) jaxbUnmarshaller.unmarshal(new StringReader(blockchain));
            BlockRecord[] blr = bl.getBlockLedger();
            for (int i = 0; i < blr.length; i++) {
                verificationCredit[blr[i].getAVerificationProcessID()] += 1;
            }

            System.out.println("Process | Number Verified");
            for(int i = 0; i < verificationCredit.length; i++){
                if(verificationCredit[i] != 0) {
                    System.out.println(i + " | " + verificationCredit[i]);
                }
            }
            System.out.println("");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
