package agh.cs.lab3;
import agh.cs.lab3.Vector2d;

public interface IPositionChangeObserver {
    boolean positionChanged(Vector2d oldPosition, Vector2d newPosition, Object o);
}
