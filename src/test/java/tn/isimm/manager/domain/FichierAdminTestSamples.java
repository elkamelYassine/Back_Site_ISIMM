package tn.isimm.manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FichierAdminTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FichierAdmin getFichierAdminSample1() {
        return new FichierAdmin().id(1L).titre("titre1");
    }

    public static FichierAdmin getFichierAdminSample2() {
        return new FichierAdmin().id(2L).titre("titre2");
    }

    public static FichierAdmin getFichierAdminRandomSampleGenerator() {
        return new FichierAdmin().id(longCount.incrementAndGet()).titre(UUID.randomUUID().toString());
    }
}
