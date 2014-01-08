//JXTAStarter
package myPackage;

//This class is for initializing Peer and launch it into Default JXTA network and use its
//Services to Create out own group.

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import javax.swing.JTextArea;

//importing JXTA Libraries
import net.jxta.credential.AuthenticationCredential;
import net.jxta.credential.Credential;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredTextDocument;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.membership.Authenticator;
import net.jxta.membership.MembershipService;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;

public class StartJXTA
{ 

	public JTextArea log;
	private final static int TIMEOUT = 5*1000;
	private PeerGroup netPeerGroup = null,
	private BITSGroup =null;
	DiscoveryService myDiscoveryService =null,
	BITSGroupDiscoveryService =null;
	PeerGroupAdvertisement BITSAdv =null;
	
	//unique id for group, it is taken from JXTA services
	private final String stringID = "jxta:uuid-4E0742B0E54F4D0ABAC6809BB82A311E02";
	
	/** Creates a new instance of StartJXTA */

	public StartJXTA(JTextArea txt){

	this.log = txt;
	
	launchJXTA();
	getServices();
	searchForGroup();
	}

	private void launchJXTA(){

	log.append("[+]Launching into JXTA Network...\n");
	
	try{

		netPeerGroup = PeerGroupFactory.newNetPeerGroup();

		}catch(PeerGroupException e){

		System.out.println("[-]Fatal Error:" + e.getMessage());
		e.printStackTrace();
		System.exit(-1);
		}
	}
	
	private void getServices(){

	//Obtaining JXTA Services from JXTA Global group
	log.append("[+]Obtaining Peer Group Services.\n");

	myDiscoveryService = netPeerGroup.getDiscoveryService();
	}

	private void searchForGroup(){
	
	//If group found it will be join into it otherwise it

	Enumeration adv=null; //will create the group itself.
	int count =0;
	
	log.append("[+]Searching for BITSGroup Advertisements.\n");

	while(count < 5){
		try {
			log.append("[+]Try Number: " + count +"\n");
			
			//Searching for Group Advertisements , first from local cach if not
			//found then search from remote peers
			adv = myDiscoveryService.getLocalAdvertisements(DiscoveryService.GROUP,"Name","BITSGroup");
	
			if((adv != null) && adv.hasMoreElements()){
					log.append("[+]BITSGroup found in Local advertisement.\n");
					BITSAdv = (PeerGroupAdvertisement)adv.nextElement();
					BITSGroup = netPeerGroup.newGroup(BITSAdv);
					joinToGroup(BITSGroup);
			break;
			
			}else{
					log.append("[-]No Group Found in Local advertisement.\n[+]Starting Remote Search...\n");
					myDiscoveryService.getRemoteAdvertisements(null,DiscoveryService.GROUP,"Name","BITSGroup",1);
			}

			Thread.sleep(TIMEOUT);

			//if group not found after couple of tries it will create the group itself

			if((count == 4) && (adv == null || !adv.hasMoreElements())){

					log.append("[-]No Group Found!!! - Creating Group\n");
					
					BITSGroup = createGroup();
					joinToGroup(BITSGroup);
					break;
			
			}
				} catch (IOException ex) {
						ex.printStackTrace();
						}catch(PeerGroupException e){
						
						System.out.println("[-]Fatal Error:" + e.getMessage());
						e.printStackTrace();
						System.exit(-1);
						
						}catch(InterruptedException e){

						System.out.println("[-]Fatal Error:" + e.getMessage());
						e.printStackTrace();}
			
			count++;
				}
		}
		
private PeerGroup createGroup() //This method will Create BITS group :-)
{
log.append("[+]Creating New Group...\n");
PeerGroup myNewGroup = null;
try{
//specifying advertisement for group and configure group, then publish it
//Advertisement for remote peers
ModuleImplAdvertisement myMIA = netPeerGroup.getAllPurposePeerGroupImplAdvertisement();
myNewGroup = netPeerGroup.newGroup(getGID(),
myMIA,
"BITSGroup",
"COP");
BITSAdv = myNewGroup.getPeerGroupAdvertisement();
//publishing new group advertisements
myDiscoveryService.publish(BITSAdv);
myDiscoveryService.remotePublish(BITSAdv);
log.append("[+]New Peer Group Successfully created :-)\n");
log.append("[+]Publishing new Group Advertisements.\n");
log.append("[+]Group Information:\n");
log.append("[===========================]\n");
log.append("[+]Group Name: " + BITSAdv.getName()+"\n");
log.append("[+]Group ID:" + BITSAdv.getPeerGroupID().toString()+"\n");
log.append("[+]Group Description: " + BITSAdv.getDescription()+"\n");
log.append("[+]Group Module ID: " + BITSAdv.getModuleSpecID().toString()+"\n");
log.append("[+]Advertisement Type: " + BITSAdv.getAdvertisementType()+"\n");
log.append("[===========================]\n");
}catch(Exception e){
System.out.println("[*]Fatal Error:" + e.getMessage());
e.printStackTrace();
System.exit(-1);
}
return myNewGroup;
}
//This method will return peerGroupID from given String ID
private PeerGroupID getGID() throws Exception{
return (PeerGroupID) IDFactory.fromURL(new URL("urn","",stringID));
}
private void joinToGroup(PeerGroup group) //This method will join to either found group or created group
{
StructuredDocument creds = null;
log.append("[===========================]\n");
log.append("[+]Joining into BITSGroup..\n");
try{
//Athenticate and join to group
AuthenticationCredential authCred = new AuthenticationCredential(group,null,creds);
MembershipService membership = group.getMembershipService();
Authenticator auth = membership.apply(authCred);
if(auth.isReadyForJoin()){
Credential myCred = membership.join(auth);
System.out.println("[===== Group Details =====]");
StructuredTextDocument doc = (StructuredTextDocument)myCred.getDocument(new MimeMediaType("text/
plain"));
StringWriter out = new StringWriter();
doc.sendToWriter(out);
System.out.println(out.toString());
log.append("[+]Peer Name : " + group.getPeerName() + " is now online :-)\n");
log.append("[+]Obtaining BITSGroup Services.\n");
//Publishing Peer Advertisements.
BITSGroupDiscoveryService = group.getDiscoveryService();
log.append("[+]Publishing Peer Advertisement.\n");
BITSGroupDiscoveryService.publish(group.getPeerAdvertisement());
BITSGroupDiscoveryService.remotePublish(group.getPeerAdvertisement());
log.append("[===========================]\n");
}
else{
System.out.println("[!!]Fatal Error: Cannot Join to The Group!");
System.exit(-1);
}
}catch(Exception e){
System.out.println("[!]Fatal Error: " + e.getMessage());
e.printStackTrace();
System.exit(-1);
}
}
public PeerGroup getBITSGroup() //This accessor will return group
{
return BITSGroup;
}
public PeerGroupAdvertisement getBITSAdv(){//This accessor will return Advertisements
return BITSAdv;
}}
