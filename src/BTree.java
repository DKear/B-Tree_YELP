public class BTree {
    Node root;
    int t = 2; //min degree


    Business searchBusiness(Node n, Business b){
        int i = 0;
        while(i <= n.count && b.name.compareTo(n.businesses.get(i).name) > 0){
            i = i + 1;
            if(i <= n.count && b.name.compareTo(n.businesses.get(i).name) == 0){
                return(n.businesses.get(i));
            } else if(n.leaf){
                return null;
            }else{
                //disk read
                return searchBusiness(n.children.get(i), b);
            }

        }
        return null;

    }

    void insert(Business b){
        Node r = root;
        if (r.count == 2*t -1){
            Node s = new Node();
            root = s;
            s.leaf = false;
            s.count = 0;
            s.children.add(r);
            s.children.sort(s);
            splitChild(s, 0);
            insertNonfull(s, b);

        } else{
            insertNonfull(r, b);
        }
    }

    void insertNonfull(Node n, Business b){
        int i = n.count;
        Business nplusone = n.businesses.get(i+1);

        if (n.leaf){
            while (i >= 0 && b.compare(b, n.businesses.get(i))< 0){
                nplusone = n.businesses.get(i);
                i = i -1;
            }
            nplusone = b;
            n.count++;
            //write x
        }
        else while(i >= 0 && b.compare(b, n.businesses.get(i))< 0){
            i--;
        }
        i++;
        //read (child i of n)
        if(n.children.get(i).count == 3){
            splitChild(n, i);
            if(b.compare(b, n.businesses.get(i)) > 1){
                i++;
            }
        }
        insertNonfull(n.children.get(i), b);
    }

    void splitChild(Node n, int i){
        Node z = new Node();
        Node y = n.children.get(i);
        z.leaf = y.leaf;
        z.count = t - 1;
        for(int j = 0; j < t-1; j++){
            //Business zbj = z.businesses.get(j);
            //zbj = y.businesses.get(j + t);
            z.businesses.add(y.businesses.get(j + t));

        }
        if(!y.leaf){
            for(int j = 0; j < t; j++){
                //Node zcjplus1 = z.children.get(j);
                //zcjplus1 = y.children.get(j+t);
                z.children.add(y.children.get(j+t));
            }
        }
        y.count = t - 1;

        n.children.add(z);
        n.children.sort(n);


        n.businesses.add(y.businesses.get(t));
        n.businesses.sort(new Business());


        n.count++;
        //write y
        //write z
        //write n

    }

}
