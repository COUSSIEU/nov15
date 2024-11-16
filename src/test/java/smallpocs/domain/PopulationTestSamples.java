package smallpocs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PopulationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Population getPopulationSample1() {
        return new Population().id(1L).tableName("tableName1").attributNames("attributNames1").typeNames("typeNames1");
    }

    public static Population getPopulationSample2() {
        return new Population().id(2L).tableName("tableName2").attributNames("attributNames2").typeNames("typeNames2");
    }

    public static Population getPopulationRandomSampleGenerator() {
        return new Population()
            .id(longCount.incrementAndGet())
            .tableName(UUID.randomUUID().toString())
            .attributNames(UUID.randomUUID().toString())
            .typeNames(UUID.randomUUID().toString());
    }
}
