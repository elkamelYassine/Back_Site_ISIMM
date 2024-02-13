package tn.isimm.manager.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SeanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Seance getSeanceSample1() {
        return new Seance().id(1L).numSeance(1);
    }

    public static Seance getSeanceSample2() {
        return new Seance().id(2L).numSeance(2);
    }

    public static Seance getSeanceRandomSampleGenerator() {
        return new Seance().id(longCount.incrementAndGet()).numSeance(intCount.incrementAndGet());
    }
}
