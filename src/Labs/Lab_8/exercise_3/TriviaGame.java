package Labs.Lab_8.exercise_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class TriviaQuestion {
    public String question;        // Actual question
    public String answer;        // Answer to question
    public int value;            // Point value of question

    public TriviaQuestion() {
        question = "";
        answer = "";
        value = 0;
    }

    public TriviaQuestion(String q, String a, int v) {
        question = q;
        answer = a;
        value = v;
    }

    public abstract void showQuestion(int questionNumber);

    public abstract boolean isCorrectAnswer(String userAnswer);

    public int getValue() {
        return value;
    }

    public String getAnswer() {
        return answer;
    }
}

class TrueFalseQuestion extends TriviaQuestion {
    public TrueFalseQuestion(String question, String answer, int value) {
        super(question, answer, value);
    }

    @Override
    public void showQuestion(int questionNumber) {
        System.out.println("Question " + questionNumber + ".  " + value + " points.");
        System.out.println(question);
        System.out.println("Enter 'T' for true or 'F' for false.");
    }

    @Override
    public boolean isCorrectAnswer(String userAnswer) {
        return userAnswer.equalsIgnoreCase(answer);
    }
}

class FreeFormQuestion extends TriviaQuestion {

    public FreeFormQuestion(String question, String answer, int value) {
        super(question, answer, value);
    }

    @Override
    public void showQuestion(int questionNumber) {
        System.out.println("Question " + questionNumber + ".  " + value + " points.");
        System.out.println(question);
    }

    @Override
    public boolean isCorrectAnswer(String userAnswer) {
        return userAnswer.equalsIgnoreCase(answer);
    }
}

class TriviaData {
    private List<TriviaQuestion> data;

    public TriviaData() {
        data = new ArrayList<TriviaQuestion>();
    }

    public void addQuestion(TriviaQuestion question) {
        data.add(question);
    }

    public int numQuestions() {
        return data.size();
    }

    public TriviaQuestion getQuestion(int index) {
        return data.get(index);
    }
}

public class TriviaGame {

    public TriviaData triviaData;    // Questions

    public TriviaGame() {
        // Load questions
        triviaData = new TriviaData();
        loadQuestions();
    }

    private void loadQuestions() {
        triviaData.addQuestion(new FreeFormQuestion("The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3));
        triviaData.addQuestion(new TrueFalseQuestion("Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1));
        triviaData.addQuestion(new FreeFormQuestion("1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2));
        triviaData.addQuestion(new FreeFormQuestion("I am the geometric figure most like a lost parrot",
                "polygon", 2));
        triviaData.addQuestion(new TrueFalseQuestion("Generics were introducted to Java starting at version 5.0.",
                "T", 1));
    }

    private void playGame() {
        int score = 0;
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < triviaData.numQuestions(); i++) {
            TriviaQuestion currentQuestion = triviaData.getQuestion(i);
            currentQuestion.showQuestion(i + 1);

            String answer = scanner.nextLine();
            if (currentQuestion.isCorrectAnswer(answer)) {
                System.out.println("That is correct!  You get " + currentQuestion.getValue() + " points.");
                score += currentQuestion.getValue();
            } else {
                System.out.println("Wrong, the correct answer is " + currentQuestion.getAnswer());
            }

            System.out.println("Your score is " + score);

        }
        System.out.println("Game over!  Thanks for playing!");
    }

    // Main game loop
    public static void main(String[] args) {
        TriviaGame game = new TriviaGame();
        game.playGame();
    }
}
