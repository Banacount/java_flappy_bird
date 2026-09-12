package io.java.flappy_bird;

import com.badlogic.gdx.utils.viewport.Viewport;
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

class Obstacle extends GameEntity {
    float obstacleSlideSpeed = 1;

    Obstacle (float x, float y, float height)
    {
        super(new Rectangle(x, y, 0.4f, height));
    }

    void move (float delta, Viewport viewport) {
        if (this.rect.x > 0 - this.rect.width)
            this.rect.x -= delta * obstacleSlideSpeed;
        else
            this.rect.x = viewport.getWorldWidth();
    }
}
