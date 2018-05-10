package Clustering;

import java.io.Serializable;
import java.util.Random;

public class Point implements Serializable{
    private double x = 0;
    private double y = 0;
    private int key;
    private int cluster = 0;
    public long rafPos = 4;

    public Point(int key, double x, double y){
        this.key = key;
        this.x = x;
        this.y = y;
    }

    public Point(){}

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setKey(int k){
        this.key = k;
    }

    public void setCluster(int c ){
        this.cluster = c;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getKey(){
        return key;
    }

    public int getCluster(){
        return cluster;
    }

    public static double getDistance(Point p, Point c){
        return Math.sqrt(Math.pow((c.getY() - p.getY()), 2) + Math.pow((c.getX() - p.getX()), 2));
    }

    public Point randomPoint(){
        Random r = new Random();
        double x = r.nextDouble()*5;
        double y = r.nextDouble()*7361;
        return new Point(999999, x, y);
    }

}
