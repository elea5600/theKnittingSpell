package com.knithelper.controller;

import com.knithelper.model.ShapingInput;
import com.knithelper.model.ShapingResult;
import com.knithelper.model.StitchCalculationInput;
import com.knithelper.model.StitchCalculationResult;
import com.knithelper.service.KnitCalculatorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * MVC controller for the knitting calculator web application.
 */
@Controller
public class KnitCalculatorController {

    private final KnitCalculatorService calculatorService;

    public KnitCalculatorController(KnitCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("stitchInput", new StitchCalculationInput());
        model.addAttribute("shapingInput", new ShapingInput());
        return "index";
    }

    @PostMapping("/calculate/stitches")
    public String calculateStitches(
            @Valid @ModelAttribute("stitchInput") StitchCalculationInput input,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("shapingInput", new ShapingInput());
        model.addAttribute("activeTab", "stitches");

        if (bindingResult.hasErrors()) {
            return "index";
        }

        StitchCalculationResult result = calculatorService.calculateStitchesAndRows(input);
        model.addAttribute("stitchResult", result);
        return "index";
    }

    @PostMapping("/calculate/shaping")
    public String calculateShaping(
            @Valid @ModelAttribute("shapingInput") ShapingInput input,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("stitchInput", new StitchCalculationInput());
        model.addAttribute("activeTab", "shaping");

        if (bindingResult.hasErrors()) {
            return "index";
        }

        ShapingResult result = calculatorService.calculateShaping(input);
        model.addAttribute("shapingResult", result);
        return "index";
    }
}
