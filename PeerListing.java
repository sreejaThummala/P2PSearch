
PeerListing
package myPackage;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
//import JXTA libraries
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;
//This class hooks the Listener Thread to Application and fined as many peers as possible in
// BITS Group
public class PeersListing extends Thread implements Runnable,DiscoveryListener
{
//Class variables
private PeerGroup BITSGroup = null;
private DiscoveryService myDiscoveryService=null;
private JTextArea log=null;
private JList list = null;
public boolean endOfSearch = false;
DefaultListModel listModel = new DefaultListModel();
/** Creates a new instance of PeersListing */
public PeersListing(PeerGroup group,JTextArea log,JList list)
{
this.BITSGroup = group;
this.log = log;
this.list = list;
myDiscoveryService = BITSGroup.getDiscoveryService();
}
public void run()
{ //this method will start this Thread
log.append("[+]Start Searching for Peers.\n");
while(true)
{
//Terminating this Thread as requested
if(checkLastTime()){
break;
}
try{
myDiscoveryService.getRemoteAdvertisements(null,DiscoveryService.PEER,null,null,5,this);
listModel.clear();
Thread.sleep(10*1000);
}catch(Exception e){
log.append("[-]Exception in Searching for Peers process!\n");
e.printStackTrace();
}
}
log.append("[-]Searching for Peers Stopped.\n");
}
private boolean checkLastTime()//causing the Thread to be terminated as requested
{
if(endOfSearch == true){
return true;
}
return false;
}
public void setEndOfSearch(boolean value) //Accessor to make an end for Thread
{
this.endOfSearch = value;
}
private void updatePeerList(Vector myList) //Updating Peer list
{
list.setListData(myList);
}
//Listener for monitoring Peers in BITS's Group
public void discoveryEvent(DiscoveryEvent event)
{
DiscoveryResponseMsg res = event.getResponse();
String name = "unknown";
boolean isInList = false;
PeerAdvertisement peerAdv = res.getPeerAdvertisement();
if(peerAdv != null){
name = peerAdv.getName();
}
PeerAdvertisement myAdv = null;
Enumeration en = res.getAdvertisements();
Vector peerList = new Vector();
//Assigning new Peers to Vector and show them
if(en != null){
while(en.hasMoreElements()){
myAdv = (PeerAdvertisement) en.nextElement();
peerList.addElement(myAdv.getName());
}
updatePeerList(peerList);
} }}
