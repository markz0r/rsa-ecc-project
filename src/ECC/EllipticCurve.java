/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package ECC;

import Random.Prime;
import Random.RandomNumber;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * EllipticCurve object contains the required values and objects for ecc
 * collaborates with ECCController, Prime, Random
 * @author Mark Culhane
 */

public class EllipticCurve {

    //instance variable
    //TODO clean up point storage
    private BigInteger p;
    private BigInteger a;
    private BigInteger b;
    private ArrayList generatorPoint;
    private ArrayList aliceBaseMessagePoint;
    private BigInteger messRand;
    private BigInteger gX;
    private BigInteger gY1;
    private BigInteger gY2;
    private BigInteger alicePrivKey;
    private ArrayList alicePubKey;
    private BigInteger bobPrivKey;
    private ArrayList bobPubKey;
            
    //Default contructor
    public EllipticCurve() {
        p = new Prime().getPrime();
        a = new RandomNumber().getRandom();
        b = new RandomNumber().getRandom();
        gX = BigInteger.ZERO;
        gY1 = BigInteger.ZERO;
        gY2 = BigInteger.ZERO;
    }
    
    /**
     * Elliptic Curve constructor
     * @param bitLength int is the bitLength selected by the user (currently locked to 32 bit)
     */
    public EllipticCurve(int bitLength) throws IOException, NoSuchAlgorithmException {
        //this.p = new Prime(24);
        this.p = new Prime(bitLength).getPrime();
    
        do {
                //TODO: Investigate what the optimal lenght of A and B are
                //      Smaller A and B values will reduce search time for square from x^3 + Ax^2 + B mop p
                a = new RandomNumber(bitLength/8).getRandom();
                b = new RandomNumber(bitLength/8).getRandom();

            //while (4a^3 + 27b^2) % p == 0
        } while ((((((a.multiply(BigInteger.valueOf((long)4))).pow(3)).add(((b.multiply(
                BigInteger.valueOf((long)27))).pow(2)))).mod(p)).compareTo(BigInteger.ZERO)) == 0);
        
        System.out.println("P: " + p.toString());
        System.out.println("A: " + a.toString());
        System.out.println("B: " + b.toString());

        gX = BigInteger.ZERO;
        
        //find a generator point which is on the finite prime curve
        //looking for easy Ys
        do {
            gX = gX.add(BigInteger.ONE);
            gY1 = findPoint(gX); //only picking some Ys where perfect squre exists
        } while ((gY1.compareTo(BigInteger.ZERO) == 0) && (gX.compareTo(p)< 0));
        gY2 = p.subtract(gY1);
        
        generatorPoint = new ArrayList(2);
        generatorPoint.add(0,gX);
        generatorPoint.add(1,gY2);
        
        System.out.println("CALCULATING MESSAGE POINT");
        BigInteger newX = gX;
        BigInteger newY = BigInteger.ZERO;
        BigInteger newY2 = BigInteger.ZERO;
        newX = newX.add(BigInteger.ONE);
        do {
            newY = findPoint(newX); //only picking some Ys where perfect squre exists
            newX = newX.add(BigInteger.ONE);
        } while ((newY.compareTo(BigInteger.ZERO) == 0) && (newX.compareTo(p)< 0));
        newY2 = p.subtract(gY1);        
        aliceBaseMessagePoint = new ArrayList(2);
        aliceBaseMessagePoint.add(0, newX);
        aliceBaseMessagePoint.add(1, newY2);
        System.out.println("Massage Point: ( " + newX.toString()+ " , " + newY2.toString() + " )");

        
        //Generate Private Keys, [0 - p -1]
        RandomNumber tempRand1 = new RandomNumber(bitLength);
        RandomNumber tempRand2 = new RandomNumber(bitLength);
        while ((tempRand1.getRandom().compareTo(BigInteger.valueOf((long)2)) < 1) || (tempRand2.getRandom().compareTo(BigInteger.valueOf((long)2))) < 1) {
                tempRand1 = new RandomNumber(bitLength - 1);
                tempRand2 = new RandomNumber(bitLength - 1);
            }
        alicePrivKey = tempRand1.getRandom();
        bobPrivKey = tempRand2.getRandom();        
        

        
        //Generate Pup Keys
        alicePubKey = nTimesP(alicePrivKey, generatorPoint);
        System.out.println("alicePubKeySuccess");
        System.out.println("alicePubKeySuccess");
        bobPubKey = nTimesP(bobPrivKey, generatorPoint);
        System.out.println("bobPubKeySuccess");
    }       
    
