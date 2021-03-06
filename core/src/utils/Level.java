package utils;

import base_classes.CharacterObject;
import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.save_your_own_skin.game_objects.Player;
import com.save_your_own_skin.game_objects.Turret;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public class Level
{
    private final int levelNum;
    private float damageMod, slowDownMod, costMod, rangeMod, rateOfFireMod, healthMod, speedMod;
    private GameObject obj;

    public Level(int levelNum, GameObject obj)
    {
        this.obj = obj;
        this.levelNum = levelNum;
        damageMod = 1;
        costMod = 1;
        rangeMod = 1;
        rateOfFireMod = 1;
        healthMod = 1;
        speedMod = 1;
        slowDownMod = 1;
    }

    public void incrementLevel()
    {
        if (levelNum % 2 == 0)
        {
            damageMod += 0.5;
            slowDownMod -= 0.25;
            healthMod += 0.5;
        }
        else
        {
            rangeMod += 0.5;
            speedMod += 0.2;
        }
        if (levelNum % 4 == 0)
        {
            damageMod -= 0.5;
            slowDownMod += 0.25;
            rateOfFireMod += 0.5;
        }

        costMod++;
    }

    public void onLevelChange()
    {
        if (obj instanceof PlaceableObject)
            ((PlaceableObject) obj).setUpgradeCost((int) (((PlaceableObject) obj).getUpgradeCost() * costMod));

        if (obj instanceof Turret)
        {
            Turret t = ((Turret) obj);
            t.setDamage(t.getDamageRadius() * damageMod);
            t.setDamageRadius(t.getDamageRadius() * rangeMod);
            t.setRateOfFire(t.getRateOfFire() * rateOfFireMod);
            if (t.isSlowDown()) t.setSlowDownFactor(t.getSlowDownFactor() * slowDownMod);
        }

        if (obj instanceof Player)
            ((Player) obj).setUpgradeCost((int) (((Player) obj).getUpgradeCost() * costMod));

        if (obj instanceof CharacterObject)
        {
            CharacterObject c = ((CharacterObject) obj);
            c.setDamage(c.getDamage() * damageMod);
            c.setMaxHealth((int) (c.getMaxHealth() * healthMod));
            c.addHealth(c.getMaxHealth()); // setting health to max
            c.setSpeed(c.getSpeed() * speedMod);
        }
    }
}
