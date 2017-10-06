package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Enemy extends CharacterObject
{
    /**
     * Creates an uninitialized sprite. The sprite will need a texture region and bounds set before it can be drawn.
     *
     * @param damage
     * @param damageRadius
     * @param level
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(float damage, float damageRadius, int level, int health, int maxHealth, float speed)
    {
        super(damage, damageRadius, level, health, maxHealth, speed);
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
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int health, int maxHealth, float speed)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level, health, maxHealth, speed);
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     * @param damage
     * @param damageRadius
     * @param level
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(Sprite sprite, float damage, float damageRadius, int level, int health, int maxHealth, float speed)
    {
        super(sprite, damage, damageRadius, level, health, maxHealth, speed);
    }

    @Override
    public void update(float delta)
    {

    }
}
