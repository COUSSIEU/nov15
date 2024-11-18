package smallpocs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EffectifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Effectif getEffectifSample1() {
        return new Effectif().id(1L).name("name1").cumul(1L);
    }

    public static Effectif getEffectifSample2() {
        return new Effectif().id(2L).name("name2").cumul(2L);
    }

    public static Effectif getEffectifRandomSampleGenerator() {
        return new Effectif().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).cumul(longCount.incrementAndGet());
    }
}
