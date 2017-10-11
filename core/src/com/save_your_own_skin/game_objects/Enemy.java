package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import base_classes.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Enemy extends CharacterObject
{
    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth     The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight    The height of the texture region. May be negative to flip the sprite when drawn.
     * @param damage
     * @param damageRadius
     * @param rateOfFire
     * @param level
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(int id, Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, float rateOfFire, int level, int health, int maxHealth, float speed)
    {
        super(id, texture, srcWidth, srcHeight, damage, damageRadius, rateOfFire, level, health, maxHealth, speed);
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param sprite
     * @param damage
     * @param damageRadius
     * @param rateOfFire
     * @param level
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(int id, Sprite sprite, float damage, float damageRadius, float rateOfFire, int level, int health, int maxHealth, float speed)
    {
        super(id, sprite, damage, damageRadius, rateOfFire, level, health, maxHealth, speed);
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        super.destroy();
        System.out.println("hit: " + collidedObject.getID());
    }

    @Override
    public void update(float delta)
    {

    }

    // TODO: find a way to call this in update
    public void moveToPlayer(Player player, float delta)
    {
        // Point towards player
        // Rotation
        Vector2 dir = new Vector2(player.getX() - super.getX(), player.getY() - super.getY());
        dir.rotate90(-1);
        super.setRotation(dir.angle());

        // Move
        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        if (angleY == 0)
            angleY += Math.PI;

        float angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
        if (angleX == 0)
            angleX += Math.PI;

        super.translate(-angleX * super.getSpeed() * delta, angleY * super.getSpeed() * delta);
    }

}
