package alok.cltplayer.Utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {

	public static void fileXor(File sourceFile, File destinationFile) throws IOException {

        FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(destinationFile);
		
		byte[] buffer = new byte[4096];
		int noOfBytes;

		if((noOfBytes = fis.read(buffer)) != -1) {
			for(int i = 0; i < noOfBytes; i++) buffer[i] ^= 255;
			fos.write(buffer, 0, noOfBytes);
			while((noOfBytes = fis.read(buffer)) != -1) fos.write(buffer, 0, noOfBytes);
		}

		fis.close();
		fos.close();
	}

    public static String getFileName(File f){
        return f.getName();
    }

    public static File getRoot(){
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CLT");
    }
}