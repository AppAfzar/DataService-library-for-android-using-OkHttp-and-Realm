package appafzar.dataservice.helper;

import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by: Mr. A.Hashemi
 * https://github.com/AppAfzar
 * Website: appafzar.com
 * phone:(+98)912-7500-206
 */
public class Tools {

    public static boolean isNullOrEmpty(View textView) {
        try {
            if (textView instanceof TextView) {
                return ((TextView) textView).getText().toString().trim().length() == 0;
            } else throw new Exception("Undefined view type");
        } catch (Exception ex) {
            Log.e(ex);
            return false;
        }
    }

    public static String nullToZeroLength(String text) {
        return text != null ? text : "";
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (String aChildren : children) {
                    deletedAll = deleteFile(new File(file, aChildren)) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }

    public static boolean isNullOrEmpty(String text) {
        return (text == null || text.trim().length() == 0);
    }

    public static String getMd5OfFile(String filePath) {
        StringBuilder returnVal = new StringBuilder();
        try {
            InputStream input = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            MessageDigest md5Hash = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = input.read(buffer);
                if (numRead > 0) {
                    md5Hash.update(buffer, 0, numRead);
                }
            }
            input.close();

            byte[] md5Bytes = md5Hash.digest();
            for (byte md5Byte : md5Bytes) {
                returnVal.append(Integer.toString((md5Byte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return returnVal.toString().toUpperCase();
    }

    public static String getMd5OfFile(byte[] data) {
        StringBuilder returnVal = new StringBuilder();
        try {
            MessageDigest md5Hash = MessageDigest.getInstance("MD5");

            byte[] md5Bytes = md5Hash.digest(data);
            for (byte md5Byte : md5Bytes) {
                returnVal.append(Integer.toString((md5Byte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return returnVal.toString().toUpperCase();
    }


    public static Integer tryParse(String value, Integer defaultValue) {
        Integer retVal;
        try {
            retVal = Integer.parseInt(value);
        } catch (Exception ex) {
            retVal = defaultValue;
        }
        return retVal;
    }

    public static Integer tryParse(String value) {
        return tryParse(value, null);
    }

    public static String randomString(int len) {
        final String AB = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String timeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(new Date());
    }

}
