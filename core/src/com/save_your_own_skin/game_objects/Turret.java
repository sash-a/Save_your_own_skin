package com.save_your_own_skin.game_objects;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Turret extends PlaceableObject
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
     * @param cost
     * @param upgradeCost
     * @param id
     */
    public Turret(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int cost, int upgradeCost, int id)
    {
        super(texture, srcWidth, srcHeight, damage, damageRadius, level, cost, upgradeCost, id);
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     * @param damage
     * @param damageRadius
     * @param level
     * @param cost
     * @param upgradeCosts
     * @param id
     */
    public Turret(Sprite sprite, float damage, float damageRadius, int level, int cost, int upgradeCosts, int id)
    {
        super(sprite, damage, damageRadius, level, cost, upgradeCosts, id);
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        if (collidedObject instanceof PlaceableObject)
        {
            // TODO: do not allow placement here
        }
    }

    @Override
    public void update(float delta)
    {

    }

    public void pointTowardsEnemy(Enemy enemy)
    {
        // Point towards player
        // Rotation
        Vector2 dir = new Vector2(enemy.getX() - super.getX(), enemy.getY() - super.getY());
        dir.rotate90(-1);
        super.setRotation(dir.angle());
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


    public Projectile attack(Projectile p, GameObject target)
    {
        Projectile projectile = new Projectile(p, p.getDamage(), p.getDamageRadius(), p.getLevel(), p.getID(), this, p.getSpeed());

        // TODO: start firing from correct part of turret
        projectile.setPosition(projectile.getParent().getX() + projectile.getParent().getWidth() / 2,
                projectile.getParent().getY() + projectile.getParent().getHeight() / 2);

        return projectile;
    }
}
