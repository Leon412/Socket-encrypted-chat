import java.io.*;
import java.net.*;
 
public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int portNumber = 65535;

        KeyGenerator generator = new KeyGenerator(); //Generatore di chiavi RSA
        KeyPair clientPair; //Paio di chiavi RSA del client
        int maxChars = 0; //Massimo numero di caratteri inviabili
        String toSend = null; //Stringa da mandare al server
        String response;
        
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Generazioni delle chiavi RSA in corso...");
            clientPair = generator.generateKeys(100); //genera le chiavi con numeri a tot cifre

            while ((response = in.readLine()) != null) {
                if(response.equals("INPUT")){
                    toSend = stdIn.readLine();
                    if(toSend.indexOf("/send ") == 0){
                        String toSendArray[] = toSend.split(" ", 3);
                        if(toSendArray.length >= 3){
                            toSendArray[2] = RSA.encrypt(toSendArray[2], clientPair.getPublicKey());
                            toSend = String.join(" ", toSendArray);
                        }
                    }
                    out.println(toSend);
                }
                else if(response.equals("DECRYPT")) {
                    response = in.readLine();
                    String responseArray[] = response.split(" ", 3);
                    responseArray[2] = RSA.decrypt(responseArray[2], clientPair.getPrivateKey());
                    response = String.join(" ", responseArray);
                    System.out.println(response);
                }
                else if(response.equals("SENDKEY")) {
                    out.println(clientPair.getPublicKey());
                }
                else
                    System.out.println(response);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } 
    }
}