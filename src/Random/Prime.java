/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 * Tool for confirming prime numbers: http://www.alpertron.com.ar/ECM.HTM
 */
package Random;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Prime class, used for generating 1024-bit primes needed in RSA and ECC
 * Collaborates with PublicKey    
 * @author Mark Culhane, 22471634
 */
public class Prime {
    //instance variables
    private BigInteger primeCandidate;
    private BigInteger primeNum;
    private int bitLength;

    //default constructor
    public Prime() {
        primeCandidate = BigInteger.ZERO;
        primeNum = BigInteger.ZERO;
        bitLength = 0;
    }
   
    
    /**    
     * RandomPrime Constructor
     * @param bitLength the bit length of prime
     */
    public Prime(int bitLength) throws IOException, NoSuchAlgorithmException {
        this.bitLength = bitLength; 
        RandomNumber randomInstance = new RandomNumber(this.bitLength);
        primeCandidate = randomInstance.getRandom();
        
        // Faster Library -> Random rnGen = new SecureRandom();
        // Faster Library -> primeNum = BigInteger.probablePrime(bitLength, rnGen);
        //System.out.println("Prime: " + primeNum);
        //System.out.println("Incoming Random: " + primeNum);
        if (!primeCandidate.testBit(0))
            primeCandidate = primeCandidate.flipBit(0);     
        int bitFlipCnt = 1;
        while (!millerRabin()) {                    
            //System.out.println("This is loop number: " + loopCount);
            //System.out.println("Candidate: " + primeNum);
            //See BigInteger source code for explination of optimized increment
            //primeNum = primeNum.nextProbablePrime();
            //bit walker!
            primeCandidate = primeCandidate.flipBit(bitFlipCnt);
            bitFlipCnt++;
            if (bitFlipCnt >= primeCandidate.bitLength())
                bitFlipCnt = 1;
        }
        primeNum = primeCandidate;
    } 
      
    /**    
     * millerRabin probable prime test, can be removed after assesment
     * as this is implemented in BigInteger.probablePrime
     */
    private boolean millerRabin() {
        boolean isPrime = true;
        int iterations = 8;
        BigInteger b;
        Random rndSrc;
        //Generate random num, b such that b[1,n-1] 
        for (int i = 0; i < iterations; i++){
            rndSrc = new SecureRandom();
            b = new BigInteger(this.bitLength, rndSrc);
            while (b.compareTo(primeCandidate) != -1){
                b = new BigInteger(this.bitLength, rndSrc);
            }
            //Find q and the odd number k such that n - 1 = (2^q)k
            BigInteger q = BigInteger.ONE;
            //define n - 1
            BigInteger nOne = primeCandidate.subtract(BigInteger.ONE); 
            //while the modulus of (n-1)/(2^q) = 0 (k is even)
            BigInteger k = nOne.divide(pow(q));
            long twoTemp = 2;
            while ((k.mod(BigInteger.valueOf(twoTemp)).compareTo(BigInteger.ZERO)) != 1){
                q = q.add(BigInteger.ONE);
                k = nOne.divide(pow(q));
            }
            k = nOne.divide(pow(q));          
           
            //Test 1: if (b^k)mod n = 1
            if (b.modPow(k, primeCandidate).compareTo(BigInteger.ONE) != 0) {
                isPrime = false;

                //Test 2: if there is an i[0,(q-1)] such that
                //b^(k*(2^i)) mod n = n - 1
                for (BigInteger iCount = BigInteger.ZERO; iCount.compareTo(q) < 0; iCount = iCount.add(BigInteger.ONE)) {
                    BigInteger twoPowi = pow(iCount);
                    BigInteger kTwoi = k.multiply(twoPowi);
                    if ( nOne.compareTo(b.modPow(kTwoi, primeCandidate)) == 0) {
                        //System.out.println("But Passed: b^(k*(2^i)) mod n = n - 1");
                        isPrime = true;
                    }
                }
            }
            //If both conditions fail on any iteration then return false
            if (isPrime == false) {    
                return isPrime;
            }    
        }
        //If complete failure is nerver found then return prime probability
        System.out.println("Prime: " + primeCandidate);
        System.out.println("BitLength: " + primeCandidate.bitLength());
        return isPrime;    
    }
    
    /**
     * pow raises 2 to the power of a BigInt
     * This code is modified from Mike Simmons post at:
     * http://www.coderanch.com/t/417341/java/java/BigInteger-Raised-power-BigInteger
     * @param BigInteger 2^x
     * @return BigInteger result
     */
    private static BigInteger pow(BigInteger y) {  
        long xTemp = 2;
        BigInteger x = BigInteger.valueOf(xTemp); 
        if (y.compareTo(BigInteger.ZERO) < 0)  
        throw new IllegalArgumentException();  
            BigInteger z = x; // z will successively become x^2, x^4, x^8, x^16, x^32...  
            BigInteger result = BigInteger.ONE;  
            byte[] bytes = y.toByteArray();  
            for (int i = bytes.length - 1; i >= 0; i--) {  
                byte bits = bytes[i];  
                for (int j = 0; j < 8; j++) {  
                    if ((bits & 1) != 0)  
                        result = result.multiply(z);  
                // short cut out if there are no more bits to handle:  
                if ((bits >>= 1) == 0 && i == 0)  
                    return result;  
                z = z.multiply(z);  
                }  
            }  
        return result;  
    }    
    
    /**
     * Get the BigInt val of Prime object
     * @return BigInteger the numeric value of prime
     */
    public BigInteger getPrime() {
        return primeNum;
    } 
    
}