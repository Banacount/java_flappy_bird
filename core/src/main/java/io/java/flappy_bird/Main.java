package io.java.flappy_bird;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    ShapeRenderer shapeRenderer;
    FitViewport viewport;
    GameStateHandler GameState = new GameStateHandler();
    SpriteBatch spriteBatch;

    // UI things
    BitmapFont font;

    // Define entities
    Birdie bird;
    ObstaclePiece[] pieces;

    @Override
    public void create() {
        // Prepare your application here.
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(6, 4);
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        // Initialize entities
        bird = new Birdie(0, 0.6f, viewport.getWorldHeight() / 2f, GameState);
        pieces = new ObstaclePiece[3];
        pieces[0] = new ObstaclePiece(viewport, 0, 0);
        pieces[1] = new ObstaclePiece(viewport, -2f, 0);
        pieces[2] = new ObstaclePiece(viewport, -4f, 0);
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
            bird.jump(1);

            if (!GameState.GameStarted)  GameState.started();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            GameState.PipeSpeed = 10f;
            bird.velocity_y = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            bird.jump(2);
        }

    }
    public void logic () {
        float delta = Gdx.graphics.getDeltaTime();
        GameState.scoreText = "Score: " + Integer.toString(GameState.Score);

        // Very bare bone 'pause' mechanism
        if (!GameState.GameStarted) return;

        // Update entities
        bird.update(delta, viewport);

        // Check collision with the obstacles pussy
        for (int i = 0; i < 3; i++) {
            pieces[i].updatePiece(delta, viewport);
            pieces[i].PipeSpeed = GameState.PipeSpeed;

            Boolean DidTopPartOverlap = pieces[i].top_part.rect.overlaps(bird.rect);
            Boolean DidBottomPartOverlap = pieces[i].bottom_part.rect.overlaps(bird.rect);

            if (DidTopPartOverlap || DidBottomPartOverlap) {
                System.out.println("Failed event: Overlapped with pipe");
                GameState.failHandle(pieces, bird, viewport);
                break;
            }

            if (bird.rect.y < 0 || bird.rect.y > viewport.getWorldHeight())
            {
                System.out.println("Failed event: Out of scope");
                GameState.failHandle(pieces, bird, viewport);
                break;
            }

            pieces[i].pipeHandle(bird, GameState);
        }
    }
    public void draw () {
        // Clear screen
        ScreenUtils.clear(Color.GRAY);

        // Draw your application here.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(0, 0, 6f, 4f);
        shapeRenderer.end();

        pieces[0].drawPiece(shapeRenderer, viewport);
        pieces[1].drawPiece(shapeRenderer, viewport);
        pieces[2].drawPiece(shapeRenderer, viewport);
        bird.drawRect(shapeRenderer, viewport);

        spriteBatch.begin();
        font.draw(spriteBatch, GameState.scoreText, 40, 600);
        spriteBatch.end();
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
        spriteBatch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}

