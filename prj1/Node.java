package prj1;

import java.io.*;
import java.util.*;

public class Node implements Runnable {

	boolean state = false;

	Thread t;

	SocketThread server;
	SocketThread client;
	File transferFile;
	
	int transferPort;
	int receivePort;
	
	Node(File f) {
		transferFile = f;

		/*	Attempts to retrieve the configuration from the file provided
			Assumes the following format:
		 *	Port for receiving data from clients 
		 *	Port for transmitting data to server
		 * 	Multiple lines of data 1
		 * 	Multiple lines of data 2
		 * 	Multiple lines of data 3
		*/
		try {
			Scanner cs = new Scanner(transferFile);
			
			try {
				receivePort = Integer.parseInt(cs.nextLine());
			} catch (NumberFormatException e) {
				receivePort = -1;
			}
			try {
				transferPort = Integer.parseInt(cs.nextLine());
			} catch (NumberFormatException e) {
				transferPort = -1;
			}
			
			if (receivePort == -1 && transferPort == -1) {
				System.out.println("Invalid configuration file\nAt least one of the two ports have to be configured in the first two lines of the file");
			}
			
			cs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		t = new Thread(this);
		t.start();
	}

	public void run() {
		// If the transfer port is defined instantiates a SocketThread that functions as a client 
		if (transferPort != -1) {
			client = new SocketThread("127.0.0.1", transferPort, transferFile);
			client.start();
		}
		
		// If the receiving port is defined instantiates a SocketThread that functions as a server
		if (receivePort != -1) {
			server = new SocketThread(receivePort);
			server.start();
			server.join();
			System.out.println("\n" + t.getName() + " printing output from transmission:\n" + server.getOutput());
		}
	}
}
