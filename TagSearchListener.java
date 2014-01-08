//TagSearchListener
package myPackage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.jxta.share.Content;
import net.jxta.share.ContentAdvertisement;
import net.jxta.share.ContentManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TagSearchListener {

private String key;
int o=0;
String peerID;


// private String Factor;
public TagSearchListener(String key,String Factor,ContentManager contentManager,String PeerIDTo,ChatInput
chatIn,ChatOutput chatOut)
{ LinkedList keys=new LinkedList();
this.peerID=PeerIDTo;
int factor=Integer.parseInt(Factor);
int j=0;
while(key.indexOf(" ")!=-1)
{keys.add(key.substring(0,key.indexOf(" ")));
key=key.substring(key.indexOf(" ")+1);
j++;
System.out.println("rrr");
}
keys.add(key);
Vector matches=new Vector();
Content[] content=contentManager.getContent();
System.out.println(content.length);
for(int i=0;i<content.length;i++)
{File file2=new File("Tag\\"+content[i].getContentAdvertisement().getName()+".tg");
if(file2.exists())
{
try {
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse (file2);
// System.out.println(file2.getTotalSpace());
NodeList list = doc.getElementsByTagName("Key");
//int totalPersons = listOfPersons.getLength();
// System.out.println("Total no of people : " + totalPersons);
float sum=(float) 0.5;
//System.out.println(list.getLength());
for(int s=0; s<list.getLength() ; s++){
Node node=list.item(s);
String word=
node.getAttributes().getNamedItem("word").getNodeValue();
String font=node.getAttributes().getNamedItem("font").getNodeValue();
System.out.println(word+”-------"+font);
for(int t=0;t<keys.size();t++)
{
System.out.println(keys.get(t));
if(word.contains((String)keys.get(t)))
{
sum= sum+((float)(((String)keys.get(t)).length()))*((float)Integer.parseInt(font)/8);
}
}
System.out.println(sum);
if((sum*100)>factor)
{matches.add(content[i].getContentAdvertisement());
o++;}}
} catch (SAXException ex) {
Logger.getLogger(TagSearchListener.class.getName()).log(Level.SEVERE, null, ex);
} catch (IOException ex) {
Logger.getLogger(TagSearchListener.class.getName()).log(Level.SEVERE, null, ex);
} catch (ParserConfigurationException ex) {
Logger.getLogger(TagSearchListener.class.getName()).log(Level.SEVERE, null, ex);
}
}
else
{
System.out.println("eeeee");
}
}
if(matches!=null)
{
chatOut.setPeerIDTo(peerID);
chatOut.setMessage("response");
chatOut.setAdvertisements(matches);
chatIn.stopListening();
chatOut.startingPipe();
chatIn.startListening();
}
else
{System.out.println("not found");}}}
