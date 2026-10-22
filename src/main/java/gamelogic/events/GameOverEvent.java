package gamelogic.events;

/**
 * Event fired when the game ends.
 */
public class GameOverEvent implements GameEvent {
    private final int finalScore;
    private final int finalCoins;
    private final int stageNumber;
    private final long timestamp;

    public GameOverEvent(int finalScore, int finalCoins, int stageNumber) {
        this.finalScore = finalScore;
        this.finalCoins = finalCoins;
        this.stageNumber = stageNumber;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getEventType() {
        return "GAME_OVER";
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public int getFinalCoins() {
        return finalCoins;
    }

    public int getStageNumber() {
        return stageNumber;
    }
}
