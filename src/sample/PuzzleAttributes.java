package sample;

import javafx.util.Pair;
import org.json.*;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class PuzzleAttributes {

    private Clue[] acrossClues;
    private Clue[] downClues;
    private String[][] answers;
    private int[][] numbers;
    private static PuzzleAttributes instance;
    private ArrayList<Integer>[] answerIndexes;
    private Clue[] acrossCluesInOrder, downCluesInOrder;
    private static ArrayList<Constraint> constraints;

    public static ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public Clue[] getAcrossCluesInOrder() {
        return acrossCluesInOrder;
    }

    public Clue[] getDownCluesInOrder() {
        return downCluesInOrder;
    }

    public PuzzleAttributes(String puzzleid) throws IOException, JSONException {

        JSONObject obj;
        if(puzzleid.equals("daily")){
            int dailyId = requestPuzzleId();
            System.out.println("Visiting with PUZZLE_ID parameter https://nyt-games-prd.appspot.com/svc/crosswords/v6/puzzle/$PUZZLE_ID.json to retrieve puzzle json file...");
            String html = Jsoup.connect("https://nyt-games-prd.appspot.com/svc/crosswords/v6/puzzle/" + dailyId + ".json").ignoreContentType(true).get().body().text();
            System.out.println("JSON Object created...");
            obj = new JSONObject(html);
            System.out.println(obj.toString());
        }else{
            String content = new String(Files.readAllBytes(Paths.get( puzzleid+"_puzzle")));
            obj = new JSONObject(content);
        }

        JSONObject contents = obj.getJSONArray("body").getJSONObject(0);

        JSONArray cells = contents.getJSONArray("cells");
        System.out.println(cells.toString());
        System.out.println("Retrieving answers from JSON Object...");

        JSONArray clues = contents.getJSONArray("clues");
        System.out.println(clues.toString());
        System.out.println("Retrieving clues from JSON Object...");

        acrossCluesInOrder = new Clue[5];
        downCluesInOrder = new Clue[5];
        acrossClues = new Clue[5];
        downClues = new Clue[5];
        for(int i = 0; i < clues.length(); i++) {
            if(i < 5) {
                acrossClues[i] = new Clue(clues.getJSONObject(i).getJSONArray("text").getJSONObject(0).get("plain").toString(), Integer.parseInt(clues.getJSONObject(i).get("label").toString()));
                acrossCluesInOrder[Integer.parseInt(clues.getJSONObject(i).getJSONArray("cells").get(0).toString())/5] = new Clue(clues.getJSONObject(i).getJSONArray("text").getJSONObject(0).get("plain").toString(), Integer.parseInt(clues.getJSONObject(i).get("label").toString()));
            }
            else {
                downClues[i - 5] = new Clue(clues.getJSONObject(i).getJSONArray("text").getJSONObject(0).get("plain").toString(), Integer.parseInt(clues.getJSONObject(i).get("label").toString()));
                downCluesInOrder[Integer.parseInt(clues.getJSONObject(i).getJSONArray("cells").get(0).toString())%5] = new Clue(clues.getJSONObject(i).getJSONArray("text").getJSONObject(0).get("plain").toString(), Integer.parseInt(clues.getJSONObject(i).get("label").toString()));
            }
        }

        numbers = new int[5][5];
        System.out.println("Retrieving numbers from JSON Object...");
        for(int i = 0; i < clues.length(); i++) {
            int target = Integer.parseInt(clues.getJSONObject(i).getJSONArray("cells").get(0).toString());
            int label = Integer.parseInt(clues.getJSONObject(i).get("label").toString());
            int count = 0;
            for(int j = 0; j < 5; j++) {
                for(int k = 0; k<5; k++) {
                    if(count == target)
                        numbers[j][k] = label;
                    count++;
                }
            }
        }

        answers = new String[5][5];
        int row = 0;
        int column = 0;
        for(int i = 0; i < cells.length(); i++) {
            column = column % 5;
            try {
                answers[row][column] = cells.getJSONObject(i).get("answer").toString();
            }
            catch(Exception e) {
                answers[row][column] = "-1";
            }
            column++;
            if(column == 5)
                row++;
        }

        System.out.println("Retrieving answer indexes from JSON Object...");
        answerIndexes = new ArrayList[10];
        for(int i = 0; i < 10; i++){
            answerIndexes[i] = new ArrayList<>();
        }
        for(int i = 0; i < clues.length(); i++) {

            for(int j = 0; j < clues.getJSONObject(i).getJSONArray("cells").length(); j++) {
                if(i<5)
                    answerIndexes[i].add(Integer.parseInt(clues.getJSONObject(i).getJSONArray("cells").get(j).toString()));
                else
                    answerIndexes[Integer.parseInt(clues.getJSONObject(i).getJSONArray("cells").get(j).toString())%5+5].add(Integer.parseInt(clues.getJSONObject(i).getJSONArray("cells").get(j).toString()));
            }
        }

        constraints = new ArrayList();
        Pair<Integer,Integer>[] placeholder = new Pair[25];
        for(int i = 0 ; i < 25; i++)
            placeholder[i] = new Pair<>(0,0);

        for(int i = 1; i < 11; i++){
            int temp = 0;
            for(int j = 0; j<answerIndexes[i-1].size();j++) {
                if (placeholder[answerIndexes[i-1].get(j)].getKey() == 0)
                    placeholder[answerIndexes[i-1].get(j)] = new Pair<>(i,temp);
                else{
                    constraints.add(new Constraint(placeholder[answerIndexes[i-1].get(j)].getKey(),i,
                            placeholder[answerIndexes[i-1].get(j)].getValue(),temp));
                }
                temp++;
            }
        }
        System.out.println("CONSTRAINTS:");
        for(int i = 0; i < constraints.size(); i++){
            System.out.println(constraints.get(i).getAcrossIndex() + ":" + constraints.get(i).getAcrossLetterNumber() + "|||" + constraints.get(i).getDownIndex() + ":" + constraints.get(i).getDownLetterNumber());
        }
    }

    private int requestPuzzleId() throws JSONException, IOException {
        String puzzleAddress = Jsoup.connect("https://nyt-games-prd.appspot.com/svc/crosswords/v3/games-hub-puzzles.json").ignoreContentType(true).get().body().text();
        System.out.println("Visiting https://nyt-games-prd.appspot.com/svc/crosswords/v3/games-hub-puzzles.json for puzzleId... ");
        JSONObject object = new JSONObject(puzzleAddress);
        return Integer.parseInt((object.getJSONObject("results").getJSONArray("mini_puzzle").getJSONObject(0).get("puzzle_id").toString()));
    }


    public Clue[] getAcrossClues() {
        return acrossClues;
    }

    public String[][] getAnswers() {
        return answers;
    }

    public Clue[] getDownClues() {
        return downClues;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public void recordPuzzle() throws IOException, JSONException {
        int puzzleid = requestPuzzleId();
        String html = Jsoup.connect("https://nyt-games-prd.appspot.com/svc/crosswords/v6/puzzle/" + puzzleid + ".json").ignoreContentType(true).get().body().text();
        Files.write(Paths.get(puzzleid+ "_puzzle"), new JSONObject(html).toString().getBytes());
    }

}