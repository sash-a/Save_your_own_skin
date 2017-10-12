package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.save_your_own_skin.interfaces.Intractable;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/10.
 */
public class Projectile extends GameObject implements Intractable
{
    private Turret parent;

    // TODO: rotation may need to be slightly ahead of the player


    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public Projectile(int id, Texture texture, int srcWidth, int srcHeight, Turret parent)
    {
        super(id, texture, srcWidth, srcHeight);
        this.parent = parent;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param sprite
     */
    public Projectile(int id, Sprite sprite, Turret parent)
    {
        super(id, sprite);
        this.parent = parent;
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        if (collidedObject instanceof Enemy)
        {
            super.attack((int) parent.getDamage(), (Enemy) collidedObject);
            collidedObject.onCollision(this, delta);
            destroy();
            return;
        }

        super.translate(-(float) (Math.sin(Math.toRadians(super.getRotation()))),
                (float) (Math.cos(Math.toRadians(super.getRotation()))));
    }

    @Override
    public void update(float delta)
    {
        // Destroying if out of range
        float yDistFromStartingPoint = Math.abs(parent.getY() + parent.getHeight() / 2 - super.getY());
        float xDistFromStartingPoint = Math.abs(parent.getX() + parent.getWidth() / 2 - super.getX());

        double hypot = Math.pow(yDistFromStartingPoint, 2) + Math.pow(xDistFromStartingPoint, 2);

        if (hypot > Math.pow(parent.getDamageRadius(), 2))
        {
            destroy();
            return;
        }

        // Movement
        float angleY = (float) (Math.cos(Math.toRadians(parent.getRotation())));
        float angleX = -(float) (Math.sin(Math.toRadians(parent.getRotation())));

        translate(parent.getBulletSpeed() * delta * angleX, parent.getBulletSpeed() * delta * angleY);
    }

    @Override
    public void onLevelChange()
    {

    }

    @Override
    public void attack(CharacterObject characterObject, float damage)
    {

    }

    @Override
    public Circle createDamageableArea(float damageRadius)
    {
        return null;
    }

    public PlaceableObject getParent()
    {
        return parent;
    }
}
