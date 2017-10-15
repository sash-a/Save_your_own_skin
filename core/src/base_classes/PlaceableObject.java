package base_classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.save_your_own_skin.interfaces.Upgradable;
import utils.Level;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class PlaceableObject extends GameObject implements Upgradable
{
    private int cost, upgradeCost;
    private int level;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public PlaceableObject(int id, Texture texture, int srcWidth, int srcHeight, int cost, int upgradeCost, int level)
    {
        super(id, texture, srcWidth, srcHeight);
        this.cost = cost;
        this.upgradeCost = upgradeCost;
        this.level = level;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param sprite
     */
    public PlaceableObject(int id, Sprite sprite, int cost, int upgradeCost, int level)
    {
        super(id, sprite);
        this.cost = cost;
        this.upgradeCost = upgradeCost;
        this.level = level;
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

    public int getLevel()
    {
        return level;
    }

    @Override
    public void onUpgrade()
    {
    }

}