    /**
     * findPoint, Compute Y from X, skipping through until x^3 + ax + b = a square number
     * @param xVal BigInteger X coordinate
     */
    private BigInteger findPoint(BigInteger xVal) {       
        //solvingTemp = x^3 + ax + b
        BigInteger solvingTemp = (xVal.pow(3).add(xVal.multiply(a)).add(b)).mod(p);
        if (solvingTemp.compareTo(BigInteger.ZERO) == 0)
            return BigInteger.ZERO;

        SquareRoot sqrt = new SquareRoot(solvingTemp);
        if (sqrt.getError().compareTo(BigDecimal.valueOf((long)0.000)) == 0) {
            System.out.println("Wow square resultant after X =: " + xVal.toString() + " result = " + solvingTemp.toString());
            sqrt = new SquareRoot(sqrt.getSqrt().multiply(sqrt.getSqrt()).toBigInteger());
            return sqrt.getSqrt().toBigInteger();
        } else {
            solvingTemp = BigInteger.ZERO;
        }
        return solvingTemp;
    }
    
// ******************* Discrete Logarithmic Problem: testing 1G, 2G, 3G, 6G is very slow ***************************
// Looping through inverse mod and big int square roots is a problem
    /**
     * Compute Y from X
     * @param BigInteger Y coordinate
     */
//    private BigInteger computeHardY(BigInteger xVal) {       
//        //solvingTemp = x^3 + ax + b
//        BigInteger solvingTemp = (xVal.pow(3).add(xVal.multiply(a.getRandom())).add(b.getRandom())).mod(p);
//        SquareRoot sqrt = new SquareRoot(solvingTemp);
//        if (sqrt.getError().compareTo(BigDecimal.valueOf((long)0.000)) == 0) {
//            System.out.println("Wow perfect square after X =: " + xVal.toString() + " result = " + solvingTemp.toString());
//            sqrt = new SquareRoot(sqrt.getSqrt().multiply(sqrt.getSqrt()).toBigInteger());
//            return sqrt.getSqrt().toBigInteger();
//        } else {
//            //Finds and valid X values in E mod p group, counts from 1 to p unless b and a are very high.
//            BigInteger step = new BigInteger("1");
//            BigInteger loopTemp;
//            do {
//                   loopTemp = (p.multiply(step)).add(solvingTemp);
//                   sqrt = new SquareRoot(loopTemp);
//                   if (sqrt.getError().compareTo(BigDecimal.valueOf((long)0.000)) == 0) {
//                       return sqrt.getSqrt().toBigInteger();
//                   }
//                   step = step.add(BigInteger.ONE);
//            } while (loopTemp.compareTo(p.multiply(p)) < 0); //if we have gone passed the possible bounds
//            solvingTemp = BigInteger.ZERO;
//        }
//        return solvingTemp;
//    }
// ******************* Discrete Logarithmic Problem: testing 1G, 2G, 3G, 6G is very slow ***************************
    
    
    /**
     * nP  ECC coordinate addition
     * Bug found in final testing, this method needs a re-write
     * @param BigInteger n in [P + P ... P] 
     * @return BigInteger[] contains [Xr, Yr]
     */
    public ArrayList nTimesP (BigInteger n, ArrayList point) {
        if (n.compareTo(BigInteger.ONE) == 0)
            return pPlusP(point);
            
        BigInteger remainingNP = n;

        //Establish multiples of P in memory
        ArrayList twoP = pPlusP(point);
        ArrayList fourP = pPlusP(twoP);
        ArrayList sixteenP = pPlusP(fourP);
        ArrayList twoFiveSixP = pPlusP(sixteenP);
        ArrayList sixtyThousandP = pPlusP(twoFiveSixP);
        
        ArrayList workingP = twoP;
        
        BigInteger two = new BigInteger("2");
        BigInteger four = new BigInteger("4");
        BigInteger sixteen = new BigInteger("16");
        BigInteger twoFiveSix = new BigInteger("256");
        BigInteger sixtyThousand = new BigInteger("65536");
        // !! Error were found in recursive method, this is 15 minute bandaid
        do {
        if (remainingNP.compareTo(sixtyThousand) > 0) {
            workingP = pPlusQ(workingP, sixtyThousandP);
            remainingNP = remainingNP.subtract(sixtyThousand);
        }

        if (remainingNP.compareTo(twoFiveSix) > 0) {
            workingP = pPlusQ(workingP, twoFiveSixP);
            remainingNP = remainingNP.subtract(twoFiveSix);
        }

        if (remainingNP.compareTo(sixteen) > 0) {
            workingP = pPlusQ(workingP, sixteenP);
            remainingNP = remainingNP.subtract(sixteen);
        }

        if (remainingNP.compareTo(four) > 0) {
            workingP = pPlusQ(workingP, fourP);
            remainingNP = remainingNP.subtract(four);
        }
            
        if (remainingNP.compareTo(two) > 0) {
            workingP = pPlusQ(workingP, twoP);
            remainingNP = remainingNP.subtract(two);
        }
        } while (remainingNP.compareTo(BigInteger.ONE) > 0);
        
        if (remainingNP.compareTo(BigInteger.ONE) == 0)
            System.out.println("Hellow!");
            System.out.println("Working P: " + workingP.toString());
            workingP = pPlusQ(workingP, point);

        return workingP;
    }
  
