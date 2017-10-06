package base_classes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import utils.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class GameObject extends Sprite
{
    private float damage, damageRadius;
    private int level;
    private Pixmap pmap;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     *
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public GameObject(Texture texture, int srcWidth, int srcHeight, float damage, float damageRadius, int level)
    {
        super(texture, srcWidth, srcHeight);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;

        updatePixmap(texture);
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     */
    public GameObject(Sprite sprite, float damage, float damageRadius, int level)
    {
        super(sprite);
        this.damage = damage;
        this.damageRadius = damageRadius;
        this.level = level;

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

    // TODO: Anything else?
    public void destroy()
    {
        super.getTexture().dispose();
    }

    public abstract void update(float delta);

    public Vector2 getVector2()
    {
        super.setOriginCenter();
        return new Vector2(super.getX(), super.getY());
    }

    /**
     * This is how complexity is reduced from O(n^2)
     * Search through all 'close' tiles and get any entities in that position from a hashmap. When doing collision check
     * only search through these few entities
     *
     * @param entity Entity for which to find the neighbours of
     * @return List<Entity>
     */
    public abstract List<GameObject> findNeighbours(GameObject entity); // TODO: implement this when grid is set up

    /**
     * Checks if the current entity is colliding with any other nearby entity (Tiles are also entities)
     *
     * @param newX The x value that the entity wants to move to
     * @param newY The y value that the entity wants to move to
     * @return True if the entity would collide at newX, newY
     */
    private boolean isColliding(List<GameObject> gameObjects, float newX, float newY)
    {
        for (GameObject otherEntity : gameObjects) // Looping through all neighbouring entities.
        {
            Rectangle intersectionRect = new Rectangle();
            Rectangle playerBoundingBox = new Rectangle(super.getX(), super.getY(), super.getWidth(), super.getHeight());

            if (this != otherEntity && // Don't want to check for collisions between the same entity.
                    Intersector.intersectRectangles(playerBoundingBox, otherEntity.getBoundingRectangle(), intersectionRect)) // Bounding box collision detection.
            {
                for (int x = 0; x < intersectionRect.width; x++)
                {
                    for (int y = 0; y < intersectionRect.height; y++)
                    {
                        // finding the co-ordinates of the same pixels within the collided bounding box of the two
                        // shapes and comparing them
                        Vector2 entityPixPos = new Vector2(
                                Math.abs(newX - intersectionRect.x) + x,
                                Math.abs(newY - intersectionRect.y) + y);
                        Vector2 otherEntityPixPos = new Vector2(
                                Math.abs(otherEntity.getX() - intersectionRect.x) + x,
                                Math.abs(otherEntity.getY() - intersectionRect.y) + y);

                        // If neither are transparent then there is a collision
                        // 0 alpha mean that the pixel is completely transparent
                        if (this.getPixelAlpha(entityPixPos) != 0f
                                && otherEntity.getPixelAlpha(otherEntityPixPos) != 0f)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public float getPixelAlpha(Vector2 vector2)
    {
        int pixelVal = pmap.getPixel((int) vector2.x, (int) vector2.y);
        return new Color(pixelVal).a;
    }

}
