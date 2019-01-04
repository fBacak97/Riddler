package sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AnswerSet {

    boolean answerSetOrientation;  //acroos true, down false
    int answerLength;
    char[][] situation = new char[5][5];

    ArrayList<String> candidates;   //list of potential answers

    //constructor
    public AnswerSet(int answerLength, boolean answerSetOrientation, ArrayList<String> candidates) {
        this.answerSetOrientation = answerSetOrientation;
        this.candidates = candidates;
        this.answerLength = answerLength;

    }

    public void filterSet(){
        removeDuplicates();
        filterByLength();
    }

    public void removeDuplicates() {
        Set<String> set = new HashSet<>(candidates);
        candidates.clear();
        candidates.addAll(set);
    }

    //filtering
    public void filterByLength() {
        for (int i = 0; i < candidates.size(); i++){
            if(answerLength != candidates.get(i).length()) {
                candidates.remove(i);
                i--;
            }
        }
    }

    public boolean isAnswerSetOrientation() { return answerSetOrientation; }
    public void setAnswerSetOrientation(boolean answerSetOrientation) { this.answerSetOrientation = answerSetOrientation; }
    public int getAnswerLength() { return answerLength; }
    public void setAnswerLength(int answerLength) { this.answerLength = answerLength; }
    public ArrayList<String> getCandidates() { return candidates; }
    public void setCandidates(ArrayList<String> candidates) { this.candidates = candidates; }

}