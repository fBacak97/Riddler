package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class WordGenerator {

    static ArrayList<String>[] candidates = new ArrayList[10];
    static ArrayList<String>[] trackerResults = new ArrayList[10];

    public static ArrayList<String>[] generate(PuzzleAttributes attributes) throws IOException {
        candidates = googleSearch(attributes);
        return candidates;
    }

    public static ArrayList<String>[] googleSearch(PuzzleAttributes attributes) throws IOException {
        ArrayList<String>[] acrossArrayLists = new ArrayList[5];
        for(int i = 0; i < 5; i++)
            acrossArrayLists[i] = new ArrayList<>();
        ArrayList<String>[] downArrayLists = new ArrayList[5];
        for(int i = 0; i < 5; i++)
            downArrayLists[i] = new ArrayList<>();

        System.out.println("SEARCH ONLINE FOR WORDSETS:");

        //across
        for(int j = 0; j < 5; j++) {
            ArrayList<String> words = getWords(attributes.getAcrossCluesInOrder()[j].getClue());
            ArrayList<String> localC = new ArrayList<>();
            String query;

                query = "http://www.google.com/search?lr=lang_en&cr=US&q=";

            for (int i = 0; i < words.size(); i++) {
                if (i != (words.size() - 1)) {
                    query = query + words.get(i).replaceAll("[-+.^:,']", "") + "+";
                } else {
                    query = query + words.get(i).replaceAll("[-+.^:,']", "") + "+-crossword";
                }
            }

            System.out.println(query);

            try {
                Document doc = Jsoup.connect(query).userAgent("Chrome").get();
                Elements elems = doc.getElementsByClass("st");

                for (int i = 0; i < elems.size(); i++) {
                    localC.addAll(getWords(elems.get(i).text()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            acrossArrayLists[j].addAll(localC);
            LinkedHashSet<String> localCset = new LinkedHashSet<>(acrossArrayLists[j]);

            acrossArrayLists[j].clear();
            acrossArrayLists[j].addAll(localCset);
            acrossArrayLists[j].replaceAll(String::toUpperCase);

        }

        //down
        for(int j = 0; j < 5; j++) {

            ArrayList<String> words = getWords(attributes.getDownCluesInOrder()[j].getClue());
            ArrayList<String> localC = new ArrayList<>();
            String query = "http://www.google.com/search?lr=lang_en&cr=US&q=";

            for (int i = 0; i < words.size(); i++) {
                if (i != (words.size() - 1)) {
                    query = query + words.get(i).replaceAll("[-+.^:,']", "") + "+";
                } else {
                    query = query + words.get(i).replaceAll("[-+.^:,']", "") + "+-crossword";
                }
            }

            System.out.println(query);

            try {
                Document doc = Jsoup.connect(query).userAgent("Chrome").get();
                Elements elems = doc.getElementsByClass("st");

                for (int i = 0; i < elems.size(); i++) {
                    localC.addAll(getWords(elems.get(i).text()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            downArrayLists[j].addAll(localC);
            LinkedHashSet<String> localCset = new LinkedHashSet<>(downArrayLists[j]);

            downArrayLists[j].clear();
            downArrayLists[j].addAll(localCset);
            downArrayLists[j].replaceAll(String::toUpperCase);

        }
        ArrayList<String>[] combinedArrayList = new ArrayList[10];
        for(int i = 0; i < 10; i++){
            if(i < 5){
                combinedArrayList[i] = acrossArrayLists[i];
            }
            else{
                combinedArrayList[i] = downArrayLists[i-5];
            }
        }
        return combinedArrayList;
    }

    public static ArrayList<String>[] trackerSearch(PuzzleAttributes attributes) throws IOException {
        ArrayList<String>[] acrossArrayLists = new ArrayList[5];
        for(int i = 0; i < 5; i++)
            acrossArrayLists[i] = new ArrayList<>();
        ArrayList<String>[] downArrayLists = new ArrayList[5];
        for(int i = 0; i < 5; i++)
            downArrayLists[i] = new ArrayList<>();

        for(int j = 0; j < 5; j++) {

            ArrayList<String> words = getWords(attributes.getAcrossCluesInOrder()[j].getClue());
            ArrayList<String> localC = new ArrayList<>();
            String query = "http://crosswordtracker.com/clue/";

            for (int i = 0; i < words.size(); i++)
            {
                if (i != (words.size() - 1))
                    query = query + words.get(i).toLowerCase() + "-";

                else
                    query = query + words.get(i).toLowerCase() + "/";

            }
            System.out.println(query);

            try {
                Document doc = Jsoup.parse(new URL(query), 10000);
                Elements elems = doc.getElementsByClass("answer");

                for(int i = 0; i < elems.size(); i++){

                    localC.add(elems.get(i).text());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            acrossArrayLists[j].addAll(localC);
            LinkedHashSet<String> localCset = new LinkedHashSet<>(acrossArrayLists[j]);
            acrossArrayLists[j].clear();
            acrossArrayLists[j].addAll(localCset);
            acrossArrayLists[j].replaceAll(String::toUpperCase);
        }

        for(int j = 0; j < 5; j++) {

            ArrayList<String> words = getWords(attributes.getDownCluesInOrder()[j].getClue());
            ArrayList<String> localC = new ArrayList<>();
            String query = "http://crosswordtracker.com/search/?answer=&clue=";

            for (int i = 0; i < words.size(); i++)
            {
                if (i != (words.size() - 1))
                    query = query + words.get(i).toLowerCase() + "+";
                else
                    query = query + words.get(i).toLowerCase() + "/";
            }
            System.out.println(query);

            try {
                Document doc = Jsoup.parse(new URL(query), 10000);
                Elements elems = doc.getElementsByClass("answer");

                for(int i = 0; i < elems.size(); i++){
                    localC.add(elems.get(i).text());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            downArrayLists[j].addAll(localC);
            LinkedHashSet<String> localCset = new LinkedHashSet<>(downArrayLists[j]);
            downArrayLists[j].clear();
            downArrayLists[j].addAll(localCset);
            downArrayLists[j].replaceAll(String::toUpperCase);
        }

        ArrayList<String>[] combinedArrayList = new ArrayList[10];
        for(int i = 0; i < 10; i++){
            if(i < 5)
                combinedArrayList[i] = acrossArrayLists[i];
            else
                combinedArrayList[i] = downArrayLists[i-5];
        }
        return combinedArrayList;
    }

    public static ArrayList<String> getWords(String text) {
        ArrayList<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();

        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex));
            }
        }
        return words;
    }
}
