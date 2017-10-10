package base_classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game_objects.Border;
import utils.Level;

import java.util.Collection;
import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class GameObject extends Sprite
{
    //TODO: move id to front of constructor
    private float damage, damageRadius;
    private int level;
    private Pixmap pmap;
    private int id;
    private boolean isDead;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     *
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public GameObject(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level, int id)
    {
        super(texture, srcWidth, srcHeight);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;
        this.id = id;
        this.isDead = false;

        updatePixmap(texture);
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     */
    public GameObject(Sprite sprite, float damage, float damageRadius, int level, int id)
    {
        super(sprite);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;
        this.id = id;

        updatePixmap(sprite.getTexture());
    }

    private void updatePixmap(Texture texture)
    {
        if (!texture.getTextureData().isPrepared())
            texture.getTextureData().prepare();

        pmap = texture.getTextureData().consumePixmap();
    }

    public float getDamage()
    {
        return damage;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    public float getDamageRadius()
    {
        return damageRadius;
    }

    public void setDamageRadius(float damageRadius) {this.damageRadius = damageRadius;}

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
        onLevelChange();
    }

    public int getID() {return id;}

    @Override
    public boolean equals(Object other)
    {
        return other.getClass() == this.getClass() && ((GameObject) other).id == this.id;
    }

    public void onLevelChange()
    {
        new Level(this.level, this).onLevelChange();
    }

    public void attack(int damage, CharacterObject otherCharacter)
    {
        otherCharacter.reduceHealth(damage);
    }

    // Is this the center of the sprite?
    // TODO: Test!
    public Circle createDamageableArea()
    {
        float centerX = Math.abs(super.getX() + super.getWidth()) / 2;
        float centerY = Math.abs(super.getY() + super.getHeight()) / 2;
        return new Circle(centerX, centerY, damageRadius);
    }

    public boolean isDead()
    {
        return isDead;
    }

    // TODO: Anything else?
    public void destroy()
    {
        this.isDead = true;
    }

    public Vector2 getVector2()
    {
        super.setOriginCenter();
        return new Vector2(super.getX(), super.getY());
    }

    /**
     * Checks if the current entity is colliding with any other nearby entity (Tiles are also entities)
     *
     * @param newX The x value that the entity wants to move to
     * @param newY The y value that the entity wants to move to
     * @return True if the entity would collide at newX, newY
     */
    // TODO: fix whole method this is not working with current movement system
    public GameObject isColliding(Collection<GameObject> gameObjects, float newX, float newY)
    {
        for (GameObject otherObject : gameObjects) // Looping through all neighbouring entities.
        {
            Rectangle intersectionRect = new Rectangle();

            // Creating a new sprite at new position
            Border b = new Border(this, 1, 1, 1, -1);
            b.setX(newX);
            b.setY(newY);
            if (!this.equals(otherObject) && // Don't want to check for collisions between the same entity.
                    Intersector.intersectRectangles(b.getBoundingRectangle(), otherObject.getBoundingRectangle(), intersectionRect)) // Bounding box collision detection.
            {
                // TODO: Better hit box or pix perfect with rotation
                return otherObject;
            }
        }
        return null;
    }

    public float getPixelAlpha(Vector2 vector2)
    {
        int pixelVal = pmap.getPixel((int) vector2.x, (int) vector2.y);
        return new Color(pixelVal).a;
    }

    public abstract void onCollision(GameObject collidedObject, float delta);

    public abstract void update(float delta);

    /**
     * This is how complexity is reduced from O(n^2)
     * Search through all 'close' tiles and get any entities in that position from a hashmap. When doing collision check
     * only search through these few entities
     *
     * @param entity Entity for which to find the neighbours of
     * @return List<Entity>
     */
    public abstract List<GameObject> findNeighbours(GameObject entity);
    // dTODO: implement this when grid is set up.
    // Only find neighbours that the entity would care about

    @Override
    public String toString()
    {
        return id + "";
    }
}
