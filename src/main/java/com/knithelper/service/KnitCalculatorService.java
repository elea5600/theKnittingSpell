package com.knithelper.service;

import com.knithelper.model.ShapingInput;
import com.knithelper.model.ShapingResult;
import com.knithelper.model.ShapingResult.ShapingType;
import com.knithelper.model.StitchCalculationInput;
import com.knithelper.model.StitchCalculationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service containing knitting calculation logic.
 */
@Service
public class KnitCalculatorService {

    /**
     * Calculates the stitch count and row count needed for a piece of given
     * dimensions based on a tension/gauge swatch.
     *
     * @param input gauge and desired dimensions
     * @return the required stitch and row counts (rounded to nearest whole number)
     */
    public StitchCalculationResult calculateStitchesAndRows(StitchCalculationInput input) {
        int stitchCount = (int) Math.round(
                input.getStitchesPer10Cm() * input.getDesiredWidthCm() / 10.0);
        int rowCount = (int) Math.round(
                input.getRowsPer10Cm() * input.getDesiredLengthCm() / 10.0);

        return new StitchCalculationResult(
                stitchCount,
                rowCount,
                input.getDesiredWidthCm(),
                input.getDesiredLengthCm(),
                input.getStitchesPer10Cm(),
                input.getRowsPer10Cm()
        );
    }

    /**
     * Calculates how to distribute increases or decreases evenly over a
     * given number of rows.
     *
     * <p>Uses the "every N rows, except some every N+1 rows" approach so that
     * the shaping events are spread as evenly as possible.</p>
     *
     * @param input current stitches, target stitches, and available rows
     * @return shaping result with instructions
     */
    public ShapingResult calculateShaping(ShapingInput input) {
        int current = input.getCurrentStitches();
        int target = input.getTargetStitches();
        int rows = input.getNumberOfRows();

        if (current == target) {
            List<String> instructions = new ArrayList<>();
            instructions.add("No shaping needed – stitch count is already " + current + ".");
            return new ShapingResult(ShapingType.NO_CHANGE, 0, current, target, rows, 0, 0, instructions);
        }

        ShapingType type = (target > current) ? ShapingType.INCREASE : ShapingType.DECREASE;
        // Each shaping row changes by 1 stitch on each side (2 stitches total per shaping row).
        int totalChanges = Math.abs(target - current);

        // Each shaping event accounts for 2 stitches (one at each edge).
        // If the difference is odd we shape one extra stitch at the end separately.
        int shapingEvents = totalChanges / 2;
        boolean hasRemainder = (totalChanges % 2) != 0;

        List<String> instructions = new ArrayList<>();

        if (shapingEvents == 0) {
            // Only 1 stitch to change – work a single shaping row.
            String action = (type == ShapingType.INCREASE) ? "increase" : "decrease";
            instructions.add("Work 1 " + action + " row (1 stitch at one edge).");
            return new ShapingResult(type, totalChanges, current, target, rows, rows, 0, instructions);
        }

        // Spread shapingEvents shaping rows across 'rows' total rows.
        // baseInterval = rows / shapingEvents (integer division).
        // longerIntervalCount shaping rows use baseInterval+1; the rest use baseInterval.
        int baseInterval = rows / shapingEvents;
        int longerIntervalCount = rows % shapingEvents;
        int shorterIntervalCount = shapingEvents - longerIntervalCount;

        String action = (type == ShapingType.INCREASE) ? "increase" : "decrease";
        String actionLabel = (type == ShapingType.INCREASE) ? "Increase" : "Decrease";

        if (longerIntervalCount == 0) {
            instructions.add(actionLabel + " 1 stitch at each end every " + baseInterval
                    + " rows, " + shapingEvents + " times.");
        } else {
            if (shorterIntervalCount > 0) {
                instructions.add(actionLabel + " 1 stitch at each end every " + baseInterval
                        + " rows, " + shorterIntervalCount + " time(s).");
            }
            instructions.add(actionLabel + " 1 stitch at each end every " + (baseInterval + 1)
                    + " rows, " + longerIntervalCount + " time(s).");
        }

        if (hasRemainder) {
            instructions.add("Work 1 additional " + action + " row for the remaining 1 stitch (one edge only).");
        }

        instructions.add("Total rows used: " + rows + ". Final stitch count: " + target + ".");

        return new ShapingResult(type, totalChanges, current, target, rows,
                baseInterval, longerIntervalCount, instructions);
    }
}