    /**
     * P + P ECC coordinate addition
     * TODO: This needs to be fast as it is used repeatedly
     * @param ArrayList point (BigInteger Px, BigInteger Py)
     * @return ArrayList contains [Xr, Yr]
     */
    public ArrayList pPlusP (ArrayList point) {
        ArrayList twoP = new ArrayList(2);
        BigInteger Xr = BigInteger.ZERO;
        BigInteger Yr = BigInteger.ZERO;
        BigInteger Xw = new BigInteger(point.get(0).toString());
        BigInteger Yw = new BigInteger(point.get(1).toString());
 
        //first calc ((3Xp^2) + a)           
        BigInteger sTemp = (((Xw.pow(2)).multiply(BigInteger.valueOf((long)3))).add(a));
       //now  Inverse of (2(Yp)) mod p
        BigInteger sTemp2 =  (Yw.multiply(BigInteger.valueOf((long)2))).modInverse(p);
        // Finally s = ((3Xp^2) + a) * 2Yp inverseMod p
        sTemp = sTemp.multiply(sTemp2);

        //Xr = s^2 - (2Xp)
        sTemp2 = Xw.multiply(BigInteger.valueOf((long)2));
        Xr = (sTemp.pow(2)).subtract(sTemp2);
        Xr = Xr.mod(p); 

        //Yr  = -Yp + s(Xp- Xr) mod p
        sTemp2 = Xw.subtract(Xr);
        Yr = sTemp.multiply(sTemp2);
        Yr = Yr.subtract(Yw);
        Yr = Yr.mod(p);
        
        twoP.add(0,Xr);
        twoP.add(1,Yr);
        
        return twoP;
    }
    
    /**
     * P + Q ECC coordinate addition
     * TODO: This needs to be fast as it is used repeatedly
     * @param ArrayList pointP (BigIntger Px, BigInteger PQy)
     * @param ArrayList pointQ (BigIntger Qx, BigInteger Qy)
     * @return ArrayList pointR (BigIntger Rx, BigInteger Ry)
     */
    public ArrayList pPlusQ (ArrayList pointP, ArrayList pointQ) {

        ArrayList pQ = new ArrayList(2);
        BigInteger Xr = BigInteger.ZERO;
        BigInteger Yr = BigInteger.ZERO;
        BigInteger Xp = new BigInteger(pointP.get(0).toString());
        BigInteger Yp = new BigInteger(pointP.get(1).toString());
        BigInteger Xq = new BigInteger(pointQ.get(0).toString());
        BigInteger Yq = new BigInteger(pointQ.get(1).toString());
        
        if (Yp.compareTo(Yq) == 0)
            return pPlusP(pointP);
        
        // s = (yP - yQ)/(xP - xQ) mod p, s is the slope of the line through P and Q.        
        BigInteger sTemp = Yp.subtract(Yq);
        BigInteger sTemp2 =  Xp.subtract(Xq);
        sTemp2 = sTemp2.modInverse(p);
        sTemp = sTemp.multiply(sTemp2);

        // xL = s^2 - xP - xQ mod p
        Xr = sTemp.pow(2);
        Xr = Xr.subtract(Xp);
        Xr = Xr.subtract(Xq);
        Xr = Xr.mod(p);
        
        // yL = -yJ + s (xJ - xL) mod p    
        Yr = Xp.subtract(Xr);
        Yr = Yr.multiply(sTemp);
        Yr = Yr.subtract(Yp);
        Yr = Yr.mod(p);

        
        pQ.add(0,Xr);
        pQ.add(1,Yr);
        
        return pQ;
    }
    
    
    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getgX() {
        return gX;
    }

