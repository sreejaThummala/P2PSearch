
Sharing
package myPackage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
//import jxta libraries
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.share.CMS;
import net.jxta.share.Content;
import net.jxta.share.ContentAdvertisement;
import net.jxta.share.ContentManager;
import net.jxta.share.SearchListener;
import net.jxta.share.metadata.ContentMetadata;
import net.jxta.share.metadata.Description;
import net.jxta.share.metadata.Keywords;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//This class will share contents through peers in BITS Group
public class Sharing extends Thread implements SearchListener
{
//Defining Class Variables
private PeerGroup BITSGroup =null;
private JTextArea log=null;
private File myPath = null;
//using Content Management Service Library for Sharing purposes
private CMS cms =null;
ContentManager contentManager = null;
/** Creates a new instance of Sharing */
public Sharing(PeerGroup group, JTextArea log, File givenPath)
{
this.log = log;
this.BITSGroup = group;
this.myPath = givenPath;
try {
try {
launchCMS();
} catch (TransformerConfigurationException ex) {
Logger.getLogger(Sharing.class.getName()).log(Level.SEVERE, null, ex);
} catch (TransformerException ex) {
Logger.getLogger(Sharing.class.getName()).log(Level.SEVERE, null, ex);
}
} catch (IOException ex) {
Logger.getLogger(Sharing.class.getName()).log(Level.SEVERE, null, ex);
} catch (ParserConfigurationException ex) {
Logger.getLogger(Sharing.class.getName()).log(Level.SEVERE, null, ex);
}
}
private void launchCMS() throws IOException, ParserConfigurationException, TransformerConfigurationException,
TransformerException
{
//This method will initializie the CMS library
log.append("[+]Initialising CMS Libraries...\n");
cms = new CMS();
try {
cms.init(BITSGroup,null,null);//binding CMS object to BITS Group
if(cms.startApp(myPath) == -1){
log.append("[-]Creating CMS object Failed.\n");
System.out.println("[!]CMS Initilization Failed.\nExiting.");
System.exit(-1);
}else{
log.append("[+]CMS object Successfully Created.\n");
}
//sharing all files in shared directory
contentManager = cms.getContentManager();
File [] list = myPath.listFiles();
File filer=new File(myPath+"\\"+"shares.ser");
System.out.println(myPath+"\\"+"shares.ser");
if(filer.exists())
{if(! filer.delete())
System.out.println("Available");
}
CheckSumCalc checkSum = new CheckSumCalc();
ContentMetadata[] mdata=null;
for(int j=0;j<list.length;j++)
{File file1=new File("Tag");
if(!file1.isDirectory())
file1.mkdir();
String name="Tag\\"+list[j].getName()+".tg";
// System.out.println(name);
File file2=new File("Tag\\"+list[j].getName()+".tg");
if(!file2.exists())
file2.createNewFile();
DocumentBuilderFactory documentBuilderFactory =
DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder =
documentBuilderFactory.newDocumentBuilder();
Document document = documentBuilder.newDocument();
Element root =document.createElement("Key");
root.setAttribute("word",list[j].getName());
root.setAttribute("font","8");
document.appendChild(root);
TransformerFactory transformerFactory =
TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(document);
StreamResult result = new StreamResult(file2);
transformer.transform(source, result);}
File file=new File("transactions");
if(file.exists())
{ DocumentBuilderFactory documentBuilderFactory =
DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder =
documentBuilderFactory.newDocumentBuilder();
Document document =documentBuilder.parse(file);
Document doc=documentBuilder.newDocument();
NodeList nlist=document.getElementsByTagName("transaction");
for(int i=0;i<list.length;i++){
if(list[i].isFile())
{String number="0";
contentManager.share(list[i],checkSum.getFileSum(list[i])+"^"+new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(), null).toString());
for(int l=0;l<nlist.getLength();l++)
{if(nlist.item(l).getAttributes().getNamedItem("name").equals(list[i].getName()))
{number=nlist.item(l).getAttributes().getNamedItem("no").getNodeValue();
}
Element root= doc.createElement("transaction");
root.setAttribute("name",list[i].getName());
root.setAttribute("no",number);
if(doc.getDocumentElement()==null)
{doc.appendChild(root);}
else
{doc.getDocumentElement().appendChild(root);
}
}
System.out.println(list.length);
}}
TransformerFactory transformerFactory =
TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(doc);
StreamResult result = new StreamResult(file);
transformer.transform(source, result);}
else
{file.createNewFile();
DocumentBuilderFactory documentBuilderFactory =
DocumentBuilderFactory.newInstance();
DocumentBuilder documentBuilder =
documentBuilderFactory.newDocumentBuilder();
Document doc=documentBuilder.newDocument();
for(int i=0;i<list.length;i++){
if(list[i].isFile())
{ Element root= doc.createElement("transaction");
root.setAttribute("name",list[i].getName());
root.setAttribute("no","0");
if(doc.getDocumentElement()==null)
{doc.appendChild(root);}
else
{doc.getDocumentElement().appendChild(root);
}
contentManager.share(list[i],checkSum.getFileSum(list[i])+"^"+new
MD5().createPeerID(BITSGroup.getPeerGroupID(),BITSGroup.getPeerName(), null));
System.out.println(list.length);
}}
TransformerFactory transformerFactory =
TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(doc);
StreamResult result = new StreamResult(file);
transformer.transform(source, result);
}
log.append("======= Shared Contents =======\n");
//viewing the shared contents
Content [] content = contentManager.getContent();
//also shows the share contents in log area
System.out.println(content.length);
for(int j=0;j< content.length;j++){
// mdata=content[j].getContentAdvertisement().getMetadata();
log.append("[*]" + content[j].getContentAdvertisement().getName()+ "\tSum: " +
content[j].getContentAdvertisement().getDescription().substring(0,content[j].getContentAdvertisement().getDescription
().indexOf("^")) +"\n");
}
log.append("[+]All Content are Successfully Shared :-)\n");
} catch (SAXException ex) {
Logger.getLogger(Sharing.class.getName()).log(Level.SEVERE, null, ex);
} catch (TransformerException ex) {
Logger.getLogger(Sharing.class.getName()).log(Level.SEVERE, null, ex);
} catch (PeerGroupException ex)
{
System.out.println("[!]CMS Initilization Failed.\nExiting.");
System.exit(-1);
}
log.append("[===========================]\n");
}
public ContentManager getContentManger()
{return contentManager;}
public void stopCMS()//this method will stop Content management Service
{
log.append("[+]Stopping CMS Object.\n");
cms.stopApp();
log.append("[+]Deleting CMS Content Advertisements File.\n");
File temp = new File(myPath.getAbsolutePath()+ File.separator +"shares.ser");
if(temp.delete())
{ //also deletes the CMS data file
log.append("[+]File \""+ myPath.getAbsolutePath()+ File.separator + "shares.ser\" successfully deleted.\n");
System.out.println("[+]File shares.ser successfully deleted.");
}else{
log.append("[-]File shares.ser Not Found!\n");
}
}
//Listener to shows requested queries
@Override
public void queryReceived(String query){
System.out.println("[Query Received]: " + query);
}}

