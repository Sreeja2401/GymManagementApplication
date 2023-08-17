package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.request.TrainingDetails;
import com.epam.service.TrainingServiceImpl;

@WebMvcTest(TrainingRestController.class)
class TrainingRestControllerTest {

    @MockBean
    TrainingServiceImpl trainingService;

    @Autowired
    MockMvc mockMvc;
    
    @Test
    void testAddTraining() throws Exception {
        mockMvc.perform(post("/gym/training"))
                .andExpect(status().isOk());
        Mockito.verify(trainingService, Mockito.times(1)).addTraining(any(TrainingDetails.class));
    }
}
