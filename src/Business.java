import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Business implements Comparator<Business>, Serializable {
    int key;
    String name;
    double stars;
    //double reviewCount;
    double similarity;
    List<String> categories;
    int reviewCount;
    long rafPos;
    ArrayList<Business> similar = new ArrayList<>();

    public int compare(Business o1, Business o2) {
        int n = (int)o1.similarity - (int)o2.similarity;
        return n;
    }

    Business(int key, String name, double stars, int reviewCount){
        this.key = key;
        this.name = name;
        this.stars = stars;
        this.reviewCount = reviewCount;

    }

    void setSimilar(Node n){
        double starXreview;
        int i;


            if(n != null) {
                for (i = 0; i < n.count; i++) {

                    if (n.leaf == 0) {
                        setSimilar(n.children[i]);
                    }
                    //for (int j = 0; j < n.count; j++) {
                    if(n.businesses[i] == null){
                        break;
                    }
                        System.out.println(n.businesses[i].key);
                        starXreview = this.stars * this.reviewCount;
                        n.businesses[i].similarity = Math.abs((n.businesses[i].stars * n.businesses[i].reviewCount) - starXreview);
                        similar.add(n.businesses[i]);

                    //}
                }
                if (n.leaf == 0) {
                    setSimilar(n.children[i]);
                }
            }



    }

    public Business(){}




}
