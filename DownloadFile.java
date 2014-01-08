

DownloadFile
package myPackage;
import java.io.File;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.plaf.ProgressBarUI;
import net.jxta.peergroup.PeerGroup;
import net.jxta.share.ContentAdvertisement;
import net.jxta.share.client.GetContentRequest;
//This class runs as Thread and start Downloading the File as soon as called
public class DownloadFile extends Thread
{
private PeerGroup BITSGroup=null;
protected GetRemoteFile myDonwloader = null;
private JTextArea log;
public DownloadFile(PeerGroup group, ContentAdvertisement contentAdv, File destination , JTextArea log,
JProgressBar progress,ChatInput chatIn,ChatOutput chatOut)
{
this.log = log;
this.log.append("[+]Starting Download Object.\n");
//inner classes used here for better performance
myDonwloader = new GetRemoteFile(group, contentAdv, destination, this.log, progress,chatIn,chatOut);
}
}
//inner class which handles download requestes
class GetRemoteFile extends GetContentRequest
{
private JProgressBar progressBar = null;
private JTextArea log =null;
private boolean downloadFinished = false;
private ChatInput chatIn;
private ChatOutput chatOut;
public GetRemoteFile(PeerGroup group, ContentAdvertisement contentAdv, File destination , JTextArea log,
JProgressBar progress,ChatInput chatIn,ChatOutput chatOut)
{
super(group, contentAdv, destination);
this.chatIn=chatIn;
this.chatOut=chatOut;
this.progressBar = progress;
this.log = log;
this.log.append("[+]Download in Progress.\n");
}
public void notifyUpdate(int percentage) //this method will notify about download progress
{
progressBar.setValue(percentage);
}
public void notifyDone()//this method will return message about download process
{
log.append("[+]Donwloading Process is sucessfully finished.\n");
chatIn.stopListening();
chatOut.setMessage("Intimate");
chatOut.startingPipe();
chatIn.startListening();
}
public void notifyFailure()//this method will return message if download failed
{
log.append("[-]Downloading File is Failed!!!!!\n");
}
}
