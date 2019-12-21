package agh.cs.lab3;

import agh.cs.lab3.Vector2d;
import agh.cs.lab3.Animal;
import agh.cs.lab3.MoveDirection;
import agh.cs.lab3.IWorldMap;
import agh.cs.lab3.Grass;
import agh.cs.lab3.IMapElement;
import agh.cs.lab3.IPositionChangeObserver;

import java.util.*;

public class WholeMap implements IWorldMap, IPositionChangeObserver {

    public int width;
    public int height;
    private final Vector2d rightCorner;
    private final Vector2d leftCorner;

    public final int jungleWidth;
    public final int jungleHeight;
    private final Vector2d leftCornerJungle;
    private final Vector2d rightCornerJungle;
    
    private int plantEnergy;
    private int moveEnergy;
    private int startEnergy;

    private final int copulationEnergy;

    public LinkedList<Animal> animalsList;
    public LinkedList<Grass> grassList;
    public Map<Vector2d, Grass> grass = new HashMap<>();
    public Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();


    public WholeMap(int width, int height, int jungleWidth, int jungleHeight, int plantEnergy, int moveEnergy, int copulationEnergy, int startEnergy) {

        this.width = width;
        this.height = height;
        this.leftCorner = new Vector2d(0, 0);
        this.rightCorner = new Vector2d(width - 1, height - 1);

        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        
        int leftX= (width-jungleWidth)/2;
        int leftY = (height-jungleHeight)/2;
        int rightX = width - 1 - (width-jungleWidth)/2;
        int rightY = height - 1 - (height-jungleHeight)/2 ;

        this.leftCornerJungle = new Vector2d(leftX, leftY);
        this.rightCornerJungle = new Vector2d(rightX, rightY);


        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.startEnergy = startEnergy;

        this.copulationEnergy = copulationEnergy;

        this.grassList = new LinkedList<>();
        this.animalsList = new LinkedList<>();
    }

    public LinkedList<Grass> getGrass() {
        return grassList;
    }
    public LinkedList<Animal> getAnimals() {
        return animalsList;
    }
    public Vector2d getCorner() {
        return leftCornerJungle;
    }

    public Vector2d calculatePosition(Vector2d position) {
        int X;
        int Y;

        if (position.x > leftCorner.x) {
            X = (position.x % width);
        } else {
            X = (width - (position.x % width)) % width;
        }

        if (position.y > leftCorner.y) {
            Y = (position.y % height);
        } else {
            Y = (height - (position.y % height)) % height;
        }
        return new Vector2d(X, Y);
    }

    public boolean place(IMapElement element) {

        Vector2d position = calculatePosition(element.getPosition());

        if (!canPlace(position)) {
            throw new IllegalArgumentException("Field " + element.getPosition() + " is occupied");

        } else {
            if (element instanceof Grass) {
                if (grass.get(position) == null)
                    grass.put(position, (Grass) element);
                    grassList.add((Grass) element);
            }
            if (element instanceof Animal) {
                addAnimal((Animal) element, position);
                animalsList.add((Animal) element);
                element.addObserver(this);
            }

        }
        return true;
    }

    private boolean addAnimal(Animal a, Vector2d p) {
        if (a == null) return false;
        Vector2d pos = calculatePosition(p);
        LinkedList<Animal> list = animals.get(pos);
        if (list == null) {
            LinkedList<Animal> l = new LinkedList<>();
            l.add(a);
            animals.put(pos, l);

        } else if (list != null) {
            list.add(a);
        }
        return true;
    }

    private boolean removeAnimal(Animal animal, Vector2d pos) {

        Vector2d position = calculatePosition(pos);
        LinkedList<Animal> l = animals.get(position);

        if (l == null || l.size()==0)
            return false;
        else {
            l.remove(animal);
            if (l.size() == 0) {
                animals.remove(position);
            }
        }
        return true;
    }

    public boolean isOccupied(Vector2d pos) {
        return objectAt(pos) != null;
    }

