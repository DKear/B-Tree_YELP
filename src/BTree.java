//Btree class

import java.io.IOException;
import java.io.Serializable;

public class BTree implements Serializable {
    int order = 32;
    Node root;
    int t = order / 2; //min degree
    long nodePosition = 1;
    long businessPosition = 1;
    RAF raf = new RAF();
    int keyCount;
    //RandomAccessFile file;

    public BTree()throws IOException{

    }


    //create blank tree, write it to RAF
    public void createTree() throws IOException{
        root = allocateNode();
        root.leaf = 1;
        root.count = 0;
        root.rafPos = nodePosition;
        nodePosition = raf.writeTree(root, root.rafPos);
        raf.setRoot(root.rafPos);
    }

    //create blank node, write to RAF
    public Node allocateNode(){
        Node n = new Node();
        n.key = new int[order - 1];
        n.children = new Node[order];
        n.childRafPos = new long[order];
        n.count = 0;
        n.leaf = 0;
        n.businesses = new Business[order - 1];
        n.busiRafPos = new long[order - 1];
        n.rafPos = nodePosition;
        return n;
    }

    //Search B-tree and RAF for a business
    Business searchBusiness(Node n, int bKey) throws IOException{
        int i = 0;
        while(i < n.count && bKey > n.key[i]) {
            i++;
        }

        if(i <= n.count - 1 && bKey == n.key[i]){
            return(n.businesses[i]);
        } else if(n.leaf == 1){
            return null;
        }else{
            //disk read
            n = raf.readTree(order, n.childRafPos[i]);

            return searchBusiness(n, bKey);
        }



    }

    //Insert a business into the btree, if the root node is full, split it and then attempt the add
    void insert(int key, Business b) throws IOException{
        Node r = root;
        if (fullNode(r)){
            Node s = allocateNode();
            root = s;
            s.leaf = 0;
            s.count = 0;
            s.children[0] = r; //Here
            s.childRafPos[0] = r.rafPos;
            nodePosition = raf.writeTree(s, s.rafPos);
            raf.setRoot(s.rafPos);
            splitChild(s, 0);
            insertNonfull(s, key , b);

        } else{
            insertNonfull(r, key, b);
        }
        this.keyCount++;

    }

    //Checks if a node is full
    public boolean fullNode(Node n){
        for(int i = 0; i < n.key.length; i++){
            if (n.key[i] == 0){
                return false;
            }
        }
        return true;
    }

    //Insert the node into a non-full node (simple insert)
    void insertNonfull(Node n, int key, Business b) throws IOException{
        int i = n.count - 1;

        if (n.leaf==1) {
            while(i >= 0 && key < n.key[i]){
                n.key[i + 1] = n.key[i ];
                n.businesses[i + 1] = n.businesses[i];
                n.busiRafPos[i+1] = n.busiRafPos[i];
                i--;
            }
            n.key[i + 1] = key;
            n.businesses[i + 1] = b;
            n.count++;
            b.rafPos = businessPosition;
            n.busiRafPos[i + 1] = b.rafPos;
            raf.writeTree(n, n.rafPos);
            businessPosition = raf.writeBusiness(b, b.rafPos);
        }
        else {
            while (i >= 0 && key < n.key[i]) {
                i--;
            }
            i++;

            if(fullNode(n.children[i])){
                splitChild(n, i);
                if(key > n.key[i]){
                    i++;
                }
            }
            insertNonfull(n.children[i], key, b);
        }
    }

    //split a child node
    void splitChild(Node n, int i) throws IOException{
        //n is the parent
        //z is the new node/right node
        //Node z = allocateNode();
        Node z = allocateNode();
        Node y = n.children[i];
        z.leaf = y.leaf;
        z.count = 0;

        for(int j = 0; j < t - 1; j++){
            z.key[j] = y.key[j + t];
            y.key[j + t] = 0;
            z.businesses[j] = y.businesses[j + t];
            z.count++;
            z.busiRafPos[j] = y.busiRafPos[j + t];
            y.businesses[j + t] = null;
            y.count--;
            y.busiRafPos[j+t] = 0;

        }

        if(y.leaf !=1){
            for(int j = 0; j < t; j++){
                z.children[j] = y.children[j + t];
                z.childRafPos[j] = y.children[j + t].rafPos;
                y.children[j + t] = null;
                y.childRafPos[j + t] = 0;
            }

        }

        //shiftting values
        for(int j = n.count; j > i ; j--){
            n.children[j - 1] = n.children[j - 2];
            n.childRafPos[j - 1] = n.children[j - 2].rafPos;
        }
        n.children[i + 1] = z;
        n.childRafPos[i + 1] = z.rafPos;

        for(int j =n.count - 1; j > i - 1; j--){
            n.key[j - 1] = n.key[j - 2];
            n.key[j - 2] = 0;
            n.businesses[j - 1] = n.businesses[j - 2];
            n.busiRafPos[j - 1] = n.busiRafPos[j - 2];
            n.businesses[j - 2] = null;
            n.busiRafPos[j - 2] = 0;
        }

        n.key[i] = y.key[t - 1];
        n.businesses[i] = y.businesses[t - 1];
        n.busiRafPos[i] = y.busiRafPos[t-1];
        y.key[t-1] = 0;
        y.businesses[t - 1] = null;
        y.count--;
        n.count++;
        y.busiRafPos[t - 1] = 0;
        raf.writeTree(y, y.rafPos);
        nodePosition = raf.writeTree(z, z.rafPos);
        raf.writeTree(n, n.rafPos);


    }



}
