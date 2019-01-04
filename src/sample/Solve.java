package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solve {

    static AnswerSet[] acrossSolutionSet = new AnswerSet[5];
    static AnswerSet[] downSolutionSet = new AnswerSet[5];

    static String[] acrossAnswers = {"","","","",""};
    static String[] downAnswers = {"","","","",""};

    static String[][] answersForSol;

    public static String[] solvePuzzle(PuzzleAttributes attributes, ArrayList<String>[] wordSet) throws IOException {
        ArrayList<String>[] candidateSets = new ArrayList[10];

        for(int i = 0; i < 10; i++){
            candidateSets[i] = new ArrayList<>();
            candidateSets[i].addAll(wordSet[i]);
        }

        answersForSol = attributes.getAnswers();

        //getting across answer array
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j ++) {
                if (!answersForSol[i][j].equals("-1")) {
                    acrossAnswers[i] += answersForSol[i][j];
                }
            }
        }

        //getting down answer array
        for(int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i ++) {
                if (!answersForSol[i][j].equals("-1")) {
                    downAnswers[j] += answersForSol[i][j];
                }
            }
        }

        //initializing and filtering the solution sets
        for(int i = 0; i < 5; i++) {
            acrossSolutionSet[i] = new AnswerSet(acrossAnswers[i].length(), true, candidateSets[i]);
            acrossSolutionSet[i].filterSet();
        }
        for(int i = 0; i < 5; i++) {
            downSolutionSet[i] = new AnswerSet(downAnswers[i].length(), false, candidateSets[i+5]);
            downSolutionSet[i].filterSet();
        }

        System.out.println("FILTERING THE CANDIDATES:");
        filterByLetter(PuzzleAttributes.getConstraints());

        System.out.println("ACCROSS CANDIDATE SOLUTIONS");
        for(int i = 0; i < 5; i++)
            System.out.println(acrossSolutionSet[i].getCandidates());
        System.out.println("DOWN CANDIDATE SOLUTIONS");
        for(int i = 0; i < 5; i++)
            System.out.println(downSolutionSet[i].getCandidates());
        System.out.println("TRYING COMBINATIONS:");

        String[] str = tryCombinations(acrossSolutionSet, downSolutionSet);
        System.out.println("FINAL SOLUTION:");
        for (int i = 0; i < 10; i++) {
            System.out.println( i + ") "+str[i]);
        }

        return str;
    }

    public static void filterByLetter(ArrayList<Constraint> constraints) {

        int acrossIndex, downIndex;
        boolean flag = true;
        Set<String> set = new HashSet<>();
        ArrayList<String> temp;
        ArrayList<String> lastrun = new ArrayList<>();
        int whileCount = 0;

        while (flag == true && whileCount < 3) {
            whileCount++;
            flag = false;
            for (int i = 0; i < constraints.size(); i +=4) {
                acrossIndex = constraints.get(i).getAcrossIndex()-1;
                downIndex = (constraints.get(i).getDownIndex()-1)%5;
                temp = new ArrayList<>();
                for (int j = 0; j < acrossSolutionSet[acrossIndex].getCandidates().size(); j++) {
                    for (int k = 0; k < downSolutionSet[downIndex].getCandidates().size(); k++) {
                        if (acrossSolutionSet[acrossIndex].getCandidates().get(j).charAt(constraints.get(i).getAcrossLetterNumber())
                                == downSolutionSet[downIndex].getCandidates().get(k).charAt(constraints.get(i).getDownLetterNumber())) {
                            if(!temp.contains(downSolutionSet[downIndex].getCandidates().get(k)))
                                temp.add(downSolutionSet[downIndex].getCandidates().get(k));
                        }

                    }
                }
                if(temp.size() != downSolutionSet[downIndex].getCandidates().size())
                    flag = true;

                downSolutionSet[downIndex].getCandidates().clear();
                downSolutionSet[downIndex].getCandidates().addAll(temp);

                temp = new ArrayList<>();
                for (int j = 0; j < downSolutionSet[downIndex].getCandidates().size(); j++) {
                    for (int k = 0; k < acrossSolutionSet[acrossIndex].getCandidates().size(); k++) {
                        if (downSolutionSet[downIndex].getCandidates().get(j).charAt(constraints.get(i).getDownLetterNumber())
                                == acrossSolutionSet[acrossIndex].getCandidates().get(k).charAt(constraints.get(i).getAcrossLetterNumber())) {
                            temp.add(acrossSolutionSet[acrossIndex].getCandidates().get(k));
                        }
                    }
                }//end inner for

                if(temp.size() != acrossSolutionSet[acrossIndex].getCandidates().size())
                    flag = true;

                acrossSolutionSet[acrossIndex].setCandidates(temp);
                acrossSolutionSet[acrossIndex].filterSet();
            } //end outer for
        } //end while
    }

    public static String[] tryCombinations(AnswerSet[] acrossWordSets, AnswerSet[] downWordsSets){
        ArrayList<Constraint> constraints = PuzzleAttributes.getConstraints();
        String[] candidateSolution = new String[10];
        String[] tempSolutions = new String[10];    //Temp Solutions Array for question 1 to 10
        int maxConstraintCount = 0;
        int maxConstraintSolution = 0;

        for (int i = 0; i < acrossWordSets[0].getCandidates().size(); i++) {
            tempSolutions[0] = acrossWordSets[0].getCandidates().get(i);

            for (int j = 0; j < acrossWordSets[1].getCandidates().size(); j++) {
                tempSolutions[1] = acrossWordSets[1].getCandidates().get(j);

                for (int k = 0; k < acrossWordSets[2].getCandidates().size(); k++) {
                    tempSolutions[2] = acrossWordSets[2].getCandidates().get(k);

                    for (int l = 0; l < acrossWordSets[3].getCandidates().size(); l++) {
                        tempSolutions[3] = acrossWordSets[3].getCandidates().get(l);

                        for (int m = 0; m < acrossWordSets[4].getCandidates().size(); m++) {
                            tempSolutions[4] = acrossWordSets[4].getCandidates().get(m);

                            for (int n = 0; n < downWordsSets[0].getCandidates().size(); n++) {
                                tempSolutions[5] = downWordsSets[0].getCandidates().get(n);

                                for (int o = 0; o < downWordsSets[1].getCandidates().size(); o++) {
                                    tempSolutions[6] = downWordsSets[1].getCandidates().get(o);

                                    for (int p = 0; p < downWordsSets[2].getCandidates().size(); p++) {
                                        tempSolutions[7] = downWordsSets[2].getCandidates().get(p);

                                        for (int q = 0; q < downWordsSets[3].getCandidates().size(); q++) {
                                            tempSolutions[8] = downWordsSets[3].getCandidates().get(q);

                                            for (int r = 0; r < downWordsSets[4].getCandidates().size(); r++) {
                                                tempSolutions[9] = downWordsSets[4].getCandidates().get(r);

                                                maxConstraintCount = 0;
                                                for(int z = 0; z < constraints.size(); z++){
                                                    if(tempSolutions[constraints.get(z).getAcrossIndex()-1].charAt(constraints.get(z).getAcrossLetterNumber()) ==
                                                            tempSolutions[constraints.get(z).getDownIndex()-1].charAt(constraints.get(z).getDownLetterNumber())){
                                                        maxConstraintCount++;
                                                    }
                                                }
                                                if(maxConstraintCount > maxConstraintSolution){
                                                    maxConstraintSolution = maxConstraintCount;
                                                    for(int x = 0; x < 10; x++)
                                                        candidateSolution[x] = tempSolutions[x];
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return candidateSolution;
    }
}