package com.save_your_own_skin.game;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game_objects.*;

import java.util.*;

public class World extends ApplicationAdapter
{
    // TODO: implement global speed mod
    public static final int WORLD_WIDTH = 750;
    public static final int WORLD_HEIGHT = 750;
    public static final int TILE_SIZE = 30;
    public static final int MAP_HEIGHT = 25;
    public static final int MAP_WIDTH = 25;


    SpriteBatch batch;

    Texture playerTexture;
    Player player;

    Texture enemyTexture;
    Enemy enemy;

    // TODO: better solution for this
    Map<Vector2, GameObject> gameObjectPositions; // All game objects
    List<Projectile> projectiles; // All projectiles current projectiles
    List<Enemy> enemies; // All alive enemies
    List<PlaceableObject> placeables; // All placeables
    List<Tile> tiles;


    public int[][] grid;

    private int id;
    // for debug
    ShapeRenderer sr;

    @Override
    public void create()
    {
        id = 0;
        batch = new SpriteBatch();
        grid = new int[MAP_WIDTH][MAP_HEIGHT];

        gameObjectPositions = new HashMap<Vector2, GameObject>();
        projectiles = new ArrayList<Projectile>();
        enemies = new ArrayList<Enemy>();


        playerTexture = new Texture("player.png");
        player = new Player(++id, playerTexture, 30, 30, 50, 60, 1000000, 1, 100, 100, 200);
        player.setX(50);
        player.setY(50);

        enemyTexture = new Texture("enemy.png");
        enemy = new Enemy(++id, enemyTexture, 30, 30, 50, 30, 60, 1, 100, 100, 200);
        enemy.setPosition(400, 400);


        tiles = createMap();

        // TODO: put method?
        gameObjectPositions.put(player.getVector2(), player);
        gameObjectPositions.put(enemy.getVector2(), enemy);
        for (Tile tile : tiles)
        {
            gameObjectPositions.put(tile.getVector2(), tile);
        }


        //borderTextures.add(new Texture())
        // Debug
        sr = new ShapeRenderer();
    }

    private List<Tile> createMap()
    {
        List<Texture> borderTextures = new ArrayList<Texture>();
        borderTextures.add(new Texture("orange_brick.png"));
        borderTextures.add(new Texture("brown_brick.png"));

        List<Texture> floorTextures = new ArrayList<Texture>();
        floorTextures.add(new Texture("green_brick.png"));
        floorTextures.add(new Texture("grey_brick.png"));

        List<Tile> tiles = new ArrayList<Tile>();
        for (int i = 0; i < MAP_WIDTH; i++)
        {
            for (int j = 0; j < MAP_HEIGHT; j++)
            {
                // TODO: create hole in top center of map
                if ((i == 0 || j == 0 || i == MAP_HEIGHT - 1 || j == MAP_WIDTH - 1))
                {
                    Texture t = borderTextures.get((int) (Math.random() * 2));
                    Tile b = new Tile(++id, t, TILE_SIZE, TILE_SIZE, true);
                    b.setPosition(i * TILE_SIZE, j * TILE_SIZE);
                    tiles.add(b);

                    grid[i][j] = 1;
                }
                else
                {
                    grid[i][j] = 0;
                    Texture t = floorTextures.get((int) (Math.random()));
                    Tile b = new Tile(++id, t, TILE_SIZE, TILE_SIZE, false);
                    b.setPosition(i * TILE_SIZE, j * TILE_SIZE);
                    tiles.add(b);
                }
            }
        }

        return tiles;
    }

    /**
     * This is how complexity is reduced from O(n^2)
     * Search through all 'close' tiles and get any entities in that position from a hashmap. When doing collision check
     * only search through these few entities
     *
     * @param entity Entity for which to find the neighbours of
     * @return List<Entity>
     */
    public List<GameObject> findNeighbours(GameObject entity)
    {
        return null;
    }

    public static int toGridPos(float pos)
    {
        return Math.round(pos / TILE_SIZE);
    }

    public static float toAbsolutePos(int pos)
    {
        return pos * TILE_SIZE;
    }

    @Override
    public void render()
    {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // Player movement and collision
        player.update(delta);

        GameObject collided = player.isColliding(gameObjectPositions.values(),
                player.getX() + player.getDx(),
                player.getY() + player.getDy(),
                player.getRotation() + player.getChangeInRotation());
        if (collided != null)
        {
            collided.onCollision(player, delta);
            player.onCollision(collided, delta);
            if (collided instanceof Enemy)
                gameObjectPositions.values().remove(collided);
        }
        else
        {
            player.rotate(player.getChangeInRotation());
            player.translate(player.getDx(), player.getDy());
        }

        PlaceableObject toPlace;
        if (Gdx.input.isKeyPressed(Input.Keys.B))
        {
            toPlace = new Turret(++id,
                    playerTexture,
                    15,
                    15,
                    10,
                    5,
                    1,
                    2,
                    TILE_SIZE * 3,
                    100000000);
            player.place(toPlace, grid);
            gameObjectPositions.put(new Vector2(toPlace.getX(), toPlace.getY()), toPlace);
        }

        /*____________________________________________________________________________________________________________*/

        // Draw tiles first
        for (Tile tile : tiles)
        {
            batch.draw(tile,
                    tile.getX(),
                    tile.getY(),
                    tile.getOriginX(),
                    tile.getOriginY(),
                    tile.getWidth(),
                    tile.getHeight(),
                    tile.getScaleX(),
                    tile.getScaleY(),
                    tile.getRotation());
        }

        for (GameObject gameObject : gameObjectPositions.values())
        {
            if (!gameObject.isVisible()) continue;

            if (gameObject instanceof Tile) continue;

            // Draw game objects if not dead
            batch.draw(gameObject,
                    gameObject.getX(),
                    gameObject.getY(),
                    gameObject.getOriginX(),
                    gameObject.getOriginY(),
                    gameObject.getWidth(),
                    gameObject.getHeight(),
                    gameObject.getScaleX(),
                    gameObject.getScaleY(),
                    gameObject.getRotation());

            // Player updates and input handling done above
            if ((gameObject instanceof Player)) continue;
            gameObject.update(delta);

            if (gameObject instanceof Enemy)
                ((Enemy) gameObject).moveToPlayer(player, delta);

            // TODO: enemy that it points towards should be closest enemy to player
            if (gameObject instanceof Turret)
            {
                Turret turret = (Turret) gameObject;

                turret.pointTowardsEnemy(enemy);

                // Shoot
                Projectile p = new Projectile(++id, enemyTexture, 5, 5, turret, 500);

                // TODO: find a way to update make a unique vector for the projectile hashmap, id?
                Projectile spawned = turret.spawnProjectile(p, null, System.nanoTime(), ++id);
                if (spawned == null)
                    continue;

                projectiles.add(spawned);
            }
        }

        // Separate loop for projectiles
        Iterator itr = projectiles.iterator();
        while (itr.hasNext())
        {
            Projectile projectile = (Projectile) itr.next();

            if (projectile.isVisible())
            {
                batch.draw(projectile,
                        projectile.getX(),
                        projectile.getY(),
                        projectile.getOriginX(),
                        projectile.getOriginY(),
                        projectile.getWidth(),
                        projectile.getHeight(),
                        projectile.getScaleX(),
                        projectile.getScaleY(),
                        projectile.getRotation());
                projectile.update(delta);
            }
            else
            {
                itr.remove();
            }
        }

        batch.end();

        // Debug
        Rectangle r = player.getBoundingRectangle();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(new Color(1, 1, 1, 0));
        sr.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        sr.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        playerTexture.dispose();
    }
}