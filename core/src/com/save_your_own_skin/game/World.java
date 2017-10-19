package com.save_your_own_skin.game;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.save_your_own_skin.game_objects.*;
import utils.EnemyWave;
import utils.*;

import java.util.*;

public class World extends ApplicationAdapter
{
    // TODO: implement global speed mod
    public static final int WORLD_WIDTH = 900;
    public static final int WORLD_HEIGHT = 750;
    public static final int TILE_SIZE = 30;
    public static final int MAP_HEIGHT = 25;
    public static final int MAP_WIDTH = 25;

    public static ScoreManager scoreManager;

    private SpriteBatch batch;

    private Player player;
    private GameObject currentSelectedObject;

    // Textures
    private Texture playerTexture;
    private Texture enemyTexture;
    private Texture machineGunTexture;
    private Texture rocketLauncherTexture;
    private Texture slowDownTexture;

    // TODO: better solution for this
    private List<GameObject> gameObjects; // All game objects

    private List<Projectile> projectiles; // All projectiles current projectiles
    private List<GameObject> enemies; // All alive enemies
    private List<Tile> tiles;
    private List<EnemyWave> enemyWaves;


    private int[][] grid;

    private int id;

    private int currentTurretIndex; // Position in list of current turret
    private List<Turret> defaultTurrets;
    private Turret machineGunTurret;
    private Turret rocketLauncherTurret;
    private Turret slowDownTurret;

    private BitmapFont bitmapFont;

    // for debug
    private ShapeRenderer sr;

    private enum types
    {
        small, medium, large
    }

    @Override
    public void create()
    {
        id = 0;
        batch = new SpriteBatch();
        grid = new int[MAP_WIDTH][MAP_HEIGHT];

    /*________________________________________________________________________________________________________________*/

        // init lists
        gameObjects = new ArrayList<GameObject>();
        projectiles = new ArrayList<Projectile>();
        enemies = new ArrayList<GameObject>();
        enemyWaves = new ArrayList<EnemyWave>();
        defaultTurrets = new ArrayList<Turret>();


    /*________________________________________________________________________________________________________________*/

        // init textures
        playerTexture = new Texture("player/player.png");
        enemyTexture = new Texture("enemies/enemy.png");
        machineGunTexture = new Texture("turrets/machine_gun_t.png");
        rocketLauncherTexture = new Texture("turrets/rocket_launcher_t.png");
        slowDownTexture = new Texture("turrets/slow_down_t.png");

    /*________________________________________________________________________________________________________________*/

        // create player
        player = new Player(++id, playerTexture, 20, 60, 1, 1, 100, 100, 200);
        player.setX(50);
        player.setY(50);
        gameObjects.add(player);

    /*________________________________________________________________________________________________________________*/
        // TODO: time delay before first wave and between waves
        // create the first wave enemies
        startWave();

    /*________________________________________________________________________________________________________________*/

        // Create the default turrets and add to the list
        machineGunTurret = new Turret(-1, machineGunTexture, 25, 25, 15, 10, 1, 25, TILE_SIZE * 3, 0.5f, 650);
        rocketLauncherTurret = new Turret(-1, rocketLauncherTexture, 25, 25, 30, 20, 1, 60, TILE_SIZE * 4, 1, 400);
        slowDownTurret = new Turret(-1, slowDownTexture, 25, 25, 20, 10, 1, 1.5f, TILE_SIZE * 3, 0.75f, 500);

        defaultTurrets.add(machineGunTurret);
        defaultTurrets.add(rocketLauncherTurret);
        defaultTurrets.add(slowDownTurret);

    /*________________________________________________________________________________________________________________*/

        // Creating map
        tiles = createMap();
        gameObjects.addAll(tiles);

        // Player info stuff
        bitmapFont = new BitmapFont();
        scoreManager = new ScoreManager(0, 20);

        sr = new ShapeRenderer();
        sr.setColor(Color.NAVY);

        // Hiding the mouse
        Gdx.input.setCursorCatched(true);
        Gdx.input.setCursorPosition(MAP_WIDTH * TILE_SIZE / 2, MAP_HEIGHT * TILE_SIZE / 2);
    }

    private List<Tile> createMap()
    {
        Texture border = new Texture("tiles/brown_brick.png");
        Texture floor = new Texture("tiles/large_white_brick.png");

        List<Tile> tiles = new ArrayList<Tile>();
        for (int i = 0; i < MAP_WIDTH; i++)
        {
            for (int j = 0; j < MAP_HEIGHT; j++)
            {
                if ((i == 0 || j == 0 || i == MAP_HEIGHT - 1 || j == MAP_WIDTH - 1)
                        && !(i == MAP_WIDTH / 2 && j == MAP_HEIGHT - 1))
                {
                    Tile b = new Tile(++id, border, TILE_SIZE, TILE_SIZE, true);
                    b.setPosition(i * TILE_SIZE, j * TILE_SIZE);
                    tiles.add(b);

                    grid[i][j] = 1;
                }
                else
                {
                    grid[i][j] = 0;
                    Tile b = new Tile(++id, floor, TILE_SIZE, TILE_SIZE, false);
                    b.setPosition(i * TILE_SIZE, j * TILE_SIZE);
                    tiles.add(b);
                }
            }
        }

        return tiles;
    }

