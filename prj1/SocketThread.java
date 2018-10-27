package prj1;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class SocketThread implements Runnable{

	boolean Server = false;
	
	Thread t;
	Socket sock;
	ServerSocket serv;
	byte[] data;
	
	String Address;
	int Port;
	
	DataInputStream Input;
	DataOutputStream Output;
	
	// This constructor allows the SocketThread Object to act as a client socket
	public SocketThread(String s, int p, File f){
		Server = false;								//This boolean is used to determine the usage of the SocketThread object
		Address = s;
		Port = p;
		data = new byte[(int) f.length()]; 			//This retrieves the raw bytes from the file to be transferred and stores it in the byte[] data
		FileInputStream tmp;
		try {
			tmp = new FileInputStream(f);
			tmp.read(data);
			tmp.close();
		} catch (IOException e) {
			System.out.print("IOException occured");
		}
	}
	
	// This constructor allows the SocketThread Object to act a server socket
	public SocketThread(int p){
		Server = true;								//Sets the object state to a server for later use
		Port = p;
	}
	
	// Starts the thread controlling the transmission or receiving of data
	public void start() {
		t = new Thread(this);						//Establishes a new thread that executes run method
		t.start();
	}
	
	// Determines if the object is a Server or not in order to execute the appropriate method 
	@Override
	public void run() {
		try {
			if (Server)
				runServer();
			else
				runClient();
		} catch (IOException e) {
		}
	}
	
	// Waits for a connection and receives data as a server
	private void runServer() throws IOException {
		serv = new ServerSocket(Port);				//Establishes an open socket on the port provided in the constructor
		
		while (sock == null) {						//Blocks the progress of the code until the server is connected to a client
			try {
			sock = serv.accept();
			} catch (IOException f) {
				System.out.println("Waiting for Connection");
			}
		}
		
		Input = new DataInputStream(sock.getInputStream());
		Output = new DataOutputStream(sock.getOutputStream());
		
		data = new byte[(int) Input.readLong()];	//Established a byte[] based on the first transmission of the client to the server
		
		do {
			Input.read(data);							//Reads the byte transmission from the client and places it into data for storage
			Output.write(data);
		} while (Input.readBoolean() != true);
		
		sock.close();								//Closes the socket since it has done its task
		serv.close();								//Closes the serversocket since it has done its task
	}
	
	private void runClient() throws UnknownHostException, IOException {
		do {										//Attempts to connect to a server indefinitely on the port provided by the constructor
			try {
				sock = new Socket(Address, Port);
			} catch (IOException e) {
			}
		} while (sock == null || !sock.isConnected());

		byte[] tmp = new byte[data.length];
		Input = new DataInputStream(sock.getInputStream());
		Output = new DataOutputStream(sock.getOutputStream());
		Output.writeLong(data.length);				//Sends the size of the file to the Server through the Socket's OutputStream
		
		do {
			Output.write(data);							//Sends the byte[] that contains the file to the Server through the Socket's OutputStream
			Input.read(tmp);
		} while (!Arrays.equals(data, tmp));
		Output.writeBoolean(true);
		
		sock.close();								//Closes the socket since it has done its task
	}
	
	// Generates a string from the byte[] that was retrieved from the output
	public String getOutput() {
		String tmp = new String();
		
		for (byte b : data) {
			tmp += (char) b;
		}
		
		return tmp;
	}
	
	// Joins the thread, blocking further execution based on whether the thread is done or not
	public void join() {
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
