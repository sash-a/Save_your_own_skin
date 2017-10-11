package utils;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.save_your_own_skin.game_objects.Player;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public class Level
{
    private int levelNum, damageMod, costMod, rangeMod;
    private GameObject obj;

    public Level(int levelNum, GameObject obj)
    {
        this.levelNum = levelNum;
        this.obj = obj;
    }

    public void onLevelChange()
    {
        for (int i = 0; i < levelNum; i++)
        {
            if (i % 2 == 0)
                damageMod++;
            else
                rangeMod++;

            costMod++;
        }

        // Changing the damage values
//        obj.setDamage(obj.getDamage() * damageMod);
//        obj.setDamageRadius(obj.getDamage() * rangeMod);

        if (obj instanceof PlaceableObject)
            ((PlaceableObject) obj).setUpgradeCost(((PlaceableObject) obj).getUpgradeCost() * costMod);
        else if (obj instanceof Player)
            ((Player) obj).setUpgradeCost(((Player) obj).getUpgradeCost() * costMod);
    }
}
