package tn.isimm.manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfesseurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Professeur getProfesseurSample1() {
        return new Professeur()
            .id(1L)
            .prenom("prenom1")
            .nom("nom1")
            .email("email1")
            .matricule("matricule1")
            .titre("titre1")
            .numTel("numTel1");
    }

    public static Professeur getProfesseurSample2() {
        return new Professeur()
            .id(2L)
            .prenom("prenom2")
            .nom("nom2")
            .email("email2")
            .matricule("matricule2")
            .titre("titre2")
            .numTel("numTel2");
    }

    public static Professeur getProfesseurRandomSampleGenerator() {
        return new Professeur()
            .id(longCount.incrementAndGet())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .matricule(UUID.randomUUID().toString())
            .titre(UUID.randomUUID().toString())
            .numTel(UUID.randomUUID().toString());
    }
}
