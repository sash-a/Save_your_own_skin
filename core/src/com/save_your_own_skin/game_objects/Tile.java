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
public class Tile extends GameObject
{
    private boolean isCollidable;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public Tile(int id, Texture texture, int srcWidth, int srcHeight, boolean isCollidable)
    {
        super(id, texture, srcWidth, srcHeight);
        this.isCollidable = isCollidable;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param sprite
     */
    public Tile(int id, Sprite sprite, boolean isCollidable)
    {
        super(id, sprite);
        this.isCollidable = isCollidable;
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
    }

    @Override
    public void update(float delta)
    {

    }

    public boolean isCollidable()
    {
        return isCollidable;
    }
}
