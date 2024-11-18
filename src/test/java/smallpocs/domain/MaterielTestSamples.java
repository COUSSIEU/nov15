package smallpocs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaterielTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Materiel getMaterielSample1() {
        return new Materiel()
            .id(1L)
            .etat("etat1")
            .release("release1")
            .modele("modele1")
            .sorte("sorte1")
            .site("site1")
            .region("region1")
            .mission("mission1")
            .entite("entite1");
    }

    public static Materiel getMaterielSample2() {
        return new Materiel()
            .id(2L)
            .etat("etat2")
            .release("release2")
            .modele("modele2")
            .sorte("sorte2")
            .site("site2")
            .region("region2")
            .mission("mission2")
            .entite("entite2");
    }

    public static Materiel getMaterielRandomSampleGenerator() {
        return new Materiel()
            .id(longCount.incrementAndGet())
            .etat(UUID.randomUUID().toString())
            .release(UUID.randomUUID().toString())
            .modele(UUID.randomUUID().toString())
            .sorte(UUID.randomUUID().toString())
            .site(UUID.randomUUID().toString())
            .region(UUID.randomUUID().toString())
            .mission(UUID.randomUUID().toString())
            .entite(UUID.randomUUID().toString());
    }
}
