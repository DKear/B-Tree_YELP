import Clustering.KMeans;
import Clustering.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriteRaf implements Serializable {









    public static void main(String args[]) throws IOException {
        String line;
        BTree bTree = new BTree();
        bTree.createTree();
        KMeans kmeans = new KMeans();
        ArrayList<Point> pointsArraylist = new ArrayList<>();
        int keycount = 1;
        long pointRafPos = 4;
        File json = new File("C:\\Users\\Jamie\\Documents\\GitHub\\B-Tree_YELP\\src\\business.json");





        try{
            Scanner scanner = new Scanner(json);
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                JSONObject obj = new JSONObject(line);
                String name = obj.getString("name");
                double stars = obj.getDouble("stars");
                int reviewCount = obj.getInt("review_count");

                //JSONArray categoriesJ = obj.getJSONArray("categories");
                //List categories = new ArrayList(categoriesJ.length());

                //for(int i = 0; i < categoriesJ.length(); i++) {
                //    categories.add(categoriesJ.toList().get(i));
                //}
                //

                System.out.println("inserting: " + keycount);

                bTree.insert(keycount, new Business(keycount, name, stars, reviewCount));
                Point p = new Point(keycount, stars, reviewCount);
                pointsArraylist.add(p);
                p.rafPos = pointRafPos;
                pointRafPos = bTree.raf.writePoint(p, pointRafPos);

                keycount++;
                bTree.raf.funcRaf.writeInt(keycount);
                //categories.


            }
            bTree.raf.pointsRaf.seek(0);
            bTree.raf.pointsRaf.writeInt(pointsArraylist.size());
            System.out.println("calcing clusters");
            kmeans.addPoints(pointsArraylist);
            kmeans.createClusters(5);
            kmeans.calc();
            for(Point p: kmeans.points){
                bTree.raf.writePoint(p, p.rafPos);
            }
            //bTree.raf.pointsRaf.writeInt();

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("file not found");
        }



/*

        Business business = new Business();
        business.name = "test0";
        business.key = 1;
        bTree.insert(business.key, business);

        Business business1 = new Business();
        business1.name = "test1";
        business1.key = 2;
        bTree.insert(business1.key, business1);

        Business business2 = new Business();
        business2.name = "test2";
        business2.key = 3;
        bTree.insert(business2.key, business2);

        Business business3 = new Business();
        business3.name = "test3";
        business3.key = 4;
        bTree.insert(business3.key, business3);

        Business business4 = new Business();
        business4.name = "test4";
        business4.key = 5;
        bTree.insert(business4.key, business4);

        Business business5 = new Business();
        business5.name = "test5";
        business5.key = 6;
        bTree.insert(business5.key, business5);

        Business business6 = new Business();
        business6.name = "test6";
        business6.key = 7;
        bTree.insert(business6.key, business6);

        Business business7 = new Business();
        business7.name = "test7";
        business7.key = 8;
        bTree.insert(business7.key, business7);

        Business business8 = new Business();
        business8.name = "test8";
        business8.key = 9;
        bTree.insert(business8.key, business8);

        Business business9 = new Business();
        business9.name = "test9";
        business9.key = 10;
        bTree.insert(business9.key, business9);



        System.out.println("searching:");
        System.out.println(bTree.searchBusiness(bTree.root, 3).key);

*/


        /*
        bTree.insert(business);
        bTree.insert(business1);
        bTree.insert(business2);
        bTree.insert(business3);
        bTree.insert(business4);
        bTree.insert(business5);
        bTree.insert(business6);
        bTree.insert(business7);
        bTree.insert(business8);
        bTree.insert(business9);
        bTree.insert(business10);
        bTree.insert(business11);
        bTree.insert(business12);
        bTree.insert(business13);
        bTree.insert(business14);
        bTree.insert(business15);
        */




        //bTree.insert(business2);
        //bTree.insert(business3);
        //bTree.insert(business1);
        //bTree.insert(business7);
        //bTree.insert(business4);
        //bTree.insert(business6);





        //System.out.println(bTree.searchBusiness(bTree.root, business7).name);

        //oos.writeObject(bTree);







    }
}
