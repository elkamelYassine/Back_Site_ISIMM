package tn.isimm.manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Etudiant getEtudiantSample1() {
        return new Etudiant().id(1L).prenom("prenom1").nom("nom1").email("email1").numEtudiant(1L).numTel("numTel1");
    }

    public static Etudiant getEtudiantSample2() {
        return new Etudiant().id(2L).prenom("prenom2").nom("nom2").email("email2").numEtudiant(2L).numTel("numTel2");
    }

    public static Etudiant getEtudiantRandomSampleGenerator() {
        return new Etudiant()
            .id(longCount.incrementAndGet())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .numEtudiant(longCount.incrementAndGet())
            .numTel(UUID.randomUUID().toString());
    }
}
