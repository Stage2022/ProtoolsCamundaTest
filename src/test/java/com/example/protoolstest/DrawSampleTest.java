package com.example.protoolstest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.bpm.scenario.Scenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.camunda.bpm.scenario.ProcessScenario;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DrawSampleTest {

    DelegateExecution execution;
    HashMap<String, Object> variables = new HashMap<>();

    @InjectMocks
    private drawSample drawSample;

    @Mock
    private ProcessScenario scenario;

    @BeforeEach
    void setUp() { // Setup de l'environement d'exécution mock
        MockitoAnnotations.openMocks(this);

        when(scenario.waitsAtUserTask("ErrorTaskSampleSize")).thenReturn((task) -> {
            task.complete();
        });

    }

    @Test
    @Deployment(resources = {"processCut.bpmn"})
    void TestDrawSample() throws Exception{
        // Récupération de variables des task précédentes
        variables.put("sampleSize", "8");
        Scenario handler = Scenario.run(scenario).startByKey("cutProcess", variables).execute();
        verify(scenario, atMostOnce()).hasCompleted("DrawSampleID");
        verify(scenario).hasFinished("Sortie");

    }

    @Test
    @Deployment(resources = {"processCut.bpmn"})
    void TestDrawSampleError() throws Exception{
        // Récupération de variables des task précédentes
        variables.put("sampleSize", "4");
        Scenario handler = Scenario.run(scenario).startByKey("cutProcess", variables).execute();
        verify(scenario).hasCompleted("ErrorTaskSampleSize");

    }
}
