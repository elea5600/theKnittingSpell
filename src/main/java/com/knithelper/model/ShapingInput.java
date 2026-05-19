package com.knithelper.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Input model for shaping (increase/decrease) calculations.
 */
public class ShapingInput {

    @NotNull(message = "Current stitch count is required")
    @Min(value = 1, message = "Current stitch count must be at least 1")
    private Integer currentStitches;

    @NotNull(message = "Target stitch count is required")
    @Min(value = 1, message = "Target stitch count must be at least 1")
    private Integer targetStitches;

    @NotNull(message = "Number of rows is required")
    @Positive(message = "Number of rows must be positive")
    private Integer numberOfRows;
    

    private Boolean symmetricalShaping; // true if shaping on both sides, false if on one side only

    public ShapingInput() {
    }

    public ShapingInput(Integer currentStitches, Integer targetStitches, Integer numberOfRows) {
        this.currentStitches = currentStitches;
        this.targetStitches = targetStitches;
        this.numberOfRows = numberOfRows;
    }

    public ShapingInput(Integer currentStitches, Integer targetStitches, Integer numberOfRows, Boolean symmetricalShaping) {
        this.currentStitches = currentStitches;
        this.targetStitches = targetStitches;
        this.numberOfRows = numberOfRows;
        this.symmetricalShaping = symmetricalShaping;
    }

    public Integer getCurrentStitches() {
        return currentStitches;
    }

    public void setCurrentStitches(Integer currentStitches) {
        this.currentStitches = currentStitches;
    }

    public Integer getTargetStitches() {
        return targetStitches;
    }

    public void setTargetStitches(Integer targetStitches) {
        this.targetStitches = targetStitches;
    }

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public Boolean getSymmetricalShaping() {
        return symmetricalShaping;
    }

    public void setSymmetricalShaping(Boolean symmetricalShaping) {
        this.symmetricalShaping = symmetricalShaping;
    }
}
