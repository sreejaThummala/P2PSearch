package myPackage;

import java.security.MessageDigest;
import net.jxta.id.IDFactory;
import net.jxta.impl.id.UUID.PeerID;

public class MD5 {

  public static net.jxta.pipe.PipeID createPipeID(net.jxta.peergroup.PeerGroupID peerGroupID,String clearTextID, String
    function){
              LOG.info("Creatig pipe ID = peerGroupID:"+peerGroupID+", clearText:'"+clearTextID+"' , function:'"+function+"'");
             
              byte[] digest = generateHash(clearTextID, function);
              
              return (net.jxta.pipe.PipeID)net.jxta.id.IDFactory.newPipeID(peerGroupID, digest );
              }
              
  public static byte[] generateHash(String clearTextID, String function) {
  
              String id;
              
              if (function == null) {
              
                     id = clearTextID;
                      
                      } else {
                      
                      id = clearTextID + "~" + function;
                  `  
                  }
                  
                byte[] buffer = id.getBytes();

                MessageDigest algorithm = null;
                
                try {

                    algorithm = MessageDigest.getInstance("MD5");

                    } catch (Exception e) {
                    
                    LOG.error("Cannot load selected Digest Hash implementation",e);

                    return null;
                 }

               // Generate the digest.
    
                algorithm.reset();
                algorithm.update(buffer);
            
             try{
                
                byte[] digest1 = algorithm.digest();

                return digest1;
              
              }catch(Exception de){
              
              LOG.error("Failed to creat a digest.",de);
              
              return null;
          }
    }
    
    
  public PeerID createPeerID(net.jxta.peergroup.PeerGroupID peerGroupID,String clearTextID, String function){

        log.append("Creatig pipe ID = peerGroupID:"+peerGroupID+", clearText:'"+clearTextID+"' , function:'"+function+"'");

        byte[] digest = generateHash(clearTextID, function);

        return (PeerID) net.jxta.id.IDFactory.newPeerID(peerGroupID, digest );
  }
  
  
  public net.jxta.peergroup.PeerGroupID createPeerGroupID(net.jxta.peergroup.PeerGroupID parentPeerGroupID,String
      clearTextID, String function){
      
      log.info("Creatig pipe ID = peerGroupID:"+parentPeerGroupID+", clearText:'"+clearTextID+"' ,function:'"+function+"'");

      byte[] digest = generateHash(clearTextID, function);
      
      net.jxta.impl.id.UUID.PeerGroupID pg = (net.jxta.impl.id.UUID.PeerGroupID)parentPeerGroupID;
      net.jxta.peergroup.PeerGroupID peerGroupID = IDFactory.newPeerGroupID( parentPeerGroupID, digest );
      net.jxta.peergroup.PeerGroupID peerGroupID = new net.jxta.impl.id.UUID.PeerGroupID((net.jxta.impl.id.UUID.PeerGroupID)parentPeerGroupID,digest);

      return peerGroupID;
    }
}
