//checkSumCalc

package myPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

//This Class generates CRC-32 Check sum to make sure that files which transfered
//are not corrupted

public class CheckSumCalc {
	private long Result =0;
	
	public static String getFileSum(File filename){
		
		//CRC-32 check sum
		
		try {
			CheckedInputStream cis = new CheckedInputStream(new FileInputStream(filename),new Adler32());
			byte[] tempBuf = new byte[512];
			
			while (cis.read(tempBuf) >= 0);
			Result = cis.getChecksum().getValue();
			
			cis.close();
			
			} catch (FileNotFoundException ex) {

			System.out.println("[-]File not Found :-(");
			ex.printStackTrace();
			
			}catch(IOException e){

			e.printStackTrace();
			}
		return Long.toHexString(Result);
	}
