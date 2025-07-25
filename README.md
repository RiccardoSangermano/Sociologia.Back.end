Il back- end è, sviluppato con Spring Boot, che espone API RESTful. Utilizza Spring Data JPA per la persistenza dei dati in un database PostgreSQL e Spring Security con JWT per la gestione dell'autenticazione e dell'autorizzazione.



application property connette al data base, indica lo schema di bas che il database deve avere, definisce una chiave segreta per firmare i token JWT  e la loro durata che essi hanno,
Le entità rappresentano le tabelle nel database e le loro relazioni. Utente contiene username e email, con una password criptata.
ruoli indiano i permessi che un utente dispone(per gestire i ruoli si usa un enum)
theory rappresenta le varie teorie sociologiche

 Con i repository si gestiscono l’interazioni con il database e definiscono  i metodi per le operazioni crud

Attraverso i dio si trasferiscono dei tra il client e il server
RegisterDto e LoginDto per richieste di registrazione e autenticazione
JwtAuthResponse la risposta dopo un login di successo contiene il token JWT e i ruoli
TheoryDto e TheoryResponseDto per creare aggiornare e visualizzare le teorie
TheoryPagedResponseDto per risposte paginate di liste di teorie
UtenteDto per visualizzazione e aggiornamento dati utente Con i repository si gestiscono l’interazioni con il database e definiscono  i metodi per le operazioni crud

I servizi Contengono la logica di business e coordinano le operazioni tra controller e repository
AuthService gestisce logica di registrazione e login interagisce con UtenteRepository e RoleRepository cripta password e genera token JWT
TheoryService si occupa delle operazioni relative alle teorie sociologiche recupera crea aggiorna elimina teorie esegue mapping DTO Entity gestisce ResourceNotFoundException
UtenteService gestisce operazioni legate agli utenti recupera e gestisce profili crea e aggiorna utenti con validazioni anti-duplicati gestisce teorie preferite esegue mapping DTO Entity include criptazione password e assegnazione ruoli

Sicurezza
Spring Security protegge le API
CustomUserDetailsService carica dettagli utente da database per autenticazione cerca per username o email
JwtAuthenticationFilter intercetta richieste estrae e valida token JWT autentica utente
JwtTokenProvider genera valida ed estrae info da token JWT
JwtAuthenticationEntryPoint gestisce errori di autenticazione restituisce 401 Unauthorized
SecurityConfig configurazione principale abilita CORS disabilita CSRF configura autenticazione stateless definisce regole autorizzazione endpoint
le eccezioni gestiscono i cui di errore
il data loader si avvia al lancio dell’ppliczione gestisce il prepopolamento dei test, utilizzato per comodità nel fare test e perché inserisce dei dei d utilizzare, mantenendo più pulito e manutenibil il codice
Utenti pre esistenti:
utente: user 
password: passworduser
utente: admin
password: passwordadmin
Operazioni con log admin:
header :
Header Key: Authorizatio
Header Value: Bearer <lIl tuo token>

GET /api/utenti (per vedere tutti gli utenti)
POST /api/theories (per creare una teoria, con ruolo ADMIN)
PUT /api/theories/{id} (per aggiornare una teoria, con ruolo ADMIN)
DELETE /api/theories/{id} (per eliminare una teoria, con ruolo ADMIN)

GET http://localhost:8080/api/theories
Questo endpoint è pubblico


Link front-end https://github.com/RiccardoSangermano/frontend-sociologia.git

Nel front e end  recupero le API e costruisco dei layout per i veri componenti: ne ho scelti due poiché il primo mi sembrava più adatto per presentare il sito, il secondo era più adatto per un’esperienza utente e  admin.

I componenti più importanti sono theorylist, dove recupero l’elenco delle teorie, in  cui interagendo con un API , creo e gestisco delle teorie preferite,.

La pagina di registrazione effettua un chiamata con l’API per inviare i dati dell’utente.Login page invia dati dell’uente registrato e se sono validi per il back end, porterà o alla Dashboard user o quella admin.

Il componente theory managment recupera le teorie esistenti e interagisce con le operazioni CRUD come create, update e delete.

Il componente user management fa la stessa cosa di quello precedente, solo per gli utenti

Unico problema è che quando registro l’utente con la lettera iniziale maiuscola dalla pagina admin, ho un problema con il case-sensity chee non sono riuscito a trovare, quindi se potete mandarmi un feedback cosi lo risolvo, L’utente viene comunque registrato ee si può accedere con l’email. Per registrationPage non c’è problem, si può scrivere che con la prima lettera maiuscola


Esmpio per aggiungere un nuova teoria:

Nome: Anomia

   Autore: Émile Durkheim

Spiegazione: L'anomia è un concetto sociologico che descrive uno stato di 'assenza di norme' o 'normlessness' in una società o in un individuo. Si verifica quando le norme sociali che regolano il comportamento individuale e collettivo si indeboliscono o scompaiono, lasciando gli individui disorientati, senza guida morale e con un senso di alienazione. Durkheim la collegava a rapidi cambiamenti sociali ed economici che disturbano l'equilibrio tradizionale, portando a frustrazione e devianza.

 Applicazione moderna: Oggi, l'anomia può essere osservata in contesti di rapida globalizzazione e digitalizzazione, dove le vecchie strutture sociali si erodono e nuove norme tardano a emergere. Esempi includono l'aumento dei tassi di suicidio o depressione in periodi di crisi economica, la disillusione giovanile in società con poche opportunità, o il comportamento disinibito e aggressivo nelle comunità online anonime, dove le norme sociali tradizionali sono meno efficaci. Rilevante anche nell'analisi delle 'bolle' speculative o dei fallimenti etici nel mondo finanziario.

Non necessita per forza l’immagine per essere caricata
