//Kmeans Class, implements points and clusters

package Clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class KMeans implements Serializable{

    public ArrayList<Point> points;
    ArrayList<Cluster> clusters;

    public KMeans(){
        points = new ArrayList<>();
        clusters = new ArrayList<>();
    }

    public void addPoints(ArrayList<Point> p){
        points = p;
    }

    public void createClusters(int n){
        Random r  = new Random();
        for(int i = 0; i < n; i++){
            Cluster cluster = new Cluster();
            Point centroid = new Point(999999,r.nextDouble()*5,r.nextDouble()*20 ); //7361
            cluster.setCenter(centroid);
            clusters.add(cluster);
        }
    }

    private ArrayList<Point> getCenters(){
        ArrayList<Point> centers = new ArrayList<>();
        for(Cluster c : clusters){
            Point a = c.getCenter();
            Point p = new Point(9999999, a.getX(), a.getY());
            centers.add(p);

        }
        return centers;
    }

    private void setCluster(){
        //cluster
        int c = 0;
        //distance
        double d;
        double nd;

        for(Point point : points){
            nd = Double.MAX_VALUE;
            for(int i = 0; i < clusters.size(); i++){
                Cluster cluster = clusters.get(i);
                d = Point.getDistance(point, cluster.center);
                if(d < nd){
                    nd = d;
                    c = i;
                }
            }
            point.setCluster(c);
            clusters.get(c).addPoint(point);
        }
    }

    private void calcCenters(){
        for(Cluster c : clusters) {
            double X = 0;
            double Y = 0;
            ArrayList<Point> points = c.getPoints();
            int n = points.size();

            for(Point p : points){
                X = X + p.getX();
                Y = Y + p.getY();
            }

            Point center = c.getCenter();
            if(n > 0){
                center.setX(X/n);
                center.setY(Y/n);
            }
        }
    }

    public void calc(){
        boolean complete = false;

        while(!complete){
            for(Cluster c: clusters){
                c.points.clear();
            }
            ArrayList<Point> mrCenter = getCenters();
            setCluster();
            calcCenters();
            ArrayList<Point> newCenters = getCenters();

            double d = 0;
            for(int i = 0; i < mrCenter.size(); i++){
                d = Point.getDistance(mrCenter.get(i), newCenters.get(i));
            }

            if(d ==0){
                complete = true;
            }



        }
    }
}
