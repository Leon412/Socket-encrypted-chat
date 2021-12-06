import java.math.BigInteger;
import java.util.Base64;
import java.util.Random;

/**
 * La classe {@code KeyGenerator} contiene un generatore di chiavi per l'algoritmo RSA.
 * <p>
 * Le chiavi sono rappresentate come un {@link KeyPair}. Ogni chiave viene memorizzata come:
 * <blockquote><pre>
 *    chiave = esponenteInBase64 + "-" + moduloInBase64;
 * </pre></blockquote>
 * @author <a href="https://github.com/Leon412">Leonardo Panichi</a>
 */
public class KeyGenerator {
    Random rnd = new Random(); //Sorgente dei numeri random

    /**
     * Costruisce usando {@code StringBuilder} un numero {@code BigInteger} casuale di {@code digits} cifre, 
     * generando una cifra alla volta e appendendole.
     * @param digits Numero di cifre del numero.
     * @return Un numero {@code BigInteger} casuale di {@code digits} cifre.
     */
    public BigInteger getRandomBigInteger(int digits) {
        StringBuilder sb = new StringBuilder(digits); //Costrutture di stringhe con grandezza digits
        sb.append((char)('0' + rnd.nextInt(9) + 1));  //Genera un numero casuale da 1 a 9 
                                                      //(il primo non può essere zero senno' il numero finale avrebbe meno cifre di quelle specificate) 
                                                      //e lo appende alla stringa
        digits--;

        //Continua a generare numeri casuali (da 0 a 9 questa volta) e ad appenderli 
        //alla stringa fino a che non arriva al numero di cifre specificato
        for(int i = 0; i < digits; i++)                         
            sb.append((char)('0' + rnd.nextInt(10))); 
        return new BigInteger(sb.toString());
    }
    
    /**
     * Trova il numero primo maggiore e piu' vicino a {@code number} usando il piccolo teorema di Fermat.
     * @param number Il numero di cui si vuole trovare il numero primo maggiore piu' vicino ad esso.
     * @return Il numero primo maggiore e piu' vicino a {@code number}.
     * @see <a href="https://it.wikipedia.org/wiki/Piccolo_teorema_di_Fermat">Wikipedia: Piccolo teorema di Fermat</a>
     */
    public BigInteger getFirstPrime(BigInteger number) {
        if(number.remainder(BigInteger.TWO) == BigInteger.ZERO)    
            number = number.add(BigInteger.ONE);
        while(BigInteger.TWO.modPow(number, number).compareTo(BigInteger.TWO) != 0)
            number = number.add(BigInteger.TWO);
        return number;
    }

    /**
     * Genera una chiave pubblica e una privata per essere usati nella criptazione e decriptazione con l'algoritmo RSA.
     * @param digits Numero di cifre dei numeri primi iniziali con i quali si trovano le chiavi.
     * @return Un {@link KeyPair} con chiave pubblica e privata.
     * @see {@link RSA}
     */
    public KeyPair generateKeys(int digits) {
        BigInteger p = getFirstPrime(getRandomBigInteger(digits)); //Numero primo p
        BigInteger q = getFirstPrime(getRandomBigInteger(digits)); //Numero primo q
        BigInteger e = getFirstPrime(getRandomBigInteger(digits)); //Esponente pubblico | più piccolo di N e primo rispetto a z
        BigInteger n = p.multiply(q); //Modulo | N = p * q
        BigInteger z = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE))); //Funzione di Eulero di N | (p – 1) * (q – 1)
        BigInteger d = e.modInverse(z); //Esponente privato | tale che e * d –> 1mod((p – 1) * (q – 1))
        
        //Codifica le parti delle chiavi in Base64
        String nBase64 = Base64.getEncoder().encodeToString(n.toString().getBytes());   
        String eBase64 = Base64.getEncoder().encodeToString(e.toString().getBytes());
        String dBase64 = Base64.getEncoder().encodeToString(d.toString().getBytes());

        //Unisce le parti delle chiavi con un "-"
        String publicKey = eBase64 + "-" + nBase64;
        String privateKey = dBase64 + "-" + nBase64;
        
        return new KeyPair(publicKey, privateKey);
    }
}