    private void startWave()
    {
        List<Enemy> enemies = new ArrayList<Enemy>();
        for (int i = 0; i < 10; i++)
        {
            Enemy e = new Enemy(++id, enemyTexture, 15, 15, 20, 1, 1, 1, 100, 100, 200);
            float x = MAP_WIDTH / 2 * TILE_SIZE;
            float y = MAP_HEIGHT * TILE_SIZE - 3 * TILE_SIZE;
            e.setPosition(x, y);

            enemies.add(e);
        }
        enemyWaves.add(new EnemyWave(enemies, 5));
    }

    public static int toGridPos(float pos)
    {
        return Math.round(pos / TILE_SIZE);
    }

    public static float toAbsolutePos(int pos)
    {
        return pos * TILE_SIZE;
    }


    private List<GameObject> filterPlayerCollisionList()
    {
        List<GameObject> playerCollidableObjects = new ArrayList<GameObject>();
        playerCollidableObjects.addAll(gameObjects);
        Iterator itr = playerCollidableObjects.iterator();

        while (itr.hasNext())
        {
            GameObject go = (GameObject) itr.next();
            if (go instanceof Enemy)
                itr.remove();
        }
        return playerCollidableObjects;
    }

    private void placeTurret()
    {
        // Only place if have enough money
        if (scoreManager.getMoney() < defaultTurrets.get(currentTurretIndex).getCost())
            return;

        PlaceableObject toPlace = new Turret(++id, defaultTurrets.get(currentTurretIndex));

        if (player.place(toPlace, grid)) gameObjects.add(toPlace);
    }


