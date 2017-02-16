/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package SHA1;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Public class, used for creating SHA1 Digests used in signatures
 * Collaborates with RSAController and ECCController
 * @author Mark Culhane, 22471634
 */
public class Digest {
    //instance variables
    private String hashValue;
    
    /**    
     * Digest Constructor
     * @param String filePath path to file to be signed/verified
     */
    public Digest(String fileContents) throws NoSuchAlgorithmException, 
            UnsupportedEncodingException {
        this.hashValue = shaHash(fileContents);
    }
    
     /**    
     * Hasher
     * @param String fileContents
     * @return String hashValue
     */
    public static String shaHash(String text) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        
        return convertToHex(sha1hash);
    } 
    
    
     /**   
     * convertToHex
     * @param byte[] byte array to be hashed
     * @return String hashValue
     */
    private static String convertToHex(byte[] data) { 
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
    
    /**    
     * getHasValue
     * @return String hashValue String of hash
     */
    public String getHashValue() {
        return hashValue;
    }  
}
