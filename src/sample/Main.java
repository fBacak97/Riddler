package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    static Scene scene;
    static Stage myStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane box = new GridPane();
        box.setMinWidth(400);
        box.setMaxWidth(400);
        box.setMinHeight(300);
        box.setMaxHeight(300);
        box.setAlignment(Pos.CENTER);
        box.setVgap(20);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Riddler");

        Label title = new Label("Riddler");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        title.setAlignment(Pos.CENTER);
        box.add(title,0,0);

        Button but = new Button("Today's Puzzle");
        but.setOnMouseClicked(e ->{
            try{
                primaryStage.setScene(PuzzleScene.startScene("daily"));
                primaryStage.setTitle("Today's Puzzle");
                AlertBox.display("logger","Visiting https://nyt-games-prd.appspot.com/svc/crosswords/v3/games-hub-puzzles.json for puzzleId... \n\n" +
                        "Visiting with PUZZLE_ID parameter https://nyt-games-prd.appspot.com/svc/crosswords/v6/puzzle/$PUZZLE_ID.json to retrieve puzzle json file..." +
                        "JSON Object created...\n\n" +
                        "Retrieving clues from JSON Object...\n\n" +
                        "Retrieving answers from JSON Object...\n\n" +
                        "Retrieving numbers from JSON Object...\n\n" +
                        "Filling puzzle scene with clues data...\n\n" +
                        "Filling puzzle scene with answers data...\n\n"+
                        "Filling puzzle scene with numbers data...\n\n");
            }catch(Exception e1){
                e1.printStackTrace();
            }
        });

        box.add(but, 0, 1);

        AtomicReference<String> puzzleid = new AtomicReference<>("");
        ObservableList<String> puzzleidList = FXCollections.observableArrayList();
        File f = new File(".");
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
        for (Iterator<String> i = names.iterator(); i.hasNext();) {
            String item = i.next();
            if(item.contains("_puzzle")){
                puzzleidList.add(item.replace("_puzzle",""));
            }
        }
        ComboBox<String> dropdown = new ComboBox<String>(puzzleidList);
        dropdown.getSelectionModel().selectFirst();
        puzzleid.set(dropdown.getSelectionModel().getSelectedItem());
        dropdown.setOnAction((event)->{
            puzzleid.set((String) dropdown.getSelectionModel().getSelectedItem());
        });
        box.add(dropdown, 1, 2);

        Button but1 = new Button("Stored Puzzle");
        but1.setOnMouseClicked(e ->{
            try{
                primaryStage.setScene(PuzzleScene.startScene(puzzleid.get()));
                primaryStage.setTitle("Stored Puzzle");
                AlertBox.display("logger","Puzzle Id received from user...\n\n" +
                        "JSON Object created from local file...\n\n" +
                        "Retrieving clues from JSON Object...\n\n" +
                        "Retrieving answers from JSON Object...\n\n" +
                        "Retrieving numbers from JSON Object...\n\n" +
                        "Filling puzzle scene with clues data...\n\n" +
                        "Filling puzzle scene with answers data...\n\n"+
                        "Filling puzzle scene with numbers data...\n\n");
            }catch(Exception e1){
                e1.printStackTrace();
            }
        });

        box.add(but1, 0, 2);

        scene =  new Scene(box);
        primaryStage.setScene(scene);
        myStage = primaryStage;
        myStage.show();
    }

    public static void main(String[] args) throws IOException, JSONException {
        launch(args);
    }

}
