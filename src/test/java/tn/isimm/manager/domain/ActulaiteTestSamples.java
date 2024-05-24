package tn.isimm.manager.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActulaiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Actulaite getActulaiteSample1() {
        return new Actulaite().id(1L).data("data1").title("title1").description("description1");
    }

    public static Actulaite getActulaiteSample2() {
        return new Actulaite().id(2L).data("data2").title("title2").description("description2");
    }

    public static Actulaite getActulaiteRandomSampleGenerator() {
        return new Actulaite()
            .id(longCount.incrementAndGet())
            .data(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
