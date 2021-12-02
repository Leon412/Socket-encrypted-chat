//classe che contiene chiave pubblica e privata
public class KeyPair {
    private String privateKey;
    private String publicKey;
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
