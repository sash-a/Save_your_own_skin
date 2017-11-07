package com.save_your_own_skin.game;

import base_classes.GameObject;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game_objects.*;
import utils.EnemyWave;
import utils.*;

import java.util.*;

public class World extends Game
{
    // Constants
    public static final int WORLD_WIDTH = 900;
    public static final int WORLD_HEIGHT = 750;
    public static final int TILE_SIZE = 30;
    public static final int MAP_HEIGHT = 25;
    public static final int MAP_WIDTH = 25;
    public static final int NANO_TIME_CONVERTER = 100000000;

    public static ScoreManager scoreManager;
    private int waveLevel;

    private Map<Vector2, Node> nodes;
    private int[][] grid;
    private int id;

    private SpriteBatch batch;

    private Player player;

    private GameObject currentSelectedObject;

    // For a turret about to be placed
    private boolean isTurretBeingPlaced;
    private Turret turretBeingPlaced;

    // Textures
    private Texture playerTexture;
    //Enemy textures
    private Texture smallEnemyTexture;
    private Texture mediumEnemyTexture;
    private Texture largeEnemyTexture;
    // Turret textures
    private Texture machineGunTexture;
    private Texture rocketLauncherTexture;
    private Texture slowDownTexture;
    //Sidebar textures (health bar)
    private Texture healthBar;
    private Texture redBar;

    // Lists
    private List<GameObject> gameObjects; // All game objects
    private List<Projectile> projectiles; // All projectiles current projectiles
    private List<GameObject> enemies; // All alive enemies
    private List<Tile> tiles;
    private List<EnemyWave> enemyWaves;

    // Waves
    private float timeEndOfWave;
    private boolean allEnemiesSpawned;

    // Turrets
    private int currentTurretIndex; // Position in list of current turret
    private List<Turret> defaultTurrets;

    private List<Enemy> defaultEnemies;

    // Sidebar
    private BitmapFont bitmapFont;
    private ShapeRenderer sr;

