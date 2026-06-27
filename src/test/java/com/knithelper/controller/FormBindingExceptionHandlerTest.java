package com.knithelper.controller;

import com.knithelper.service.KnitCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class FormBindingExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private KnitCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new KnitCalculatorController(calculatorService))
                .setControllerAdvice(new FormBindingExceptionHandler())
                .build();
    }

    @Test
    void calculateShaping_overlongTargetStitches_returnsIndexWithFieldError() throws Exception {
        mockMvc.perform(post("/calculate/shaping")
                        .param("currentStitches", "80")
                        .param("targetStitches", "2222222222")
                        .param("numberOfRows", "40")
                        .param("symmetricalShaping", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("shapingInput", "targetStitches"));
    }

    @Test
    void calculateDimensions_overlongStitchesPer10Cm_returnsIndexWithFieldError() throws Exception {
        mockMvc.perform(post("/calculate/dimensions")
                        .param("stitchesPer10Cm", "22222222")
                        .param("rowsPer10Cm", "40")
                        .param("desiredWidthCm", "20")
                        .param("desiredLengthCm", "30"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("stitchInput", "stitchesPer10Cm"));
    }
}