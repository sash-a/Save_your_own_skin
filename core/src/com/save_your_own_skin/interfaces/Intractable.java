package com.save_your_own_skin.interfaces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public interface Intractable
{
    void onLevelChange();

    void attack(float damage);
    Circle createDamageableArea(float damageRadius);

    void destroy();
}