    public BigInteger getgY1() {
        return gY1;
    }

    public BigInteger getgY2() {
        return gY2;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getAlicePrivKey() {
        return alicePrivKey;
    }

    public ArrayList getAlicePubKey() {
        return alicePubKey;
    }

    public BigInteger getBobPrivKey() {
        return bobPrivKey;
    }

    public ArrayList getBobPubKey() {
        return bobPubKey;
    }

    public ArrayList getAliceTempMessagePoint() throws NoSuchAlgorithmException, UnsupportedEncodingException {

            System.out.println("AliceBaseMsgPoint: " + aliceBaseMessagePoint.get(0).toString() + " , " + aliceBaseMessagePoint.get(0).toString());
            Random rndSrc;
            rndSrc = new SecureRandom();
            messRand = new BigInteger(12, rndSrc);
            System.out.println("messageRand: " + messRand.toString());
            ArrayList aliceTempMessagePoint = nTimesP(messRand, aliceBaseMessagePoint);
            System.out.println("Temp Message From SOURCE: " + aliceTempMessagePoint.get(0).toString() + " , " + aliceTempMessagePoint.get(1).toString());
        return aliceTempMessagePoint;
    }  
    
    public ArrayList getCypherPoint(ArrayList messagePoint) {
            //Cypher point = AlicePriv * (BobPub + MessagePoint)
            ArrayList tempPoint  = new ArrayList(2);
            tempPoint = nTimesP(alicePrivKey,bobPubKey);
        return pPlusQ(tempPoint, messagePoint);
    }
    
    public ArrayList getDecryptPoint(ArrayList cryptPoint) {
        //Decrypt point = CypherPoint - (BobRn * AlicPubKey)
        ArrayList bobRndAlicePub = nTimesP(bobPrivKey, alicePubKey);
        //make y value negative, (x,y) - (x,y) ->  (x,y) + (x, -y)
        BigInteger yTemp = new BigInteger(bobRndAlicePub.get(1).toString());
        yTemp = yTemp.negate();
        bobRndAlicePub.remove(1);
        bobRndAlicePub.add(1, yTemp);
        
        return pPlusQ(cryptPoint, bobRndAlicePub);
    }

    public void setA(BigInteger a) {
        this.a = a;
    }

    public void setAliceBaseMessagePoint(ArrayList aliceBaseMessagePoint) {
        this.aliceBaseMessagePoint = aliceBaseMessagePoint;
    }

    public void setAlicePrivKey(BigInteger alicePrivKey) {
        this.alicePrivKey = alicePrivKey;
        if ((alicePrivKey.compareTo(BigInteger.valueOf((long)2))) > 0)
            alicePubKey = nTimesP(alicePrivKey, generatorPoint);
        else
            alicePubKey = pPlusP(generatorPoint);
    }


    public void setB(BigInteger b) {
        this.b = b;
    }

    public void setBobPrivKey(BigInteger bobPrivKey) {
        this.bobPrivKey = bobPrivKey;
        if ((bobPrivKey.compareTo(BigInteger.valueOf((long)2))) > 0)
            bobPubKey = nTimesP(bobPrivKey, generatorPoint);
        else
            bobPubKey = pPlusP(generatorPoint);
    }

    public void setgX(BigInteger gX) {               
        this.gX = gX;
    }

    public void setgY1(BigInteger gY1) {
        this.gY1 = gY1;       
    }

    public void setgY2(BigInteger gY2) {
        this.gY2 = gY2;
        
        if ((this.gX.compareTo(BigInteger.valueOf((long)9999999)) == 1) && 
                (this.gY1.compareTo(BigInteger.valueOf((long)9999999)) == 1)) {
            gX = BigInteger.ZERO;
            do {
                gX = gX.add(BigInteger.ONE);
                gY1 = findPoint(gX); //only picking some Ys where perfect squre exists
            } while ((gY1.compareTo(BigInteger.ZERO) == 0) && (gX.compareTo(p)< 0));
            this.gY2 = p.subtract(gY1);
        }
        
        
        generatorPoint = new ArrayList(2);
        generatorPoint.add(0,gX);
        generatorPoint.add(1,this.gY2);
    }

    public void setGeneratorPoint(ArrayList generatorPoint) {
        this.generatorPoint = generatorPoint;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }
    
    
    
}