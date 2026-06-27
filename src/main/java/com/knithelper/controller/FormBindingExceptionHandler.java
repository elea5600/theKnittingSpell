package com.knithelper.controller;

import com.knithelper.model.ShapingInput;
import com.knithelper.model.StitchCalculationInput;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FormBindingExceptionHandler {

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, Model model) {
        model.addAllAttributes(ex.getModel());

        Object target = ex.getTarget();
        if (target instanceof ShapingInput) {
            model.addAttribute("stitchInput", new StitchCalculationInput());
            model.addAttribute("activeTab", "shaping");
        } else if (target instanceof StitchCalculationInput) {
            model.addAttribute("shapingInput", new ShapingInput());
            model.addAttribute("activeTab", "stitches");
        } else {
            model.addAttribute("stitchInput", new StitchCalculationInput());
            model.addAttribute("shapingInput", new ShapingInput());
        }

        return "index";
    }
}