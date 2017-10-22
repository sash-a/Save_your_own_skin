package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import base_classes.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game.World;
import utils.Level;

import java.util.List;

import static com.save_your_own_skin.game.World.*;

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
     * @param level
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(int id, Texture texture, int srcWidth, int srcHeight, float damage, int level, int health, int maxHealth, float speed)
    {
        super(id, texture, srcWidth, srcHeight, damage, level, health, maxHealth, speed);
        upgradeToLevel();
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param enemy
     */
    public Enemy(int id, int level, Enemy enemy)
    {
        super(id, enemy, enemy.getDamage(), level, enemy.getHealth(), enemy.getMaxHealth(), enemy.getSpeed());
        upgradeToLevel();
    }

    private void upgradeToLevel()
    {
        for (int i = 0; i < super.getLevel(); i++)
        {
            Level l = new Level(i, this);
            l.incrementLevel();
            l.onLevelChange();
        }
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        if (collidedObject instanceof Player)
        {
            super.destroy();
            super.attack((int) super.getDamage(), (Player) collidedObject);
            return;
        }
        if (collidedObject instanceof CharacterObject)
            this.move();
    }

    @Override
    public void update(float delta)
    {

    }

    public void move()
    {
        super.setRotation(changeInRotation);
        super.translate(dx, dy);
    }

    public void pointToPlayer(Player player, float delta)
    {
        // Point towards player
        // Rotation
        Vector2 dir = new Vector2(player.getX() - super.getX(), player.getY() - super.getY());
        dir.rotate90(-1);
        changeInRotation = dir.angle();

        // Move
        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        if (angleY == 0)
            angleY += Math.PI;

        float angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
        if (angleX == 0)
            angleX += Math.PI;

        dx = -angleX * super.getSpeed() * delta;
        dy = angleY * super.getSpeed() * delta;
    }

    @Override
    public void destroy()
    {
        super.destroy();
        World.scoreManager.onKill(super.getLevel()/2,super.getLevel());
    }
}
