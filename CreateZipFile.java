
package mirar;

//-----------------------------------------------------------------------------
//CreateZipFile.java
//-----------------------------------------------------------------------------

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class CreateZipFile {

 private static void doCreateZipFile(String[] files) {

     String zipFileName = files[0];
     byte[] buf = new byte[1024];

     try {

         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));

         System.out.println("Archive:  " + zipFileName);

         // Compress the files
         for (int i=1; i<files.length; i++) {

             FileInputStream in = new FileInputStream(files[i]);
             System.out.println("  adding: " + files[i]);

             out.putNextEntry(new ZipEntry(files[i]));

             // Transfer bytes from the file to the ZIP file
             int len;
             while((len = in.read(buf)) > 0) {
                 out.write(buf, 0, len);
             }

             // Complete the entry
             out.closeEntry();
             in.close();
         }

         // Complete the ZIP file
         out.close();


     } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
     }

 }
 
}

