package com.knithelper.model;

/**
 * Result of a stitch and row count calculation.
 */
public class StitchCalculationResult {

    private int stitchCount;
    private int rowCount;
    private double desiredWidthCm;
    private double desiredLengthCm;
    private double stitchesPer10Cm;
    private double rowsPer10Cm;

    public StitchCalculationResult() {
    }

    public StitchCalculationResult(int stitchCount, int rowCount, double desiredWidthCm,
                                    double desiredLengthCm, double stitchesPer10Cm, double rowsPer10Cm) {
        this.stitchCount = stitchCount;
        this.rowCount = rowCount;
        this.desiredWidthCm = desiredWidthCm;
        this.desiredLengthCm = desiredLengthCm;
        this.stitchesPer10Cm = stitchesPer10Cm;
        this.rowsPer10Cm = rowsPer10Cm;
    }

    public int getStitchCount() {
        return stitchCount;
    }

    public void setStitchCount(int stitchCount) {
        this.stitchCount = stitchCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public double getDesiredWidthCm() {
        return desiredWidthCm;
    }

    public void setDesiredWidthCm(double desiredWidthCm) {
        this.desiredWidthCm = desiredWidthCm;
    }

    public double getDesiredLengthCm() {
        return desiredLengthCm;
    }

    public void setDesiredLengthCm(double desiredLengthCm) {
        this.desiredLengthCm = desiredLengthCm;
    }

    public double getStitchesPer10Cm() {
        return stitchesPer10Cm;
    }

    public void setStitchesPer10Cm(double stitchesPer10Cm) {
        this.stitchesPer10Cm = stitchesPer10Cm;
    }

    public double getRowsPer10Cm() {
        return rowsPer10Cm;
    }

    public void setRowsPer10Cm(double rowsPer10Cm) {
        this.rowsPer10Cm = rowsPer10Cm;
    }
}
