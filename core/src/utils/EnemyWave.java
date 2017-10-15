package utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.save_your_own_skin.game_objects.Enemy;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/13.
 */
public class EnemyWave
{
    private List<Enemy> unDrawn;
    private float timeLastEnemyDrawn, timeBetweenEnemies;


    public EnemyWave(List<Enemy> unDrawn, float timeBetweenEnemies)
    {
        this.unDrawn = unDrawn;
        this.timeBetweenEnemies = timeBetweenEnemies;
        timeLastEnemyDrawn = System.nanoTime();
    }

    public Enemy spawnEnemy()
    {
        if (Math.abs(timeLastEnemyDrawn - System.nanoTime()) / 100000000 < timeBetweenEnemies)
            return null;

        timeLastEnemyDrawn = System.nanoTime();
        return unDrawn.remove(0);
    }

    public boolean isWaveFinished()
    {
        return unDrawn.isEmpty();
    }

}
