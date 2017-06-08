package com.jhipster.students.cucumber.stepdefs;

import com.jhipster.students.StudentsApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = StudentsApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
