package gamelogic.events;

/**
 * Event fired when a coin is collected.
 */
public class CoinCollectedEvent implements GameEvent {
    private final int coinValue;
    private final double posX;
    private final double posY;
    private final long timestamp;

    public CoinCollectedEvent(int coinValue, double posX, double posY) {
        this.coinValue = coinValue;
        this.posX = posX;
        this.posY = posY;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getEventType() {
        return "COIN_COLLECTED";
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
