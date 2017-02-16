/*
 * RSA.ECC Assignment 1, FIT5037, Sem 2, 2011
 * By Mark Culhane, 22471634
 */
package RSA;

/**
 * Public class, used for creating Public and Private Key needed in RSA and ECC
 * Collaborates with RSAController    
 * @author Mark Culhane, 22471634
 */
public class Message {
    //instance variables
    private String messageString;
    private int segmentSize; //How many chars per segment + 100
    private int numberOfSegments;
    private String decMessage; //0-2 digit (segmentSize), 3-5 digit (number of segments)
   
     /*    
     * Message Constructor
     * @param String incomming Message
     * @param int nLength, length of n in dec form
     */
    public Message(String incomingMessage, int nLength) {
        messageString = incomingMessage;
        segmentSize = ((nLength - 1) / 3);
        countSegments();
        decMessage = buildDecMessage();
    }
    
     /*    
     * Message Alternate Constructor
     */
    public Message() {
        decMessage = "";
        segmentSize = 0;
        numberOfSegments = 0;
        messageString = "";
    }
    
    
    /**
     * buildDecMessage encodes string message to BigInteger value using ASCII dec
    */
    public void revertDecMessage() {
        String workingMessage = decMessage.substring(6);
        StringBuilder sb = new StringBuilder();
        int holder;
        for (int i = 0; (i + 3) <= workingMessage.length(); i = i + 3) {
            holder = Integer.parseInt(workingMessage.substring(i, i + 3));
            holder = holder - 100;
            sb.append(((char) holder));
        }
        messageString = sb.toString();
    }

    /**
     * buildDecMessage encodes string message to BigInteger value using ASCII dec
     * @return String decimalized encoded, segmented message
    */
    private String buildDecMessage() {
        StringBuilder sb = new StringBuilder();
        //ensure first 3 digits indicate segmentSize
        sb.append( segmentSize + 100); 
        
        //ensure digits 3 - 5 indicates number of segments
        if (numberOfSegments < 10)
            {sb.append("00").append(String.valueOf(numberOfSegments));}
        else if (numberOfSegments < 100)
            {sb.append("0").append(String.valueOf(numberOfSegments));}
        else 
            {sb.append(String.valueOf(numberOfSegments));}
        sb.append(encodeASCII());
        return sb.toString();
    }
   
    /**
     * countSegments encodes string message to BigInteger value using ASCII dec
    */
    private void countSegments() {
        if (messageString.length()%(segmentSize) == 0) {
            numberOfSegments = messageString.length()/(segmentSize);
        }else {
        numberOfSegments = (messageString.length()/(segmentSize)) + 1;
        }
    }
   
    /**
     * encodeASCII encodes string message to BigInteger value using ASCII dec
     * @return String full message encoded to 3 digit ASCII
    */
    private String encodeASCII() {
        StringBuilder sb = new StringBuilder();
            int charVal;
           
            for (int i = 0; i < messageString.length(); i++) {
                charVal = (int)messageString.charAt(i);
                charVal = charVal + 100; //ensure 3 digits
                {sb.append(charVal);}
            }
        return sb.toString();
   }
    
    /**
     * Get the decMessage of Message obj
     * @return String decMessage
     */
    public String getDecMessage() {
        return decMessage;
    }
    
    /**
     * Set the decMessage and updates stringMessage to match
     * @param String incoming decMessage
     */
    public void setDecMessage(String incomingDec) {
        decMessage = incomingDec;
    }
    
    /**
     * Get the Message String of Message obj
     * @return String messageString
     */
    public String getStringMessage() {
        return messageString;
    }
}
