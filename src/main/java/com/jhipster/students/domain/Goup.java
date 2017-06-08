package com.jhipster.students.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Goup.
 */
@Entity
@Table(name = "goup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "goup")
public class Goup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "goup")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Student> students = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public Goup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Goup students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Goup addStudent(Student student) {
        this.students.add(student);
        student.setGoup(this);
        return this;
    }

    public Goup removeStudent(Student student) {
        this.students.remove(student);
        student.setGoup(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Goup goup = (Goup) o;
        if (goup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, goup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Goup{" +
            "id=" + id +
            ", groupName='" + groupName + "'" +
            '}';
    }
}
