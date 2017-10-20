package com.save_your_own_skin.game_objects;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import javafx.scene.Parent;
import utils.Level;

import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Turret extends PlaceableObject
{

    private float lastAttackTime;
    private float bulletSpeed;
    private float damage, damageRadius, rateOfFire, slowDownFactor;
    private boolean isSlowDown;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth    The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight   The height of the texture region. May be negative to flip the sprite when drawn.
     * @param cost
     * @param upgradeCost
     * @param level
     */
    public Turret(int id, Texture texture, int srcWidth, int srcHeight, int cost, int upgradeCost, int level,
                  float damage, float damageRadius, float rateOfFire, float bulletSpeed)
    {
        super(id, texture, srcWidth, srcHeight, cost, upgradeCost, level);
        this.lastAttackTime = System.nanoTime();
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.rateOfFire = rateOfFire;
        this.bulletSpeed = bulletSpeed;
        this.isSlowDown = false;
        this.slowDownFactor = 1;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param turret
     */
    public Turret(int id, Turret turret)
    {
        super(id, turret, turret.getCost(), turret.getUpgradeCost(), turret.getLevel());
        this.lastAttackTime = System.nanoTime();
        this.damage = turret.damage;
        this.damageRadius = turret.damageRadius;
        this.rateOfFire = turret.rateOfFire;
        this.bulletSpeed = turret.bulletSpeed;
        this.isSlowDown = turret.isSlowDown;
        this.slowDownFactor = turret.slowDownFactor;
    }

    public Turret(
            int id, Texture texture, int srcWidth, int srcHeight, int cost, int upgradeCost, int level, float damage,
            float damageRadius, float rateOfFire, float bulletSpeed, boolean isSlowDown, float slowDownFactor)
    {
        super(id, texture, srcWidth, srcHeight, cost, upgradeCost, level);
        this.lastAttackTime = System.nanoTime();
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.rateOfFire = rateOfFire;
        this.bulletSpeed = bulletSpeed;
        this.isSlowDown = isSlowDown;
        this.slowDownFactor = slowDownFactor;
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

    public Projectile spawnProjectile(Projectile p, GameObject target, float time, int id)
    {
        if (Math.abs(time - lastAttackTime) / 100000000 < rateOfFire)
            return null;

        lastAttackTime = time;
        Projectile projectile = new Projectile(++id, p, this);

        projectile.setPosition(projectile.getParent().getX() + projectile.getParent().getWidth() / 2,
                projectile.getParent().getY() + projectile.getParent().getHeight() / 2);

        return projectile;
    }

    public float getDamage()
    {
        return damage;
    }

    public float getDamageRadius()
    {
        return damageRadius;
    }

    public float getRateOfFire()
    {
        return rateOfFire;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    public void setRateOfFire(float rateOfFire)
    {
        this.rateOfFire = rateOfFire;
    }

    public void setDamageRadius(float damageRadius)
    {
        this.damageRadius = damageRadius;
    }

    public float getSlowDownFactor()
    {
        return slowDownFactor;
    }

    public void setSlowDownFactor(float slowDownFactor)
    {
        this.slowDownFactor = slowDownFactor;
    }

    public boolean isSlowDown()
    {
        return isSlowDown;
    }

    public float getBulletSpeed()
    {
        return bulletSpeed;
    }

    public Circle getRange()
    {
        return new Circle(super.getX() + super.getWidth() / 2, super.getY() + super.getHeight() / 2, damageRadius);
    }


}
