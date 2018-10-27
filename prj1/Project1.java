package prj1;

import java.io.File;
import java.util.ArrayList;

public class Project1 {

	public static void main(String[] args) {
		
                ArrayList<Node> nodes = new ArrayList<>();
                Node n;

		if (args.length < 2){
                        System.out.println("At least two configuration files are required in order to execute the program correctly");
                        System.exit(0);
                }

                for (String s: args) {
                        n = new Node(new File(s)); 
                        nodes.add(n);
                        
                        n.start();
                }
	}
}
