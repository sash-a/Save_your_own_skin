package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game.World;
import com.save_your_own_skin.interfaces.Upgradable;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Player extends CharacterObject implements Upgradable
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
    public Player(int id, Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, float rateOfFire, int level, int health, int maxHealth, float speed)
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
    public Player(int id, Sprite sprite, float damage, float damageRadius, float rateOfFire, int level, int health, int maxHealth, float speed)
    {
        super(id, sprite, damage, damageRadius, rateOfFire, level, health, maxHealth, speed);
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
    public void onCollision(GameObject collidedObject, float delta)
    {
        if (collidedObject instanceof CharacterObject)
        {
            update(delta);
        }
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
    public void handleInput(float delta)
    {
        // So that if player isn't pressing keys obj this stop
        float angleX = 0;
        float angleY = 0;
        changeInRotation = 0;

        if (Gdx.input.isKeyPressed(Keys.W))
        {
            angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
            angleX = -(float) (Math.sin(Math.toRadians(super.getRotation())));
        }
        if (Gdx.input.isKeyPressed(Keys.S))
        {
            angleY = -(float) (Math.cos(Math.toRadians(super.getRotation())));
            angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
        }

        if (Gdx.input.isKeyPressed(Keys.A))
        {
            changeInRotation = 5;
        }
        if (Gdx.input.isKeyPressed(Keys.D))
        {
            changeInRotation = -5;
        }

        dx = angleX * super.getSpeed() * delta;
        dy = angleY * super.getSpeed() * delta;

        // Point towards mouse
        /*
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        Vector2 dir = new Vector2(mouseX - super.getX(), mouseY - super.getY());
        dir.rotate90(-1);
        super.setRotation(dir.angle());
        */
    }

    public boolean place(PlaceableObject obj, int[][] grid)
    {
        // Find direction to place block in
        float roundedDirection = 90 * (float) Math.round(super.getRotation() / 90);

        // Normalize direction (0 >= dir >= 360)
        while (roundedDirection > 360)
            roundedDirection -= 360;

        while (roundedDirection < 0)
            roundedDirection += 360;

        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        float angleX = -(float) (Math.sin(Math.toRadians(super.getRotation())));

        if (roundedDirection == 0 || roundedDirection == 360)
            angleY += 2 * World.TILE_SIZE;
        else if (roundedDirection == 90)
            angleX -= 2 * World.TILE_SIZE;
        else if (roundedDirection == 180)
            angleY -= 2 * World.TILE_SIZE;
        else
            angleX += 2 * World.TILE_SIZE;

        int xPos = World.toGridPos(super.getX() + angleX);
        int yPos = World.toGridPos(super.getY() + angleY);

        obj.setPosition(World.toAbsolutePos(xPos) + World.TILE_SIZE / 4, World.toAbsolutePos(yPos) + World.TILE_SIZE / 4);
        grid[xPos][yPos] = 2;
        // TODO: snap to grid and return false if something is already there
        return true;
    }

    public void move()
    {
        super.rotate(changeInRotation);
        super.translate(dx, dy);
    }

}
