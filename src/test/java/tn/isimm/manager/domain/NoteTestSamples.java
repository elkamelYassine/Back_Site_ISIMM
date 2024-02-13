package tn.isimm.manager.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Note getNoteSample1() {
        return new Note().id(1L).note(1);
    }

    public static Note getNoteSample2() {
        return new Note().id(2L).note(2);
    }

    public static Note getNoteRandomSampleGenerator() {
        return new Note().id(longCount.incrementAndGet()).note(intCount.incrementAndGet());
    }
}
