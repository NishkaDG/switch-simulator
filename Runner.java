/*This class will be used to set up the network as shown in Figure 1. 
 * Switch s5 refers to Switch 0 in Figure 1
 * Host h7 refers to Laptop 0 in Figure 1
 * The remaining hosts and switches correspond numerically to their counterparts in Figure 1   
   */
import java.util.ArrayList;
import java.io.*;
public class Runner
{
    static ArrayList<Switch> switches = new ArrayList<Switch>();
    static ArrayList<Host> hosts = new ArrayList<Host>();
    
    /* Sets up the network and then asks for inputs.
     * @param args Set as empty 
     */
    public static void main(String [] args) throws IOException
    {
        /*
         * Initialize 5 switches and 13 hosts with 48-byte unique MAC addresses.
         */
        
        Switch s1 = new Switch("0a-1b-3c-4d-5e-6f");
        Switch s2 = new Switch("0b-1c-3d-4e-5f-6a");
        Switch s3 = new Switch("0c-1d-3e-4f-5a-6b");
        Switch s4 = new Switch("0d-1e-3f-4a-5b-6c");
        Switch s5 = new Switch("0e-1f-3a-4b-5c-6d");
        
        Host h1 = new Host("a0-1b-3c-4d-5e-6f");
        Host h2 = new Host("a0-2b-3c-4d-5e-6f");
        Host h3 = new Host("a0-3b-3c-4d-5e-6f");
        Host h4 = new Host("a0-4b-3c-4d-5e-6f");
        Host h5 = new Host("a0-5b-3c-4d-5e-6f");
        Host h6 = new Host("a0-6b-3c-4d-5e-6f");
        Host h7 = new Host("a0-7b-3c-4d-5e-6f");
        Host h8 = new Host("a0-8b-3c-4d-5e-6f");
        Host h9 = new Host("a0-9b-3c-4d-5e-6f");
        Host h10 = new Host("a0-ab-3c-4d-5e-6f");
        Host h11 = new Host("a0-bb-3c-4d-5e-6f");
        Host h12 = new Host("a0-cb-3c-4d-5e-6f");
        Host h13 = new Host("a0-db-3c-4d-5e-6f");
        
        /*
         * Add the hosts and switches to a collection.
         */
        switches.add(s1);
        switches.add(s2);
        switches.add(s3);
        switches.add(s4);
        switches.add(s5);
        hosts.add(h1);
        hosts.add(h2);
        hosts.add(h3);
        hosts.add(h4);
        hosts.add(h5);
        hosts.add(h6);
        hosts.add(h7);
        hosts.add(h8);
        hosts.add(h9);
        hosts.add(h10);
        hosts.add(h11);
        hosts.add(h12);
        hosts.add(h13);
        
        /*
         * Connect the hosts and switches as seen in Figure 1.
         */
        
        h3.setNearestSwitch(s1);
        h2.setNearestSwitch(s1);
        s1.setNearestSwitch(s2);
        s1.setNearestSwitch(s4);
        
        h5.setNearestSwitch(s2);
        h6.setNearestSwitch(s2);
        h9.setNearestSwitch(s2);
        s2.setNearestSwitch(s3);
        
        h8.setNearestSwitch(s3);
        h11.setNearestSwitch(s3);
        h10.setNearestSwitch(s3);
        
        s4.setNearestSwitch(s5);
        h12.setNearestSwitch(s4);
        h13.setNearestSwitch(s4);
        
        h7.setNearestSwitch(s5);
        h1.setNearestSwitch(s5);
        h2.setNearestSwitch(s5);
        
        //s1.showAll();
        //s2.showAll();
        //s3.showAll();
        //s4.showAll();
        //s5.showAll();
        
        /*
         * Prompt user for inputs.
         */
        acceptInputs();
    }
    
    /*
     * Asks for inputs and then sends the messages.
     */
    public static void acceptInputs() throws IOException
    {
        /*
         * BufferedReader to read user input from console
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        /*
         * Print the MAC addresses of the hosts for the benefit of the user.
         * Recall that Laptop 0 in Figure 1 is Host h7 here.
         */
        System.out.println("The host addresses are:");
        for(int i = 0; i < hosts.size(); i++)
        {
            System.out.println((i+1)+ " "+ hosts.get(i).mac);
        }
        
        /*
         * Get user input.
         */
        System.out.println("Enter sender host address:");
        String src_addr = br.readLine();
        System.out.println("Enter message:");
        String msg = br.readLine();
        System.out.println("Enter destination host address:");
        String dest_addr = br.readLine();
        
        /*
         * Attempt to send the message if the MAC address given is a valid one for this network.
         */
        boolean sent = false;
        for(Host h : hosts)
        {
            if(h.mac.equals(src_addr))
            {
                h.send(dest_addr, msg);
                sent = true;
            }
        }
        if(sent==false)
        {
            System.out.println("Wrong address!");
        }
        
    }
}