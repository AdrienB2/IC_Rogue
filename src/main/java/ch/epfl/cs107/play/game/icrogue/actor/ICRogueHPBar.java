package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.GraphicsEntity;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.ShapeGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Polygon;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICRogueHPBar implements Actor {
    private int hp;
    private int maxHP;
    private ShapeGraphics shape;
    private ImageGraphics image;
    private Vector[] vertices = new Vector[]{
                new Vector(0.75f, 0.1f),
                new Vector(3.8f, 0.4f),
                new Vector(3.8f, 0.1f),
                new Vector(0.75f, 0.4f)
    };
    public ICRogueHPBar(int maxHP) {
        this.hp = maxHP;
        this.maxHP = maxHP;
        this.image = new ImageGraphics("images/sprites/HPbar.png", 4, 1/2f);
        shape = new ShapeGraphics(new Polygon(Arrays.stream(vertices).toList()), Color.RED, Color.RED, 0.1f, 1.0f, 0.0f);
    }

    @Override
    public void draw(Canvas canvas) {
        shape.draw(canvas);
        image.draw(canvas);
    }

    public void updateHP(int hp) {
        if(hp <= 0) return;
        this.hp = hp;
        float ratio = (float) hp / maxHP;
        vertices[1] = new Vector(0.8f + 3 * ratio, 0.1f);
        vertices[2] = new Vector(0.8f + 3 * ratio, 0.4f);
        shape.setShape(new Polygon(Arrays.stream(vertices).toList()));
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    @Override
    public Vector getVelocity() {
        return null;
    }
}
