
chatInput
package myPackage;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;
//importing JXTA libraries
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Attribute;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.Message.ElementIterator;
import net.jxta.endpoint.MessageElement;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.share.ContentAdvertisement;
import net.jxta.share.ContentManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
// this class will create input pipe for chatting services and shows the incoming messages
public class ChatInput extends Thread implements PipeMsgListener
{ //Class variables
private JTextArea txtChat=null , log=null;
private PeerGroup BITSGroup=null;
private String myPeerID = null;
private PipeService myPipeService =null;
private PipeAdvertisement pipeAdv =null;
private InputPipe pipeInput = null;
private JTable Table;
public ContentAdvertisement[] content =new ContentAdvertisement[80];
//private ContentManager contentManger;
private ChatOutput chatOut;
private ContentManager contentManager;
private static int b=0;
/** Creates a new instance of ChatInput */
public ChatInput(PeerGroup group, JTextArea log, JTextArea chat)
{
this.log = log;
this.txtChat = chat;
this.BITSGroup = group;
getServices();
}
private void getServices()
{ //Obtaining Peer Group services
log.append("[+]Getting Services for Chat component...\n");
myPipeService = BITSGroup.getPipeService();
myPeerID = BITSGroup.getPeerID().toString();
try{ //Creates input pipe
// FileInputStream is = new FileInputStream("Pipe.adv");
// pipeAdv = (PipeAdvertisement)AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType()
);
// is.close();
FileInputStream is = new FileInputStream("Pipe.adv");
// Create the pipeAdvertisement object from the is FileInputStream (use the AdvertisementFactory class)
XMLDocument xml = (XMLDocument)(StructuredDocumentFactory.newStructuredDocument(
MimeMediaType.XMLUTF8, is ));
this.pipeAdv = (PipeAdvertisement)AdvertisementFactory.newAdvertisement(xml);
is.close();
}catch(Exception e){
log.append("[+]Exception: " + e.getMessage()+"\n");
e.printStackTrace();
System.exit(-1);
}
log.append("[+]Input Pipe Successfully Created.\n");
}
public void setContentManager(ContentManager contentManager)
{this.contentManager=contentManager;}
public void setTable(JTable Table)
{this.Table =Table;}
public void startListening() //This method will start listening for incoming messages thro created pipe
{
log.append("[+]Start Listening for Incoming Messages.\n");
try{
pipeInput = myPipeService.createInputPipe(this.pipeAdv,this);
}catch(Exception e){
log.append("[-]Exception: " + e.getMessage()+"\n");
return;
}
if(pipeInput == null){
log.append("[-]Failure in Opening Input Pipe :-(\n");
System.exit(-1);
}
}
public void setChatOut(ChatOutput chatOut)
{this.chatOut =chatOut;}
public void stopListening() //This method will stop input pipe
{
pipeInput.close();
log.append("[-]Input Pipe Closed for Incomming Message.\n");
}
//this listener will respond to incoming messages and show them in Designated area
public void pipeMsgEvent(PipeMsgEvent ev)
{
log.append("[+]Message Received...\n");
Message myMessage = null;
try{
myMessage = ev.getMessage();
if(myMessage == null){
return;
}
}catch(Exception e){
System.out.println("[-]Exception happend when trying to get Message element!");
e.printStackTrace();
}
System.out.println(myMessage.toString());
//Assigning values to wanted Tages
ElementIterator el = myMessage.getMessageElements();
MessageElement me = myMessage.getMessageElement("peerName");
MessageElement me2 = myMessage.getMessageElement("peerID");
MessageElement me3 = myMessage.getMessageElement("chatMessage");
MessageElement me4 = myMessage.getMessageElement("Time");
ElementIterator me5=myMessage.getMessageElements("response");
//
if(myMessage.getMessageElement("PeerIDTo")==null||
//
myMessage.getMessageElement("PeerIDTo").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
while(me5.hasNext())
{ if( myMessage.getMessageElement("PeerIDTo").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
try {
XMLDocument xml = (XMLDocument)
(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8,new
StringReader(me5.next().toString())));
ContentAdvertisement contentAdvertisement=(ContentAdvertisement) AdvertisementFactory.newAdvertisement(xml);
content[b]=contentAdvertisement;
System.out.println("pp");
Table.setValueAt(contentAdvertisement.getName(),b,0);
Table.setValueAt(contentAdvertisement.getLength(), b, 1);
Table.setValueAt(contentAdvertisement.getDescription().substring(0,contentAdvertisement.getDescription().indexOf("^
"))
, b, 2);
b++;
} catch (IOException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
}
}
// if(!myMessage.getMessageElement("PeerIDFrom").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
if(myMessage.getMessageElement("query")!=null)
{ //{ if(!myMessage.getMessageElement("PeerIDFrom").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
System.out.println("query received");
TagSearchListener tg=new
TagSearchListener(myMessage.getMessageElement("query").toString(),myMessage.getMessageElement("Factor").toStri
ng(),contentManager,myMessage.getMessageElement("PeerIDFrom").toString(),this,chatOut);
}
else
//if(myMessage.getMessageElement("PeerIDTo").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
if(myMessage.getMessageElement("Intimate")!=null)
{ if(myMessage.getMessageElement("PeerIDTo").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
{
log.append("System is intimating");
File filet=new File("transactions");
if(!filet.exists())
{System.out.println("transactions file is not formed");}
else
{ try {
DocumentBuilderFactory documentBuilderFactory =
DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder =
documentBuilderFactory.newDocumentBuilder();
Document document = documentBuilder.parse(filet);
Document doc=documentBuilder.newDocument();
NodeList nlist=document.getElementsByTagName("transaction");
for(int j=0;j<nlist.getLength();j++)
{int no;
if(nlist.item(j).getAttributes().getNamedItem("name").getNodeValue().equals(myMessage.getMessageElement("FileNa
me").toString()))
{ {no=Integer.parseInt(nlist.item(j).getAttributes().getNamedItem("no").getNodeValue())+1;
Element root= doc.createElement("transaction");
root.setAttribute("name",nlist.item(j).getAttributes().getNamedItem("name").getNodeValue());
root.setAttribute("no",Integer.toString(no));
if(doc.getDocumentElement()==null)
{doc.appendChild(root);}
else
{doc.getDocumentElement().appendChild(root);
}
}
}
else
{Node adopt=doc.adoptNode(nlist.item(j));
if(doc.getDocumentElement()==null)
{doc.appendChild(adopt);}
else
{doc.getDocumentElement().appendChild(adopt);
}
}
}
TransformerFactory transformerFactory =
TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(doc);
StreamResult result = new StreamResult(filet);
transformer.transform(source, result);
} catch (TransformerException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (SAXException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (IOException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (ParserConfigurationException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
}
}
chatOut.setFileName(myMessage.getMessageElement("FileName").toString());
chatOut.setID(myMessage.getMessageElement("PeerIDFrom").toString());
this.stopListening();
chatOut.setMessage("TagsSent");
log.append("System is sending tags");
chatOut.startingPipe();
this.startListening();}}
else
if(myMessage.getMessageElement("update")!=null)
{ if(myMessage.getMessageElement("PeerIDTo").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
{try {
MessageElement file=myMessage.getMessageElement("Tag");
XMLDocument xml = (XMLDocument)
(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8,new StringReader(file.toString())));
ElementIterator e1=myMessage.getMessageElements("word");
ElementIterator e2=myMessage.getMessageElements("font");
MessageElement name=myMessage.getMessageElement("FileName");
DocumentBuilderFactory documentBuilderFactory =
DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder =
documentBuilderFactory.newDocumentBuilder();
int No_transaction = 0;
Document document = documentBuilder.newDocument();
Document doc=documentBuilder.parse(new File("Tag"+"\\"+name+".tg"));
Document doc1=documentBuilder.parse(new File("transactions"));
Document doc2=documentBuilder.parse(file.getStream());
NodeList nlist1=doc1.getElementsByTagName("transaction");
for(int r=0;r<nlist1.getLength();r++)
{if(nlist1.item(r).getAttributes().getNamedItem("name").getNodeValue().equals(name))
{No_transaction=Integer.parseInt(nlist1.item(r).getAttributes().getNamedItem("no").getNodeValue());
}
}
NodeList nlist2=doc2.getElementsByTagName("Key");
String word=null;;
String font1 = null;
String font=null;
NodeList nlist=doc.getElementsByTagName("Key");
for(int h1=0;h1<nlist2.getLength();h1++)
{
font=nlist2.item(h1).getAttributes().getNamedItem("word").getNodeValue();
word=nlist2.item(h1).getAttributes().getNamedItem("word").getNodeValue().toString();
for(int h=0;h<nlist.getLength();h++)
{//NodeList nlist2=doc2.getElementsByTagName("Key");
//for(int h=0;h<nlist.getLength();h++)
font1=nlist.item(h).getAttributes().getNamedItem("font").getNodeValue().toString();
if(word.toString().equals(nlist.item(h).getAttributes().getNamedItem("word").getNodeValue().toString()))
{font1=Integer.toString(((Integer.parseInt(font1)*(No_transaction-1))+Integer.parseInt(font.toString()))/
(No_transaction));
}}
Element root1= document.createElement("Key");
root1.setAttribute("word",word.toString());
root1.setAttribute("font",font1);
if(document.getDocumentElement()==null)
{document.appendChild(root1);}
else
{document.getDocumentElement().appendChild(root1);}
}
TransformerFactory transformerFactory =
TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(document);
File file5=new File("Tag"+"\\"+name+".tg");
if(!file5.isFile())
file5.createNewFile();
StreamResult result = new StreamResult(file5);
transformer.transform(source, result);} catch (TransformerException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (SAXException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (IOException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (ParserConfigurationException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
}
}
}
else
if(myMessage.getMessageElement("TagsSent")!=null)
{ if(!myMessage.getMessageElement("PeerIDFrom").toString().equals(new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(),null).toString()))
{log.append("[+]System has received tags");
File file2=new File("Tags.Downloads");
if(!file2.isDirectory())
file2.mkdir();
try {
MessageElement file= myMessage.getMessageElement("Tag");
//log.append(file.toString());
ElementIterator e1=myMessage.getMessageElements("word");
ElementIterator e2=myMessage.getMessageElements("font");
MessageElement name=myMessage.getMessageElement("FileName");
String fname=name.toString();
XMLDocument xml = (XMLDocument)
(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8,new StringReader(file.toString())));
DocumentBuilderFactory documentBuilderFactory =
DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder =
documentBuilderFactory.newDocumentBuilder();
Document document = documentBuilder.newDocument();
Document doc=documentBuilder.parse( xml.getStream());
// Attribute attribute = xml.getAttribute("Key");
NodeList nlist1=doc.getElementsByTagName("Key");
for(int i=0;i<nlist1.getLength();i++)
{String word=nlist1.item(i).getAttributes().getNamedItem("word").getNodeValue();
String font=nlist1.item(i).getAttributes().getNamedItem("font").getNodeValue();
log.append(word+"\n");
log.append(font+"\n");
Element rootElement = document.createElement("Key");
rootElement.setAttribute("word",word.toString());
rootElement.setAttribute("font",font.toString());
if(document.getDocumentElement()==null)
document.appendChild(rootElement);
else
document.getDocumentElement().appendChild(rootElement);
}
/* while(e1.hasNext())
{MessageElement word=el.next();
MessageElement font=e2.next();
Element rootElement = document.createElement("key");
rootElement.setAttribute("word",word.toString());
rootElement.setAttribute("font",font.toString());
if(document.getDocumentElement()==null)
document.appendChild(rootElement);
else
document.getDocumentElement().appendChild(rootElement);*/
Date date=new Date();
SimpleDateFormat s=new SimpleDateFormat("dd-MM-yy");
String d=s.format(date);
Element root=document.createElement("date");
root.setAttribute("Date", d);
document.getDocumentElement().appendChild(root);
Element root1=document.createElement("PeerIDFrom");
root1.setAttribute("Value",myMessage.getMessageElement("PeerIDFrom").toString());
document.getDocumentElement().appendChild(root1);
TransformerFactory transformerFactory =
TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(document);
File file5=new File("Tags.Downloads"+"\\"+name.toString()+".tg");
file5.createNewFile();
log.append("file is created");
StreamResult result = new StreamResult(file5);
transformer.transform(source, result);
} catch (SAXException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (IOException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (TransformerException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
} catch (ParserConfigurationException ex) {
Logger.getLogger(ChatInput.class.getName()).log(Level.SEVERE, null, ex);
}
}}
else
if(me == null || me2.equals(myPeerID)){
return;
}
else{
txtChat.append("[ " + me+ "@" + me4 +"] " + me3 + "\n");}}}
