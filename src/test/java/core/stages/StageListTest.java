package core.stages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StageList and Stage value objects.
 */
public class StageListTest {

    // =========================
    // STAGE LIST CONTENTS
    // =========================

    @Test
    public void testStageList_HasFiveStages() {
        assertEquals(5, StageList.ALL.size(),
                "Game has five worlds; StageList should have five stages");
    }

    @Test
    public void testStageList_OrderedByIndex() {
        for (int i = 0; i < StageList.ALL.size(); i++) {
            assertEquals(i, StageList.ALL.get(i).getStageIndex(),
                    "Stage at position " + i + " should have stageIndex " + i);
        }
    }

    @Test
    public void testStageList_DifficultyIncreases() {
        // Speed multiplier should monotonically increase across stages
        double previous = -1;
        for (Stage s : StageList.ALL) {
            assertTrue(s.getSpeedMultiplier() >= previous,
                    "Speed multiplier should be non-decreasing across stages");
            previous = s.getSpeedMultiplier();
        }
    }

    // =========================
    // GET STAGE
    // =========================

    @Test
    public void testGetStage_ValidIndex() {
        Stage s = StageList.getStage(2);
        assertNotNull(s);
        assertEquals(2, s.getStageIndex());
    }

    @Test
    public void testGetStage_NegativeIndex_FallsBackToFirst() {
        Stage s = StageList.getStage(-1);
        assertEquals(0, s.getStageIndex(),
                "Invalid index should fall back to first stage");
    }

    @Test
    public void testGetStage_TooLargeIndex_FallsBackToFirst() {
        Stage s = StageList.getStage(999);
        assertEquals(0, s.getStageIndex());
    }

    // =========================
    // STAGE METADATA
    // =========================

    @Test
    public void testStage_DisplayName_NotEmpty() {
        Stage s = StageList.getStage(0);
        assertNotNull(s.getDisplayName());
        assertFalse(s.getDisplayName().isEmpty());
    }

    @Test
    public void testStage_BackgroundPath_ContainsWorld() {
        Stage s = StageList.getStage(0);
        assertTrue(s.getPlayBackgroundPath().contains("World1"),
                "Stage 0 (Forest) should reference World1 assets");
    }

    @Test
    public void testStage_JellyLogoPath_FormedCorrectly() {
        Stage s = StageList.getStage(1);
        assertTrue(s.getJellyLogoPath().endsWith("Jelly.png"));
    }

    @Test
    public void testStage_AllHaveDifficultyMultiplierAtLeastOne() {
        for (Stage s : StageList.ALL) {
            assertTrue(s.getDifficultyMultiplier() >= 1.0,
                    "All stages should have difficulty >= 1.0");
        }
    }

    @Test
    public void testStage_SpawnMultiplier_AlwaysPositive() {
        for (Stage s : StageList.ALL) {
            assertTrue(s.getSpawnMultiplier() > 0);
        }
    }

    @Test
    public void testStage_FirstIsForest() {
        Stage first = StageList.getStage(0);
        assertEquals("Forest", first.getDisplayName());
    }

    @Test
    public void testStage_LastIsCPGrave() {
        Stage last = StageList.getStage(StageList.ALL.size() - 1);
        assertEquals("CP Grave", last.getDisplayName());
    }
}
