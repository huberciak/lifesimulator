package agh.cs.lab3;

import agh.cs.lab3.IMapElement;
import agh.cs.lab3.MoveDirection;
import agh.cs.lab3.IPositionChangeObserver;

import java.awt.*;

public class Grass implements IMapElement {

    protected Vector2d position;

    public Vector2d getPosition() {
        return this.position;
    }

    public Grass(Vector2d position) {
        this.position = position;
    }

    public boolean isMovable() {
        return false;
    }


    public void move(MoveDirection d) {
    }

    public void addObserver(IPositionChangeObserver observer) {
        return;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public Color toColor() {
        return new Color(67, 222, 31);
    }
}
