package smallpocs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChampEffectifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChampEffectif getChampEffectifSample1() {
        return new ChampEffectif().id(1L).nom("nom1").valeur(1L);
    }

    public static ChampEffectif getChampEffectifSample2() {
        return new ChampEffectif().id(2L).nom("nom2").valeur(2L);
    }

    public static ChampEffectif getChampEffectifRandomSampleGenerator() {
        return new ChampEffectif().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).valeur(longCount.incrementAndGet());
    }
}
