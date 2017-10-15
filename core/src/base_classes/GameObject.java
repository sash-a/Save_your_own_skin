package base_classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game.World;
import com.save_your_own_skin.game_objects.Tile;

import java.util.Collection;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/10/05.
 */
public abstract class GameObject extends Sprite
{
    private int id;
    private Pixmap pmap; // Not sure if using
    private boolean isVisible;

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     *
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public GameObject(int id, Texture texture, int srcWidth, int srcHeight)
    {
        super(texture, srcWidth, srcHeight);
        this.id = id;
        isVisible = true;
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param sprite
     */
    public GameObject(int id, Sprite sprite)
    {
        super(sprite);
        this.id = id;
        this.isVisible = true;
    }

    private void updatePixmap(Texture texture)
    {
        if (!texture.getTextureData().isPrepared())
            texture.getTextureData().prepare();

        pmap = texture.getTextureData().consumePixmap();
    }

    // Is this needed anymore?
    public float getPixelAlpha(Vector2 vector2)
    {
        int pixelVal = pmap.getPixel((int) vector2.x, (int) vector2.y);
        return new Color(pixelVal).a;
    }

    public int getID() {return id;}

    @Override
    public boolean equals(Object other)
    {
        return other.getClass() == this.getClass() && ((GameObject) other).id == this.id;
    }


    public void attack(int damage, CharacterObject otherCharacter)
    {
        otherCharacter.reduceHealth(damage);
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void destroy()
    {
        this.isVisible = false;
    }

    public Vector2 getVector2()
    {
        super.setOriginCenter();
        return new Vector2(super.getX(), super.getY());
    }

    public Vector2 getGridPos()
    {
        return new Vector2(World.toGridPos(super.getX()),
                World.toGridPos(super.getY()));
    }

    /**
     * Checks if the current entity is colliding with any other nearby entity (Tiles are also entities)
     *
     * @param newX The x value that the entity wants to move to
     * @param newY The y value that the entity wants to move to
     * @return A game object if one is in that position otherwise null
     */
    // TODO: Add in pixel collision test or polygon instead of rectangle
    public GameObject isColliding(Collection<GameObject> gameObjects, float newX, float newY, float newRotation)
    {
        for (GameObject otherObject : gameObjects) // Looping through all (ideally) neighbouring entities.
        {
            if (otherObject instanceof Tile && !((Tile) otherObject).isCollidable())
                continue;

            Rectangle intersectionRect = new Rectangle();

            // Creating a new sprite at new position to use the bounding box method
            Tile b = new Tile(1, this, true);
            b.setX(newX);
            b.setY(newY);
            b.setRotation(newRotation);

            if (!this.equals(otherObject) && // Don't want to check for collisions between the same entity.
                    Intersector.intersectRectangles(b.getBoundingRectangle(), otherObject.getBoundingRectangle(), intersectionRect)) // Bounding box collision detection.
            {
                // Polygon
                return otherObject;
            }
        }
        return null;
    }

    public abstract void onCollision(GameObject collidedObject, float delta);

    public abstract void update(float delta);

    @Override
    public String toString()
    {
        return id + "";
    }
}
