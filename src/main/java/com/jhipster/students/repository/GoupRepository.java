package com.jhipster.students.repository;

import com.jhipster.students.domain.Goup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Goup entity.
 */
@SuppressWarnings("unused")
public interface GoupRepository extends JpaRepository<Goup,Long> {

}
