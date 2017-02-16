/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package ECC;

import SHA1.Digest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * ECCController manages objects needed for ECC
 * collaborates with EllipticCurve, Random Number, Prime and Message
 * @author Mark Culhane
 */
public class ECCController {
    private EllipticCurve curve;

    //instance variables
    
    public ECCController() {
        curve = new EllipticCurve();
    }

    public ECCController(int bitLength) throws IOException, NoSuchAlgorithmException {
        curve = new EllipticCurve(32);
    }
    
    public BigInteger getA() {
        return curve.getA();
    }
    
    public void setA(BigInteger inA) {
        curve.setA(inA);
    }

    public BigInteger getB() {
        return curve.getB();
    }
    
    public void setB(BigInteger inB) {
        curve.setB(inB);
    }

    public BigInteger getgX() {
        return curve.getgX();
    }

    public void setgX(BigInteger gX) {
        curve.setgX(gX);
    }
    
    public BigInteger getgY1() {
        return curve.getgY1();
    }

    public void setgY(BigInteger gY) {
        curve.setgY1(gY);
    }
    
    public BigInteger getgY2() {
        return curve.getgY2();
    }
    
    public void setgY2(BigInteger gY2) {
        curve.setgY2(gY2);
    }
    
    public BigInteger getP() {
        return curve.getP();
    }
    
    public void setP(BigInteger p) {
        curve.setP(p);
    }
    
    public BigInteger getAlicePrivKey() {
        return curve.getAlicePrivKey();
    }

    public void setAlicePriveKey(BigInteger apk) {
        curve.setAlicePrivKey(apk);
    }
    
    public ArrayList getAlicePubKey() {
        return curve.getAlicePubKey();
    }

    public BigInteger getBobPrivKey() {
        return curve.getBobPrivKey();
    }

    public void setBobPriveKey(BigInteger bpk) {
        curve.setBobPrivKey(bpk);
    }
    
    public ArrayList getBobPubKey() {
        return curve.getBobPubKey();
    }
    
     /**
     * Hash function - uses SHA1
     * @param fileContents String
     * @return SHA1Digest String output of SHA1 Library
     */
    public String getDigest(String fileContents) throws NoSuchAlgorithmException, 
            UnsupportedEncodingException {
        Digest shaHash = new Digest(fileContents);
        return shaHash.getHashValue();
    }
    
     /**
     * eccOpteration encrypt/decrypt/sign/verify
     * TODO: this method should simply create required object and call methods
     *       at present this method is to heavy.
     * @param opType char advises which action to perform
     * @return String eccOutput String
     */
    public String eccOperation(String incomingMessage, char opType, int sender) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //Creates an rsa decMessage Cypher based on current Message object
        String returnVal = "";
        if (opType == 'e')
            {             
                //TODO parameter for Alice and Bob
                ArrayList aliceTempMessagePoint = curve.getAliceTempMessagePoint();
                Message eccMessage = new Message(incomingMessage, aliceTempMessagePoint, curve.getCypherPoint(aliceTempMessagePoint), curve.getAlicePubKey());
                return eccMessage.getDecMessage();
        }
        else if (opType == 's') {
                ArrayList aliceTempMessagePoint = curve.getAliceTempMessagePoint();
                Message eccMessage = new Message(incomingMessage, aliceTempMessagePoint, curve.getCypherPoint(aliceTempMessagePoint), curve.getAlicePubKey());
                return eccMessage.getDecMessage();
        }
        else if (opType == 'd') {
            //get decrypt point
            String cryptX = "";
            String cryptY = "";
            int i = 4;
            while (incomingMessage.charAt(i) != ','){
                cryptX = cryptX + incomingMessage.charAt(i);
                i++;
            }
            i++; //skip ','
            while (incomingMessage.charAt(i) != ')'){
                cryptY = cryptY + incomingMessage.charAt(i);
                i++;
            }
            BigInteger cX = new BigInteger(cryptX);
            BigInteger cY = new BigInteger(cryptY);
            ArrayList tempPoint = new ArrayList(2);
            tempPoint.add(0,cX);
            tempPoint.add(1,cY);
            tempPoint = curve.getDecryptPoint(tempPoint);
            BigInteger messagePointVal = new BigInteger(tempPoint.get(0).toString() + tempPoint.get(1).toString());
            System.out.println("DECRYPT POINT: ("+ tempPoint.get(0) + " , " + tempPoint.get(1) + " )");
            Message eccMessage = new Message(incomingMessage, messagePointVal);
            eccMessage.revertDecMessage();
            return eccMessage.getStringMessage();
        }
        else if (opType == 'v') {
            
        }
        else {
            returnVal = "Error";
        }
        return returnVal;
    }
    
    public String pPlusQ (ArrayList p, ArrayList q) {
        return curve.pPlusQ(p, q).toString();
    }
    
    public String nTimesP (ArrayList p, String n) {
        BigInteger temp = new BigInteger(n);
        //temp = temp.subtract(BigInteger.ONE);
        return curve.nTimesP(temp, p).toString();
    }
}
