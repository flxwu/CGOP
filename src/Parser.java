import javafx.util.Pair;

import javax.swing.*;
import java.util.*;

/**
 * Project "CGOP"
 * "Parser.java" created by David Wu on Wed,19.07.17.
 */

public class Parser {
    private static Hashtable<Integer, Pair<String, Pair<Boolean, Double>>> answers = new Hashtable<>();
    private static ArrayList<String> incorrects = new ArrayList<>();


    public void parse(StringTokenizer tokenizer) throws TokenizerEmptyException,Exception {
        try {
            if (!tokenizer.hasMoreTokens())
                throw new TokenizerEmptyException("Tokenizer empty");
            int counter = 0;
            while (tokenizer.hasMoreTokens()) {
                String s = tokenizer.nextToken();
                if (s.equals("Function")) {
                    String functionName = tokenizer.nextToken();
                    tokenizer.nextToken();
                    tokenizer.nextToken();
                    String verdict = tokenizer.nextToken();
                    boolean correct = (verdict.equals("correctly,")) ? true : false;
                    double points = Double.parseDouble(tokenizer.nextToken());
                    if (!correct) {
                        tokenizer.nextToken(); //points
                        tokenizer.nextToken(); //were
                        tokenizer.nextToken(); //not
                        tokenizer.nextToken(); //rewarded
                        String info = tokenizer.nextToken();
                        while (true) {
                            String next = tokenizer.nextToken();
                            info += " " + next;
                            if (next.charAt(next.length() - 1) == ']')
                                break;
                        }
                        incorrects.add(info);
                    }
                    answers.put(counter, new Pair<>(functionName, new Pair<>(correct, points)));
                    counter++;
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }


    public static Hashtable<Integer, Pair<String, Pair<Boolean, Double>>> getAnswers() {
        return answers;
    }

    public ArrayList<String> getIncorrects() {
        return incorrects;
    }

    public void fillin(JTextArea textArea) throws Exception {
        textArea.setText(buildAnalysis());
    }

    public void fillin(Analysis analysis) throws Exception {
        analysis.getOutputField().setText(buildAnalysis());
    }

    public String getAnalysis() throws Exception {
        return buildAnalysis();
    }

    private String buildAnalysis() throws FunctionsEmptyException, Exception {
        try {
            if (answers.size() == 0)
                throw new FunctionsEmptyException("No Functions found");
            String s = new String("Analysis started: "
                    + "\n"
                    + "=========================================="
                    + "\n");
            int incorrectCounter = 0;
            for (int answercounter = 0; answercounter < answers.size(); answercounter++) {
                String t = answers.get(answercounter).getKey();
                Pair<Boolean, Double> verdict = answers.get(answercounter).getValue();
                s += "Function: " + t + "\t";
                s += (verdict.getKey()) ? "Accepted" + "\t[" + verdict.getValue() + "P rewarded]" : incorrects.get(incorrectCounter);
                incorrectCounter += (verdict.getKey()) ? 0 : 1;
                s += "\n";
            }
            s += wrapUp();
            s += "\n=========================================="
                    + "\nAnalysis finished.";
            return s;
        } catch (Exception e) {
            throw e;
        }
    }

    private String wrapUp() {
        String s = "";
        double incorrect = incorrects.size();
        double all = answers.size();
        double percent = ((all - incorrect) / all) * 100;
        s += "\n=========================================="
                + "\nwrap-up: "
                + (int) (all - incorrect) + "/" + (int) all
                + " , " + (int) percent + "%";
        return s;
    }
}

/**
 * End of Parser.java
 */