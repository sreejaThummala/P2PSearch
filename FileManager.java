package myPackage;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FileCarer {
public FileCarer()
{File file=new File("Tags.Downloads");
if(!file.exists())
System.out.println("No Tags Found");
else
{File[] flist=file.listFiles();
for(int i=0;i<flist.length;i++)
{
try {
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse (flist[i]);
doc.getDocumentElement ().normalize ();
NodeList nlist= doc.getElementsByTagName("date");
if(nlist.getLength()>1)
System.out.println("Something has gone wrong");
Node node =nlist.item(0);
String dateSent=node.getAttributes().getNamedItem("Date").getNodeValue();
String date1=dateSent.substring(0,dateSent.indexOf("-"));
String month1=dateSent.substring(dateSent.indexOf("-")+1).substring(0,dateSent.substring(dateSent.indexOf("-")
+1).indexOf("-"));
int date11=((((int)(date1).charAt(0))*10))+((int)((date1.charAt(1))));
int month11=(((int)((month1).charAt(0)))*10)+((int)(month1.charAt(1)));
int time1=(month11*30)+date11;
Date date=new Date();
SimpleDateFormat s=new SimpleDateFormat("dd-MM-yy");
String d=s.format(date);
String date2=d.substring(0,d.indexOf("-"));
String month2=d.substring(d.indexOf("-")+1).substring(0,d.substring(d.indexOf("-")+1).indexOf("-"));
int date21=((((int)(date2).charAt(0))*10))+((int)((date2.charAt(1))));
int month21=(((int)((month2).charAt(0)))*10)+((int)(month2.charAt(1)));
int time2=(month21*30)+date21;
if(time2-time1>2)
flist[i].deleteOnExit();
} catch (SAXException ex) {
Logger.getLogger(FileCarer.class.getName()).log(Level.SEVERE, null, ex);
} catch (IOException ex) {
Logger.getLogger(FileCarer.class.getName()).log(Level.SEVERE, null, ex);
} catch (ParserConfigurationException ex) {
Logger.getLogger(FileCarer.class.getName()).log(Level.SEVERE, null, ex);}}}}}
