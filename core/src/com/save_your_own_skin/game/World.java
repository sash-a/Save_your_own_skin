package com.save_your_own_skin.game;

import base_classes.GameObject;
import base_classes.PlaceableObject;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game_objects.Border;
import com.save_your_own_skin.game_objects.Enemy;
import com.save_your_own_skin.game_objects.Player;
import com.save_your_own_skin.game_objects.Turret;

import java.security.Key;
import java.util.*;

public class World extends ApplicationAdapter
{
    public static final int WORLD_WIDTH = 800;
    public static final int WORLD_HEIGHT = 800;
    public static final int TILE_SIZE = 20;
    public static final int MAP_HEIGHT = 40;
    public static final int MAP_WIDTH = 40;


    SpriteBatch batch;
    Texture playerTexture;
    Player player;
    Texture enemyTexture;
    Enemy enemy;
    Texture borderTexture;

    ShapeRenderer sr;

    Map<Vector2, GameObject> gameObjectPositions;

    public int[][] grid;

    private int id;

    @Override
    public void create()
    {
        id = 0;
        batch = new SpriteBatch();
        grid = new int[MAP_WIDTH][MAP_HEIGHT];

        playerTexture = new Texture("player.png");
        player = new Player(playerTexture, 20, 20, 1, 1, 1, 100, 100, 200, id);
        player.setX(50);
        player.setY(50);
        id++;

        enemyTexture = new Texture("enemy.png");
        enemy = new Enemy(enemyTexture, 20, 20, 1, 1, 1, 100, 100, 210, id);
        enemy.setPosition(400, 400);
        id++;

        borderTexture = new Texture("block.png");
        List<Border> borders = createMap();

        // TODO: put method?
        gameObjectPositions = new HashMap<Vector2, GameObject>();
        gameObjectPositions.put(player.getVector2(), player);
        gameObjectPositions.put(enemy.getVector2(), enemy);
        for (Border border : borders)
        {
            gameObjectPositions.put(border.getVector2(), border);
        }
        // Debug
        sr = new ShapeRenderer();
    }

    private List<Border> createMap()
    {
        List<Border> borders = new ArrayList<Border>();
        for (int i = 0; i < MAP_WIDTH; i++)
        {
            for (int j = 0; j < MAP_HEIGHT; j++)
            {
                if (i == 0 || j == 0 || i == MAP_HEIGHT - 1 || j == MAP_WIDTH - 1)
                {
                    Border b = new Border(borderTexture, TILE_SIZE, TILE_SIZE, 0, 0, 0, id);
                    b.setPosition(i * TILE_SIZE, j * TILE_SIZE);
                    borders.add(b);
                    id++;

                    grid[i][j] = 0;
                }
                else
                {
                    grid[i][j] = 1;
                }
            }
        }

        return borders;
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

        player.handleInput(delta);

        // Just player
        GameObject collided = player.isColliding(gameObjectPositions.values(),
                player.getX() + player.getDx(),
                player.getY() + player.getDy());
        if (collided != null)
        {
            collided.onCollision(player, delta);
            player.onCollision(collided, delta);
            if (collided instanceof Enemy)
                gameObjectPositions.values().remove(collided);
        }
        else
        {
            player.translate(player.getDx(), player.getDy());
        }

        PlaceableObject toPlace = null;
        if (Gdx.input.isKeyPressed(Input.Keys.B))
        {
            toPlace = new Turret(borderTexture, 20, 20, 20, 20, 20, 1, 1, 1);
            player.place(toPlace);
            gameObjectPositions.put(new Vector2(toPlace.getX(), toPlace.getY()), toPlace);
        }

        for (GameObject gameObject : gameObjectPositions.values())
        {
            if (!gameObject.isDead())
            {
                // Draw game objects
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

                if (!(gameObject instanceof Player))
                {
                    gameObject.update(delta);
                    if (gameObject instanceof Enemy)
                        ((Enemy) gameObject).moveToPlayer(player, delta);
                }
            }

        }


//        if (!enemy.isDead())
//        {
//            batch.draw(enemy, enemy.getX(), enemy.getY(), enemy.getOriginX(), enemy.getOriginY(), enemy.getWidth(), enemy.getHeight(), enemy.getScaleX(), enemy.getScaleY(), enemy.getRotation());
//        }
//        player.translate((float) (5 * Math.sin(player.getRotation() * Math.PI / 180)), (float) (5 * Math.cos(player.getRotation() * Math.PI / 180)));
//        player.rotate(1f);

        batch.end();


        // Debug
//        Rectangle r = player.getBoundingRectangle();
//
//        sr.begin(ShapeRenderer.ShapeType.Line);
//        sr.setColor(new Color(1, 1, 1, 0));
//        sr.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
//        sr.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        playerTexture.dispose();
    }
}