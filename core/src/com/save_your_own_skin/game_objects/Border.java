package com.save_your_own_skin.game_objects;

import base_classes.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/08.
 */
public class Border extends GameObject
{
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
    public Border(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int id)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level, id);
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     */
    public Border(Sprite sprite, float damage, float damageRadius, int level, int id)
    {
        super(sprite, damage, damageRadius, level, id);
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
    }

    @Override
    public void update(float delta)
    {

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
}
