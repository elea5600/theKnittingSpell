package com.knithelper.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Input model for stitch and row count calculations.
 * Gauge is expressed as stitches (or rows) per 10 cm.
 */
public class StitchCalculationInput {

    @NotNull(message = "Stitches per 10 cm is required")
    @DecimalMin(value = "0.1", message = "Stitches per 10 cm must be greater than 0")
    @DecimalMax(value = "999999.9", message = "Stitches per 10 cm is too large")
    private BigDecimal stitchesPer10Cm;

    @NotNull(message = "Rows per 10 cm is required")
    @DecimalMin(value = "0.1", message = "Rows per 10 cm must be greater than 0")
    @DecimalMax(value = "999999.9", message = "Rows per 10 cm is too large")
    private BigDecimal rowsPer10Cm;

    @NotNull(message = "Desired width is required")
    @DecimalMin(value = "0.1", message = "Desired width must be greater than 0")
    @DecimalMax(value = "999999.9", message = "Desired width is too large")
    private BigDecimal desiredWidthCm;

    @NotNull(message = "Desired length is required")
    @DecimalMin(value = "0.1", message = "Desired length must be greater than 0")
    @DecimalMax(value = "999999.9", message = "Desired length is too large")
    private BigDecimal desiredLengthCm;

    public StitchCalculationInput() {
    }

    public StitchCalculationInput(BigDecimal stitchesPer10Cm, BigDecimal rowsPer10Cm,
                                   BigDecimal desiredWidthCm, BigDecimal desiredLengthCm) {
        this.stitchesPer10Cm = stitchesPer10Cm;
        this.rowsPer10Cm = rowsPer10Cm;
        this.desiredWidthCm = desiredWidthCm;
        this.desiredLengthCm = desiredLengthCm;
    }

    public BigDecimal getStitchesPer10Cm() {
        return stitchesPer10Cm;
    }

    public void setStitchesPer10Cm(BigDecimal stitchesPer10Cm) {
        this.stitchesPer10Cm = stitchesPer10Cm;
    }

    public BigDecimal getRowsPer10Cm() {
        return rowsPer10Cm;
    }

    public void setRowsPer10Cm(BigDecimal rowsPer10Cm) {
        this.rowsPer10Cm = rowsPer10Cm;
    }

    public BigDecimal getDesiredWidthCm() {
        return desiredWidthCm;
    }

    public void setDesiredWidthCm(BigDecimal desiredWidthCm) {
        this.desiredWidthCm = desiredWidthCm;
    }

    public BigDecimal getDesiredLengthCm() {
        return desiredLengthCm;
    }

    public void setDesiredLengthCm(BigDecimal desiredLengthCm) {
        this.desiredLengthCm = desiredLengthCm;
    }
}
