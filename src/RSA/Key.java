/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package RSA;

import Random.Prime;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Public class, used for creating Public and Private Key needed in RSA
 * Collaborates with RandomPrime    
 * @author Mark Culhane, 22471634
 */
public class Key {
    //instance variables
    private BigInteger e; //public key
    private BigInteger d; //private key
    private BigInteger n; //modulos

    
     /*    
     * Key Constructor
     * @param int bitLength the bit length of public key
     */
    public Key(int bitLength) {
        try {
            Prime p = new Prime(bitLength);
            Prime q = new Prime(bitLength);
            this.n = p.getPrime().multiply(q.getPrime());
            // e is an integer[1, (p-1*q-1)] that is co-prime with (p-1*q-1)
            //http://introcs.cs.princeton.edu/java/78crypto/  
            // common practice is to use e = 65,537
            // a unqiue d always exists provided gcd(e, (p-1)(q-1)) = 1
            e =  BigInteger.valueOf((long)65537);
            BigInteger phi = p.getPrime().subtract(BigInteger.ONE).multiply(
                    q.getPrime().subtract(BigInteger.ONE));
            d = e.modInverse(phi);
            //TODO  gcd(e,phi) = 1 algoritm for checking co primality
            //TODO  check keys adhere to: http://en.wikipedia.org/wiki/RSA#Key_generation_2
            //TODO  check keys adhere to: http://en.wikipedia.org/wiki/RSA#Key_generation_2
        } catch (IOException ex) {
            Logger.getLogger(Key.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
     /*    
     * Key Default Constructor
     */
    public Key() {
        this.n = BigInteger.ZERO;
        this.e = BigInteger.ZERO;
        this.d = BigInteger.ZERO;
    }
    
    /**
     * Get the BigInt val of public Key, e
     * @return BigInteger the public key, e
     */
    public BigInteger getPublicKey() {
        return e;
    }
    
    /**
     * Set the BigInt val of public Key, e
     * @param BigInteger the public key, e
     */
    public void setPublicKey(BigInteger incomingPubKey) {
        this.e = incomingPubKey;
    }
    
    /**
     * Set the BigInt val of priv Key, d
     * @param BigInteger the priv key, d
     */
    public void setPrivateKey(BigInteger incomingPrivKey) {
        this.d = incomingPrivKey;
    }
    
    /**
     * Set the BigInt val of Key mod, n
     * @param BigInteger the key mod, n
     */
    public void setKeyMod(BigInteger incomingKeyMod) {
        this.n = incomingKeyMod;
    }
    
    /**
     * Get the BigInt val of private key, d
     * @return BigInteger the private key, d
     */
    public BigInteger getPrivateKey() {
        return d;
    }
    
    /**
     * Get the BigInt val of key modulus, n
     * @return BigInteger the key modulus, n
     */
    public BigInteger getModulus() {
        return n;
    }
}