    @Override
    public void create()
    {
        id = 0;
        batch = new SpriteBatch();
        grid = new int[MAP_WIDTH][MAP_HEIGHT];
        nodes = new HashMap<Vector2, Node>();
        isTurretBeingPlaced = false;

    /*________________________________________________________________________________________________________________*/

        // init lists
        gameObjects = new ArrayList<GameObject>();
        projectiles = new ArrayList<Projectile>();
        enemies = new ArrayList<GameObject>();
        enemyWaves = new ArrayList<EnemyWave>();
        defaultTurrets = new ArrayList<Turret>();
        defaultEnemies = new ArrayList<Enemy>();


    /*________________________________________________________________________________________________________________*/

        // init textures
        playerTexture = new Texture("player/player_t.png");
        smallEnemyTexture = new Texture("enemies/enemy_small_t.png");
        mediumEnemyTexture = new Texture("enemies/enemy_medium_t.png");
        largeEnemyTexture = new Texture("enemies/enemy_large_t.png");
        machineGunTexture = new Texture("turrets/machine_gun_t.png");
        rocketLauncherTexture = new Texture("turrets/rocket_launcher_t.png");
        slowDownTexture = new Texture("turrets/slow_down_t.png");
        healthBar = new Texture("healthbar.png");
        redBar = new Texture("red_healthbar.png ");

    /*________________________________________________________________________________________________________________*/

        // create player
        player = new Player(++id, playerTexture, 22, 26, 1, 1, 100, 100, 200);
        player.setX(50);
        player.setY(50);
        gameObjects.add(player);

    /*________________________________________________________________________________________________________________*/

        // Create the default turrets and add to the list
        // dmg at 0 for a* testing
        Turret machineGunTurret = new Turret(-1, machineGunTexture, 20, 20, 15, 10, 1, 0, TILE_SIZE * 3, 0.5f, 650);
        Turret rocketLauncherTurret = new Turret(-1, rocketLauncherTexture, 25, 25, 30, 20, 1, 60, TILE_SIZE * 4, 1, 400);
        Turret slowDownTurret = new Turret(-1, slowDownTexture, 25, 25, 20, 10, 1, 2, TILE_SIZE * 3, 2, 500, true, 0.8f);

        defaultTurrets.add(machineGunTurret);
        defaultTurrets.add(rocketLauncherTurret);
        defaultTurrets.add(slowDownTurret);

    /*________________________________________________________________________________________________________________*/

        // Creating map
        tiles = createMap();
        gameObjects.addAll(tiles);

        // Create default enemies and add to list
        Enemy defaultSmallEnemy = new Enemy(++id, smallEnemyTexture, 16, 25, 5, 1, 40, 40, 200);
        Enemy defaultMediumEnemy = new Enemy(++id, mediumEnemyTexture, 21, 25, 15, 1, 75, 75, 90);
        Enemy defaultLargeEnemy = new Enemy(++id, largeEnemyTexture, 23, 34, 25, 1, 100, 100, 13);

        defaultEnemies.add(defaultSmallEnemy);
        defaultEnemies.add(defaultMediumEnemy);
        defaultEnemies.add(defaultLargeEnemy);
    /*________________________________________________________________________________________________________________*/


        // Side bar
        bitmapFont = new BitmapFont();
        scoreManager = new ScoreManager(0, 100000); // for a* testing
        waveLevel = 0;

        // For turret range
        sr = new ShapeRenderer();

        // Initial enemy wave time
        timeEndOfWave = System.nanoTime();
        allEnemiesSpawned = false;

        // Hide cursor
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
                if ((i == 0 || j == 0 || i == MAP_HEIGHT - 1 || j == MAP_WIDTH - 1) // Draw border
                        && !((i == MAP_WIDTH / 2 || i == MAP_WIDTH / 2 + 1 || i == MAP_WIDTH / 2 - 1) && j == MAP_HEIGHT - 1)) // Leave hole for enemies
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

                    // No structure in the way --> add to hash map
                    nodes.put(new Vector2(i, j), new Node(i, j, 1));
                }
            }
        }

        return tiles;
    }

    /**
     * Randomly pick between the 3 different enemies.
     * Amount of enemies and time between enemies depends on <code>waveLevel</code>
     */
    private void generateEnemies()
    {
        Node playerNode = nodes.get(new Vector2(toGridPos(player.getX()), toGridPos(player.getY())));
        List<Enemy> enemies = new ArrayList<Enemy>();

        int numEnemies = (int) Math.pow(waveLevel, 1.7);
        for (int i = 0; i < numEnemies; i++)
        {
            //TODO back to 3
            int enemyType = (int) (Math.random() * 2);
            float x = MAP_WIDTH / 2 * TILE_SIZE;
            float y = MAP_HEIGHT * TILE_SIZE - 2 * TILE_SIZE;

            Enemy enemy = new Enemy(id++, waveLevel, defaultEnemies.get(enemyType), playerNode, nodes, x, y);
            enemies.add(enemy);
        }

        enemyWaves.add(new EnemyWave(enemies, (float) Math.max(0.75, 15 - waveLevel)));
    }

    /**
     * Incrementally spawns enemies.
     * Initializes a new wave <code>timeBetweenEnemyWaves</code> seconds after last wave is finished.
     */
    private void spawnEnemies()
    {
        if (!enemyWaves.isEmpty() && !enemyWaves.get(0).isWaveFinished()) // Spawning waves
        {
            Enemy enemy = enemyWaves.get(0).spawnEnemy();
            if (enemy != null)
            {
                gameObjects.add(enemy);
                enemies.add(enemy);
            }
        }
        else // If all enemies spawned
        {
            if (!allEnemiesSpawned)
            {
                timeEndOfWave = System.nanoTime();
                allEnemiesSpawned = true;
                waveLevel++;
            }

            float timeBetweenEnemyWaves = 60;
            if (Math.abs(timeEndOfWave - System.nanoTime()) / NANO_TIME_CONVERTER < timeBetweenEnemyWaves)
                return;

            if (!enemyWaves.isEmpty()) enemyWaves.remove(0);

            generateEnemies();
            allEnemiesSpawned = false;
        }
    }

    public static int toGridPos(float pos)
    {
        return Math.round(pos / TILE_SIZE);
    }

    public static float toAbsolutePos(int pos)
    {
        return pos * TILE_SIZE;
    }

    /**
     * @return A list of game objects that the player can collide with
     */
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

    /**
     * Place a turret if player has enough money
     */
    private void placeTurret()
    {
        if (player.place(turretBeingPlaced, grid) &&
                scoreManager.buy(
                        turretBeingPlaced.getCost(),
                        sr, System.nanoTime() / NANO_TIME_CONVERTER + 10,
                        false))
        {
            turretBeingPlaced.setAlpha(1);
            gameObjects.add(turretBeingPlaced);
            // There is now an obstruction here so remove it from 'travelable' nodes
            int x = toGridPos(turretBeingPlaced.getX());
            int y = toGridPos(turretBeingPlaced.getY());

            // removing node and all surrounding nodes from turret
            for (int i = x - 1; i <= x + 1; i++)
            {
                for (int j = y - 1; j <= y + 1; j++)
                {
                    System.out.println(nodes.remove(new Vector2(i, j)));
                }
            }

            Node playerNode = nodes.get(new Vector2(toGridPos(player.getX()), toGridPos(player.getY())));
            for (GameObject enemy : enemies)
            {
                ((Enemy) enemy).aStar(playerNode, nodes);
            }
        }
    }

    private void changeCurrentTurretToPlace(int amount)
    {
        currentTurretIndex += amount;

        if (currentTurretIndex == 3) currentTurretIndex = 0;
        else if (currentTurretIndex == -1) currentTurretIndex = 2;

    }

    /**
     * All input handling for player actions
     * Player movement is done in player class
     */
    private void handleInput()
    {
        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean scrolled(int amount)
            {
                changeCurrentTurretToPlace(amount);
                return true;
            }

            @Override
            public boolean keyUp(int keyCode)
            {
                if (keyCode == Input.Keys.F)
                {
                    placeTurret();
                    isTurretBeingPlaced = false;
                    turretBeingPlaced = null;
                    return true;
                }
                return false;
            }
        });

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.input.setCursorCatched(false);

        // Create greyed out turret in front of player so that he can see where turret is being placedD
        // Only place turret on key release
        if (Gdx.input.isKeyPressed(Input.Keys.F))
        {
            isTurretBeingPlaced = true;
            if (turretBeingPlaced == null)
                turretBeingPlaced = new Turret(++id, defaultTurrets.get(currentTurretIndex));
            turretBeingPlaced.setAlpha(0.5f);
        }

        // Upgrade a turret
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && currentSelectedObject instanceof Turret)
        {
            Turret currentTurret = (Turret) currentSelectedObject;
            if (scoreManager.buy(currentTurret.getUpgradeCost(), sr, System.nanoTime() / NANO_TIME_CONVERTER + 5, true))
                currentTurret.levelUp();
        }

        // Upgrade player
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q))
        {
            if (scoreManager.buy(player.getUpgradeCost(), sr, System.nanoTime() / NANO_TIME_CONVERTER + 5, true))
                player.levelUp();
        }
    }

    /**
     * Finds the current turret the player is looking at and highlights it.
     * If there isn't one on that square highlight the square instead.
     * Sets <code>currentSelectedObject</code> to the highlighted object
     */
    private void highlightAimingTile()
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

        if (isTurretBeingPlaced)
        {
            currentSelectedObject = turretBeingPlaced;
            turretBeingPlaced.setPosition(x * TILE_SIZE, y * TILE_SIZE);
            turretBeingPlaced.setAlpha(0.5f);
            return;
        }


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
    private void drawPlayer(float delta)
    {
        // If player has moved
        Node playerNode = nodes.get(new Vector2(toGridPos(player.getX()), toGridPos(player.getY())));
        if (player.handleInput(delta))
        {
            for (GameObject enemy : enemies)
            {
                ((Enemy) enemy).aStar(playerNode, nodes);
            }
        }
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

    /**
     * Draws enemy and checks for collisions
     */
    private void drawEnemy(Enemy enemy, float delta, Iterator itr)
    {
        GameObject collided = enemy.isColliding(gameObjects,
                enemy.getX() + enemy.getDx(),
                enemy.getY() + enemy.getDy(),
                enemy.getRotation() + enemy.getChangeInRotation());


        if (collided == null)
        {
            enemy.gotoNextNode(player, delta);
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

    /**
     * Draw turret and make it fire at enemies in range
     */
    private void drawTurret(Turret turret)
    {
        // Shoot
        Projectile p = new Projectile(++id, mediumEnemyTexture, 10, 10, turret);

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

    /**
     * Draw all projectiles and handles their collision
     */
    private void drawProjectiles(float delta)
    {
        // Separate loop for projectiles
        Iterator itr = projectiles.iterator();
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
    }


    /**
     * Draw side bar
     */
    private void drawSideBar()
    {
        // Score and money
        bitmapFont.draw(batch, "Score: " + scoreManager.getScore(), 760, 700, 120, 1, true);
        bitmapFont.draw(batch, "Money: " + scoreManager.getMoney(), 760, 680, 120, 1, true);

        // Turret picturedwa
        Turret currentTurret = defaultTurrets.get(currentTurretIndex);
        String title = "Turret that will be placed";
        if (currentSelectedObject instanceof Turret)
        {
            currentTurret = (Turret) currentSelectedObject;
            title = "Current selected turret";
        }

        // Turret stats
        bitmapFont.draw(batch, title, 760, 620, 120, 1, true);
        batch.draw(currentTurret.getTexture(), 760, 450, 120, 120);
        String turretStats = "STATS" +
                "\nCost: " + currentTurret.getCost() +
                "\nDamage: " + currentTurret.getDamage() +
                "\nRange: " + currentTurret.getDamageRadius() / TILE_SIZE +
                "\nRate of fire: " + 1 / currentTurret.getRateOfFire() +
                "\nUpgrade cost: " + currentTurret.getUpgradeCost();

        if (currentTurret.isSlowDown())
            turretStats += "\nSlow down factor: " + 1 / currentTurret.getSlowDownFactor();

        bitmapFont.draw(batch, turretStats, 760, 430, 120, 1, true);

        // Health bar
        batch.draw(healthBar, 760, 200);
        float remainingHealth = Math.max(0, 65 * player.getHealth() / player.getMaxHealth());
        batch.draw(redBar, 822, 219, remainingHealth, 13);

        // Level
        bitmapFont.draw(batch, "Level: " + waveLevel, 760, 150, 120, 1, true);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();

        highlightAimingTile();
        drawPlayer(delta);
        spawnEnemies();
    /*________________________________________________________________________________________________________________*/

        batch.begin();

        // Draw tiles first
        for (Tile tile : tiles)
        {
            tile.draw(batch);
        }

    /*________________________________________________________________________________________________________________*/

        // Main game loop
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

            if (gameObject instanceof Enemy) drawEnemy((Enemy) gameObject, delta, itr);

            else if (gameObject instanceof Turret) drawTurret(((Turret) gameObject));

        }

        // Done outside of main loop
        drawProjectiles(delta);
        drawSideBar();
        // Draw turret if holding b
        if (isTurretBeingPlaced && turretBeingPlaced != null) turretBeingPlaced.draw(batch);

        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.NAVY);

        //Draw radius around current turret
        if (currentSelectedObject instanceof Turret)
            sr.circle(
                    ((Turret) currentSelectedObject).getRange().x,
                    ((Turret) currentSelectedObject).getRange().y,
                    ((Turret) currentSelectedObject).getRange().radius);

        // Here because if not enough money need to draw shape to notify player
        for (GameObject gameObject : gameObjects)
        {
            if (gameObject instanceof Turret)
                sr.rect(gameObject.getBoundingRectangle().x, gameObject.getBoundingRectangle().y, gameObject.getBoundingRectangle().width, gameObject.getBoundingRectangle().height);
        }
        handleInput();
        sr.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        playerTexture.dispose();
        smallEnemyTexture.dispose();
        mediumEnemyTexture.dispose();
        largeEnemyTexture.dispose();
        slowDownTexture.dispose();
        rocketLauncherTexture.dispose();
        machineGunTexture.dispose();
        redBar.dispose();
        healthBar.dispose();
    }
}