    // TODO find out if scroll up or down
    private void changeCurrentTurretToPlace(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            currentTurretIndex++;

            if (currentTurretIndex == 3) currentTurretIndex = 0;
            else if (currentTurretIndex == -1) currentTurretIndex = 2;
        }
    }


    private void onScrollWheel()
    {
        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean scrolled(int amount)
            {
                changeCurrentTurretToPlace(1);
                return true;
            }
        });
    }


    /**
     * Finds the current turret the player is looking at and highlights it.
     * If there isn't one on that square highlight the square instead.
     */
    private void findCurrentTurret()
    {
        // If pointing outside of world
        if (currentSelectedObject != null)
            currentSelectedObject.setAlpha(1f);
        currentSelectedObject = null;

        // Find direction to pointing in
        float roundedDirection = 90 * (float) Math.round(player.getRotation() / 90);

        // Normalize direction (0 >= dir >= 360)
        while (roundedDirection > 360)
            roundedDirection -= 360;

        while (roundedDirection < 0)
            roundedDirection += 360;

        float angleY = (float) (Math.cos(Math.toRadians(player.getRotation())));
        float angleX = -(float) (Math.sin(Math.toRadians(player.getRotation())));

        if (roundedDirection == 0 || roundedDirection == 360)
            angleY += 2 * World.TILE_SIZE;
        else if (roundedDirection == 90)
            angleX -= 2 * World.TILE_SIZE;
        else if (roundedDirection == 180)
            angleY -= 2 * World.TILE_SIZE;
        else
            angleX += 2 * World.TILE_SIZE;

        int x = World.toGridPos(player.getX() + angleX);
        int y = World.toGridPos(player.getY() + angleY);

        for (GameObject gameObject : gameObjects)
        {
            if (!(gameObject instanceof Turret || gameObject instanceof Tile))
                continue;

            if (gameObject.getGridPos().x == x && gameObject.getGridPos().y == y)
            {
                if (currentSelectedObject != null)
                    currentSelectedObject.setAlpha(1);

                currentSelectedObject = gameObject;
                gameObject.setAlpha(0.5f);

                if (currentSelectedObject instanceof Turret) break; // if turret found can stop searching
            }
        }
    }

    /**
     * Player manipulations go here
     */
    private void updatePlayer(float delta)
    {
        findCurrentTurret();
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) placeTurret();

        player.update(delta);
        // Player movement and collision
        GameObject collided = player.isColliding(
                filterPlayerCollisionList(),
                player.getX() + player.getDx(),
                player.getY() + player.getDy(),
                player.getRotation() + player.getChangeInRotation());

        if (collided == null)
        {
            player.move();
            return;
        }
        player.onCollision(collided, delta);
    }

    // This is useful if go back to hashmap
    /*private void moveEnemy(Enemy enemy, float delta)
    {
        enemy.pointToPlayer(player, delta);
        for (Vector2 vector2 : gameObjectsOnScreen.keySet())
        {
            if (gameObjectsOnScreen.get(vector2) != null &&
                    gameObjectsOnScreen.get(vector2).equals(enemy))
            {
                vector2.set(enemy.getDx(), enemy.getDy());
                break;
            }
        }
        enemy.move();
    }
    */

    private void updateEnemy(Enemy enemy, float delta, Iterator itr)
    {
        GameObject collided = enemy.isColliding(gameObjects,
                enemy.getX() + enemy.getDx(),
                enemy.getY() + enemy.getDy(),
                enemy.getRotation() + enemy.getChangeInRotation());


        if (collided == null)
        {
            enemy.pointToPlayer(player, delta);
            enemy.move();
            return;
        }

        enemy.onCollision(collided, delta);

        if (collided instanceof Player)
        {
            itr.remove();
            enemies.remove(enemy);
            enemy.onCollision(player, delta);
        }
    }

    @Override
    public void render()
    {
        float delta = Gdx.graphics.getDeltaTime();
        onScrollWheel();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updatePlayer(delta);

        // Spawning waves
        if (!enemyWaves.get(0).isWaveFinished())
        {
            Enemy enem = enemyWaves.get(0).spawnEnemy();
            if (enem != null)
            {
                gameObjects.add(enem);
                enemies.add(enem);
            }
        }
        else
        {
            enemyWaves.remove(0);
            startWave();
        }

    /*________________________________________________________________________________________________________________*/

        // Render stuff
        batch.begin();


        // Draw tiles first
        for (Tile tile : tiles)
        {
            tile.draw(batch);
        }

        Iterator itr = gameObjects.iterator();
        while (itr.hasNext())
        {
            GameObject gameObject = (GameObject) itr.next();

            if (!gameObject.isVisible())
            {
                itr.remove();
                continue;
            }
            if (gameObject instanceof Tile) continue;

            // Draw game objects if visible
            gameObject.draw(batch);

            // Call update method for objects
            gameObject.update(delta);

            // Player updated above
            if ((gameObject instanceof Player)) continue;

            if (gameObject instanceof Enemy) updateEnemy((Enemy) gameObject, delta, itr);

            else if (gameObject instanceof Turret)
            {
                Turret turret = (Turret) gameObject;

                // Shoot
                // TODO make default projectiles global
                Projectile p = new Projectile(++id, enemyTexture, 10, 10, turret);

                for (GameObject enemy : enemies)
                {
                    Circle c = turret.getRange();

                    if (!Intersector.overlaps(c, enemy.getBoundingRectangle()))
                        continue;
                    turret.pointTowardsEnemy((Enemy) enemies.get(0)); // TODO: make this closest enemy

                    Projectile spawned = turret.spawnProjectile(p, null, System.nanoTime(), ++id);
                    if (spawned == null)
                        continue;

                    projectiles.add(spawned);
                }
            }
        }

        // Separate loop for projectiles
        itr = projectiles.iterator();
        while (itr.hasNext())
        {
            Projectile projectile = (Projectile) itr.next();

            GameObject gameObject = projectile.isColliding(enemies,
                    projectile.getX(), projectile.getY(), projectile.getRotation());

            if (gameObject != null)
            {
                projectile.onCollision(gameObject, delta);
                if (!gameObject.isVisible()) enemies.remove(gameObject); // main loop removes from other list
            }

            if (projectile.isVisible())
            {
                projectile.draw(batch);
                projectile.update(delta);
            }
            else
            {
                itr.remove();
            }
        }

        // Sidebar
        bitmapFont.draw(batch, "Score: " + scoreManager.getScore(), 760, 700, 120, 1, true);
        bitmapFont.draw(batch, "Money: " + scoreManager.getMoney(), 760, 680, 120, 1, true);

        Turret currentTurret = defaultTurrets.get(currentTurretIndex);
        String title = "Turret that will be placed";
        if (currentSelectedObject instanceof Turret)
        {
            currentTurret = (Turret)currentSelectedObject;
            title = "Current selected turret";
        }

        bitmapFont.draw(batch, title, 760, 600, 120, 1, true);
        batch.draw(currentTurret.getTexture(), 760, 450, 120, 120);
        bitmapFont.draw(batch,
                "STATS" +
                    "\nDamage: " + currentTurret.getDamage() +
                    "\nRange: " + currentTurret.getDamageRadius() +
                    "\nRate of fire: " + 1/currentTurret.getRateOfFire() +
                    "\nUpgrade cost: " + currentTurret.getUpgradeCost(),
                760, 430, 120, 1, true);


        batch.end();


        // Debug



        sr.begin(ShapeRenderer.ShapeType.Line);
        if (currentSelectedObject instanceof Turret)
        {
            Circle c = ((Turret) currentSelectedObject).getRange();
            sr.circle(c.x, c.y, c.radius);
        }
        sr.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        playerTexture.dispose();
    }
}