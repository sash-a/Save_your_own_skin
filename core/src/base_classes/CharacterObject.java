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
    private float damage, damageRadius, rateOfFire;
    private int level;
    private int health, maxHealth;
    private float speed;
    protected float dx, dy, changeInRotation; // change in distance

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public CharacterObject(int id, Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, float rateOfFire, int level, int health, int maxHealth, float speed)
    {
        super(id, texture, srcWidth, srcHeight);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.rateOfFire = rateOfFire;
        this.level = level;
        this.health = health;
        this.maxHealth = maxHealth;
        this.speed = speed;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param sprite
     */
    public CharacterObject(int id, Sprite sprite, float damage, float damageRadius, float rateOfFire, int level, int health, int maxHealth, float speed)
    {
        super(id, sprite);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.rateOfFire = rateOfFire;
        this.level = level;
        this.health = health;
        this.maxHealth = maxHealth;
        this.speed = speed;
    }

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

    public float getChangeInRotation()
    {
        return changeInRotation;
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
