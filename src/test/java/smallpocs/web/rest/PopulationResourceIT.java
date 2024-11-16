package smallpocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static smallpocs.domain.PopulationAsserts.*;
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
import smallpocs.domain.Population;
import smallpocs.repository.PopulationRepository;
import smallpocs.service.dto.PopulationDTO;
import smallpocs.service.mapper.PopulationMapper;

/**
 * Integration tests for the {@link PopulationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PopulationResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUT_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUT_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAMES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/populations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PopulationRepository populationRepository;

    @Autowired
    private PopulationMapper populationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPopulationMockMvc;

    private Population population;

    private Population insertedPopulation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Population createEntity() {
        return new Population().tableName(DEFAULT_TABLE_NAME).attributNames(DEFAULT_ATTRIBUT_NAMES).typeNames(DEFAULT_TYPE_NAMES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Population createUpdatedEntity() {
        return new Population().tableName(UPDATED_TABLE_NAME).attributNames(UPDATED_ATTRIBUT_NAMES).typeNames(UPDATED_TYPE_NAMES);
    }

    @BeforeEach
    public void initTest() {
        population = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPopulation != null) {
            populationRepository.delete(insertedPopulation);
            insertedPopulation = null;
        }
    }

    @Test
    @Transactional
    void createPopulation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);
        var returnedPopulationDTO = om.readValue(
            restPopulationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PopulationDTO.class
        );

        // Validate the Population in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPopulation = populationMapper.toEntity(returnedPopulationDTO);
        assertPopulationUpdatableFieldsEquals(returnedPopulation, getPersistedPopulation(returnedPopulation));

        insertedPopulation = returnedPopulation;
    }

    @Test
    @Transactional
    void createPopulationWithExistingId() throws Exception {
        // Create the Population with an existing ID
        population.setId(1L);
        PopulationDTO populationDTO = populationMapper.toDto(population);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPopulationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPopulations() throws Exception {
        // Initialize the database
        insertedPopulation = populationRepository.saveAndFlush(population);

        // Get all the populationList
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(population.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].attributNames").value(hasItem(DEFAULT_ATTRIBUT_NAMES)))
            .andExpect(jsonPath("$.[*].typeNames").value(hasItem(DEFAULT_TYPE_NAMES)));
    }

    @Test
    @Transactional
    void getPopulation() throws Exception {
        // Initialize the database
        insertedPopulation = populationRepository.saveAndFlush(population);

        // Get the population
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL_ID, population.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(population.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.attributNames").value(DEFAULT_ATTRIBUT_NAMES))
            .andExpect(jsonPath("$.typeNames").value(DEFAULT_TYPE_NAMES));
    }

    @Test
    @Transactional
    void getNonExistingPopulation() throws Exception {
        // Get the population
        restPopulationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPopulation() throws Exception {
        // Initialize the database
        insertedPopulation = populationRepository.saveAndFlush(population);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the population
        Population updatedPopulation = populationRepository.findById(population.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPopulation are not directly saved in db
        em.detach(updatedPopulation);
        updatedPopulation.tableName(UPDATED_TABLE_NAME).attributNames(UPDATED_ATTRIBUT_NAMES).typeNames(UPDATED_TYPE_NAMES);
        PopulationDTO populationDTO = populationMapper.toDto(updatedPopulation);

        restPopulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, populationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPopulationToMatchAllProperties(updatedPopulation);
    }

    @Test
    @Transactional
    void putNonExistingPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, populationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePopulationWithPatch() throws Exception {
        // Initialize the database
        insertedPopulation = populationRepository.saveAndFlush(population);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the population using partial update
        Population partialUpdatedPopulation = new Population();
        partialUpdatedPopulation.setId(population.getId());

        partialUpdatedPopulation.typeNames(UPDATED_TYPE_NAMES);

        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPopulation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPopulation))
            )
            .andExpect(status().isOk());

        // Validate the Population in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPopulationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPopulation, population),
            getPersistedPopulation(population)
        );
    }

    @Test
    @Transactional
    void fullUpdatePopulationWithPatch() throws Exception {
        // Initialize the database
        insertedPopulation = populationRepository.saveAndFlush(population);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the population using partial update
        Population partialUpdatedPopulation = new Population();
        partialUpdatedPopulation.setId(population.getId());

        partialUpdatedPopulation.tableName(UPDATED_TABLE_NAME).attributNames(UPDATED_ATTRIBUT_NAMES).typeNames(UPDATED_TYPE_NAMES);

        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPopulation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPopulation))
            )
            .andExpect(status().isOk());

        // Validate the Population in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPopulationUpdatableFieldsEquals(partialUpdatedPopulation, getPersistedPopulation(partialUpdatedPopulation));
    }

    @Test
    @Transactional
    void patchNonExistingPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, populationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePopulation() throws Exception {
        // Initialize the database
        insertedPopulation = populationRepository.saveAndFlush(population);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the population
        restPopulationMockMvc
            .perform(delete(ENTITY_API_URL_ID, population.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return populationRepository.count();
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

    protected Population getPersistedPopulation(Population population) {
        return populationRepository.findById(population.getId()).orElseThrow();
    }

    protected void assertPersistedPopulationToMatchAllProperties(Population expectedPopulation) {
        assertPopulationAllPropertiesEquals(expectedPopulation, getPersistedPopulation(expectedPopulation));
    }

    protected void assertPersistedPopulationToMatchUpdatableProperties(Population expectedPopulation) {
        assertPopulationAllUpdatablePropertiesEquals(expectedPopulation, getPersistedPopulation(expectedPopulation));
    }
}
