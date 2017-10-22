package utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScoreManager
{
    private int score;
    private int money;
    public static final int BASE_KILL_INCREASE = 2;
    public static final int BASE_SCORE_INCREASE = 10;

    public ScoreManager(int score, int money)
    {
        this.score = score;
        this.money = money;
    }

    public int getScore()
    {
        return score;
    }

    public int getMoney()
    {
        return money;
    }

    public void onKill(int rewardMod, int scoreMod)
    {
        money += BASE_KILL_INCREASE * rewardMod;
        score += BASE_SCORE_INCREASE * scoreMod;
    }

    /**
     * Takes money from total money if there is enough
     *
     * @param price
     * @param sr
     * @param timeStopDrawing
     * @return true if enough money to buy object, false otherwise
     */
    public boolean buy(int price, ShapeRenderer sr, float timeStopDrawing, boolean showNotEnoughMoney)
    {
        if (money < price)
        {
            if (showNotEnoughMoney) onNotEnoughMoney(sr, timeStopDrawing);
            return false;
        }

        money -= price;
        return true;
    }

    /**
     * Puts square around money on sidebar to let user know not enough money to buy
     * Does this until time > timeStopDrawing
     *
     * @param sr
     * @param timeStopDrawing
     */
    private void onNotEnoughMoney(ShapeRenderer sr, float timeStopDrawing)
    {
        if (System.nanoTime() / 100000000 > timeStopDrawing) return;
        sr.setColor(Color.RED);
        sr.rect(787, 665, 70, 20);
    }
}
