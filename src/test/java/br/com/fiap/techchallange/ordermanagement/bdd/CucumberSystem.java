package br.com.fiap.techchallange.ordermanagement.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import org.springframework.test.context.ContextConfiguration;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@CucumberContextConfiguration
@ContextConfiguration(classes = { CucumberSpringConfig.class, SharedData.class })
public class CucumberSystem {
}
