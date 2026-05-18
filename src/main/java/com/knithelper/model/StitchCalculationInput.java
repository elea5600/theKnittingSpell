package com.knithelper.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Input model for stitch and row count calculations.
 * Gauge is expressed as stitches (or rows) per 10 cm.
 */
public class StitchCalculationInput {

    @NotNull(message = "Stitches per 10 cm is required")
    @DecimalMin(value = "0.1", message = "Stitches per 10 cm must be greater than 0")
    private Double stitchesPer10Cm;

    @NotNull(message = "Rows per 10 cm is required")
    @DecimalMin(value = "0.1", message = "Rows per 10 cm must be greater than 0")
    private Double rowsPer10Cm;

    @NotNull(message = "Desired width is required")
    @DecimalMin(value = "0.1", message = "Desired width must be greater than 0")
    private Double desiredWidthCm;

    @NotNull(message = "Desired length is required")
    @DecimalMin(value = "0.1", message = "Desired length must be greater than 0")
    private Double desiredLengthCm;

    public StitchCalculationInput() {
    }

    public StitchCalculationInput(Double stitchesPer10Cm, Double rowsPer10Cm,
                                   Double desiredWidthCm, Double desiredLengthCm) {
        this.stitchesPer10Cm = stitchesPer10Cm;
        this.rowsPer10Cm = rowsPer10Cm;
        this.desiredWidthCm = desiredWidthCm;
        this.desiredLengthCm = desiredLengthCm;
    }

    public Double getStitchesPer10Cm() {
        return stitchesPer10Cm;
    }

    public void setStitchesPer10Cm(Double stitchesPer10Cm) {
        this.stitchesPer10Cm = stitchesPer10Cm;
    }

    public Double getRowsPer10Cm() {
        return rowsPer10Cm;
    }

    public void setRowsPer10Cm(Double rowsPer10Cm) {
        this.rowsPer10Cm = rowsPer10Cm;
    }

    public Double getDesiredWidthCm() {
        return desiredWidthCm;
    }

    public void setDesiredWidthCm(Double desiredWidthCm) {
        this.desiredWidthCm = desiredWidthCm;
    }

    public Double getDesiredLengthCm() {
        return desiredLengthCm;
    }

    public void setDesiredLengthCm(Double desiredLengthCm) {
        this.desiredLengthCm = desiredLengthCm;
    }
}
