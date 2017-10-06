package base_classes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import utils.Level;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class GameObject extends Sprite
{
    private float damage, damageRadius;
    private int level;


    /**
     * Creates an uninitialized sprite. The sprite will need a texture region and bounds set before it can be drawn.
     */
    public GameObject(float damage, float damageRadius, int level)
    {
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;
    }

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     *
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public GameObject(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level)
    {
        super(texture, srcWidth, srcHeight);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     */
    public GameObject(Sprite sprite, float damage, float damageRadius, int level)
    {
        super(sprite);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;
    }

    public float getDamage()
    {
        return damage;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    public float getDamageRadius()
    {
        return damageRadius;
    }

    public void setDamageRadius(float damageRadius) {this.damageRadius = damageRadius;}

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
        onLevelChange();
    }

    public void onLevelChange()
    {
        new Level(this.level, this).onLevelChange();
    }

    public void attack(int damage, CharacterObject otherCharacter)
    {
        otherCharacter.reduceHealth(damage);
    }

    // Is this the center of the sprite?
    // TODO: Test!
    public Circle createDamageableArea()
    {
        float centerX = Math.abs(super.getX() + super.getWidth()) / 2;
        float centerY = Math.abs(super.getY() + super.getHeight()) / 2;
        return new Circle(centerX, centerY, damageRadius);
    }

    // TODO: Anything else?
    public void destroy()
    {
        super.getTexture().dispose();
    }

    public abstract void update(float delta);

    public Vector2 getVector2()
    {
        super.setOriginCenter();
        return new Vector2(super.getX(), super.getY());
    }
}
