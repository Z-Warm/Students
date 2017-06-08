package com.jhipster.students.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.students.domain.Goup;

import com.jhipster.students.repository.GoupRepository;
import com.jhipster.students.repository.search.GoupSearchRepository;
import com.jhipster.students.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Goup.
 */
@RestController
@RequestMapping("/api")
public class GoupResource {

    private final Logger log = LoggerFactory.getLogger(GoupResource.class);

    private static final String ENTITY_NAME = "goup";
        
    private final GoupRepository goupRepository;

    private final GoupSearchRepository goupSearchRepository;

    public GoupResource(GoupRepository goupRepository, GoupSearchRepository goupSearchRepository) {
        this.goupRepository = goupRepository;
        this.goupSearchRepository = goupSearchRepository;
    }

    /**
     * POST  /goups : Create a new goup.
     *
     * @param goup the goup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new goup, or with status 400 (Bad Request) if the goup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/goups")
    @Timed
    public ResponseEntity<Goup> createGoup(@Valid @RequestBody Goup goup) throws URISyntaxException {
        log.debug("REST request to save Goup : {}", goup);
        if (goup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new goup cannot already have an ID")).body(null);
        }
        Goup result = goupRepository.save(goup);
        goupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/goups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goups : Updates an existing goup.
     *
     * @param goup the goup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated goup,
     * or with status 400 (Bad Request) if the goup is not valid,
     * or with status 500 (Internal Server Error) if the goup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/goups")
    @Timed
    public ResponseEntity<Goup> updateGoup(@Valid @RequestBody Goup goup) throws URISyntaxException {
        log.debug("REST request to update Goup : {}", goup);
        if (goup.getId() == null) {
            return createGoup(goup);
        }
        Goup result = goupRepository.save(goup);
        goupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, goup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goups : get all the goups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of goups in body
     */
    @GetMapping("/goups")
    @Timed
    public List<Goup> getAllGoups() {
        log.debug("REST request to get all Goups");
        List<Goup> goups = goupRepository.findAll();
        return goups;
    }

    /**
     * GET  /goups/:id : get the "id" goup.
     *
     * @param id the id of the goup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the goup, or with status 404 (Not Found)
     */
    @GetMapping("/goups/{id}")
    @Timed
    public ResponseEntity<Goup> getGoup(@PathVariable Long id) {
        log.debug("REST request to get Goup : {}", id);
        Goup goup = goupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(goup));
    }

    /**
     * DELETE  /goups/:id : delete the "id" goup.
     *
     * @param id the id of the goup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/goups/{id}")
    @Timed
    public ResponseEntity<Void> deleteGoup(@PathVariable Long id) {
        log.debug("REST request to delete Goup : {}", id);
        goupRepository.delete(id);
        goupSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/goups?query=:query : search for the goup corresponding
     * to the query.
     *
     * @param query the query of the goup search 
     * @return the result of the search
     */
    @GetMapping("/_search/goups")
    @Timed
    public List<Goup> searchGoups(@RequestParam String query) {
        log.debug("REST request to search Goups for query {}", query);
        return StreamSupport
            .stream(goupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
