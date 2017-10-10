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
    private PlaceableObject parent;
    private float speed;

    // TODO: rotation may need to be slightly ahead of the player
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
     * @param id
     */
    public Projectile(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int id, PlaceableObject parent, float speed)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level, id);
        this.parent = parent;
        this.speed = speed;
        super.setRotation(parent.getRotation());
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     * @param damage
     * @param damageRadius
     * @param level
     * @param id
     */
    public Projectile(Sprite sprite, float damage, float damageRadius, int level, int id, PlaceableObject parent, float speed)
    {
        super(sprite, damage, damageRadius, level, id);
        this.parent = parent;
        this.speed = speed;
        super.setRotation(parent.getRotation());
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        destroy();
    }

    @Override
    public void update(float delta)
    {
        // Destroying if out of range
        float yDistFromStartingPoint = Math.abs(parent.getY() + parent.getHeight() / 2 - super.getY());
        float xDistFromStartingPoint = Math.abs(parent.getX() + parent.getWidth() / 2 - super.getX());

        double hypot = Math.pow(yDistFromStartingPoint, 2) + Math.pow(xDistFromStartingPoint, 2);

        if (hypot > Math.pow(super.getDamageRadius(), 2))
        {
            destroy();
            return;
        }

        // Movement
        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        float angleX = -(float) (Math.sin(Math.toRadians(super.getRotation())));

        translate(this.speed * delta * angleX, this.speed * delta * angleY);
    }

    /**
     * This is how complexity is reduced from O(n^2)
     * Search through all 'close' tiles and get any entities in that position from a hashmap. When doing collision check
     * only search through these few entities
     *
     * @param entity Entity for which to find the neighbours of
     * @return List<Entity>
     */
    @Override
    public List<GameObject> findNeighbours(GameObject entity)
    {
        return null;
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

    public float getSpeed()
    {
        return speed;
    }

    public PlaceableObject getParent()
    {
        return parent;
    }
}
