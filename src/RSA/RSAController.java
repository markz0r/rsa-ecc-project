/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package RSA;

import SHA1.Digest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * RSAController manages objects needed for RSA encryption
 * collaborates with Key, Cypher, Prime, Message
 * @author Mark Culhane, 22471634
 */
public class RSAController {

    //instance variables
    private Key rsaKey; // only 1 key in memory
    private Message rsaMessage; //only 1 message in memory
    private Cypher rsaCypher;
    
    /**
     * RSAController constructor, creates a new RSAController
     * @param int userBitLength is the bitLength selected by the user
     */
    public RSAController(int userBitLength) {
        rsaKey = new Key(userBitLength / 2);
        System.out.println("-----BEGIN RSA PUBLIC KEY-----");
        System.out.println(rsaKey.getPublicKey());
        System.out.println("-----END RSA PUBLIC KEY-----");
        System.out.println("****************************");
        System.out.println("-----BEGIN RSA PRIVATE KEY-----");
        System.out.println(rsaKey.getPrivateKey());
        System.out.println("-----END RSA PRIVATE KEY-----");
        System.out.println("****************************");
        System.out.println("-----BEGIN RSA KEY MODULUS-----");
        System.out.println(rsaKey.getModulus());
        System.out.println("-----END RSA KEY MODULUS-----"); 
        System.out.println("****************************");
        System.out.println("BitLength of modulus: " + rsaKey.getModulus().bitLength());
    }
    
    /**
     * Incoming Key RSAController constructor
     * @param String privKey
     * @param String pubKey
     * @param String nMod
     */
    public RSAController(String privKey, String pubKey, String nMod) {
        rsaKey = new Key(); //using null constructor
        BigInteger keyMod = new BigInteger(nMod);
        rsaKey.setKeyMod(keyMod);
        BigInteger inPubKey = new BigInteger(pubKey);
        rsaKey.setPublicKey(inPubKey);
        BigInteger inPrivKey = new BigInteger(privKey);
        rsaKey.setPrivateKey(inPrivKey);
        
    }
    
     /**
     * Get the BigInt val of public Key, e
     * @return BigInteger the public key, e
     */
    public BigInteger getPublicKey() {
        return rsaKey.getPublicKey();
    }

     /**
     * Get the BigInt val of private Key, d
     * @return BigInteger the private key, d
     */
    public BigInteger getPrivateKey() {
        return rsaKey.getPrivateKey();
    }
    
     /**
     * Get the BigInt val of key mod, n
     * @return BigInteger the key mod, n
     */
    public BigInteger getModulus() {
        return rsaKey.getModulus();
    }
    
     /**
     * Hash function - uses SHA1
     * @param string of Read File
     * @return SHA1Digest
     */
    public String getDigest(String fileContents) throws NoSuchAlgorithmException, 
            UnsupportedEncodingException {
        Digest shaHash = new Digest(fileContents);
        return shaHash.getHashValue();
    }
    

    
     /**
     * Encrypt / Verify a message uses public key and modulus
     * @param char useKey advises which key to use in m^e/modn
     * @return String rsaOutput
     */
    public String rsaOperation(String incomingMessage, char useKey) {
        //Creates an rsa decMessage Cypher based on current Message object
        String returnVal;
        if (useKey == 'e')
            {
                // Input will be user message or has value
                rsaMessage = new Message(incomingMessage, rsaKey.getModulus().toString().length());
                rsaCypher = new Cypher(rsaMessage.getDecMessage(), 
                rsaKey.getPublicKey(), rsaKey.getModulus());
                
                returnVal = rsaCypher.getRSACypher();
        }
        else if (useKey == 's') {
                // Input will be user message or has value
                rsaMessage = new Message(incomingMessage, rsaKey.getModulus().toString().length());
                rsaCypher = new Cypher(rsaMessage.getDecMessage(), 
                rsaKey.getPrivateKey(), rsaKey.getModulus());
                
                returnVal = rsaCypher.getRSACypher();
        }
        else if (useKey == 'd') {
                //Input will be user entered cyphertext
                rsaCypher = new Cypher();
                rsaMessage = new Message(); //uses alternate construcutor
                rsaCypher.setRSAKey(rsaKey.getPrivateKey(), rsaKey.getModulus());
                rsaCypher.setRSACypher(incomingMessage); //Will Complete decypt too
                rsaMessage.setDecMessage(rsaCypher.getRSADecMessage());
                
                rsaMessage.revertDecMessage();
                returnVal = rsaMessage.getStringMessage();
        }
        else if (useKey == 'v') {
                //Input will be user entered cyphertext
                rsaCypher = new Cypher();
                rsaMessage = new Message(); //uses alternate construcutor
                rsaCypher.setRSAKey(rsaKey.getPublicKey(), rsaKey.getModulus());
                rsaCypher.setRSACypher(incomingMessage); //Will Complete decypt too
                rsaMessage.setDecMessage(rsaCypher.getRSADecMessage());
                
                rsaMessage.revertDecMessage();
                returnVal = rsaMessage.getStringMessage();
        }
        else {
            returnVal = "Error";
        }
        return returnVal;
    }
   
}