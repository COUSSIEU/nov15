package smallpocs.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EffectifAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEffectifAllPropertiesEquals(Effectif expected, Effectif actual) {
        assertEffectifAutoGeneratedPropertiesEquals(expected, actual);
        assertEffectifAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEffectifAllUpdatablePropertiesEquals(Effectif expected, Effectif actual) {
        assertEffectifUpdatableFieldsEquals(expected, actual);
        assertEffectifUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEffectifAutoGeneratedPropertiesEquals(Effectif expected, Effectif actual) {
        assertThat(expected)
            .as("Verify Effectif auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEffectifUpdatableFieldsEquals(Effectif expected, Effectif actual) {
        assertThat(expected)
            .as("Verify Effectif relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getCumul()).as("check cumul").isEqualTo(actual.getCumul()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEffectifUpdatableRelationshipsEquals(Effectif expected, Effectif actual) {
        // empty method
    }
}
