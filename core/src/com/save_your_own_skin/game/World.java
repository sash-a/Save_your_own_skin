package com.save_your_own_skin.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.save_your_own_skin.game_objects.Player;

public class World extends ApplicationAdapter
{
    SpriteBatch batch;
    Texture img;
    Player p;

    Camera camera;
    public static final int WORLD_WIDTH = 800;
    public static final int WORLD_HEIGHT = 800;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        img = new Texture("player.png");
        p = new Player(img, 20, 20, 1, 1, 1, 100, 100, 100);
        p.setX(50);
        p.setY(50);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.update();
    }

    @Override
    public void render()
    {
        float delta = Gdx.graphics.getDeltaTime();

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        p.update(delta);
        batch.draw(p, p.getX(), p.getY(), p.getOriginX(), p.getOriginY(), p.getWidth(), p.getHeight(), p.getScaleX(), p.getScaleY(), p.getRotation());
//        p.translate((float) (5 * Math.sin(p.getRotation() * Math.PI / 180)), (float) (5 * Math.cos(p.getRotation() * Math.PI / 180)));
//        p.rotate(1f);

        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        img.dispose();
    }
}