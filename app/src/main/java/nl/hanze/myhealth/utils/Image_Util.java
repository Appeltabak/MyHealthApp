package nl.hanze.myhealth.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Image_Util {

    public static String imageToBytes(File image) throws IOException {
        byte[] buffer = new byte[4096];
        try {
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            for (int readNum; (readNum = fis.read(buffer)) != -1; ) {
                //Writes to this byte array output stream
                bos.write(buffer, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }

            return bos.toString(); //return the bytes array.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }




}
