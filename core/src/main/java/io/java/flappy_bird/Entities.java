package io.java.flappy_bird;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

public class Entities {}

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
        jumpHeight = (jump_height <= 0) ? 2.3f : jump_height;
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
            System.out.println("L you fukin folded");
            System.exit(0);
        }
    }
}

class Obstacle extends GameEntity {
    float obstacleSlideSpeed = 1;

    Obstacle (float x, float y, float height)
    {
        super(new Rectangle(x, y, 0.4f, height));

        // Auto values if all parameters are 0
        //if (x == 0 && y == 0 && height == 0)
        //{
        //}
        // Will do something with this in the future.
    }

    void move (float delta, Viewport viewport) {
        if (this.rect.x > 0 - this.rect.width) {
            this.rect.x -= delta * obstacleSlideSpeed;
        }
        else {
            this.rect.x = viewport.getWorldWidth();
        }
    }

    void obstacleControlBottom (Obstacle other, Viewport viewport) {
        if (this.rect.x < 0 - this.rect.width)
        {
            randomHoleTrigger(other, viewport);
        }
    }

    void randomHoleTrigger (Obstacle other, Viewport viewport)
    {
        float worldHeight = viewport.getWorldHeight();
        float gap = 1f;

        float bottomPipeYOffset = MathUtils.random(0.3f, 3f);
        float topPipeYOffset = (worldHeight - bottomPipeYOffset) - gap;

        this.rect.y = -(worldHeight - bottomPipeYOffset);
        other.rect.y = worldHeight - topPipeYOffset;
    }
}

class ObstaclePiece {
    Obstacle top_part, bottom_part;
    Rectangle pipe_rect;
    Boolean isInPipe = false, isInPipeState = false;

    ObstaclePiece (Viewport viewport, float x_offset)
    {
        bottom_part = new Obstacle(viewport.getWorldWidth() + x_offset, 0, viewport.getWorldHeight());
        top_part = new Obstacle(viewport.getWorldWidth() + x_offset, 0, viewport.getWorldHeight());
        pipe_rect = new Rectangle(viewport.getWorldWidth() + x_offset, 0, 0.4f, viewport.getWorldHeight());

        bottom_part.randomHoleTrigger(top_part, viewport);
    }

    void updatePiece (float delta, Viewport viewport) {
        bottom_part.move(delta, viewport);
        top_part.move(delta, viewport);
        bottom_part.obstacleControlBottom(top_part, viewport);
        pipe_rect.x = bottom_part.rect.x;
    }

    void pipeHandle (Birdie bird, GameStateHandler GameState) {
        // Handles when the bird is inside the pipe
        if (bird.rect.overlaps(pipe_rect)) isInPipe = true;
        else isInPipe = false;

        if (isInPipeState != isInPipe)
        {
            // Execute one time events here
            if (isInPipe) System.out.println("You is inside a pipe");
            else if (!isInPipe) GameState.scored();

            isInPipeState = isInPipe;
        }
    }

    void drawPiece (ShapeRenderer shapeRenderer, Viewport viewport) {
        top_part.drawRect(shapeRenderer, viewport);
        bottom_part.drawRect(shapeRenderer, viewport);
    }
}


class GameStateHandler {
    Boolean GameStarted = false;
    int Score = 0;

    GameStateHandler () {}

    void started () {
        GameStarted = true;
    }

    void scored () {
        Score += 1;
        System.out.printf("Score: %d\n", Score);
    }
}
