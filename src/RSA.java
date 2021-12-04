import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;                        //libreria per codificare e decodificare in base 64

public class RSA {
    //funzione che restituisce il numero massimo di caratteri che possono essere criptati in una volta
    public static int maxChars(String key){
        BigInteger modulus = new BigInteger(new String(Base64.getDecoder().decode(key.substring(key.indexOf("-") + 1))));      //prende n
        return (modulus.bitLength() / 8) - 1;       //lunghezza di n
    }
    
    public static String encrypt(String message, String key){
        String[] keyArray = key.split("-");     
        BigInteger exponent = new BigInteger(new String(Base64.getDecoder().decode(keyArray[0])));
        BigInteger modulus = new BigInteger(new String(Base64.getDecoder().decode(keyArray[1])));
        //Spezza la chiave in esponente e modulo e li decodifica da Base64 in BigInteger
        byte[] bytes = null;
        try {
            bytes = message.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigInteger IntMessage = new BigInteger(bytes);
        //converte la stringa message in numero usando l'UTF-8 per ogni carattere poi convertendolo usando il complemento a due per avere un singolo numero

        if(IntMessage.compareTo(modulus.subtract(BigInteger.ONE)) >= 0)
            throw new ArithmeticException("Message too long for key lenght");
        //Se il numero dato dalla conversione da stringa di message e' piu' grande di N-1, ovvero del modulo usato per l'RSA, l'algoritmo non puo' funzionare quindi lancia un eccezione

        BigInteger IntEncryptedMessage = IntMessage.modPow(exponent, modulus); //criptazione RSA
        String encryptedMessageBase64 = Base64.getEncoder().encodeToString(IntEncryptedMessage.toString().getBytes()); //codifica del numero criptato in Base64
        return encryptedMessageBase64;
    }//prende un messaggio in stringa e una chiave RSA e cripta il messaggio con RSA poi lo codifica il Base64

    public static String decrypt(String encryptedMessageBase64, String key){
        String[] keyArray = key.split("-");
        BigInteger exponent = new BigInteger(new String(Base64.getDecoder().decode(keyArray[0])));
        BigInteger modulus = new BigInteger(new String(Base64.getDecoder().decode(keyArray[1])));
        //Spezza la chiave in esponente e modulo e li decodifica da Base64 in BigInteger

        BigInteger IntEncryptedMessage = new BigInteger(new String(Base64.getDecoder().decode(encryptedMessageBase64))); //decodifica del numero criptato da Base64
        BigInteger IntMessage = IntEncryptedMessage.modPow(exponent, modulus); //decriptazione RSA

        byte[] bytes = IntMessage.toByteArray();

        String message = null;
        try {
            message = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //converte il messaggio da numero a array di Bytes poi converte ogni Byte in caratteri usando l'UTF-8 e li unisce in una stringa
        return message;
    }//prende un messaggio criptato codificato in Base64 e una chiave RSA e decodifica il messaggio
}
