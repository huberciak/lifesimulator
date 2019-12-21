package agh.cs.lab3;

import javax.swing.*;
import java.awt.*;

public class MapRender extends JPanel {

    public WholeMap map;
    public Simulation simulation;

    public MapRender(WholeMap map, Simulation simulation) {
        this.map = map;
        this.simulation = simulation;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize(1000,1000);
        int width = this.getWidth();
        int height = this.getHeight();
        int wScale = width / map.width;
        int hScale = height / map.height;

        //Steppe
        g.setColor(new Color(255, 244, 79));
        g.fillRect(0, 0, width, height);

        //Jungle
        g.setColor(new Color(50, 130, 7));
        g.fillRect(map.getCorner().x * wScale,
                map.getCorner().y * hScale,
                map.jungleWidth * wScale,
                map.jungleHeight * hScale);

        for (Grass grass : map.getGrass()) {
            g.setColor(grass.toColor());
            int y = map.calculatePosition(grass.getPosition()).y * hScale;
            int x = map.calculatePosition(grass.getPosition()).x * wScale;
            g.fillRect(x, y, wScale, hScale);
        }

        for (Animal animal : map.getAnimals()) {
            g.setColor(animal.toColor());
            int y = map.calculatePosition(animal.getPosition()).y * hScale;
            int x = map.calculatePosition(animal.getPosition()).x * wScale;
            g.fillRect(x, y, wScale, hScale);
        }
    }

}
