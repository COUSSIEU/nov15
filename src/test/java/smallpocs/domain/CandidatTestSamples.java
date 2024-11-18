package smallpocs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CandidatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Candidat getCandidatSample1() {
        return new Candidat().id(1L).nom("nom1").age(1L).springboot(1L).angular(1L).html(1L).css(1L).transport(1L).sport("sport1");
    }

    public static Candidat getCandidatSample2() {
        return new Candidat().id(2L).nom("nom2").age(2L).springboot(2L).angular(2L).html(2L).css(2L).transport(2L).sport("sport2");
    }

    public static Candidat getCandidatRandomSampleGenerator() {
        return new Candidat()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .age(longCount.incrementAndGet())
            .springboot(longCount.incrementAndGet())
            .angular(longCount.incrementAndGet())
            .html(longCount.incrementAndGet())
            .css(longCount.incrementAndGet())
            .transport(longCount.incrementAndGet())
            .sport(UUID.randomUUID().toString());
    }
}
