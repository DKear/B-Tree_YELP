import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Node implements  Serializable {
    Business[] businesses;
    long[] busiRafPos;


    public Node[] children;
    long[] childRafPos;

    /*
    public int compare(Node o1, Node o2){
        int i = o1.businesses.get(0).compare(o1.businesses.get(0), o2.businesses.get(0));
        return i;
    }
    */

    int count;
    int leaf;
    int[] key;

    long rafPos;


    public void sort(){
    }



}
