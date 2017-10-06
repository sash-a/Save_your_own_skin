package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import base_classes.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game.World;
import com.save_your_own_skin.interfaces.Upgradable;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Player extends CharacterObject implements Upgradable
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
    public Player(float damage, float damageRadius, int level, int health, int maxHealth, float speed)
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
    public Player(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int health, int maxHealth, float speed)
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
    public Player(Sprite sprite, float damage, float damageRadius, int level, int health, int maxHealth, float speed)
    {
        super(sprite, damage, damageRadius, level, health, maxHealth, speed);
    }

    @Override
    public void setUpgradeCost(int upgradeCost)
    {

    }

    @Override
    public int getUpgradeCost()
    {
        return 0;
    }

    @Override
    public void onUpgrade()
    {

    }

    @Override
    public void update(float delta)
    {
        handleInput(delta);
    }

    /**
     * W moves player forward in the current direction
     * S does the opposite of W
     *
     * @param delta
     */
    private void handleInput(float delta)
    {

        if (Gdx.input.isKeyPressed(Keys.W))
        {
            float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
            if (angleY == 0)
                angleY += Math.PI;

            float angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
            if (angleX == 0)
                angleX += Math.PI;

            super.translate(-angleX * super.getSpeed() * delta,angleY * super.getSpeed() * delta);
        }
        if (Gdx.input.isKeyPressed(Keys.S))
        {
            float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
            if (angleY == 0)
                angleY += Math.PI;

            float angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
            if (angleX == 0)
                angleX += Math.PI;

            super.translate(angleX * super.getSpeed() * delta,-angleY * super.getSpeed() * delta);
        }
        // TODO: Fix strafing!
        if (Gdx.input.isKeyPressed(Keys.A))
        {
            super.translateX(-super.getSpeed() * delta);

        }
        if (Gdx.input.isKeyPressed(Keys.D))
        {
            super.translateX(super.getSpeed() * delta);
        }

        // Rotation
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        Vector2 dir = new Vector2(mouseX - super.getX(), mouseY - super.getY());
        dir.rotate90(-1);
        super.setRotation(dir.angle());
    }
}
