/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */

package Random;

import SHA1.Digest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

/**
 * RandomNumber class
 * Collaborates with Prime and ECCController
 * @author Mark Culhane, 22471634
 */
public class RandomNumber {
    
    private BigInteger random;
    private int bitLength;
    
    // Default Constuctor
    public RandomNumber() {
        random = BigInteger.ZERO;
        bitLength = 0;
    }
    
    /**
     * RandomNumber constructor
     * @param int Bit Length of random number to be generated
     */    
    public RandomNumber (int inBitLength) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.bitLength = inBitLength;
        StringBuilder decentRnd = new StringBuilder();
        // My Random Number generator
        //String pageResult;
        Digest rndDigest;
        String sysTime;
        
        //Start with systime milliseconds
        sysTime = String.valueOf(System.currentTimeMillis());
        decentRnd.append(sysTime.substring(10));
        
        //Ask user for random input
        String userInput = JOptionPane.showInputDialog(null, "Enter Some random string!: ","", 1);
        userInput = userInput + String.valueOf(System.currentTimeMillis());
        rndDigest = new Digest(userInput);
        for (int i = 0; i < rndDigest.getHashValue().length(); i++) {
            decentRnd.append((int)rndDigest.getHashValue().charAt(i));
        }
        random = new BigInteger(decentRnd.toString());
        //loop fast PRNG source system memory addresses
        if (random.bitLength() < bitLength) {
            do {
                // Dynamic Page Entropy Concept
                //pageResult = getDynamicPageData("http://en.wikipedia.org/wiki/Special:Random");
                sysTime = String.valueOf(System.currentTimeMillis()).substring(9, 12);
                decentRnd.append(sysTime);
                //The memory address of a randomly sized array 
                int[] rndArray = new int[Integer.parseInt(sysTime)];
                rndDigest = new Digest(rndArray.toString().substring(4));
                for (int i = 0; i < rndDigest.toString().length(); i++) {
                    decentRnd.append((int)(rndDigest.toString().charAt(i)));
                }
                random = new BigInteger(decentRnd.toString());
            } while (random.bitLength() < bitLength);
            //The random num generator may overshoot required bit length, so trim down 16 bitchar by char
        }
        while (random.bitLength() > (bitLength + 6)) {
                random = new BigInteger(random.toString().substring(0, random.toString().length() - 2));
            }
        System.out.println("random cand Final: " + random.toString());
    }

//  **** Concept work for generating random numbers ******
//    private static String getDynamicPageData(String endpoint) throws MalformedURLException, IOException {
//        String result = null;
//    // Send a GET request to the servlet
//                    // Send data
//                    String urlStr = endpoint;
//                    URL url = new URL(urlStr);
//                    URLConnection conn = url.openConnection ();
//
//                    // Get the response
//                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = rd.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    rd.close();
//                    result = sb.toString();
//        System.out.println("GET result: " + result);
//        return result;
//    }

    /**
     * Get the random number
     * @return BigInteger random number
     */
    public BigInteger getRandom() {
        return random;
    }
}
