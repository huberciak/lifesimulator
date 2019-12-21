package agh.cs.lab3;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int hashCode(){
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }
    public String toString() {
        return ("(" + x + "," + y + ")");
    }

    public boolean precedes(Vector2d other){
        return (other.x >= this.x && other.y >= this.y);
    }

    public boolean follows(Vector2d other){
        return (other.x <= this.x && other.y <= this.y);
    }
    public Vector2d upperRight(Vector2d other){
        int mx;
        int my;
        if(this.x >= other.x){
            mx = this.x;
        }
        else{
            mx = other.x;
        }
        if(this.y >= other.y){
            my = this.y;
        }
        else{
            my = other.y;
        }
        Vector2d V = new Vector2d(mx,my);
        return V;
    }
    public Vector2d lowerLeft(Vector2d other){
        int mx;
        int my;
        if(this.x <= other.x){
            mx = this.x;
        }
        else{
            mx = other.x;
        }
        if(this.y <= other.y){
            my = this.y;
        }
        else{
            my = other.y;
        }
        Vector2d V = new Vector2d(mx,my);
        return V;
    }
    public Vector2d add(Vector2d other){
        int ax, ay;
        ax = this.x + other.x;
        ay = this.y + other.y;
        Vector2d V = new Vector2d(ax,ay);
        return V;
    }

    public Vector2d subtract(Vector2d other){
        int sx, sy;
        sx = this.x - other.x;
        sy = this.y - other.y;
        Vector2d V = new Vector2d(sx,sy);
        return V;
    }
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return true;
        if (!(other instanceof Vector2d)) return false;

        Vector2d v = (Vector2d) other;
        return ( this.x == v.x && this.y == v.y );
    }
    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }
}