import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Business implements Comparator<Business>, java.io.Serializable {
    String key;
    String name;
    double stars;
    double similarity;
    List<String> categories;

    public int compare(Business o1, Business o2) {
        int n = o2.name.compareTo(o1.name);
        return n;
    }


}
