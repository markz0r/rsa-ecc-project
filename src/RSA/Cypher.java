/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package RSA;

import java.math.BigInteger;

/**
 * Public class, used for encrypting ascii decimal message
 * Collaborates with RSA Controller    
 * @author Mark Culhane, 22471634
 */
public class Cypher {
    //instance variables
    private String rsaCypher;
    private String decMessage;
    private BigInteger key;
    private BigInteger nMod;
    
     /*    
     * Cypher Constructor
     * @param String decmessage to be Encrypted
     * @param BigInteger key to be used in rsa operations
     * @param BigInteger nMod is the n part of key
     */
    public Cypher (String decMessage, BigInteger key, BigInteger nMod){
        this.decMessage = decMessage;
        this.key = key;
        this.nMod = nMod;
        this.rsaCypher = rsaEncrypt();
    }
    
    /*
     * Alternate constructor
     */
    public Cypher () {
        this.decMessage = "";
        this.key = BigInteger.ZERO;
        this.nMod = BigInteger.ZERO;
        this.rsaCypher = ""; 
    };
    
    /**
     * rsaEncrypt - completes m^e * mod n
     * The message is assumed to be in valid format, see Message class
     * Segments are used to ensure message value does not exceed modulus
     * @return String rsaOutput
    */
    private String rsaEncrypt() {
        int segmentSize = Integer.parseInt(String.valueOf(decMessage.charAt(0))
                + String.valueOf(decMessage.charAt(1))
                + String.valueOf(decMessage.charAt(2)));
        segmentSize = (segmentSize - 100) * 3;//digits per seg

        
        int numOfSegments = Integer.parseInt(String.valueOf(decMessage.charAt(3))
                + String.valueOf(decMessage.charAt(4))
                + String.valueOf(decMessage.charAt(5)));


        StringBuilder sb = new StringBuilder();
        sb.append(decMessage.substring(0, 6));

        int charIndex = 6;
        BigInteger workingSegment;

        for (int i = 0; i < numOfSegments; i++) {
            if ((decMessage.substring(charIndex).length()) > segmentSize)
                {workingSegment = new BigInteger(decMessage.substring(charIndex, (charIndex + segmentSize)));}
            else
                {workingSegment = new BigInteger(decMessage.substring(charIndex));}

            sb.append(workingSegment.modPow(key, nMod));
            sb.append(">:D");
            charIndex = charIndex + segmentSize;
        }              
        return sb.toString();
    }
    

    /**
     * rsaDecrypt - completes m^d * mod n
     * The message is assumed to be in valid format, see Message class
     * @return String rsaOutput
    */
    private String rsaDecrypt() {
        StringBuilder sb = new StringBuilder();
        sb.append(rsaCypher.substring(0, 6));
        int charIndex = 6;
        for (int i = 6; i < rsaCypher.length(); i++) {
            //check to see if segment end
            if (rsaCypher.charAt(i) == '>'){
                System.out.println("Segement to be Decrypted: " + rsaCypher.substring(charIndex, (i)));
                BigInteger workingSegment = new BigInteger
                        (rsaCypher.substring(charIndex, (i)));
                sb.append(workingSegment.modPow(key, nMod));
                i = i + 2;
                charIndex = i + 1;
            }
        }
        return sb.toString();
    }
       
    /**
     * Get the output of the RSA operation
     * @return String rsaCypher
     */
    public String getRSACypher() {
        return rsaCypher;
    }
    
    /**
     * Set the rsa Cypher
     * @param String rsaCypher
     */
    public void setRSACypher(String incomingCypher) {
        rsaCypher = incomingCypher;
        decMessage = rsaDecrypt();
    }

    
    /**
     * Set the rsa operation key
     * @param String rsa pub or private key
     * @param String rsa mod key
     */
    public void setRSAKey(BigInteger pubprivKey, BigInteger Modulus) {
        this.key = pubprivKey;
        this.nMod = Modulus;
    }

    
    /**
     * Get the output of the RSA operation
     * @return String rsaResult
     */
    public String getRSADecMessage() {
        return decMessage;
    }
    
}
