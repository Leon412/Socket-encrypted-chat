# Socket-encrypted-chat
### Membri del progetto;
1. Panichi Leonardo (Leon421)
2. Pesaresi Adriano (adrianopesaresi)
3. Mazzaferro Sebastiano (SebastianoMazzaferro)

### Obiettivo del progetto:
Realizzazione un’applicazione client-server per la comunicazione cifrata tra coppie di utenti. Codifica e decodifica dei messaggi deve avvenire tramite l’algoritmo asimmetrico [RSA](https://it.wikipedia.org/wiki/RSA_(crittografia)).
### Macro funzionalità dell’applicazione:
1. Generazione di coppie di chiavi pubblica-privata. Ad ogni utente del sistema deve essere
   assegnata una coppia di chiavi per consentirgli di comunicare con altri utenti. La generazione
   delle chiavi viene eseguita lato client (per garantire la riservatezza della chiave privata). Ogni
   client, dopo aver generato la sua coppia di chiavi, deve inviare al server la sua chiave pubblica
   per consentirne la distribuzione ad altri utenti.
2. Invio di messaggi cifrati;
3. Ricezione di messaggi cifrati.

### Compiti del server:
1. Rimane in attesa di eventuali richieste di connessione da parte di client;
2. Quando arriva una richiesta di connessione da parte di un client, avvia un thread che gestisce
   le richieste che vengono da quello specifico client.

### Il thread che gestisce le richieste di un client C deve poter:
1. Memorizzare la chiave pubblica di C (chiave che viene inviata da C); questa informazione deve
   essere memorizzata in una struttura dati condivisa con altri thread;
2. Inviare, su richiesta, a C la chiave pubblica di un’altro utente connesso in quel momento;
3. Ricevere e memorizzare messaggi cifrati provenienti da C e diretti verso un altro client
   destinatario del messaggio cifrato; questi messaggi vanno memorizzati in una opportuna
   struttura dati condivisa tra tutti i thread.
4. Inviare a C tutti i messaggi cifrati a lui indirizzati (selezionandoli dalla struttura dati condivisa
   descritta al punto 3).

### Ogni client C deve poter:
1. Generare la propria coppia di chiavi pubblica-privata;
2. Inviare al server la propria chiave pubblica in modo che questa possa essere condivisa con
   altri client;
3. Richiedere al server la chiave pubblica di un altro client a cui vuole mandare un messaggio;
4. Cifrare un messaggio m usando la chiave pubblica di un client destinatario;
5. Inviare al server un messaggio cifrato;
6. Richiedere al server tutti i messaggi cifrati a lui indirizzati;
7. Decifrare e visualizzare tutti i messaggi ricevuti da altri client.

# IMPLEMENTAZIONE IN JAVA:
## Classi:
#### Client

#### KeyGenerator
(Classe che implementa i metodi per la generazione delle chiavi di un qualsiasi utente che utilizzi l'applicazione)

#### KeyPair
(Classe che implementa la struttura dati per la memorizzazione delle chiavi)

#### Message
(Classe che implementa la struttura dati per la memorizzazione del messaggio criptato, del mittente e del destinatario)

#### MessageBox
(Classe che crea la stuttura che gestisce i messaggi con i relativi metodi)

#### RSA
(Classe che implementa la crittografia RSA del messaggio) 

#### Server

#### ServerThread

