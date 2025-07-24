package it.epicode.Sociologia.Back.end.util;

import it.epicode.Sociologia.Back.end.enums.Roles;
import it.epicode.Sociologia.Back.end.model.Role;
import it.epicode.Sociologia.Back.end.model.Theory;
import it.epicode.Sociologia.Back.end.model.Utente;
import it.epicode.Sociologia.Back.end.repository.RoleRepository;
import it.epicode.Sociologia.Back.end.repository.TheoryRepository;
import it.epicode.Sociologia.Back.end.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UtenteRepository utenteRepository;
    private final TheoryRepository theoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, UtenteRepository utenteRepository, TheoryRepository theoryRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.utenteRepository = utenteRepository;
        this.theoryRepository = theoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.findByNome(Roles.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setNome(Roles.ROLE_USER);
            roleRepository.save(userRole);
        }
        if (roleRepository.findByNome(Roles.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setNome(Roles.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }

        Role userRole = roleRepository.findByNome(Roles.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo USER non trovato."));
        Role adminRole = roleRepository.findByNome(Roles.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo ADMIN non trovato."));



        if (utenteRepository.findByUsername("admin").isEmpty()) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);

            Utente adminUtente = new Utente();
            adminUtente.setUsername("admin");
            adminUtente.setEmail("admin@example.com");
            adminUtente.setPassword(passwordEncoder.encode("passwordadmin"));
            adminUtente.setRuoli(adminRoles);
            utenteRepository.save(adminUtente);
            System.out.println("Utente Admin creato: admin/passwordadmin (con ruoli ADMIN e USER)");
        } else {
            System.out.println("Utente Admin esiste già.");
        }

        if (utenteRepository.findByUsername("user").isEmpty()) {
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole); // Assegna solo ROLE_USER

            Utente normalUtente = new Utente();
            normalUtente.setUsername("user");
            normalUtente.setEmail("user@example.com");
            normalUtente.setPassword(passwordEncoder.encode("passworduser"));
            normalUtente.setRuoli(userRoles);
            utenteRepository.save(normalUtente);
            System.out.println("Utente normale creato: user/passworduser (con ruolo USER)");
        } else {
            System.out.println("Utente normale esiste già.");
        }

        if (theoryRepository.count() == 0) {
            System.out.println("Inizio pre-popolamento delle teorie...");

            Theory cignoNero = new Theory();
            cignoNero.setNomeTeoria("Cigno Nero");
            cignoNero.setAutore("Nassim Nicholas Taleb");
            cignoNero.setImmagineAutoreUrl("/images/nassim-nicholas-taleb.jpg");
            cignoNero.setSpiegazione("La teoria del Cigno Nero si riferisce a eventi imprevedibili e rari che hanno un impatto estremo. Sono eventi così rari e con conseguenze così grandi da non poter essere previsti con metodi di previsione tradizionali. Tre caratteristiche principali: è una anomalia (nulla nel passato ne indica la possibilità), ha un impatto estremo e, dopo il suo verificarsi, la natura umana trova spiegazioni che lo rendono prevedibile o spiegabile a posteriori.");
            cignoNero.setEsempioApplicazioneModerna("Si applica a crisi finanziarie (es. crisi del 2008), pandemie (es. COVID-19), scoperte scientifiche rivoluzionarie o cambiamenti politici inaspettati. Sottolinea l'importanza della robustezza e dell'anti-fragilità piuttosto che della semplice ottimizzazione, poiché è impossibile prevedere tutti i rischi.");
            theoryRepository.save(cignoNero);

            Theory interazionismoSimbolico = new Theory();
            interazionismoSimbolico.setNomeTeoria("Interazionismo Simbolico");
            interazionismoSimbolico.setAutore("George Herbert Mead");
            interazionismoSimbolico.setImmagineAutoreUrl("/images/george-mead.jpg");
            interazionismoSimbolico.setSpiegazione("L'interazionismo simbolico è una prospettiva sociologica che si concentra su come gli individui creano e mantengono la realtà sociale attraverso le loro interazioni quotidiane. Le persone agiscono in base ai significati che attribuiscono agli oggetti e alle situazioni, e questi significati sono prodotti e modificati attraverso l'interazione sociale. Il linguaggio, i gesti e i simboli sono fondamentali in questo processo.");
            interazionismoSimbolico.setEsempioApplicazioneModerna("Applicazione moderna include l'analisi di come le identità online sono costruite e negoziate sui social media, come le etichette sociali (es. 'influencer', 'hater') influenzano il comportamento, o come i movimenti sociali emergono attraverso la condivisione di simboli e narrazioni comuni.");
            theoryRepository.save(interazionismoSimbolico);

            Theory capitaleSociale = new Theory();
            capitaleSociale.setNomeTeoria("Capitale Sociale");
            capitaleSociale.setAutore("Pierre Bourdieu");
            capitaleSociale.setImmagineAutoreUrl("/images/pierre-bourdieu.jpg");
            capitaleSociale.setSpiegazione("Il capitale sociale si riferisce al valore delle reti sociali e delle connessioni che gli individui e i gruppi possiedono. Include risorse come la fiducia, le norme di reciprocità e le relazioni che facilitano l'azione collettiva e la cooperazione. Può essere 'bonding' (all'interno di gruppi omogenei) o 'bridging' (tra gruppi diversi).");
            capitaleSociale.setEsempioApplicazioneModerna("Si osserva nella capacità delle comunità online di mobilitare risorse (es. crowdfunding), nell'efficacia delle reti di ex-alunni per opportunità di carriera, o nel ruolo delle associazioni civiche nel rafforzare la coesione sociale e la partecipazione democratica.");
            theoryRepository.save(capitaleSociale);

            Theory teorieEtichettamento = new Theory();
            teorieEtichettamento.setNomeTeoria("Teorie dell'Etichettamento");
            teorieEtichettamento.setAutore("Howard Becker");
            teorieEtichettamento.setImmagineAutoreUrl("/images/becker.jpg");
            teorieEtichettamento.setSpiegazione("Le teorie dell'etichettamento (Labeling Theory) sostengono che la devianza non è una qualità intrinseca di un atto, ma piuttosto il risultato dell'applicazione di regole e sanzioni da parte della società. Una volta che un individuo viene etichettato come 'deviante', questo può portare a una 'profezia che si autoavvera', influenzando l'identità dell'individuo e il suo comportamento futuro.");
            teorieEtichettamento.setEsempioApplicazioneModerna("Applicazioni moderne includono lo studio di come la stigmatizzazione delle malattie mentali influenzi la ricerca di aiuto, come le etichette criminali influenzino la reintegrazione sociale, o come i profili online (es. 'troll', 'cancel culture') possano influenzare la percezione e il trattamento degli individui.");
            theoryRepository.save(teorieEtichettamento);

            Theory saggezzaDellaFolla = new Theory();
            saggezzaDellaFolla.setNomeTeoria("Saggezza della Folla");
            saggezzaDellaFolla.setAutore("James Surowiecki");
            saggezzaDellaFolla.setImmagineAutoreUrl("/images/james-surowiecki.jpg");
            saggezzaDellaFolla.setSpiegazione("La saggezza della folla è l'idea che, in determinate condizioni, la media di un gran numero di stime o giudizi indipendenti sia più accurata di qualsiasi stima individuale, anche quella di un esperto. Le condizioni chiave includono la diversità di opinioni, l'indipendenza dei giudizi, la decentralizzazione e un meccanismo di aggregazione.");
            saggezzaDellaFolla.setEsempioApplicazioneModerna("Si manifesta nei sistemi di recensioni online (es. Amazon, TripAdvisor), nelle previsioni dei mercati finanziari, nei concorsi di pronostici sportivi, o nei sistemi di intelligenza collettiva come Wikipedia, dove la combinazione di numerosi contributi porta a risultati sorprendentemente accurati.");
            theoryRepository.save(saggezzaDellaFolla);

            System.out.println("Teorie pre-populate nel database.");
        } else {
            System.out.println("Il database delle teorie non è vuoto, salto il pre-popolamento.");
        }
    }
}