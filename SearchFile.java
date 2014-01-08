package myPackage;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import net.jxta.peergroup.PeerGroup;
import net.jxta.share.ContentAdvertisement;
import net.jxta.share.client.CachedListContentRequest;
//inner class for search

public class SearchFile extends Thread{

	private String RelevanceFactor;
	
	private static class ListTagSearchRequestor extends Thread{
	private ContentAdvertisement[] searchResult;
	private String Key;
	private ChatInput chatIn;
	private ChatOutput chatOut;
	private JTextArea log=null;
	private JTable table1;
	private PeerGroup Group;
	private String RelevanceFactor;
	
	
	public ListTagSearchRequestor(PeerGroup BITSGroup, String searchValue, JTextArea log, JTable table,ChatInput
	chatIn,ChatOutput chatOut,String RelevanceFactor) {
		this.Group=BITSGroup;
		this.Key=searchValue;
		this.chatIn=chatIn;
		this.chatOut=chatOut;
		this.log=log;
		this.RelevanceFactor=RelevanceFactor;
		this.table1=table;
		
		chatOut.setPeerIDFrom(new MD5().createPeerID(Group.getPeerGroupID(),Group.getPeerName(),null).toString()
);
System.out.println(Key);
chatOut.setFileName(searchValue);
chatOut.setFactor(RelevanceFactor);
chatOut.setMessage("query");
chatIn.stopListening();
chatOut.startingPipe();
chatIn.startListening();
log.append("[+]Query has been sent");
}
private void activateRequest() {
}
}
//Defining Class Variables
private JTextArea log=null;
private PeerGroup BITSGroup=null;
private String searchValue =null;
protected ListTagSearchRequestor reqestor =null;
private JTable table = null;
private ChatInput chatIn=null;
private ChatOutput chatOut=null;
public static ContentAdvertisement [] contents=null;
private boolean running = true;
public SearchFile(PeerGroup group,String searchKey, JTextArea log, JTable table,ChatInput chatIn,ChatOutput
chatOut,String Factor)
{
this.BITSGroup = group;
this.searchValue = searchKey;
this.log = log;
this.chatIn=chatIn;
this.chatOut=chatOut;
this.table = table;
this.RelevanceFactor=Factor;
}
public void run() //cause this thread to execute as long as needed to find
{
// the Contents
while(true)
{
if(running == false){
break;
}
reqestor = new ListTagSearchRequestor(BITSGroup,searchValue,log,table,chatIn,chatOut, RelevanceFactor);
reqestor.activateRequest();
try{
Thread.sleep(8*1000); //Time out for each search through network
} catch(InterruptedException ie)
{
stopThread();
}
}
log.append("[-]Searching for content is finished.\n");
}
public void stopThread() //This method will stop search Process
{
running = false;
// if (reqestor != null){
// reqestor.cancel();
}
public void killThread() //This method will Terminate the Search Thread
{
log.append("[-]Searching Thread is stopping.\n");
running =false;
}
public ContentAdvertisement [] getContentAdvs() //Accessor to show found contents
{
return reqestor.searchResult;
}
}
class ListRequestor extends CachedListContentRequest
{
public static ContentAdvertisement [] searchResult = null;
private JTextArea log = null;
private JTable table =null;
public ListRequestor(PeerGroup BITSGroup , String SubStr, JTextArea log,JTable table){
super(BITSGroup,SubStr);
this.log = log;
this.table = table;
}
public void notifyMoreResults() //this method will notify user when new contents are found
{
log.append("[+]Searching for More Contents.\n");
searchResult = getResults();
//showing the results
String [] titles = {"File Name" , "Size Bytes","Check Sum (CRC-32)"};
//add new contents to Search table
DefaultTableModel TableModel1 = new DefaultTableModel(titles, searchResult.length);
table.setModel(TableModel1);
for(int i=0; i < searchResult.length;i++){
log.append("[*]Found: " + searchResult[i].getName()+"\n" +
"Size: " + searchResult[i].getLength() + " Bytes\n");
table.setValueAt(searchResult[i].getName(),i,0);
table.setValueAt(searchResult[i].getLength(),i,1);
System.out.println(searchResult[i].getDescription());
table.setValueAt(searchResult[i].getDescription().toString().substring(0,searchResult[i].getDescription().indexOf("^")
),i,2);
}
}
public ContentAdvertisement [] getContentAdvs()//acessor to return contents
{
return searchResult;}}
