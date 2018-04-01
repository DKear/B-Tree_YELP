import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Node implements Comparator<Node>, java.io.Serializable{
    public ArrayList<Business> businesses = new ArrayList<Business>();
    public ArrayList<Node> children = new ArrayList<Node>();

    public int compare(Node o1, Node o2){
        int i = o1.businesses.get(0).compare(o1.businesses.get(0), o2.businesses.get(0));
        return i;
    }

    int count = 0;
    boolean leaf;
    public void sort(){
    }



}
