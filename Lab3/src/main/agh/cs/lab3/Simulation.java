package agh.cs.lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Simulation implements ActionListener {


    public WholeMap map;
    public int startAnimals;
    public int grassPerDay;
    public JFrame frame;
    public MapRender render;
    public Timer timer;
    public int time;

    public Simulation(WholeMap map) {

        this.map = map;
        this.startAnimals = 10;
        this.grassPerDay = 3;
        this.time = 50;
        timer = new Timer(time, this);
        frame = new JFrame("Evolution");
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        render = new MapRender(map,this);
        frame.add(render);

    }
    public void start() {
        for (int i = 0; i < startAnimals; i++) {
            map. placeAnimalRandom();
        }
        timer.start();
    }

    public void actionPerformed(ActionEvent event) {

        render.repaint();

        map.deadAnimals();
        map.moveAnimals();
        map.eat();
        map.movingEnergySubtract();
        map.copulation();

        for (int i = 0; i < grassPerDay; i++) {
            map.spawnGrass();
        }

    }

}
