//ChatOutput

package myPackage;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTextArea;

//import JXTA libraries
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.endpoint.InputStreamMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.OutputPipeEvent;
import net.jxta.pipe.OutputPipeListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.rendezvous.RendezVousService;
import net.jxta.rendezvous.RendezvousEvent;
import net.jxta.rendezvous.RendezvousListener;
import net.jxta.share.ContentAdvertisement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//This class will send messages thro pipe for chatting services
public class ChatOutput extends Thread implements Runnable,
OutputPipeListener,
RendezvousListener
{ //Class Variables
private JTextArea log=null , txtChat =null;
private String myPeerID = null,
myPeerName= null;
private PeerGroup BITSGroup =null;
private PipeService myPipeService = null;
private OutputPipe pipeOut =null;
private PipeAdvertisement pipeAdv=null;
private DiscoveryService myDiscoveryService=null;
private RendezVousService myRendezVousService=null;
private String msg = null;
private String ID=null;
private String name2;
private String PeerIDTo=null;
private String PeerIDFrom=null;
private String Factor;
private Vector matches;
private String ID1;
/** Creates a new instance of ChatOutput */
public ChatOutput(PeerGroup group,JTextArea log,JTextArea chat)
{
this.log = log;
this.txtChat = chat;
this.BITSGroup=group;
getServices();
}
private void getServices() //This method will obtain Peer Group Services
{
log.append("[+]Obtaining Services for chat...\n");
try{
myRendezVousService = BITSGroup.getRendezVousService();
myRendezVousService.addListener(this);
}catch(Exception e){
System.out.println("[-]Cannot obtain RendezVous Services.");
System.out.println("[-]Fatal Error: " + e.getMessage());
e.printStackTrace();
System.exit(-1);
}
myDiscoveryService = BITSGroup.getDiscoveryService();
myPipeService = BITSGroup.getPipeService();
myPeerID = BITSGroup.getPeerID().toString();
myPeerName = BITSGroup.getPeerName();
try{//Creating Pipe Advertisements from file
FileInputStream in = new FileInputStream("Pipe.adv");
XMLDocument xml = (XMLDocument)(StructuredDocumentFactory.newStructuredDocument(
MimeMediaType.XMLUTF8, in ));
this.pipeAdv = (PipeAdvertisement)AdvertisementFactory.newAdvertisement(xml);
in.close();
}
catch(Exception e){
System.out.println("[-]Exception: " + e.getMessage());
e.printStackTrace();
System.exit(-1);
}
log.append("[+]Chat Services sucessfully obtained.\n");
}
public void startingPipe()
{
log.append("[+]Creating Output Pipe.\n");
try{
//starting remoted advertisement to search for pipe
myDiscoveryService.getRemoteAdvertisements(null,DiscoveryService.ADV,null,null,1);
myPipeService.createOutputPipe(pipeAdv,this);
}catch(Exception e){
log.append("[+]Exception: " + e.getMessage()+"\n");
e.printStackTrace();
System.exit(-1);
}
log.append("[+]Output Pipe Successfully Created.\n");
}
public void setMessage(String message){//This accessor will set messages that need to be sent
this.msg = message;
}
public void setPeerIDTo(String ID)
{this.PeerIDTo=ID;
}
public void setPeerIDFrom(String ID)
{this.PeerIDFrom=ID;
}
public void setID(String ID)
{this.ID=ID;}
public void setFileName(String name)
{this.name2=name;}
public void setFactor(String Factor)
{this.Factor=Factor;}
// public void setAdvertisements(ContentAdvertisement[] string)
//{this.matches=string;}
//Listener to send message thro pipe as requested
@Override
public void outputPipeEvent(OutputPipeEvent ev)
{
log.append("[+]Sending Message.\n");
pipeOut = ev.getOutputPipe();
Message myMessage = null;
DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
Date date = new Date();
String myTime = dateFormat.format(date).toString();
try{
myMessage = new Message();
//adding timestap and peers details also messages to XML tag and send them
StringMessageElement sme = new StringMessageElement("peerName",myPeerName,null);
StringMessageElement sme1 = new StringMessageElement("peerID",myPeerID,null);
StringMessageElement sme2 = new StringMessageElement("chatMessage",msg,null);
StringMessageElement sme3 = new StringMessageElement("Time",myTime,null);
if(msg.toString().equals("response"))
{log.append("[+]System is responding");
for(int r=0;r<matches.size();r++)
{StringMessageElement sme4=new StringMessageElement("response",matches.get(r).toString(),null);
myMessage.addMessageElement(sme4);}
StringMessageElement sme5=new StringMessageElement("PeerIDTo",PeerIDTo,null);
StringMessageElement sme6=new StringMessageElement("PeerIDFrom",new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString(),null);
myMessage.addMessageElement(sme6);
myMessage.addMessageElement(sme5);
pipeOut.send(myMessage);
}
else
if(msg.toString().equals("Intimate"))
{log.append("[+]System is intimatimg");
sme2=new StringMessageElement("FileName",name2,null);
sme3=new StringMessageElement("Intimate","true",null);
//StringMessageElement sme5=new StringMessageElement("ContentID",contentID,null);
StringMessageElement sme4=new StringMessageElement("PeerIDTo",ID1,null);
// myMessage.addMessageElement(sme5);
StringMessageElement sme6=new StringMessageElement("PeerIDFrom",new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString(),null);
myMessage.addMessageElement(sme6);
myMessage.addMessageElement(sme4);
myMessage.addMessageElement(sme3);
myMessage.addMessageElement(sme2);
System.out.println("sme");
pipeOut.send(myMessage);
}
else
if(msg.toString().equals("query"))
{log.append("[+}System is querying");
sme2=new StringMessageElement("query",name2,null);
sme3=new StringMessageElement("Factor",Factor,null);
StringMessageElement sme4=new StringMessageElement("PeerIDFrom",new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString(),null);
myMessage.addMessageElement(sme2);
myMessage.addMessageElement(sme3);
myMessage.addMessageElement(sme4);
System.out.println(myMessage.toString());
pipeOut.send(myMessage);
}
else
if(msg.equals("TagsSent"))
{log.append("[+]System is sending tags");
sme2=new StringMessageElement("PeerIDTo",ID,null);
myMessage.addMessageElement(null, sme2);
StringMessageElement sme6=new StringMessageElement("PeerIDFrom",new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString(),null);
myMessage.addMessageElement(sme6);
StringMessageElement sme8=new StringMessageElement("TagsSent","true",null);
myMessage.addMessageElement(sme8);
File file2=new File("Tag\\"+name2+".tg");
InputStreamMessageElement in=new InputStreamMessageElement("Tag",MimeMediaType.XMLUTF8,new
FileInputStream(new File("Tag\\"+name2+".tg")) ,null);
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse (file2);
doc.getDocumentElement ().normalize ();
NodeList list = doc.getElementsByTagName("Key");
for(int j=0;j<list.getLength();j++)
{Node node=list.item(j);
StringMessageElement sme4=new
StringMessageElement("word",node.getAttributes().getNamedItem("word").getNodeValue(),null);
StringMessageElement sme5=new
StringMessageElement("font",node.getAttributes().getNamedItem("font").getNodeValue(),null);
System.out.println(node.getAttributes().getNamedItem("font").getNodeValue());
myMessage.addMessageElement(null,sme4);
myMessage.addMessageElement(null,sme5);}
StringMessageElement sme7=new StringMessageElement("FileName",name2,null);
myMessage.addMessageElement(sme7);
myMessage.addMessageElement(in);
System.out.println(myMessage.toString());
pipeOut.send(myMessage);}
else
if(msg.equals("update"))
{log.append("[+]System is updating");
File file2=new File("Tags.Downloads"+"\\"+name2+".tg");
InputStreamMessageElement in=new InputStreamMessageElement("Tag",MimeMediaType.XMLUTF8,new
FileInputStream(new File("Tmp.downloads\\"+name2+".tg")) ,null);
// in1=new InputStreamMessageElement("Tag",file2);
myMessage.addMessageElement(in);
StringMessageElement sme9=new StringMessageElement("update","true",null);
myMessage.addMessageElement(sme9);
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse (file2);
doc.getDocumentElement ().normalize ();
String PeerID=
doc.getElementsByTagName("PeerIDFrom").item(0).getAttributes().getNamedItem("Value").getNodeValue();
StringMessageElement sme6=new StringMessageElement("PeerIDTo",PeerID,null);
myMessage.addMessageElement(sme6);
File file3=new File("Tmp.downloads"+"\\"+name2+".tg");
Document doc1=docBuilder.parse(file3);
doc1.normalize();/*here
*
*/
NodeList list = doc1.getElementsByTagName("Key");
for(int j=0;j<list.getLength();j++)
{Node node=list.item(j);
StringMessageElement sme4=new
StringMessageElement("word",node.getAttributes().getNamedItem("word").getNodeValue(),null);
StringMessageElement sme5=new
StringMessageElement("font",node.getAttributes().getNamedItem("font").getNodeValue(),null);
System.out.println(node.getAttributes().getNamedItem("font").getNodeValue());
myMessage.addMessageElement(null,sme4);
myMessage.addMessageElement(null,sme5);}
StringMessageElement sme7=new StringMessageElement("FileName",name2,null);
myMessage.addMessageElement(sme7);
System.out.println(myMessage.toString());
pipeOut.send(myMessage);
}
else
{ InputStreamMessageElement in=new InputStreamMessageElement("Advertisement",MimeMediaType.XMLUTF8,new
FileInputStream(new File("Pipe.Adv")) ,null);
myMessage.addMessageElement(in);
System.out.println(in.toString());
myMessage.addMessageElement(null,sme);
myMessage.addMessageElement(null,sme1);
myMessage.addMessageElement(null,sme2);
myMessage.addMessageElement(null,sme3);
pipeOut.send(myMessage);
txtChat.append("[ " + myPeerName+"@" + myTime+ "] " + msg + "\n");}
//Trigger the Sending
// pipeOut.send(myMessage);
//txtChat.append("[ " + myPeerName+"@" + myTime+ "] " + msg + "\n");
}catch(Exception e)
{
log.append("[-]Exception: " + e.getMessage()+"\n");
e.printStackTrace();
System.exit(-1);
} }
public synchronized void rendezvousEvent(RendezvousEvent event)
{
if(event.getType() == event.RDVCONNECT || event.getType() == event.RDVRECONNECT){
notify();
}
}
void setAdvertisements(Vector matches) {
this.matches=matches;
}
void setID1(String substring) {
this.ID1=substring;
}}
