package smallpocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static smallpocs.domain.MaterielAsserts.*;
import static smallpocs.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import smallpocs.IntegrationTest;
import smallpocs.domain.Materiel;
import smallpocs.repository.MaterielRepository;
import smallpocs.service.dto.MaterielDTO;
import smallpocs.service.mapper.MaterielMapper;

/**
 * Integration tests for the {@link MaterielResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterielResourceIT {

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE = "BBBBBBBBBB";

    private static final String DEFAULT_MODELE = "AAAAAAAAAA";
    private static final String UPDATED_MODELE = "BBBBBBBBBB";

    private static final String DEFAULT_SORTE = "AAAAAAAAAA";
    private static final String UPDATED_SORTE = "BBBBBBBBBB";

    private static final String DEFAULT_SITE = "AAAAAAAAAA";
    private static final String UPDATED_SITE = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_MISSION = "AAAAAAAAAA";
    private static final String UPDATED_MISSION = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/materiels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterielRepository materielRepository;

    @Autowired
    private MaterielMapper materielMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterielMockMvc;

    private Materiel materiel;

    private Materiel insertedMateriel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Materiel createEntity() {
        return new Materiel()
            .etat(DEFAULT_ETAT)
            .release(DEFAULT_RELEASE)
            .modele(DEFAULT_MODELE)
            .sorte(DEFAULT_SORTE)
            .site(DEFAULT_SITE)
            .region(DEFAULT_REGION)
            .mission(DEFAULT_MISSION)
            .entite(DEFAULT_ENTITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Materiel createUpdatedEntity() {
        return new Materiel()
            .etat(UPDATED_ETAT)
            .release(UPDATED_RELEASE)
            .modele(UPDATED_MODELE)
            .sorte(UPDATED_SORTE)
            .site(UPDATED_SITE)
            .region(UPDATED_REGION)
            .mission(UPDATED_MISSION)
            .entite(UPDATED_ENTITE);
    }

    @BeforeEach
    public void initTest() {
        materiel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMateriel != null) {
            materielRepository.delete(insertedMateriel);
            insertedMateriel = null;
        }
    }

    @Test
    @Transactional
    void createMateriel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);
        var returnedMaterielDTO = om.readValue(
            restMaterielMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterielDTO.class
        );

        // Validate the Materiel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMateriel = materielMapper.toEntity(returnedMaterielDTO);
        assertMaterielUpdatableFieldsEquals(returnedMateriel, getPersistedMateriel(returnedMateriel));

        insertedMateriel = returnedMateriel;
    }

    @Test
    @Transactional
    void createMaterielWithExistingId() throws Exception {
        // Create the Materiel with an existing ID
        materiel.setId(1L);
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMateriels() throws Exception {
        // Initialize the database
        insertedMateriel = materielRepository.saveAndFlush(materiel);

        // Get all the materielList
        restMaterielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materiel.getId().intValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].release").value(hasItem(DEFAULT_RELEASE)))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE)))
            .andExpect(jsonPath("$.[*].sorte").value(hasItem(DEFAULT_SORTE)))
            .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].mission").value(hasItem(DEFAULT_MISSION)))
            .andExpect(jsonPath("$.[*].entite").value(hasItem(DEFAULT_ENTITE)));
    }

    @Test
    @Transactional
    void getMateriel() throws Exception {
        // Initialize the database
        insertedMateriel = materielRepository.saveAndFlush(materiel);

        // Get the materiel
        restMaterielMockMvc
            .perform(get(ENTITY_API_URL_ID, materiel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materiel.getId().intValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.release").value(DEFAULT_RELEASE))
            .andExpect(jsonPath("$.modele").value(DEFAULT_MODELE))
            .andExpect(jsonPath("$.sorte").value(DEFAULT_SORTE))
            .andExpect(jsonPath("$.site").value(DEFAULT_SITE))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.mission").value(DEFAULT_MISSION))
            .andExpect(jsonPath("$.entite").value(DEFAULT_ENTITE));
    }

    @Test
    @Transactional
    void getNonExistingMateriel() throws Exception {
        // Get the materiel
        restMaterielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMateriel() throws Exception {
        // Initialize the database
        insertedMateriel = materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materiel
        Materiel updatedMateriel = materielRepository.findById(materiel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMateriel are not directly saved in db
        em.detach(updatedMateriel);
        updatedMateriel
            .etat(UPDATED_ETAT)
            .release(UPDATED_RELEASE)
            .modele(UPDATED_MODELE)
            .sorte(UPDATED_SORTE)
            .site(UPDATED_SITE)
            .region(UPDATED_REGION)
            .mission(UPDATED_MISSION)
            .entite(UPDATED_ENTITE);
        MaterielDTO materielDTO = materielMapper.toDto(updatedMateriel);

        restMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materielDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isOk());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterielToMatchAllProperties(updatedMateriel);
    }

    @Test
    @Transactional
    void putNonExistingMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materielDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterielWithPatch() throws Exception {
        // Initialize the database
        insertedMateriel = materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materiel using partial update
        Materiel partialUpdatedMateriel = new Materiel();
        partialUpdatedMateriel.setId(materiel.getId());

        partialUpdatedMateriel.etat(UPDATED_ETAT).modele(UPDATED_MODELE).site(UPDATED_SITE).entite(UPDATED_ENTITE);

        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMateriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMateriel))
            )
            .andExpect(status().isOk());

        // Validate the Materiel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterielUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMateriel, materiel), getPersistedMateriel(materiel));
    }

    @Test
    @Transactional
    void fullUpdateMaterielWithPatch() throws Exception {
        // Initialize the database
        insertedMateriel = materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materiel using partial update
        Materiel partialUpdatedMateriel = new Materiel();
        partialUpdatedMateriel.setId(materiel.getId());

        partialUpdatedMateriel
            .etat(UPDATED_ETAT)
            .release(UPDATED_RELEASE)
            .modele(UPDATED_MODELE)
            .sorte(UPDATED_SORTE)
            .site(UPDATED_SITE)
            .region(UPDATED_REGION)
            .mission(UPDATED_MISSION)
            .entite(UPDATED_ENTITE);

        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMateriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMateriel))
            )
            .andExpect(status().isOk());

        // Validate the Materiel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterielUpdatableFieldsEquals(partialUpdatedMateriel, getPersistedMateriel(partialUpdatedMateriel));
    }

    @Test
    @Transactional
    void patchNonExistingMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materielDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materiel.setId(longCount.incrementAndGet());

        // Create the Materiel
        MaterielDTO materielDTO = materielMapper.toDto(materiel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterielMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materielDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Materiel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMateriel() throws Exception {
        // Initialize the database
        insertedMateriel = materielRepository.saveAndFlush(materiel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materiel
        restMaterielMockMvc
            .perform(delete(ENTITY_API_URL_ID, materiel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materielRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Materiel getPersistedMateriel(Materiel materiel) {
        return materielRepository.findById(materiel.getId()).orElseThrow();
    }

    protected void assertPersistedMaterielToMatchAllProperties(Materiel expectedMateriel) {
        assertMaterielAllPropertiesEquals(expectedMateriel, getPersistedMateriel(expectedMateriel));
    }

    protected void assertPersistedMaterielToMatchUpdatableProperties(Materiel expectedMateriel) {
        assertMaterielAllUpdatablePropertiesEquals(expectedMateriel, getPersistedMateriel(expectedMateriel));
    }
}
