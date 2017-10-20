package utils;

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

    public boolean buy (int price)
    {
        if (money < price) return false;

        money -= price;
        return true;
    }
}
