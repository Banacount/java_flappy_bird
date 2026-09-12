package io.java.flappy_bird;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    ShapeRenderer shapeRenderer;
    FitViewport viewport;

    // Define entities
    Birdie bird;
    Obstacle test_obs;

    @Override
    public void create() {
        // Prepare your application here.
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(6, 4);

        // Initialize entities
        bird = new Birdie(0, 1, viewport.getWorldHeight() / 2f);
        test_obs = new Obstacle(viewport.getWorldWidth(), 0, 2f);
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        viewport.update(width, height, true);

        // Resize your application here. The parameters represent the new window size.
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    public void input () {
        boolean jumpExecuted = (
            Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
            Gdx.input.isKeyJustPressed(Input.Keys.W) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
        );

        if (jumpExecuted) {
            bird.jump();
        }
    }
    public void logic () {
        float delta = Gdx.graphics.getDeltaTime();

        // Update entities
        bird.update(delta, viewport);
        test_obs.move(delta, viewport);
    }
    public void draw () {
        // Clear screen
        ScreenUtils.clear(Color.GRAY);

        // Draw your application here.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(0, 0, 6f, 4f);
        shapeRenderer.end();

        test_obs.drawRect(shapeRenderer, viewport);
        bird.drawRect(shapeRenderer, viewport);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}

