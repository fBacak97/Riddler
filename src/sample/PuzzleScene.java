package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

public class PuzzleScene {

    static Scene scene;

    public static void start(String puzzleid) throws Exception {

        PuzzleAttributes puzzleAttributes = new PuzzleAttributes(puzzleid);

        GridPane maingrid = new GridPane();
        maingrid.setAlignment(Pos.CENTER);
        maingrid.setHgap(10);
        maingrid.setVgap(10);
        maingrid.setPadding(new Insets(25, 25, 25, 25));
        maingrid.setStyle("-fx-background: #FFFFFF;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.setStyle("-fx-background: #FFFFFF;");
        grid.setGridLinesVisible(true);

        GridPane solGrid = new GridPane();
        solGrid.setAlignment(Pos.CENTER);
        solGrid.setPadding(new Insets(5, 5, 5, 5));
        solGrid.setStyle("-fx-background: #FFFFFF;");
        solGrid.setGridLinesVisible(true);

        TextField[][] textFields = new TextField[5][5];
        TextField[][] solFields = new TextField[5][5];
        int[][] numbers = puzzleAttributes.getNumbers();

        //solution grid
        System.out.println("Filling puzzle scene with answers data...");
        String[][] answers = puzzleAttributes.getAnswers();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                solFields[i][j] = new TextField(answers[i][j]);
                solFields[i][j].setStyle("-fx-display-caret: false;");
                solFields[i][j].setEditable(false);

                solFields[i][j].setMinSize(35, 35);
                solFields[i][j].setMaxSize(35, 35);
                solFields[i][j].setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
                solFields[i][j].setAlignment(Pos.CENTER);
                solGrid.add(solFields[i][j], j, i);

                if(numbers[i][j] != 0){
                    Label clueNumber = new Label(""+numbers[i][j]);
                    clueNumber.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                    clueNumber.setPadding(new Insets(-20, 0, 0, 5));
                    solGrid.add(clueNumber, j, i);
                }
            }
        }

        System.out.println("Filling puzzle scene with clues data...");
        System.out.println("Filling puzzle scene with numbers data...");
        //Main puzzle
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //System.out.println(numbers[i][j]);

                textFields[i][j] = new TextField();
                textFields[i][j].setStyle("-fx-display-caret: false;");
                textFields[i][j].setEditable(false);
                textFields[i][j].setMinSize(100, 100);
                textFields[i][j].setMaxSize(100, 100);
                textFields[i][j].setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
                textFields[i][j].setAlignment(Pos.CENTER);

                TextField temp = textFields[i][j];
                textFields[i][j].setOnKeyReleased(ke -> {
                    temp.setText(ke.getText().toUpperCase());
                });
                grid.add(textFields[i][j], j, i);

