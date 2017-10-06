package base_classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.save_your_own_skin.interfaces.Upgradable;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class PlaceableObject extends GameObject implements Upgradable
{
    private int cost, upgradeCost;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     *
     * @param texture
     * @param srcWidth     The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight    The height of the texture region. May be negative to flip the sprite when drawn.
     * @param damage
     * @param damageRadius
     * @param level
     */
    public PlaceableObject(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int cost, int upgradeCost)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level);
        this.cost = cost;
        this.upgradeCost = upgradeCost;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     * @param damage
     * @param damageRadius
     * @param level
     */
    public PlaceableObject(Sprite sprite, float damage, float damageRadius, int level, int cost, int upgradeCosts)
    {
        super(sprite, damage, damageRadius, level);
        this.cost = cost;
        this.upgradeCost = upgradeCost;
    }

    public int getCost()
    {
        return cost;
    }

    @Override
    public void setUpgradeCost(int upgradeCost)
    {
        this.upgradeCost = upgradeCost;
    }

    @Override
    public int getUpgradeCost()
    {
        return upgradeCost;
    }

    @Override
    public void onUpgrade()
    {

    }

    @Override
    public void onLevelChange()
    {

    }

    @Override
    public void destroy()
    {

    }
}
