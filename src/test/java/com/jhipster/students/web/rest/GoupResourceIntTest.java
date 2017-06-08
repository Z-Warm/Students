package com.jhipster.students.web.rest;

import com.jhipster.students.StudentsApp;

import com.jhipster.students.domain.Goup;
import com.jhipster.students.domain.Student;
import com.jhipster.students.repository.GoupRepository;
import com.jhipster.students.repository.search.GoupSearchRepository;
import com.jhipster.students.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GoupResource REST controller.
 *
 * @see GoupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentsApp.class)
public class GoupResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    @Autowired
    private GoupRepository goupRepository;

    @Autowired
    private GoupSearchRepository goupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGoupMockMvc;

    private Goup goup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GoupResource goupResource = new GoupResource(goupRepository, goupSearchRepository);
        this.restGoupMockMvc = MockMvcBuilders.standaloneSetup(goupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goup createEntity(EntityManager em) {
        Goup goup = new Goup()
            .groupName(DEFAULT_GROUP_NAME);
        // Add required entity
        Student student = StudentResourceIntTest.createEntity(em);
        em.persist(student);
        em.flush();
        goup.getStudents().add(student);
        return goup;
    }

    @Before
    public void initTest() {
        goupSearchRepository.deleteAll();
        goup = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoup() throws Exception {
        int databaseSizeBeforeCreate = goupRepository.findAll().size();

        // Create the Goup
        restGoupMockMvc.perform(post("/api/goups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goup)))
            .andExpect(status().isCreated());

        // Validate the Goup in the database
        List<Goup> goupList = goupRepository.findAll();
        assertThat(goupList).hasSize(databaseSizeBeforeCreate + 1);
        Goup testGoup = goupList.get(goupList.size() - 1);
        assertThat(testGoup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);

        // Validate the Goup in Elasticsearch
        Goup goupEs = goupSearchRepository.findOne(testGoup.getId());
        assertThat(goupEs).isEqualToComparingFieldByField(testGoup);
    }

    @Test
    @Transactional
    public void createGoupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goupRepository.findAll().size();

        // Create the Goup with an existing ID
        goup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoupMockMvc.perform(post("/api/goups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goup)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Goup> goupList = goupRepository.findAll();
        assertThat(goupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGoups() throws Exception {
        // Initialize the database
        goupRepository.saveAndFlush(goup);

        // Get all the goupList
        restGoupMockMvc.perform(get("/api/goups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGoup() throws Exception {
        // Initialize the database
        goupRepository.saveAndFlush(goup);

        // Get the goup
        restGoupMockMvc.perform(get("/api/goups/{id}", goup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoup() throws Exception {
        // Get the goup
        restGoupMockMvc.perform(get("/api/goups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoup() throws Exception {
        // Initialize the database
        goupRepository.saveAndFlush(goup);
        goupSearchRepository.save(goup);
        int databaseSizeBeforeUpdate = goupRepository.findAll().size();

        // Update the goup
        Goup updatedGoup = goupRepository.findOne(goup.getId());
        updatedGoup
            .groupName(UPDATED_GROUP_NAME);

        restGoupMockMvc.perform(put("/api/goups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoup)))
            .andExpect(status().isOk());

        // Validate the Goup in the database
        List<Goup> goupList = goupRepository.findAll();
        assertThat(goupList).hasSize(databaseSizeBeforeUpdate);
        Goup testGoup = goupList.get(goupList.size() - 1);
        assertThat(testGoup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);

        // Validate the Goup in Elasticsearch
        Goup goupEs = goupSearchRepository.findOne(testGoup.getId());
        assertThat(goupEs).isEqualToComparingFieldByField(testGoup);
    }

    @Test
    @Transactional
    public void updateNonExistingGoup() throws Exception {
        int databaseSizeBeforeUpdate = goupRepository.findAll().size();

        // Create the Goup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGoupMockMvc.perform(put("/api/goups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goup)))
            .andExpect(status().isCreated());

        // Validate the Goup in the database
        List<Goup> goupList = goupRepository.findAll();
        assertThat(goupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGoup() throws Exception {
        // Initialize the database
        goupRepository.saveAndFlush(goup);
        goupSearchRepository.save(goup);
        int databaseSizeBeforeDelete = goupRepository.findAll().size();

        // Get the goup
        restGoupMockMvc.perform(delete("/api/goups/{id}", goup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean goupExistsInEs = goupSearchRepository.exists(goup.getId());
        assertThat(goupExistsInEs).isFalse();

        // Validate the database is empty
        List<Goup> goupList = goupRepository.findAll();
        assertThat(goupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGoup() throws Exception {
        // Initialize the database
        goupRepository.saveAndFlush(goup);
        goupSearchRepository.save(goup);

        // Search the goup
        restGoupMockMvc.perform(get("/api/_search/goups?query=id:" + goup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Goup.class);
    }
}
