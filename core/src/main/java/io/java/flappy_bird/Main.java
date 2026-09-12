package io.java.flappy_bird;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Rectangle;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    ShapeRenderer shapeRenderer;
    FitViewport viewport;
    GameEntity testentity;
    Birdie test_flappy;

    @Override
    public void create() {
        // Prepare your application here.
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(6, 4);

        Rectangle initRectTestEntity = new Rectangle(0, 0, 0.3f, 0.3f);
        testentity = new GameEntity(initRectTestEntity);
        test_flappy = new Birdie(0, 1, viewport.getWorldHeight() / 2f);
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
        float delta = Gdx.graphics.getDeltaTime();
        float speed = 2;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            testentity.rect.x += delta * speed;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            test_flappy.jump();
        }
    }
    public void logic () {
        float delta = Gdx.graphics.getDeltaTime();
        test_flappy.update(delta, viewport);
    }
    public void draw () {
        // Clear screen
        ScreenUtils.clear(Color.GRAY);

        // Draw your application here.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(0, 0, 6f, 4f);
        shapeRenderer.end();

        testentity.drawRect(shapeRenderer, viewport);
        test_flappy.drawRect(shapeRenderer, viewport);
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

class GameEntity {
    Rectangle rect;

    GameEntity (Rectangle entity_rect) {
        rect = entity_rect;
    }

    void drawRect (ShapeRenderer shapeRender, Viewport viewport) {
        shapeRender.begin(ShapeRenderer.ShapeType.Filled);
        shapeRender.setProjectionMatrix(viewport.getCamera().combined);
        shapeRender.setColor(Color.RED);
        shapeRender.rect(rect.x, rect.y, rect.width, rect.height);
        shapeRender.end();
    }
}

class Birdie extends GameEntity {
    float jumpHeight;
    float velocity_x = 0, velocity_y = 0;

    Birdie (float jump_height, float x, float y) {
        super(new Rectangle(x, y, 0.3f, 0.3f));
        jumpHeight = (jump_height <= 0) ? 2.8f : jump_height;
    }

    void jump () {
        this.velocity_y = 0;
        this.velocity_y += jumpHeight;
    }

    void update (float delta_time, Viewport viewport) {
        // Gravity
        this.rect.y += delta_time * velocity_y;
        this.rect.x += delta_time * velocity_x;

        if (this.rect.y > 0)
            this.velocity_y += delta_time * GameConfig.GRAVITY;
        else {
            this.velocity_y = 0;
            this.rect.y = viewport.getWorldHeight() / 2;
        }
    }
}
