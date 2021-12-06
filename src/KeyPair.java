/**
 * La classe {@code KeyPair} rappresenta un paio di chiavi sotto forma di stringhe, una pubblica una privata.
 * @author <a href="https://github.com/Leon412">Leonardo Panichi</a>
 */
public class KeyPair {
    private String privateKey;
    private String publicKey;

    /**
     * Costruisce un nuovo paio di chiavi
     * @param publicKey chiave pubblica
     * @param privateKey chiave privata
     */
    public KeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
    
    public String getPrivateKey() {
        return privateKey;
    }
}
