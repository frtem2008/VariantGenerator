package newCreators;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class VariantGui {
    private static final HashMap<Integer, Question> questions = new HashMap<>();
    private static final ArrayList<JTextField> answerFields = new ArrayList<>();
    private static final ArrayList<JLabel> answerNumbers = new ArrayList<>();
    private static final JFrame frame = new JFrame("Variant builder");
    private static final File outputFolder = new File("Output");
    private static final File output = new File(outputFolder.getAbsolutePath() + "/Test.doc");
    private static GuiManager gm;
    private static int currentQuestion, answerCount;
    private static String testName;

    private static void configureFrame() {
        Mouse m = new Mouse();
        frame.addMouseListener(m);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
    }

    private static void createFiles() {
        try {
            System.out.println("Output folder:" + (outputFolder.mkdir() ? " created(" + outputFolder.getAbsolutePath() + ")" : " exists (" + outputFolder.getAbsolutePath() + ")"));
            System.out.println("Output file:  " + (output.createNewFile() ? " created(" + output.getAbsolutePath() + ")" : " exists (" + output.getAbsolutePath() + ")"));
        } catch (IOException e) {
            System.err.println("Error creating files: " + Arrays.toString(e.getStackTrace()));
        }
        Utils.clearFile(output);
        System.out.println();
    }

    //Test function for gui
    private static void fillTestQuestions() {
        questions.put(1, new Question("question 1"));
        questions.put(2, new Question("question 2"));
        questions.put(3, new Question("question 3"));
        questions.put(4, new Question("question 4"));
        HashMap<Integer, String> a = new HashMap<>();
        HashMap<Integer, String> b = new HashMap<>();
        HashMap<Integer, String> c = new HashMap<>();
        HashMap<Integer, String> d = new HashMap<>();
        a.put(1, "question 1 answer 1");
        a.put(2, "question 1 answer 2");
        a.put(3, "question 1 answer 3");
        b.put(1, "question 2 answer 1");
        b.put(2, "question 2 answer 2");
        b.put(3, "question 2 answer 3");
        c.put(1, "question 3 answer 1");
        c.put(2, "question 3 answer 2");
        c.put(3, "question 3 answer 3");
        d.put(1, "question 4 answer 1");
        d.put(2, "question 4 answer 2");
        d.put(3, "question 4 answer 3");
        questions.get(1).setAnswers(a);
        questions.get(2).setAnswers(b);
        questions.get(3).setAnswers(c);
        questions.get(4).setAnswers(d);
        System.out.println("Begin questions");
        for (int i = 0; i < questions.keySet().size(); i++) {
            System.out.println(questions.get(i + 1).getAnswers());
        }
        System.out.println("____________________________\n\n");
    }

    public static void main(String[] args) {
        createFiles();
        configureFrame();
        //fillTestQuestions();

        gm = new GuiManager(frame, """
                StateInfo:
                1) Кнопка "Начать"
                2) От 2 до 4 вопросов и кнопка "+"
                3) 5 вопросов и кнопки "+" нет
                4) от 6 до 9 вопросов на 2 странице, кнопка "+" есть
                5) 10 вопросов на 2 странице, кнопки "+" нет
                """
        );

        currentQuestion = 1;
        answerCount = 0;
        addAnswer();
        addAnswer();

        JLabel enterTestName = gm.createJLabel(
                "Введите название теста",
                new Rectangle(200, 190, 500, 100),
                new Font("Calibri", Font.PLAIN, 20),
                1
        );

        JTextField testNameField = gm.createJTextField(
                new Rectangle(150, 260, 300, 50),
                new Font("Times New Roman", Font.BOLD, 24),
                1
        );


        JButton beginButton = gm.createJButton("Начать создание теста",
                new Rectangle(150, 325, 300, 80),
                e -> {
                    testName = testNameField.getText();

                    if (!testName.isEmpty()) {
                        File testFolder = new File(outputFolder.getAbsolutePath() + "/" + testName);
                        System.out.println("Output folder:" + (testFolder.mkdir() ? " created(" + testFolder.getAbsolutePath() + ")" : " exists (" + testFolder.getAbsolutePath() + ")"));

                        gm.setState(2);
                    }
                },
                1
        );

        JLabel enterQuestions = gm.createJLabel("Введите вопрос: ",
                new Rectangle(85, 30, 3000, 100),
                new Font("Calibri", Font.BOLD, 64),
                2, 3, 4, 5
        );

        JLabel questionNumberLabel = gm.createJLabel("1)",
                new Rectangle(30, 115, 100, 100),
                new Font("Calibri", Font.BOLD, 48),
                2, 3, 4, 5
        );

        JTextField questionField = gm.createJTextField(
                new Rectangle(110, 135, 400, 50),
                new Font("Times New Roman", Font.BOLD, 20),
                2, 3, 4, 5
        );

        JButton addAnswer = gm.createJButton("+",
                new Rectangle(55, 325, 50, 50),
                null,
                2, 4
        );


        JButton changePage = gm.createJButton(">",
                new Rectangle(520, 240, 45, 120),
                null,
                3, 4, 5
        );

        JLabel helpText = gm.createJLabel("Введите предидущий ответ, чтобы добавить новый",
                new Rectangle(70, 100, 600, 50),
                new Font("Calibri", Font.ITALIC, 20)
        );

        changePage.addActionListener(e -> {
            if (gm.getState() == 3) {
                if (answerCount != 10)
                    gm.setState(4);
                else
                    gm.setState(5);

                if (answerCount == 5)
                    addAnswer();

                addAnswer.setBounds(55, 205 + 60 * ((answerCount % 5)), 60, 60);

                changePage.setBounds(5, 240, 45, 120);
                changePage.setText("<");
            } else if (gm.getState() == 4 || gm.getState() == 5) {
                gm.setState(3);
                changePage.setBounds(520, 240, 45, 120);
                changePage.setText(">");
            }
        });

        addAnswer.addActionListener(e -> {
            System.out.println("_________________________");
            for (int i = 0; i < answerFields.size(); i++) {
                System.out.println("Answer " + i + ")" + answerFields.get(i).getText());
            }

            if (!answerFields.get(answerCount - 1).getText().trim().equals("")) {
                helpText.setVisible(false);
                addAnswer.setBounds(55, 325 + 60 * ((answerCount % 5) - 1), 60, 60);
                addAnswer();

                if (answerCount % 5 == 0) {
                    if (gm.getState() == 2)
                        gm.setState(3);
                    else if (gm.getState() == 4)
                        gm.setState(5);
                }
            } else {
                helpText.setVisible(true);
            }
        });

        JButton nextQuestion = gm.createJButton("Следующий вопрос",
                new Rectangle(340, 520, 160, 50),

                e -> {
                    if (!questionField.getText().isEmpty() &&
                            !answerFields.get(0).getText().isEmpty() &&
                            !answerFields.get(1).getText().isEmpty()
                    ) {
                        saveQuestion(currentQuestion, questionField);
                        currentQuestion++;

                        if (questions.size() > currentQuestion - 1) {
                            loadQuestion(currentQuestion, questionNumberLabel, questionField, addAnswer, changePage);
                        } else {
                            clearTextFields(questionField);
                            addAnswer.setBounds(55, 325, 60, 60);
                            answerCount = 2;

                            questionNumberLabel.setText(currentQuestion + ")");
                            changePage.setBounds(520, 240, 45, 120);
                            changePage.setText(">");

                            gm.setState(2);
                        }
                    }
                },
                2, 3, 4, 5
        );

        JButton prevQuestion = gm.createJButton("Предыдущий вопрос",
                new Rectangle(70, 520, 160, 50),
                e -> {
                    if (currentQuestion > 1) {
                        if (!questionField.getText().isEmpty() &&
                                !answerFields.get(0).getText().isEmpty() &&
                                !answerFields.get(1).getText().isEmpty()
                        ) {
                            saveQuestion(currentQuestion, questionField);
                        }
                        System.out.println(questions);
                        currentQuestion--;

                        loadQuestion(currentQuestion, questionNumberLabel, questionField, addAnswer, changePage);
                    }
                },
                2, 3, 4, 5
        );

        JFileChooser testFileChooser = gm.createJFileChooser(output.getParentFile().getAbsolutePath(),
                "Выбор теста", "Выбрать", "ъуй",
                0
        );

        JSlider variantCountSlider = gm.createJSlider(
                new Rectangle(150, 300, 300, 50),
                1, 10, 2, JSlider.HORIZONTAL,
                e -> {
                    System.out.println("Variants:" + ((JSlider) e.getSource()).getValue());
                },
                1, 1, true, true,
                6
        );


        JSlider questionCountSlider = gm.createJSlider(
                new Rectangle(150, 100, 300, 50),
                1, 10, 1, JSlider.HORIZONTAL,
                e -> {
                    variantCountSlider.setMaximum((int) Utils.CNK(questions.size(), ((JSlider) e.getSource()).getValue()));
                },
                1, 1, true, true,
                6
        );

        JLabel questionsPerVariantSliderLabel = gm.createJLabel(
                "Количество вопросов в одном варианте",
                new Rectangle(120, 50, 400, 50),
                new Font("Calibri", Font.ITALIC, 20),
                6
        );

        JLabel variantSliderLabel = gm.createJLabel(
                "Количество вариантов",
                new Rectangle(195, 250, 400, 50),
                new Font("Calibri", Font.ITALIC, 20),
                6
        );

        JLabel variantsSavedLabel = gm.createJLabel(
                "Варианты успешно сохранены",
                new Rectangle(100, 180, 2000, 200),
                new Font("Calibri", Font.ITALIC, 30),
                7
        );

        JButton saveRandomisedTest = gm.createJButton(
                "Сохранить варианты",
                new Rectangle(200, 500, 200, 100),
                e -> {
                    saveRandomisedVariants(variantCountSlider, questionCountSlider);
                    gm.setState(7);
                },
                6
        );


        JButton saveTest = gm.createJButton("✓",
                new Rectangle(255, 520, 60, 60),

                e -> {
                    saveQuestion(currentQuestion, questionField);
                    questionCountSlider.setMaximum(questions.size());
                    System.out.println("FIRST QUESTION: " + questions.get(1));
                    gm.setState(6);
                },
                2, 3, 4, 5
        );

        frame.pack();
        frame.setBounds(400, 100, 600, 650);
        frame.setLocationRelativeTo(null); //оцентровка
        frame.setVisible(true);

        gm.setState(1);
    }

    private static void saveRandomisedVariants(JSlider variantSlider, JSlider questionSlider) {
        ArrayList<Variant> variants = Utils.fillVariants(variantSlider.getValue(), questionSlider.getValue(), questions);

        for (int i = 0; i < variants.size(); i++) {
            Variant variant = variants.get(i);
            File cur = new File(outputFolder + "/" + testName + "/Variant №" + variant.number() + ".txt");
            ArrayList<Question> questions = new ArrayList<>(variant.questions());
            System.out.println("questions.size() = " + questions.size());
            try {
                System.out.println("Variant №" + variant.number() + " file" + (cur.createNewFile() ? " created(" + cur.getAbsolutePath() + ")" : " exists (" + cur.getAbsolutePath() + ")"));
                Utils.clearFile(cur);
                writeQuestions(cur, questions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void writeQuestions(File file, ArrayList<Question> questions) {
        System.out.println();

        for (int i = 0; i < questions.size(); i++) {
            String question = "";
            Question cur = questions.get(i);
            if (cur == null) {
                System.out.println("Skip");
                continue;
            } else if (cur.getQuestion().equals("")) {
                System.out.println("No question");
                continue;
            }

            question += (i + 1) + ") " + cur.getQuestion() + "\n";

            for (int j = 0; j < cur.getAnswerList().size(); j++) {
                question += "\t " + (j + 1) + "." + cur.getAnswerList().get(j) + "\n";
            }

            Utils.appendStrToFile(file, question);
        }
    }

    private static void clearTextFields(JTextField questionField) {
        questionField.setText("");

        for (int i = 0; i < answerFields.size(); i++) {
            gm.removeFromStates(answerNumbers.get(i), new int[]{2, 3, 4, 5});
            gm.removeFromStates(answerFields.get(i), new int[]{2, 3, 4, 5});
        }

        answerNumbers.clear();
        answerFields.clear();
        answerCount = 0;
        addAnswer();
        addAnswer();
    }

    private static void saveQuestion(int number, JTextField questionField) {
        Question question = new Question(questionField.getText());

        HashMap<Integer, String> answers = new HashMap<>();
        for (int i = 0; i < answerFields.size(); i++) {
            if (!answerFields.get(i).getText().trim().equals(""))
                answers.put(i, answerFields.get(i).getText());
        }

        question.setAnswers(answers);
        questions.put(number, question);
    }

    private static void loadQuestion(int number, JLabel questionNumberLabel, JTextField questionField, JButton addAnswer, JButton changePage) {
        clearTextFields(questionField);

        Question question = questions.get(number);
        questionField.setText(question.getQuestion());
        questionNumberLabel.setText(number + ")");

        if (question.getAnswers() != null) {
            setAnswers(question, addAnswer, changePage);
        }

        if (answerCount < 5)
            gm.setState(2);
        else if (answerCount == 5) {
            gm.setState(3);
            changePage.setBounds(520, 240, 45, 120);
            changePage.setText(">");
        } else if (answerCount < 10)
            gm.setState(4);
        else if (answerCount == 10)
            gm.setState(5);


    }

    private static void setAnswers(Question question, JButton addAnswer, JButton changePage) {
        while (answerFields.size() < question.getAnswers().size()) {
            addAnswer();
        }

        for (int i = 0; i < question.getAnswers().size(); i++) {
            answerFields.get(i).setText(question.getAnswers().get(i));
        }

        System.out.println("Answer count: " + answerCount);

        if (answerCount % 5 == 0) {
            if (gm.getState() == 2)
                gm.setState(3);
            else if (gm.getState() == 4)
                gm.setState(5);
        }

        addAnswer.setBounds(55, 205 + 60 * ((answerCount % 5)), 60, 60);
        if (gm.getState() == 3 || gm.getState() == 2) {
            addAnswer.setBounds(55, 205 + 60 * ((answerCount % 5)), 60, 60);
            changePage.setBounds(5, 240, 45, 120);
            changePage.setText("<");
        } else if (gm.getState() == 4 || gm.getState() == 5) {
            changePage.setBounds(520, 240, 45, 120);
            changePage.setText(">");
        }
    }

    private static void addAnswer() {
        int index = answerCount++;
        answerNumbers.add(addAnswerNumber(index));
        answerFields.add(addAnswerText(index));
    }

    private static JLabel addAnswerNumber(int index) {
        return gm.createJLabel((index < 9 ? " " : "") + (index + 1) + ".",
                new Rectangle(55, 90 + 60 * (index % 5), 100, 300),
                new Font("Calibri", Font.BOLD, 42), index < 5 ? 2 : 5, index < 5 ? 3 : 4
        );
    }

    private static JTextField addAnswerText(int index) {
        return gm.createJTextField(
                new Rectangle(110, 210 + 60 * (index % 5), 400, 50),
                new Font("Times New Roman", Font.BOLD, 20), index < 5 ? 2 : 5, index < 5 ? 3 : 4
        );
    }
}

class Utils {
    public static long CNK(int n, int k) {
        return factorial(n) / factorial(n - k) / factorial(k);
    }

    public static long factorial(int n) {
        long res = 1;

        for (int i = 1; i < n + 1; i++) {
            res *= i;
        }
        return res;
    }

    public static void appendStrToFile(File file, String str) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
            bw.write(str);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(File file) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            bw.write("");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Variant generateVariant(int variantQuestionCount, int variantNumber, HashMap<Integer, Question> questions) {
        ArrayList<Question> questionsArrayList = new ArrayList<>();

        if (questions.keySet().size() <= variantQuestionCount) {
            throw new RuntimeException("Unable to generate variants: too many questions per variant");
        }

        for (int i = 1; i < questions.keySet().size() + 1; i++) {
            questionsArrayList.add(questions.get(i));
        }
        Collections.reverse(questionsArrayList);
        Collections.shuffle(questionsArrayList);

        System.out.println("questionsArrayList = " + questionsArrayList);

        for (Question question : questionsArrayList) {
            ArrayList<String> answers = hashMapToArrayList(question.getAnswers());

            Collections.reverse(answers);
            Collections.shuffle(answers);

            question.setAnswers(answers);
        }

        for (int i = 0; i < questionsArrayList.subList(0, variantQuestionCount).size(); i++) {
            System.out.println(questionsArrayList.subList(0, variantQuestionCount).get(i));
        }

        return new Variant(variantNumber, questionsArrayList.subList(0, variantQuestionCount));
    }

    public static ArrayList<Variant> fillVariants(int variantCount, int variantQuestionCount, HashMap<Integer, Question> questions) {
        ArrayList<Variant> res = new ArrayList<>();

        int variantNumber = 1;

        //цикл, чтобы все варианты отличались хотя-бы на 1 вопрос
        while (res.size() < variantCount) {
            System.out.println("\nGenerating variant №" + variantNumber);
            Variant var = generateVariant(variantQuestionCount, variantNumber, questions);

            if (!res.contains(var)) {
                res.add(var);
                variantNumber++;
            }
        }

        return res;
    }

    //выносит массив значений hashMap в отдельный ArrayList
    public static ArrayList<String> hashMapToArrayList(HashMap<Integer, String> hashMap) {
        ArrayList<String> res = new ArrayList<>();

        for (int i = 0; i < hashMap.keySet().size(); i++) {
            res.add(hashMap.get(i + 1));
        }

        return res;
    }

}
