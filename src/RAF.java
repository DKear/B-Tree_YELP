import Clustering.Point;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;

public class RAF implements Serializable {
    RandomAccessFile treeRaf;
    RandomAccessFile businessRaf;
    RandomAccessFile funcRaf;
    RandomAccessFile kmeansRaf;
    RandomAccessFile pointsRaf;

    public RAF() throws FileNotFoundException{
        treeRaf = new RandomAccessFile("raf/treeRAF", "rw");
        businessRaf = new RandomAccessFile("raf/businessRAF", "rw");
        funcRaf = new RandomAccessFile("raf/funcRAF", "rw");
        kmeansRaf = new RandomAccessFile("raf/kmeansRaf", "rw");
        pointsRaf = new RandomAccessFile("raf/pointsRaf", "rw");

    }

    public void setRoot(long pos) throws IOException{
        funcRaf.seek(0);
        funcRaf.writeLong(pos);
    }

    public long getRoot() throws IOException{
        funcRaf.seek(0);
        return funcRaf.readLong();
    }





    public long writeTree(Node n, long pos) throws IOException{
        treeRaf.seek(pos);

        for(int i = 0; i < n.key.length; i++){
            treeRaf.writeInt(n.key[i]);


        }

        for(int i = 0; i < n.childRafPos.length; i++){
            treeRaf.writeLong(n.childRafPos[i]);

        }

        treeRaf.writeInt(n.count);
        treeRaf.writeInt(n.leaf);

        for(int i = 0; i < n.busiRafPos.length; i++){
            treeRaf.writeLong(n.busiRafPos[i]);
        }

        treeRaf.writeLong(pos);
        return treeRaf.getFilePointer();
    }


    public long writeBusiness(Business b, long pos) throws IOException{
        int n = 64;
        String name= "";


        businessRaf.seek(pos);
        businessRaf.writeInt(b.key);
        for(int i = 0; i < (n - b.name.length()); i++){
            name = b.name + " ";
        }
        businessRaf.writeUTF(name);
        businessRaf.writeDouble(b.stars);
        businessRaf.writeInt(b.reviewCount);
        businessRaf.writeLong(pos);

        return businessRaf.getFilePointer();
    }

    public long writePoint(Point p, long pos) throws IOException{
        pointsRaf.seek(pos);
        pointsRaf.writeDouble(p.getX());
        pointsRaf.writeDouble(p.getY());
        pointsRaf.writeInt(p.getKey());
        pointsRaf.writeInt(p.getCluster());
        pointsRaf.writeLong(pos);

        return pointsRaf.getFilePointer();
    }

    public Node readTree(int order, long pos) throws IOException{
        Node n = new Node();
        n.key = new int[order -1];
        n.children = new Node[order];
        n.childRafPos = new long[order];
        n.businesses = new Business[order - 1];
        n.busiRafPos = new long[order - 1];
        treeRaf.seek(pos);
        for(int i =0; i< n.key.length; i++){
            n.key[i] = treeRaf.readInt();
        }

        for(int i = 0; i < n.childRafPos.length; i++){
            n.childRafPos[i] = treeRaf.readLong();
        }

        n.count = treeRaf.readInt();
        n.leaf = treeRaf.readInt();


        for(int i = 0; i < n.busiRafPos.length; i++){
            n.busiRafPos[i] = treeRaf.readLong();
            if(n.busiRafPos[i] != 0) {
                n.businesses[i] = readBusiness(n.busiRafPos[i]);
            }
        }



        n.rafPos = pos;

        if(n.leaf == 0){
            for(int i = 0; i < n.count + 1; i++){
                n.children[i] = readTree(order, n.childRafPos[i]);
            }
        }


        return n;
    }

    public Business readBusiness(long pos) throws IOException{
        Business business = new Business();
        businessRaf.seek(pos);
        business.key = businessRaf.readInt();
        business.name = businessRaf.readUTF().trim();
        business.stars = businessRaf.readDouble();
        business.reviewCount = businessRaf.readInt();
        business.rafPos = pos;
        return business;
    }

    public Point readPoint(long pos) throws IOException{
        Point p = new Point();
        pointsRaf.seek(pos);
        p.setX(pointsRaf.readDouble());
        p.setY(pointsRaf.readDouble());
        p.setKey(pointsRaf.readInt());
        p.setCluster(pointsRaf.readInt());
        p.rafPos = pos;
        return p;
    }

    public Node createTree(int order, long pos) throws IOException{
        Node n = new Node();
        n.key = new int[order - 1];
        n.children = new Node[order];
        n.childRafPos = new long[order];
        n.businesses = new Business[order - 1 ];
        n.busiRafPos = new long [order - 1 ];
        treeRaf.seek(pos);
        for(int i = 0; i < n.key.length; i++){
            n.key[i] = treeRaf.readInt();
        }
        for(int i = 0; i < n.childRafPos.length; i++){
            n.childRafPos[i] = treeRaf.readLong();
        }
        n.count = treeRaf.readInt();
        n.leaf = treeRaf.readInt();
        for(int i = 0; i < n.busiRafPos.length; i++){
            n.busiRafPos[i] = treeRaf.readLong();
        }
        n.rafPos = pos;

        for(int i = 0; i < n.count; i++){
            n.businesses[i] = readBusiness(n.busiRafPos[i]);
        }

        if(n.leaf == 0){
            for(int i =0; i < n.count + 1; i++){
                n.children[i] = createTree(order, n.childRafPos[i]);
            }
        }
        System.out.println(n.key[0]);
        return n;
    }






}


