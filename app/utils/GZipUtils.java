
/**  
 * 2010-4-13  
 */  
package utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP工具
 * 
 * @author
 * @since 1.0
 */  
public abstract class GZipUtils {   
  
    public static final int BUFFER = 1024;   
    public static final String EXT = ".gz";   
  
    /**
     * 将字符串先压缩再解密
     * @param data 原字符串
     * @return  压缩后的字符串
     */
    public static String encoderStr(String data){
    	String result=null;
    	try {
			//result=Base64.encode(compress(data.getBytes("UTF-8")));
    		result = new BASE64Encoder().encode(compress(data.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    /**
     * 将字符串先解密再解压缩
     * @param data 压缩的字符串
     * @return 解压的字符串
     */
    public static String decoderStr(String data) throws Exception {
    	String result = new String((decompress(new BASE64Decoder().decodeBuffer(data))),"UTF-8");
    	return result;
    }
    
    /**  
     * 数据压缩  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] compress(byte[] data) throws Exception {   
        ByteArrayInputStream bais = new ByteArrayInputStream(data);   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
  
        // 压缩   
        compress(bais, baos);   
  
        byte[] output = baos.toByteArray();   
  
        baos.flush();   
        baos.close();   
  
        bais.close();   
  
        return output;   
    }   
  
    /**  
     * 文件压缩  
     *   
     * @param file  
     * @throws Exception  
     */  
    public static void compress(File file) throws Exception {   
        compress(file, true);   
    }   
  
    /**  
     * 文件压缩  
     *   
     * @param file  
     * @param delete  
     *            是否删除原始文件  
     * @throws Exception  
     */  
    public static void compress(File file, boolean delete) throws Exception {   
        FileInputStream fis = new FileInputStream(file);   
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);   
  
        compress(fis, fos);   
  
        fis.close();   
        fos.flush();   
        fos.close();   
  
        if (delete) {   
            file.delete();   
        }   
    }   
  
    /**  
     * 数据压缩  
     *   
     * @param is  
     * @param os  
     * @throws Exception  
     */  
    public static void compress(InputStream is, OutputStream os)   
            throws Exception {   
  
        GZIPOutputStream gos = new GZIPOutputStream(os);   
  
        int count;   
        byte data[] = new byte[BUFFER];   
        while ((count = is.read(data, 0, BUFFER)) != -1) {   
            gos.write(data, 0, count);   
        }   
  
        gos.finish();   
  
        gos.flush();   
        gos.close();   
    }   
  
    /**  
     * 文件压缩  
     *   
     * @param path  
     * @throws Exception  
     */  
    public static void compress(String path) throws Exception {   
        compress(path, true);   
    }   
  
    /**  
     * 文件压缩  
     *   
     * @param path  
     * @param delete  
     *            是否删除原始文件  
     * @throws Exception  
     */  
    public static void compress(String path, boolean delete) throws Exception {   
        File file = new File(path);   
        compress(file, delete);   
    }   
  
    /**  
     * 数据解压缩  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
  
        // 解压缩   
  
        decompress(bais, baos);   
  
        data = baos.toByteArray();   
  
        baos.flush();   
        baos.close();   
  
        bais.close();   
  
        return data;   
    }   
  
    /**  
     * 文件解压缩  
     *   
     * @param file  
     * @throws Exception  
     */  
    public static void decompress(File file) throws Exception {   
        decompress(file, true);   
    }   
  
    /**  
     * 文件解压缩  
     *   
     * @param file  
     * @param delete  
     *            是否删除原始文件  
     * @throws Exception  
     */  
    public static void decompress(File file, boolean delete) throws Exception {   
        FileInputStream fis = new FileInputStream(file);   
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT,   
                ""));   
        decompress(fis, fos);   
        fis.close();   
        fos.flush();   
        fos.close();   
  
        if (delete) {   
            file.delete();   
        }   
    }   
  
    /**  
     * 数据解压缩  
     *   
     * @param is  
     * @param os  
     * @throws Exception  
     */  
    public static void decompress(InputStream is, OutputStream os)   
            throws Exception {   
  
        GZIPInputStream gis = new GZIPInputStream(is);   
  
        int count;   
        byte data[] = new byte[BUFFER];   
        while ((count = gis.read(data, 0, BUFFER)) != -1) {   
            os.write(data, 0, count);   
        }   
  
        gis.close();   
    }   
  
    /**  
     * 文件解压缩  
     *   
     * @param path  
     * @throws Exception  
     */  
    public static void decompress(String path) throws Exception {   
        decompress(path, true);   
    }   
  
    /**  
     * 文件解压缩  
     *   
     * @param path  
     * @param delete  
     *            是否删除原始文件  
     * @throws Exception  
     */  
    public static void decompress(String path, boolean delete) throws Exception {   
        File file = new File(path);   
        decompress(file, delete);   
    }   
  
    public static void main(String[] args)throws Exception {
	
    	    String path = "D:\\开源软件\\ehcache-2.6.2-distribution\\ehcache-2.6.2\\ehcache.xml";
    	    
    	    FileInputStream read = new FileInputStream(path);
    	    byte[]buff = new byte[read.available()];
    	    read.read(buff);
    	    
    	   
    	    
    	    String str1 = new String(buff);
    	    String str2 = encoderStr(str1);
    	    System.out.println(str1.length());
    	    //System.out.println(str1);
    	    //System.out.println(str2);
    	    System.out.println(str2.length());
    	    String str3 = decoderStr(str2);
    	   // System.out.println(str3);
    	    System.out.println(str3.length());
    
	}
 
}  
