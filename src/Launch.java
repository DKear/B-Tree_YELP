import Clustering.KMeans;
import Clustering.Point;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class Launch extends Application implements EventHandler<ActionEvent> {
    TextField searchField;
    Button searchButton;
    Text title;
    HBox hbox;
    HBox hbox2;
    VBox vbox;
    TextArea textAreaQuery;
    TextArea textAreaResult1;
    TextArea textAreaResult2;
    TextArea textAreaResult3;
    TextArea textAreaResult4;
    TextArea textAreaResult5;

    BTree btree;
    Node root;
    RAF raf;
    KMeans kmeans = new KMeans();
    ArrayList<Point> points = new ArrayList<>();

    public void setup() throws IOException{

        btree = new BTree();
        btree.root = new Node();

        raf = new RAF();
        btree.root = raf.createTree(32, raf.getRoot());

        raf.pointsRaf.seek(0);
        int asize = raf.pointsRaf.readInt()*32;
        for(int i = 4; i <= asize; i+= 32){
            points.add(raf.readPoint(i));
        }




//
//        raf = new RAF();
//        root = raf.createTree(4, raf.getRoot());

        /*
        root.businesses[0].setSimilar(root);
        root.businesses[0].similar.sort(new Business());
        root.businesses[0].similar.subList(4,174566).clear();
*/




    }

    public static void main(String args[]){
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception{
        setup();

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        VBox vbox = addVBox();
        HBox hbox2 = addHBox2();
        border.setTop(hbox);
        border.setCenter(vbox);
        border.setBottom(hbox2);
        hbox2.setAlignment(Pos.TOP_LEFT);



        primaryStage.setTitle("Yelp similarity finder");



        Scene scene = new Scene(border, 1500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    HBox addHBox(){
        hbox = new HBox();
        searchField = new TextField();
        searchField.setPrefColumnCount(30);
        searchButton = new Button("Search");
        searchButton.setOnAction(this);
        hbox.getChildren().addAll(searchField, searchButton);
        return hbox;
    }

    VBox addVBox(){
        vbox = new VBox();
        title = new Text("Search result:");
        textAreaQuery = new TextArea();
        textAreaQuery.setEditable(false);
        Text lowertitle = new Text("Similar:");

        vbox.getChildren().addAll(title, textAreaQuery, lowertitle);
        return vbox;
    }

    HBox addHBox2(){
        hbox2 = new HBox();
        textAreaResult1 = new TextArea();
        textAreaResult2 = new TextArea();
        textAreaResult3 = new TextArea();
        textAreaResult4 = new TextArea();
        textAreaResult5 = new TextArea();
        textAreaResult1.setEditable(false);
        textAreaResult1.setWrapText(true);
        textAreaResult2.setEditable(false);
        textAreaResult2.setWrapText(true);
        textAreaResult3.setEditable(false);
        textAreaResult3.setWrapText(true);
        textAreaResult4.setEditable(false);
        textAreaResult4.setWrapText(true);
        textAreaResult5.setEditable(false);
        textAreaResult5.setWrapText(true);
        hbox2.getChildren().addAll(textAreaResult1, textAreaResult2, textAreaResult3, textAreaResult4, textAreaResult5);
        return hbox2;

    }

    public void handle(ActionEvent event){
        if(event.getSource() == searchButton){
            try{

                Business n = btree.searchBusiness(btree.root, Integer.parseInt(searchField.getText()));
                n.setSimilar(btree.root);
                n.similar.sort(new Business());
                n.similar.subList(5, 174566).clear();//174567

                textAreaQuery.setText("Name: " + n.name + "\n" + "Rating: " + n.stars + "\n" + "Reviews: " + n.reviewCount + "\n" + "Cluster: " + points.get(n.key -1).getCluster());
                System.out.println("Name: " + n.name + "\n" + "Rating: " + n.stars + "\n" + "Reviews: " + n.reviewCount);
                textAreaResult1.setText("Name: " + n.similar.get(0).name + "\n" + "Rating: " + n.similar.get(0).stars + "\n" + "Reviews: " + n.similar.get(0).reviewCount);
                System.out.println("Name: " + n.similar.get(0).name + "\n" + "Rating: " + n.similar.get(0).stars + "\n" + "Reviews: " + n.similar.get(0).reviewCount);
                textAreaResult2.setText("Name: " + n.similar.get(1).name + "\n" + "Rating: " + n.similar.get(1).stars + "\n" + "Reviews: " + n.similar.get(1).reviewCount);
                System.out.println("Name: " + n.similar.get(1).name + "\n" + "Rating: " + n.similar.get(1).stars + "\n" + "Reviews: " + n.similar.get(1).reviewCount);
                textAreaResult3.setText("Name: " + n.similar.get(2).name + "\n" + "Rating: " + n.similar.get(2).stars + "\n" + "Reviews: " + n.similar.get(2).reviewCount);
                System.out.println("Name: " + n.similar.get(2).name + "\n" + "Rating: " + n.similar.get(2).stars + "\n" + "Reviews: " + n.similar.get(2).reviewCount);
                textAreaResult4.setText("Name: " + n.similar.get(3).name + "\n" + "Rating: " + n.similar.get(3).stars + "\n" + "Reviews: " + n.similar.get(3).reviewCount);
                System.out.println("Name: " + n.similar.get(3).name + "\n" + "Rating: " + n.similar.get(3).stars + "\n" + "Reviews: " + n.similar.get(3).reviewCount);
                textAreaResult5.setText("Name: " + n.similar.get(4).name + "\n" + "Rating: " + n.similar.get(4).stars + "\n" + "Reviews: " + n.similar.get(4).reviewCount);
                System.out.println("Name: " + n.similar.get(4).name + "\n" + "Rating: " + n.similar.get(4).stars + "\n" + "Reviews: " + n.similar.get(4).reviewCount);


            }catch(Exception e){
                e.printStackTrace();

            }

        }

    }
}
