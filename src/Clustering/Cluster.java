package Clustering;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cluster implements Serializable{

    ArrayList<Point> points;
    Point center;
    DecimalFormat format;

    public Cluster(){
        points = new ArrayList<>();
        format = new DecimalFormat("#.00");
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public Point getCenter(){
        return center;
    }


    public void setCenter(Point cp){
        center = cp;
    }
}
