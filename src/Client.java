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
        String response = null;
        String key = null;

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);                          //Scrivere nel Buffer del Server
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));     //Leggere dal Buffer del Client
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));                    //input da tastiera
        ) {
            System.out.println("Generazioni delle chiavi RSA in corso...");
            clientPair = generator.generateKeys(100); //genera le chiavi con numeri a tot cifre
            maxChars = RSA.maxChars(clientPair.getPublicKey());

            while ((response = in.readLine()) != null && !response.equals("QUIT")) {        //mentre l'inserimento non è nullo e non è QUIT
                if(response.equals("INPUTC")) {     //input comando
                    System.out.print("\r\n>");
                    toSend = stdIn.readLine();      //stringa che arriva dal server
                    if(toSend.indexOf("send ") == 0) {      //se la prima parola che appare nella stringa è send
                        String toSendArray[] = toSend.split(" ", 3);
                        if(toSendArray.length == 3) {       //se la stringa è composta da 3 parole
                            out.println("getkey " + toSendArray[1]);        //invia getkey e lo user del quale prendere la chiave pubblica
                            key = in.readLine();
                            in.readLine();
                            if(!key.equals("<Server> sintassi errata") && toSendArray[2].length() <= maxChars) {
                                toSendArray[2] = RSA.encrypt(toSendArray[2], key);
                                toSend = String.join(" ", toSendArray);
                                out.println(toSend);
                            }
                            else {
                                if(key.equals("<Server> sintassi errata"))
                                    System.out.println("Username non trovato");
                                else
                                    System.out.println("Il messaggio non puo' superare gli/i " + maxChars + " caratteri");
                                out.println("send ");
                            }
                        }
                        else {
                            out.println(toSend);
                        }
                    }
                    else {
                        out.println(toSend);
                    }
                }
                else if(response.equals("INPUT")) {         //se il comando è   INPUT       
                    System.out.print("\r\n>");
                    toSend = stdIn.readLine();
                    out.println(toSend);
                }
                else if(response.equals("DECRYPT")) {
                    response = in.readLine();                   //messaggio [ dataricevente contenuto ]
                    String responseArray[] = response.split(" ", 2);
                    responseArray[1] = RSA.decrypt(responseArray[1], clientPair.getPrivateKey());
                    response = String.join(" ", responseArray);     //rimette insieme le stringe di responseArray
                    System.out.println(response);
                }
                else if(response.equals("SENDKEY")) {
                    out.println(clientPair.getPublicKey());     //manda la sua chiave pubblica
                }
                else {
                    System.out.println(response);
                }
            }
            out.println("QUIT");        //lo mando al server per farmi rimandare QUIT e chiude
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
