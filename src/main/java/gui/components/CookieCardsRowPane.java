package gui.components;

import core.entities.base.Cookie;
import game.managers.CookieManager;
import game.cookies.CookieList;
import audio.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Row pane displaying all cookie selection cards in a horizontal layout.
 * Manages card selection and provides access to the selected cookie index.
 * Uses CookieManager for centralized cookie access.
 * Cookies are sorted by tier (S→C) then by name alphabetically.
 */
public class CookieCardsRowPane extends HBox {

    private int selectedIndex;
    private final List<CookieSelectionCard> cards = new ArrayList<>();

    public CookieCardsRowPane(int initialSelection) {
        // No visual pre-selection - user must click a cookie
        // But keep selectedIndex at 0 internally for valid state
        this.selectedIndex = 0;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: transparent;");

        // Create a list of (originalIndex, cookie) pairs
        List<java.util.AbstractMap.SimpleEntry<Integer, Cookie>> cookieList = new ArrayList<>();
        for (int i = 0; i < CookieList.size(); i++) {
            cookieList.add(new java.util.AbstractMap.SimpleEntry<>(i, CookieList.get(i)));
        }

        // Sort by tier, then by name (using the cookie's properties)
        cookieList.sort(
            Comparator
                .comparingInt((java.util.AbstractMap.SimpleEntry<Integer, Cookie> entry) -> getTierRank(entry.getValue().getTier()))
                .thenComparing(entry -> entry.getValue().getDisplayName())
        );

        // Create cards in sorted order
        for (int i = 0; i < cookieList.size(); i++) {
            java.util.AbstractMap.SimpleEntry<Integer, Cookie> entry = cookieList.get(i);
            int originalIndex = entry.getKey();
            Cookie cookie = entry.getValue();

            // No cards are pre-selected
            CookieSelectionCard card = new CookieSelectionCard(
                cookie,
                i,
                originalIndex,
                false
            );

            cards.add(card);
            this.getChildren().add(card);

            // Set click handler to select cookie using original index
            final int index = originalIndex;
            card.setOnMouseClicked(e -> {
                selectCard(index);
                e.consume();  // Consume event to prevent propagation
            });
        }
    }

    private void selectCard(int originalIndex) {
        // Play click sound
        SoundManager.getInstance().playClickSound();

        this.selectedIndex = originalIndex;

        // Notify CookieManager of button press
        CookieManager.getInstance().onCookieButtonPressed(originalIndex);

        // Update visual selection on all cards (compare by original index)
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSelected(cards.get(i).getOriginalIndex() == originalIndex);
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Get numeric rank for tier (lower = first).
     * C-tier=0, B-tier=1, A-tier=2, S-tier=3
     */
    private int getTierRank(String tier) {
        return switch (tier) {
            case "C" -> 0;
            case "B" -> 1;
            case "A" -> 2;
            case "S" -> 3;
            default -> 4;  // Unknown tiers go to the end
        };
    }
}
