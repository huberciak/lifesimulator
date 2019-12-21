package agh.cs.lab3;

import java.awt.*;
import java.util.ArrayList;

public class Animal implements IMapElement {
    public MapDirection direction;
    public IWorldMap map;
    public Vector2d position;
    public int baseEnergy;
    public int energy;
    public Genotype genotype;
    public ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal() {
        position = new Vector2d(2, 2);
        this.direction = MapDirection.NORTH;
        genotype = new Genotype(8,32);
    }

    public Animal(IWorldMap map) { this(); this.map = map; }

    public Animal(IWorldMap map, Vector2d initialPosition) { this(map); this.position = initialPosition; }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy) {
        this(map, initialPosition);
        this.energy = energy;
        this.baseEnergy = energy;
    }
    public void move(MoveDirection moveDirection) {

        switch (moveDirection) {
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case FORWARD:
            case BACKWARD:
                MapDirection thisMoveDirection = this.direction;
                if(moveDirection == MoveDirection.BACKWARD)
                    thisMoveDirection = thisMoveDirection.next().next();
                Vector2d expectedMove = this.position.add(thisMoveDirection.toUnitVector());

                if (this.map.canMoveTo(expectedMove)) {
                    Vector2d before = new Vector2d(this.getPosition().x, this.getPosition().y);
                    position = position.add(direction.toUnitVector());
                    this.positionChanged(before, this.position, this);
                }
                break;
            default:
                break;
        }
    }

    public void chooseDirection() {
        int numOfRotation = genotype.randomGen();
        for (int i = 0; i < numOfRotation; i++) {
            this.move(MoveDirection.RIGHT);
        }
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public boolean isMovable() {
        return true;
    }

    public boolean death() { return this.energy <= 0; }
    public void addEnergy(int value) { this.energy = this.energy + value; }
    public void subtractEnergy(int value) { this.energy = this.energy - value; }

    public Animal copulation(Animal parent) {

        int childEnergy = (int) ((this.energy*0.25) + (parent.energy)*0.25);
        this.subtractEnergy((int) (this.energy*0.25));
        parent.subtractEnergy((int) (parent.energy*0.25));
        Animal child = new Animal(map, parent.getPosition(), childEnergy);
        child.genotype = new Genotype(this.genotype, parent.genotype);

        return child;
    }

    public Color toColor() {
        return new Color(0, 0, 0);
    }

    private void positionChanged(Vector2d before, Vector2d vector, Object object) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(before, vector, object);
        }
    }
    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }


}
