package agh.cs.lab3;

public class World {

    public static void main(String[] args) {

        WholeMap map = new WholeMap(30, 30, 10, 10, 15, 1, 15, 40);
        Simulation simulation = new Simulation(map);
        simulation.start();

    }

}
