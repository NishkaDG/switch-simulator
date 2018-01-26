/*
 * A machine which can send and receive messages, but has only one port.
 */
public class Host
{
    String mac;//MAC address
    Switch nearest_switch;//The switch the send messages through
    String mode = "promiscuous";//The mode
    int switchPort; //The port of the switch through which this host is connected to the switch
    
    /*
     * Constructor/
     * @param m MAC address
     */
    public Host(String m)
    {
        this.mac = m;
    }
    
    /*
     * Connect to a switch.
     * @param s The switch to connect to.
     */
    public void setNearestSwitch(Switch s)
    {
        /*
         * Save which port of the switch this host is being connected to.
         */
        this.switchPort = s.addConnection(this);
        //System.out.println(this.mac);
        
        /*
         * If the switch has any empty ports, operation succeeds.
         */
        if(switchPort>=0)
        {
            this.nearest_switch = s;
            
            /*
             * If connecting to a switch, this object's ports also have to be set.
             */
            if(this instanceof Switch)
            {
                ((Switch)this).ports.add(s);
                ((Switch)this).switchPorts[((Switch)this).ports.size()-1] = s.mac;
            }
        }
        
        /*
         * If all 5 ports of the switch are already occupied, operation fails.
         */
        else
        {
            System.out.println("All ports of this switch are taken. Choose a different switch.");
        }
    }
    
    /*
     * Receives a message.
     * If not a switch, prints acknowledgement.
     * @param src_addr MAC address of sender.
     * @param dest_addr MAC address of receiver.
     * @param msg The message to be sent.
     */
    public void receive(String src_addr, String dest_addr, String msg)
    {
        if(!(this instanceof Switch))
        {
            System.out.println("Message received by "+mac);
        }
    }
    
    /*
     * Send a message via the connected switch.
     * @param dest_addr MAC address of receiver.
     * @param msg Message to be sent.
     */
    public void send(String dest_addr, String msg)
    {
        this.nearest_switch.receive(switchPort, mac, dest_addr, msg);
    }
    
}