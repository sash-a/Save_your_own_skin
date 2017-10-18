package com.save_your_own_skin.game_objects;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public class FloorTile extends PlaceableObject
{
    private float damage, rateOfFire;

    public FloorTile(int id, Texture texture, int srcWidth, int srcHeight, int cost, int upgradeCost, int level, float damage, float rateOfFire)
    {
        super(id, texture, srcWidth, srcHeight, cost, upgradeCost, level);
        this.damage = damage;
        this.rateOfFire = rateOfFire;
    }

    public FloorTile(int id, Sprite sprite, int cost, int upgradeCost, int level, float damage, float rateOfFire)
    {
        super(id, sprite, cost, upgradeCost, level);
        this.damage = damage;
        this.rateOfFire = rateOfFire;
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        if (collidedObject instanceof Enemy) attack((int) this.damage, (Enemy) collidedObject);
    }

    @Override
    public void update(float delta)
    {

    }
}
