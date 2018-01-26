import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * A special kind of Host.
 * Has 5 ports and does not save messages itself.
 * Automatically passes on all messages it receives.
 * Maintains a forwarding table for each object.
 */
public class Switch extends Host
{
    File host_table; //File object directing to the host table for the current switch object
    ArrayList<Host> ports; //To store the ports
    String[] switchPorts; //To store the ports which direct to switches
    
    /*
     * Constructor
     * @param m the MAC address
     */
    public Switch(String m)
    {
        /*
         * Call the constructor of the superclass.
         */
        super(m);
        
        /*
         * Set the file name according to the MAC address and create a forwarding table if none exists.
         */
        String fname = "host_table_"+m+".csv";
        this.host_table = new File(fname);
        if(!(this.host_table.exists()))
        {
            try
            {
                this.host_table.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        
        /*
         * Initialize other variables.
         */
        this.ports = new ArrayList<Host>();
        this.switchPorts = new String[5];
        for(int j = 0; j < 5; j++)
        {
            this.switchPorts[j] = "";
        }
    }
    
    /*
     * Link a host to a port in order to understand incoming messages.
     * @param h The host to be connected to the lowest unoccupied port.
     */
    public int addConnection(Host h)
    {
        //System.out.println(ports.size());
        /*
         * Maximum number of ports is 5.
         */
        if(this.ports.size()<5)
        {
            this.ports.add(h);
            int index = this.ports.size()-1;
            if(h instanceof Switch)
            {
                //System.out.println("adding switch");
                this.switchPorts[index] = h.mac;
            }
            return index;
        }
        else
        {
            return -1;
        }
    }
    
    /*
     * Receive and forward messages.
     * @param src_port The port from which the message entered the switch.
     * @param src_addr The MAC address of the sender.
     * @param dest_addr The MAC address of the receiver.
     * @param msg the message to be forwarded.
     */
    public void receive(int src_port, String src_addr, String dest_addr, String msg)
    {
        /*
         * Check if the port for the receiver already exists in the forwarding table.
         */
        int dest_port = currPort(dest_addr);
        
        /*
         * Add the sender to the forwarding table.
         */
        this.setPort(src_port, src_addr);
        //System.out.println("Transmitting from: "+this.mac);
        //System.out.println(src_addr+" "+src_port);
        
        /*
         * If the destination port is unknown, broadcast.
         */
        if(dest_port == -1)
        {
            /*
             * Send to all ports.
             */
            for(int i = 0; i < this.ports.size(); i++)
            {
                /*
                 * No need to send to source port as well.
                 */
                if(i!=src_port)
                {
                    Host h = this.ports.get(i);
                    
                    /*
                     * If it is a switch, receive() will have different parameters.
                     */
                    if(h instanceof Switch)
                    {
                        ((Switch)h).receive(((Switch)h).getSwitchPort(this.mac), src_addr, dest_addr, msg);
                    }
                    
                    /*
                     * Normal hosts can simply be sent the message as is.
                     */
                    else
                    {
                        h.receive(src_addr, dest_addr, msg);
                    }
                }
            }
        }
        
        /*
         * Otherwise, if the destination port exists in the forwarding table, send only to that port.
         */
        else
        {
            Host dest_host = this.ports.get(dest_port);
            
            /*
             * If that port connects to a switch, forward as above.
             */
            if(dest_host instanceof Switch)
            {
                ((Switch)dest_host).receive(((Switch)dest_host).getSwitchPort(this.mac), src_addr, dest_addr, msg);
            }
            
            /*
             * Otherwise send to the receiver directly.
             */
            else
            {
                dest_host.receive(src_addr, dest_addr, msg);
            }
        }
    }
    
    /*
     * Adds a host and its port for this switch to the forwarding table.
     * @param p the incoming port index.
     * @param addr the MAC address of the host to be added.
     */
    public void setPort(int p, String addr)
    {
        /*
         * BufferedWriter object will write to the file.
         */
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(this.host_table, true));
            
            /*
             * If the host is not already in the forwarding table, then add it.
             */
            if(this.currPort(addr)==-1)
            {
                bw.write(addr+","+p+"\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bw.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /*
     * Reads the forwarding table for hosts and their corresponding ports for this switch.
     */
    public int currPort(String m)
    {
        /*
         * BufferedReader will read the file.
         */
        BufferedReader br = null;
        String line = "";
        int port = -1;
        try 
        {
            if(host_table.exists())
            {
                br = new BufferedReader(new FileReader(host_table));
                
                /*
                 * Read the file line by line.
                 */
                while ((line = br.readLine()) != null) 
                {
                    String[] row = line.split(",");
                    
                    /*
                     * Upon finding the host, extract its port.
                     */
                    if(row[0].equals(m))
                    {
                        port = Integer.parseInt(row[1]);
                    }
                }
            }

        } 
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        finally 
        {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return port;
    }
    
    /*
     * Returns the ports which connect directly to other switches.
     * @param m The connection to check for whether it is a switch.
     */
    public int getSwitchPort(String m)
    {
        for(int j = 0; j < 5; j++)
        {
            if(this.switchPorts[j].equals(m))
            {
              return j;
            }
        }
        return -1;
    }
    
    /*
     * Prints the MAC addresses of all machines connected to this switch.
     */
    public void showAll()
    {
        System.out.println("Displaying all connections of "+mac);
        for(int i = 0; i < this.ports.size(); i++)
        {
            Host h = this.ports.get(i);
            System.out.println(i+" "+h.mac);
        }
    }
}