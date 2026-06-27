package com.knithelper.service;

import com.knithelper.model.ShapingInput;
import com.knithelper.model.ShapingResult;
import com.knithelper.model.ShapingResult.ShapingType;
import com.knithelper.model.StitchCalculationInput;
import com.knithelper.model.StitchCalculationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        BigDecimal stitchCountValue = input.getStitchesPer10Cm()
            .multiply(input.getDesiredWidthCm())
            .divide(BigDecimal.TEN);
        BigDecimal rowCountValue = input.getRowsPer10Cm()
            .multiply(input.getDesiredLengthCm())
            .divide(BigDecimal.TEN);

        int stitchCount = stitchCountValue.setScale(0, java.math.RoundingMode.HALF_UP).intValueExact();
        int rowCount = rowCountValue.setScale(0, java.math.RoundingMode.HALF_UP).intValueExact();

        return new StitchCalculationResult(
                stitchCount,
                rowCount,
            input.getDesiredWidthCm().doubleValue(),
            input.getDesiredLengthCm().doubleValue(),
            input.getStitchesPer10Cm().doubleValue(),
            input.getRowsPer10Cm().doubleValue()
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
        Boolean symmetrical = input.getSymmetricalShaping() != null ? input.getSymmetricalShaping() : Boolean.FALSE;
        int stitchDifference = Math.abs(target - current);
        List<String> instructions = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (current == target) {    
            instructions.add("No shaping needed – stitch count is already " + current + ".");
            return new ShapingResult(ShapingType.NO_CHANGE, 0, current, target, rows, 0, 0, instructions, warnings);
        }

        if (symmetrical && (stitchDifference % 2 != 0)) {
            warnings.add("Symmetrical shaping selected but stitch difference is odd. " +
                "One extra stitch will be shaped at the end of the process.");
        }

        if (stitchDifference > rows) {
            warnings.add("More shaping events needed than available rows. " +
                "You will need to shape more than 1 stitch at a time in some rows.");
        }

        // if we increase or decrease generally (just for clearer instructions)
        ShapingType type = (target > current) ? ShapingType.INCREASE : ShapingType.DECREASE;
        int baseInterval = 1; // shape every row by default
        int additionalInterval = 0; // how often we need to do additional shaping to shape the remaining stitches within the available rows if we cannot shape every baseInterval rows
        int shapingsPerShapingRow = 1; // how many shapings we need to do in each shaping row to reach the target stitch count within the available rows when shaping on one side only
        int remainingStitches = 0; // how many shapings we have left to do after shaping every baseInterval rows by shapingsPerShapingRow stitches when shaping on one side only
        double remainingShapingperRow = 0; // how often we need to do additional shaping to shape the remaining stitches within the available rows when shaping on one side only
        double numberOfShapingsperRow = 0; // how often shaping must be done in a row when shaping on one side only - can be less than 1, then we do not shape every row, but every x rows

        if (!symmetrical) {
            
            numberOfShapingsperRow = (double) stitchDifference / rows; // how often shaping must be done in a row - can be less than 1, then we do not shape every row, but every x rows

            if (numberOfShapingsperRow < 1) { // do not shape every row, but every x rows
                baseInterval = (int) Math.ceil(1 / numberOfShapingsperRow);
                System.out.println("baseInterval for asymmetrical shaping: " + baseInterval);
            }
        
            if (((double) stitchDifference * baseInterval )== rows) { // if shaping every baseInterval rows gives us the exact number of shapings we need, then we can just do that
                if (baseInterval == 1) {
                    instructions.add(type.toString() + " 1 stitch every row.");
                } else {
                    instructions.add(type.toString() + " one 1 stitch every " + baseInterval + " rows.");
                }
                } else { 
                    // otherwise, we need to shape more than 1 stitch at a time
                    shapingsPerShapingRow = (int)  Math.floor((double) stitchDifference / (rows / baseInterval)); // how many shapings we need to do in each shaping row to reach the target stitch count within the available rows
                    //if we round then we might end up with too many or too few shapings, so we just round down and then do 1 additional shaping at the end if needed
                    if (shapingsPerShapingRow == 1) {
                        instructions.add(type.toString() + " 1 stitch every " + baseInterval + " rows.");
                    } else {
                        instructions.add(type.toString() + " " + shapingsPerShapingRow + " stitches every " + baseInterval + " rows.");
                    }
                    remainingStitches = stitchDifference - (shapingsPerShapingRow * (rows / baseInterval)); // how many shapings we have left to do after shaping every baseInterval rows by shapingsPerShapingRow stitches
                    if (remainingStitches > 0) {
                        remainingShapingperRow = (double) remainingStitches / rows;
                        additionalInterval = (int) Math.ceil(1.0 / remainingShapingperRow); // how often we need to do 1 additional shaping to shape the remaining stitches within the available rows
                        if (additionalInterval == 1) {
                            instructions.add("Additionally, " + type.toString().toLowerCase() + " 1 stitch every row.");
                        } else {
                            if (additionalInterval*2 > rows) { // if the additional shaping interval is larger than the number of rows, we just do the remaining shapings at the end of the process
                                instructions.add("Additionally, " + type.toString().toLowerCase() + " the remaining 1 stitch in the last row.");
                            } else {
                                instructions.add("Additionally, " + type.toString().toLowerCase() + " 1 stitch every " + additionalInterval + " rows.");
                            }
                        }
                    }
            }
        } else {

            numberOfShapingsperRow = (double) stitchDifference / rows / 2; 
            if (numberOfShapingsperRow < 1) { // do not shape every row, but every x rows
                baseInterval = (int) Math.ceil(1 / numberOfShapingsperRow); //Ceil to ensure we do not end up with too many shapings if we round
                System.out.println("baseInterval for symmetrical shaping: " + baseInterval);
            }

            // For symmetrical shaping, we shape on both sides, so we need to do half the number of shapings on each side.
            // This means we can shape every baseInterval rows, but we need to shape 2 stitches at a time (1 on each side).
            if (((double) stitchDifference * baseInterval / 2 )== rows) { // if shaping every baseInterval rows gives us the exact number of shapings we need, then we can just do that
                if (baseInterval == 1) {
                    instructions.add(type.toString() + " 1 stitch on each side every row.");
                } else {
                    instructions.add(type.toString() + " 1 stitch on each side every " + baseInterval + " rows.");
                }
            } else { 
                // otherwise, we need to shape more than 1 stitch at a time
                shapingsPerShapingRow = (int) Math.floor((double) stitchDifference / (2 * (rows / baseInterval))); // how many shapings we need to do in each shaping row
                //if we round then we might end up with too many or too few shapings, so we just round down and then do 1 additional shaping at the end if needed
                if (shapingsPerShapingRow == 1) {
                    instructions.add(type.toString() + " 1 stitch on each side every " + baseInterval + " rows.");
                } else {
                    instructions.add(type.toString() + " " + shapingsPerShapingRow + " stitches on each side every " + baseInterval + " rows.");
                }
                remainingStitches = stitchDifference - (shapingsPerShapingRow * 2 * (rows / baseInterval)); // how many shapings we have left to do after shaping every baseInterval rows by shapingsPerShapingRow stitches on each side
                System.out.println("remainingStitches for symmetrical shaping: " + remainingStitches);

                if (remainingStitches == 1) {
                    instructions.add("Additionally, " + type.toString().toLowerCase() + " the remaining 1 stitch on only one side in the last row.");
                }

                if (remainingStitches > 1) {
                    remainingShapingperRow = (double) remainingStitches / rows / 2;
                    additionalInterval = (int) Math.ceil(1.0 / remainingShapingperRow); // how often we need to do 1 additional shaping on each side to shape the remaining stitches within the available rows
                    System.out.println("additionalInterval for symmetrical shaping: " + additionalInterval);
                    if (additionalInterval == 1) {
                        instructions.add("Additionally, " + type.toString().toLowerCase() + " 1 stitch on each side every row.");
                    } else {
                            if (additionalInterval*2 > rows) { // if the additional shaping interval is larger than the number of rows, we just do the remaining shapings at the end of the process
                                instructions.add("Additionally, " + type.toString().toLowerCase() + " 1 stitch on each side in the last row.");
                            } else {
                                instructions.add("Additionally, " + type.toString().toLowerCase() + " 1 stitch on each side every " + additionalInterval + " rows.");
                            }
                    }
                }
            }
        }

        return new ShapingResult(type, stitchDifference, current, target, rows,
            baseInterval, additionalInterval, instructions, warnings);
    }
}
