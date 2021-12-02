import java.math.BigInteger;
import java.util.Base64;
import java.util.Random;

public class KeyGenerator {
    Random rnd = new Random(); //Sorgente per i numeri random

    public BigInteger getRandomBigInteger(int digits) {
        StringBuilder sb = new StringBuilder(digits);       //ogetto per creazione stringa con parametro lunghezza
        sb.append((char)('0' + rnd.nextInt(9) + 1));        //genera un numero casuale da 1 a 9,(il primo non può essere zero) e lo appende alla stringa
        digits--;
        for(int i = 0; i < digits; i++)                         
            sb.append((char)('0' + rnd.nextInt(10)));       //continua ad aggiungere numeri alla stringa
        return new BigInteger(sb.toString());
    } //BigInteger casuale a numero di cifre specificato
    
    //Calcolo numero primo tramite formula di Fermat
    public BigInteger getFirstPrime(BigInteger start) {
        if(start.remainder(BigInteger.TWO) == BigInteger.ZERO)    
            start = start.add(BigInteger.ONE);
        while(BigInteger.TWO.modPow(start, start).compareTo(BigInteger.TWO) != 0)
            start = start.add(BigInteger.TWO);
        return start;
    } //Partendo da un numero trova il numero primo piu' grande e piu' vicino ad esso

    public KeyPair generateKeys(int digits) {
        BigInteger p = getFirstPrime(getRandomBigInteger(digits)); //numero primo p
        BigInteger q = getFirstPrime(getRandomBigInteger(digits)); //numero primo q
        BigInteger e = getFirstPrime(getRandomBigInteger(digits)); //esponente pubblico | più piccolo di N e primo rispetto a f(N) = (p – 1) * (q – 1), dove f è la funzione di Eulero
        BigInteger n = p.multiply(q); //modulo | N = p * q
        BigInteger z = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE))); //(p – 1) * (q – 1)
        BigInteger d = e.modInverse(z); //esponente privato | tale che e * d –> 1mod((p – 1) * (q – 1))
           
        
        String nBase64 = Base64.getEncoder().encodeToString(n.toString().getBytes());   
        String eBase64 = Base64.getEncoder().encodeToString(e.toString().getBytes());
        String dBase64 = Base64.getEncoder().encodeToString(d.toString().getBytes());
        //Codifica delle parti delle chiavi in Base64

        String publicKey = eBase64 + "-" + nBase64;
        String privateKey = dBase64 + "-" + nBase64;
        //Unione della chiave
        return new KeyPair(publicKey, privateKey);
    } //Genera un paio di chiavi RSA specificando il numero di cifre dei numeri primi da trovare
}