                //fills clue numbers
                if(numbers[i][j] != 0){
                    Label clueNumber = new Label(""+numbers[i][j]);
                    clueNumber.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    clueNumber.setPadding(new Insets(-70, 0, 0, 5));
                    grid.add(clueNumber, j, i);
                }
            }
        }

        //painting empty black logic
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(solFields[i][j].getText().equals("-1")){
                    solFields[i][j].setStyle("-fx-background-color: #000;");
                    textFields[i][j].setDisable(true);
                    textFields[i][j].setStyle("-fx-background-color: #000;");
                    solFields[i][j].setText("");
                }
            }
        }

        //  textFields[3][3].setDisable(true);

        //SOLVING PUZZLE
        System.out.println("START SOLVING THE PUZZLE:");
        ArrayList<String>[] wordSet = WordGenerator.generate(puzzleAttributes);
        System.out.println("GIVE GENERATED WORDSETS TO SOLVER:");
        String[] solutionSet = Solve.solvePuzzle(puzzleAttributes, wordSet);

        // Puts solutions of the puzzle into a string array
        StringBuilder[] solutions_in_string = new StringBuilder[10];
        for (int i = 0; i < solutions_in_string.length; i++) {
            solutions_in_string[i] = new StringBuilder("");
        }

        int count = 0;
        for (int i = 0; i < solFields.length ; i++) {
            for (int j = 0; j < solFields[i].length; j++) {
                if(solFields[i][j].getText().equals("I"))
                    solutions_in_string[count].append("İ");
                else
                    solutions_in_string[count].append(solFields[i][j].getText());
            }
            count++;
        }

        for (int i = 0; i < solFields.length ; i++) {
            for (int j = 0; j < solFields[i].length; j++) {
                if(solFields[j][i].getText().equals("I"))
                    solutions_in_string[count].append("İ");
                else
                    solutions_in_string[count].append(solFields[j][i].getText());
            }
            count++;
        }

        for (StringBuilder s: solutions_in_string
                ) {
            System.out.println("solution " + s.toString());
        }

        //fills the true solutions found by AI
        for (int i = 0; i < solutionSet.length/2; i++) { //only the first 5 (across)

            int counter = 0;
            for (int j = 0; j < solutionSet[i].length(); j++) { //length of the word
                if(solFields[i][j].getText().equals("")){ //skip empty blocks
                    counter++;
                }
                else{
                    if(!textFields[i][j+counter].isDisabled())
                        textFields[i][j+counter].setText(solutionSet[i].substring(j,j+1));
                }
            }
        }

        //finds matching answers with solution set and puzzle solutions and prints them on the puzzle grid
        for (int i = 0; i < solutionSet.length ; i++) {

            if(solutionSet[i].equals(solutions_in_string[i].toString())){
                System.out.println("match found");
                if(i<5){
                    for(int j = 0; j < solutionSet[i].length();j++)
                        if(!textFields[i][j+(5-solutionSet[i].length())].isDisabled())
                            textFields[i][j+(5-solutionSet[i].length())].setText(solutionSet[i].substring(j,j+1));
                }
                else
                    for(int j = 0; j < solutionSet[i].length();j++)
                        if(!textFields[j + (5-solutionSet[i].length())][i%5].isDisabled())
                            textFields[j + (5-solutionSet[i].length())][i%5].setText(solutionSet[i].substring(j,j+1));
            }
        }

        // accross clue section
        VBox accross = new VBox();
        accross.setMinWidth(300);
        accross.setMaxWidth(300);
        accross.setPadding(new Insets(10));
        accross.setSpacing(8);

        Label title = new Label("Accross");
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(0, 0, 0, 30));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        accross.getChildren().add(title);

        Clue[] aC = puzzleAttributes.getAcrossClues();
        Label[] clueAccross = new Label[5];

        for (int i = 0; i < 5; i++) {
            clueAccross[i] = new Label(aC[i].getLabel() + ". " + aC[i].getClue());
        }

        for (int i=0; i < 5; i++) {
            VBox.setMargin(clueAccross[i], new Insets(10, 0, 0, 8));
            clueAccross[i].setWrapText(true);
            accross.getChildren().add(clueAccross[i]);
        }

        // down clue section
        VBox down = new VBox();
        down.setMinWidth(300);
        down.setMaxWidth(300);
        down.setPadding(new Insets(10));
        down.setSpacing(8);

        Label titled = new Label("Down");
        titled.setPadding(new Insets(0, 0, 0, 30));
        titled.setAlignment(Pos.CENTER);
        titled.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        down.getChildren().add(titled);

        Clue[] dC = puzzleAttributes.getDownClues();
        Label[] clueDown = new Label[5];

        for (int i = 0; i < 5; i++) {
            clueDown[i] = new Label(dC[i].getLabel() + ". " + dC[i].getClue());
        }

        for (int i=0; i < 5; i++) {
            VBox.setMargin(clueDown[i], new Insets(10, 0, 0, 8));
            clueDown[i].setWrapText(true);
            down.getChildren().add(clueDown[i]);
        }

        //configurations
        maingrid.add(grid , 0,1, 1,1);
        maingrid.add(solGrid, 1 , 1, 4,4);
        solGrid.setAlignment(Pos.BOTTOM_CENTER);
        grid.setAlignment(Pos.BOTTOM_CENTER);

        Label currentClue = new Label("Riddler");
        maingrid.add(currentClue , 0,0);

        maingrid.add(accross, 1,0, 1,2);

        maingrid.add(down, 2,0, 2, 2);

        if(puzzleid.equals("daily")){
            Button recordButton = new Button("Record");
            recordButton.setOnMouseClicked(e ->{
                try {
                    puzzleAttributes.recordPuzzle();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            });
            maingrid.add(recordButton, 4, 4);
        }

        Button returnButton = new Button("Back");
        returnButton.setOnMouseClicked(event -> {
            Main.myStage.setScene(Main.scene);
            Main.myStage.setTitle("Riddler");
        });
        maingrid.add(returnButton, 4,3);

        scene = new Scene(maingrid);
    }

    public static Scene startScene(String puzzleid) throws Exception {
        start(puzzleid);
        return scene;
    }
}
