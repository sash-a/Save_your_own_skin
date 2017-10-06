package com.save_your_own_skin.interfaces;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public interface Upgradable
{
    void setUpgradeCost(int upgradeCost);
    int getUpgradeCost();
    void onUpgrade();

    void destroy();
}
