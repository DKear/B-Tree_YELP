import java.io.IOException;
//import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BTree implements Serializable {
    int order = 32;
    Node root;
    int t = order / 2; //min degree
    long nodePosition = 1;
    long businessPosition = 1;
    long countPosition = 100;
    RAF raf = new RAF();
    int keyCount;
    //RandomAccessFile file;

    public BTree()throws IOException{

    }

    /*
    public BTree(RandomAccessFile file) throws IOException{
        root = null;
        this.file = file;
        createTree(this);

    }
    */

    public void createTree() throws IOException{
        root = allocateNode();
        root.leaf = 1;
        root.count = 0;
        root.rafPos = nodePosition;
        nodePosition = raf.writeTree(root, root.rafPos);
        raf.setRoot(root.rafPos);
    }

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
            /*for(int j = 0; j < n.children[i].count; j++){
                n.children[i].businesses[j] = raf.readBusines(n.children[i].busiRafPos[j]);
            }*/
            return searchBusiness(n, bKey);
        }


        //return null;

    }

    void insert(int key, Business b) throws IOException{
        Node r = root;
        if (fullNode(r)){
            //Node s = allocateNode();
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
        //this.countPosition =

    }

    public boolean fullNode(Node n){
        for(int i = 0; i < n.key.length; i++){
            if (n.key[i] == 0){
                return false;
            }
        }
        return true;
    }

    void insertNonfull(Node n, int key, Business b) throws IOException{
        int i = n.count - 1;
        //Business nplusone = n.businesses.get(i+1);

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
        //!!!!!!!!!!!!!!!!!!!!!!!KEEP AN EYE ON THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!
        else {
            while (i >= 0 && key < n.key[i]) {
                i--;
            }
            i++;
            //read (child i of n)

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!END!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //n.children[i] =raf.readTree(order, n.childRafPos[i]);
            /*
            for(int j = 0; j < n.children[i].count; j++){
                n.children[i].businesses[j] = raf.readBusines(n.children[i].busiRafPos[j]);
            }
            */
            if(fullNode(n.children[i])){
                splitChild(n, i);
                if(key > n.key[i]){
                    i++;
                }
            }
            //System.out.println("recursing");
            insertNonfull(n.children[i], key, b);
        }
    }

    void splitChild(Node n, int i) throws IOException{
        //System.out.println("SPLITTING");
        //n is the parent
        //z is the new node/right node
        //Node z = allocateNode();
        Node z = allocateNode();
        //y is the left node
        Node y = n.children[i];
        z.leaf = y.leaf;
        z.count = 0;
        //z.count = t - 1;

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
                //y.count--;
            }

        }
        //y.count = t - 2;

        //------------------KEEP AN EYE ON THIS LOOP--------------------
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
            //n.count--;
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
        //raf.write
        /*
        diskWrite(y);
        diskWrite(z);
        diskWrite(n);
        */

    }

//    public int readCount(){
//        raf.readCount();
//    }

    /*
    public Node diskRead(long position) throws IOException {
        file.seek(position);
        FileChannel fc = file.getChannel();
        ByteBuffer b = ByteBuffer.allocate(nodeSize);
        fc.read(b);
        b.flip();
        Node tempNode = new Node(-1);
        tempNode.leaf = b.getInt();
        tempNode.id = b.getInt();
        tempNode.count = b.getInt();

        for(int i = 0; i <2*t; i++){
            tempNode.businesses.get(i).key = b.getInt();
        }
        for(int i = 0; i< 2*t; i++){
            tempNode.children.get(i).id = b.getInt();
        }
        b.clear();
        return tempNode;
    }

    public void diskWrite(Node node) throws IOException{
        file.seek(node.id);
        FileChannel f = file.getChannel();

        ByteBuffer b = ByteBuffer.allocate(nodeSize);
        b.putInt(node.leaf);
        b.putLong(node.id);
        b.putInt(node.count);

        for(Business busi: node.businesses){
            b.putInt(busi.key);
        }
        for(Node n: node.children){
            for(int i = 0; i < n.children.size(); i++){
                b.putLong(n.children.get(i).id);
            }
        }
        b.flip();
        f.write(b);
        b.clear();
    }
    */

    /*
    public Node allocateNode() throws IOException{
        file.seek(file.length());
        Node temp = new Node(file.getFilePointer());
        diskWrite(temp);
        return temp;
    }
    */

}
