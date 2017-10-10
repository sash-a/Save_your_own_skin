package base_classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class CharacterObject extends GameObject
{
    private int health, maxHealth;
    private float speed;
    protected float dx, dy; // change in distance

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
    public CharacterObject(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int health, int maxHealth, float speed, int id)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level, id);
        this.health = health;
        this.maxHealth = maxHealth;
        this.speed = speed;
        dx = 0;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     * @param damage
     * @param damageRadius
     * @param level
     */
    public CharacterObject(Sprite sprite, float damage, float damageRadius, int level, int health, int maxHealth, float speed, int id)
    {
        super(sprite, damage, damageRadius, level, id);
        this.health = health;
        this.maxHealth = maxHealth;
        this.speed = speed;
        dy = 0;
    }

    public void reduceHealth(int damagedAmount)
    {
        health -= damagedAmount;

        if (health <= 0)
            destroy();
    }

    public void addHealth(int damageAmount)
    {
        health += damageAmount;

        if (health > maxHealth)
            health = maxHealth;
    }

    public float getDy()
    {
        return dy;
    }

    public float getDx()
    {
        return dx;
    }

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
