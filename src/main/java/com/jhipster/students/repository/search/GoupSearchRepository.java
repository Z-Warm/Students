package com.jhipster.students.repository.search;

import com.jhipster.students.domain.Goup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Goup entity.
 */
public interface GoupSearchRepository extends ElasticsearchRepository<Goup, Long> {
}
