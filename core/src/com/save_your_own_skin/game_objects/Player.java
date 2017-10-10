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
    public Player(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int health, int maxHealth, float speed, int id)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level, health, maxHealth, speed, id);
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
    public Player(Sprite sprite, float damage, float damageRadius, int level, int health, int maxHealth, float speed, int id)
    {
        super(sprite, damage, damageRadius, level, health, maxHealth, speed, id);
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
        System.out.println("Collided");
        if (collidedObject instanceof CharacterObject)
            update(delta);
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

    /**
     * W moves player forward in the current direction
     * S does the opposite of W
     *
     * @param delta
     */
    public void handleInput(float delta)
    {
        float angleX = 0;
        float angleY = 0;
        // TODO: Find a better solution than adding pi to rotation
        if (Gdx.input.isKeyPressed(Keys.W))
        {
            angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
//            if (angleY == 0)
//                angleY += Math.PI;

            angleX = -(float) (Math.sin(Math.toRadians(super.getRotation())));
//            if (angleX == 0)
//                angleX += Math.PI;
        }
        if (Gdx.input.isKeyPressed(Keys.S))
        {
            angleY = -(float) (Math.cos(Math.toRadians(super.getRotation())));
//            if (angleY == 0)
//                angleY += Math.PI;

            angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
//            if (angleX == 0)
//                angleX += Math.PI;
        }
        // TODO: don't allow rotation if it leads to a collision
        if (Gdx.input.isKeyPressed(Keys.A))
        {
            super.rotate(5);
        }
        if (Gdx.input.isKeyPressed(Keys.D))
        {
            super.rotate(-5);
        }

        // TODO: fix is collision with new x and y and put it here
        // Point towards mouse
//        float mouseX = Gdx.input.getX();
//        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//        Vector2 dir = new Vector2(mouseX - super.getX(), mouseY - super.getY());
//        dir.rotate90(-1);
//        super.setRotation(dir.angle());

        dx = angleX * super.getSpeed() * delta;
        dy = angleY * super.getSpeed() * delta;
    }

    public boolean place(PlaceableObject obj)
    {
        // Find direction to place block in
        float roundedDirection = 90 * (float) Math.round(super.getRotation() / 90);
        // Normalize direction (to less than  360)
        while (roundedDirection > 360)
            roundedDirection -= 360;

        while (roundedDirection < 0)
            roundedDirection += 360;

        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        float angleX = -(float) (Math.sin(Math.toRadians(super.getRotation())));

        if (roundedDirection == 0 || roundedDirection == 360)
            angleY += World.TILE_SIZE;
        else if (roundedDirection == 90)
            angleX -= World.TILE_SIZE;
        else if (roundedDirection == 180)
            angleY -= World.TILE_SIZE;
        else
            angleX += World.TILE_SIZE;


        obj.setPosition(super.getX() + angleX,
                super.getY() + angleY);

        return true;
    }

}
