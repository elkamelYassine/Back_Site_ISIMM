package tn.isimm.manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClubTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Club getClubSample1() {
        return new Club().id(1L).nom("nom1").pageFB("pageFB1").pageIg("pageIg1").email("email1");
    }

    public static Club getClubSample2() {
        return new Club().id(2L).nom("nom2").pageFB("pageFB2").pageIg("pageIg2").email("email2");
    }

    public static Club getClubRandomSampleGenerator() {
        return new Club()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .pageFB(UUID.randomUUID().toString())
            .pageIg(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