    public Object objectAt(Vector2d pos) {
        Vector2d position = calculatePosition(pos);
        LinkedList<Animal> list = animals.get(position);
        if (list == null) return grass.get(position);
        else if (list.size() == 0) return grass.get(position);
        else
            return list.getFirst();
    }

    public boolean canPlace(Vector2d pos) {
        Vector2d position = calculatePosition(pos);
        if (animals.get(position) == null) return true;
        if (animals.get(position).size() < 3) return true;
        return false;
    }

    public boolean canMoveTo(Vector2d pos) {
        Vector2d position = calculatePosition(pos);
        if (animals.get(position) == null) return true;
        if (animals.get(position).size() < 2) return true;
        return false;
    }

    public void moveAnimals() {
        LinkedList<Animal> list = getAnimals();
        for (int i = 0; i < list.size(); i++) {
            animalsList.get(i).chooseDirection();
            animalsList.get(i).move(MoveDirection.FORWARD);
        }
    }

    public void movingEnergySubtract() {
        for (LinkedList<Animal> animalList : animals.values()) {
            if (animalList != null && animalList.size() > 0) {
                for (Animal animal : animalList) {
                    animal.subtractEnergy(moveEnergy);
                }
            }
        }
    }

    public void eat() {
        LinkedList<Grass> afterEating = new LinkedList<>();

        for (Grass eatenGrass : grass.values()) {
            LinkedList<Animal> list = animals.get(eatenGrass.getPosition());
            if (list != null && list.size()>0) {
                    for (Animal animal : list) {
                        animal.addEnergy(plantEnergy);
                        afterEating.add(eatenGrass);
                    }
                }
            }
        for (Grass _grass : afterEating) {
            grass.remove(_grass.getPosition());
            grassList.remove(_grass);
        }
    }

    public void copulation() {
        for (LinkedList<Animal> animalList : animals.values()) {
                if (animalList != null &&animalList.size() == 2) {
                    Animal parent1 = animalList.get(0);
                    Animal parent2 = animalList.get(1);
                    if (parent1.energy >= copulationEnergy && parent2.energy >= copulationEnergy){
                            Animal child = parent2.copulation(parent1);
                            place(child);
                    }
                }
        }
    }

    public void deadAnimals() {
        LinkedList<Animal> l = getAnimals();
        for (int i = 0; i < l.size(); i++) {
            Animal a = animalsList.get(i);
            if (a.death()) {
                removeAnimal(a, a.getPosition());
                a.removeObserver(this);
                animalsList.remove(a);
            }
        }
    }

    public boolean placeAnimalRandom() {
        Vector2d position = new Vector2d((int) (Math.random() * (jungleWidth) + leftCornerJungle.x), (int) (Math.random() * (jungleHeight) + leftCornerJungle.y));
            if (canPlace(position)) {
                place(new Animal(this, position, startEnergy));
                return true;
    }
        return false;
    }

    public boolean positionChanged(Vector2d Position1, Vector2d Position2, Object object) {

        Vector2d oldP = calculatePosition(Position1);
        Vector2d newP = calculatePosition(Position2);

        if (canMoveTo(newP)) {
            removeAnimal((Animal) object, oldP);
            addAnimal((Animal) object, newP);
            return true;
        }
        return false;
    }

    public void spawnGrass() {

        //Jungle
        int jungle = jungleWidth * jungleHeight;
        int n = 0;
        while (n < 2 * jungle) {
            Vector2d newGrass = new Vector2d((int) (Math.random() * (jungleWidth) + leftCornerJungle.x), (int) (Math.random() * (jungleHeight) + leftCornerJungle.y));
            if (grass.get(newGrass) == null && canPlace(newGrass)) {
                place(new Grass(newGrass));
                break;
            }
            n++;
        }
        //Steppe
        n = 0;
        while (n < jungle) {
            Vector2d newGrass = new Vector2d((int) (Math.random() * (width) + leftCorner.x), (int) (Math.random() * (height) + leftCorner.y));
            if (grass.get(newGrass) == null && canPlace(newGrass)) {
                place(new Grass(newGrass));
                break;
            }
            n++;
        }
    }


}