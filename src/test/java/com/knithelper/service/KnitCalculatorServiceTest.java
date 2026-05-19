package com.knithelper.service;

import com.knithelper.model.ShapingInput;
import com.knithelper.model.ShapingResult;
import com.knithelper.model.ShapingResult.ShapingType;
import com.knithelper.model.StitchCalculationInput;
import com.knithelper.model.StitchCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnitCalculatorServiceTest {

    private KnitCalculatorService service;

    @BeforeEach
    void setUp() {
        service = new KnitCalculatorService();
    }

    // ──────────────────────────────────────────────────────────
    // Stitch & Row Count Tests
    // ──────────────────────────────────────────────────────────

    @Test
    void calculateStitchesAndRows_basicGauge() {
        // 22 stitches per 10 cm, 50 cm wide → 22 * 50 / 10 = 110 stitches
        // 28 rows per 10 cm, 60 cm long  → 28 * 60 / 10 = 168 rows
        StitchCalculationInput input = new StitchCalculationInput(22.0, 28.0, 50.0, 60.0);
        StitchCalculationResult result = service.calculateStitchesAndRows(input);

        assertEquals(110, result.getStitchCount());
        assertEquals(168, result.getRowCount());
    }

    @Test
    void calculateStitchesAndRows_roundsToNearestInteger() {
        // 22 stitches per 10 cm, 15 cm wide → 22 * 15 / 10 = 33.0 exact
        // 22 rows per 10 cm, 13 cm → 22 * 13 / 10 = 28.6 → rounds to 29
        StitchCalculationInput input = new StitchCalculationInput(22.0, 22.0, 15.0, 13.0);
        StitchCalculationResult result = service.calculateStitchesAndRows(input);

        assertEquals(33, result.getStitchCount());
        assertEquals(29, result.getRowCount());
    }

    @Test
    void calculateStitchesAndRows_preservesDimensions() {
        StitchCalculationInput input = new StitchCalculationInput(20.0, 30.0, 45.0, 70.0);
        StitchCalculationResult result = service.calculateStitchesAndRows(input);

        assertEquals(45.0, result.getDesiredWidthCm(), 0.001);
        assertEquals(70.0, result.getDesiredLengthCm(), 0.001);
        assertEquals(20.0, result.getStitchesPer10Cm(), 0.001);
        assertEquals(30.0, result.getRowsPer10Cm(), 0.001);
    }

    // ──────────────────────────────────────────────────────────
    // Shaping Tests – No Change
    // ──────────────────────────────────────────────────────────

    @Test
    void calculateShaping_noChange_whenCurrentEqualsTarget() {
        ShapingInput input = new ShapingInput(50, 50, 20);
        ShapingResult result = service.calculateShaping(input);

        assertEquals(ShapingType.NO_CHANGE, result.getShapingType());
        assertEquals(0, result.getTotalChanges());
        assertFalse(result.getInstructions().isEmpty());
    }

    // ──────────────────────────────────────────────────────────
    // Shaping Tests – Decrease
    // ──────────────────────────────────────────────────────────

    @Test
    void calculateShaping_decrease_returnsDecreaseType() {
        ShapingInput input = new ShapingInput(80, 60, 40);
        ShapingResult result = service.calculateShaping(input);

        assertEquals(ShapingType.DECREASE, result.getShapingType());
        assertEquals(20, result.getTotalChanges());
        assertEquals(80, result.getCurrentStitches());
        assertEquals(60, result.getTargetStitches());
        assertEquals(40, result.getNumberOfRows());
    }

    @Test
    void calculateShaping_decrease_evenDistribution() {
        // 20 stitches to decrease (10 shaping events), 40 rows
        // → every 4 rows (40 / 10 = 4), 0 longer intervals
        ShapingInput input = new ShapingInput(80, 60, 40);
        ShapingResult result = service.calculateShaping(input);

        // My logic decreases every 2 rows 1 stitch!
        assertEquals(2, result.getBaseInterval());
        assertEquals(0, result.getLongerIntervalCount());
    }

    @Test
    void calculateShaping_decrease_unevenDistribution() {
        // 10 stitches to decrease (5 shaping events), 22 rows
        // baseInterval = 22 / 5 = 4, longerIntervalCount = 22 % 5 = 2
        ShapingInput input = new ShapingInput(60, 50, 22);
        ShapingResult result = service.calculateShaping(input);

        assertEquals(10, result.getTotalChanges());
        // 5 shaping events over 22 rows

        // My logic is better, decreases 1 stitch every 3 rows, and every 8 rows decreases 1 stitch
        assertEquals(3, result.getBaseInterval());
        assertEquals(8, result.getLongerIntervalCount());
    }

    // ──────────────────────────────────────────────────────────
    // Shaping Tests – Increase
    // ──────────────────────────────────────────────────────────

    @Test
    void calculateShaping_increase_returnsIncreaseType() {
        ShapingInput input = new ShapingInput(40, 60, 30);
        ShapingResult result = service.calculateShaping(input);

        assertEquals(ShapingType.INCREASE, result.getShapingType());
        assertEquals(20, result.getTotalChanges());
    }

    @Test
    void calculateShaping_increase_hasInstructions() {
        ShapingInput input = new ShapingInput(40, 60, 30);
        ShapingResult result = service.calculateShaping(input);

        assertNotNull(result.getInstructions());
        assertFalse(result.getInstructions().isEmpty());
    }

    // ──────────────────────────────────────────────────────────
    // Shaping Tests – Odd difference (remainder)
    // ──────────────────────────────────────────────────────────

    @Test
    void calculateShaping_oddDifference_handledCorrectly() {
        // 3 stitches to decrease: 1 shaping event (2 sts) + 1 remainder (1 st)
        ShapingInput input = new ShapingInput(53, 50, 20);
        ShapingResult result = service.calculateShaping(input);

        assertEquals(ShapingType.DECREASE, result.getShapingType());
        assertEquals(3, result.getTotalChanges());
        // Instructions should mention the remainder
        boolean mentionsRemainder = result.getInstructions().stream()
                .anyMatch(s -> s.contains("remaining 1 stitch"));
        assertTrue(mentionsRemainder, "Instructions should mention the remaining 1-stitch adjustment");
    }
}
