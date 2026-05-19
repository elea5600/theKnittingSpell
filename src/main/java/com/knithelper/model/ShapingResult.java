package com.knithelper.model;

import java.util.List;

/**
 * Result of a shaping (increase/decrease) calculation.
 * Describes how to evenly distribute increases or decreases over a set of rows.
 */
public class ShapingResult {

    public enum ShapingType {
        INCREASE, DECREASE, NO_CHANGE;

        @Override
        public String toString() {
            switch (this) {
                case INCREASE:
                    return "Increase";
                case DECREASE:
                    return "Decrease";
                case NO_CHANGE:
                    return "No change";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
    

    private ShapingType shapingType;
    private int totalChanges;
    private int currentStitches;
    private int targetStitches;
    private int numberOfRows;

    /** Number of rows between each shaping row (the base interval). */
    private int baseInterval;

    /**
     * Some shaping rows occur every {@code baseInterval} rows, others every
     * {@code baseInterval + 1} rows.  This field holds the count of shaping
     * rows that use the longer {@code baseInterval + 1} interval.
     */
    private int longerIntervalCount;

    /**
     * Human-readable shaping instructions summarising the result.
     */
    private List<String> instructions;

    public ShapingResult() {
    }

    public ShapingResult(ShapingType shapingType, int totalChanges, int currentStitches,
                          int targetStitches, int numberOfRows, int baseInterval,
                          int longerIntervalCount, List<String> instructions) {
        this.shapingType = shapingType;
        this.totalChanges = totalChanges;
        this.currentStitches = currentStitches;
        this.targetStitches = targetStitches;
        this.numberOfRows = numberOfRows;
        this.baseInterval = baseInterval;
        this.longerIntervalCount = longerIntervalCount;
        this.instructions = instructions;
    }

    public ShapingType getShapingType() {
        return shapingType;
    }

    public void setShapingType(ShapingType shapingType) {
        this.shapingType = shapingType;
    }

    public int getTotalChanges() {
        return totalChanges;
    }

    public void setTotalChanges(int totalChanges) {
        this.totalChanges = totalChanges;
    }

    public int getCurrentStitches() {
        return currentStitches;
    }

    public void setCurrentStitches(int currentStitches) {
        this.currentStitches = currentStitches;
    }

    public int getTargetStitches() {
        return targetStitches;
    }

    public void setTargetStitches(int targetStitches) {
        this.targetStitches = targetStitches;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getBaseInterval() {
        return baseInterval;
    }

    public void setBaseInterval(int baseInterval) {
        this.baseInterval = baseInterval;
    }

    public int getLongerIntervalCount() {
        return longerIntervalCount;
    }

    public void setLongerIntervalCount(int longerIntervalCount) {
        this.longerIntervalCount = longerIntervalCount;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
    
}
