package gui.components;

import core.stages.StageList;
import audio.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage row pane that displays all stage selection cards horizontally.
 * Manages card creation, selection state, and click handlers.
 * All stages are visible at once with the initially selected stage highlighted.
 */
public class StagesCardRowPane extends HBox {

    private final List<StageSelectionCard> cards;
    private Runnable onStageSelected;
    private int selectedIndex;
    /**
     * Create a stage row pane with all stages visible.
     * Always selects the first stage (index 0) with hover style applied.
     * @param initialSelectedIndex Ignored - always selects first stage
     */
    public StagesCardRowPane(int initialSelectedIndex) {
        super(12);
        // Always select first stage
        this.selectedIndex = 0;
        this.cards = new ArrayList<>();
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

        buildStages();
    }

    /**
     * Build all stage selection cards horizontally.
     * First stage is selected and has hover style applied.
     */
    private void buildStages() {
        for (int i = 0; i < StageList.ALL.size(); i++) {
            final int index = i;
            StageSelectionCard card = new StageSelectionCard(StageList.ALL.get(index), index);
            cards.add(card);

            // Click handler
            card.setOnMouseClicked(e -> {
                SoundManager.getInstance().playClickSound();
                selectedIndex = index;
                refreshStages();
                if (onStageSelected != null) {
                    onStageSelected.run();
                }
            });

            this.getChildren().add(card);
        }

        // Set first stage as selected with selected style (golden border)
        StageSelectionCard firstCard = cards.get(0);
        firstCard.setSelected(true);
        // Selected style is applied automatically via setSelected(true)
    }

    /**
     * Refresh stage selection states.
     */
    private void refreshStages() {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSelected(i == selectedIndex);
        }
    }

    /**
     * Get the currently selected stage index.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

}
