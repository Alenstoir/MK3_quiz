import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class main extends JFrame {
    private JPanel rootPanel;
    private JPanel lPanel;
    private JPanel mPanel;
    private JPanel qPanel;
    private JPanel rPanel;
    private JButton mQuitButton;
    private JButton mReservedButton;
    private JButton mResultButton;
    private JButton mStartButton;
    private JTextField lLoginField;
    private JPasswordField lPasswordField;
    private JButton lLoginButton;
    private JLabel mHelloLabel;
    private JButton qThreeBtn;
    private JButton qFourBtn;
    private JButton qOneBtn;
    private JButton qTwoBtn;
    private JTextArea qQuestionArea;
    private JList rList;
    private JTextPane rTextResult;
    private JButton rOkButton;
    private JPanel sPanel;
    private JTable sTable;
    private JButton sOkButton;
    private Map<String, String> Users = new HashMap<>();
    private CardLayout cl;
    private String currentUser;
    private int round;
    private int lastResult;
    private Vector<String> results = new Vector<>();
    private ArrayList<String> answers = new ArrayList<>();
    private Map<String, Integer> scoreBoard= new HashMap<>();
    private String questions[][] = {
            {
                "В каком году вышла игра Mortal Kombat 3?", "1995", "1996", "1999", "2003", "1"
            },
            {
                "Кто является создателем MK3?", "Sega GENESIS", "J.Tobias и E.Boon", "Shao Khan", "Nintendo", "2"
            },
            {
                "Сколько играбельных персонажей в Ultimate Mortal Kombat 3?", "21", "23", "24", "25", "4"
            },
            {
                "К какому клану принадлежал Нууб Сайбот при жизни?", "Лин Куэй", "Братство теней", "Белый лотос", "Ширай рю", "1"
            },
            {
                "Кто был главным злодеем MK3?", "Ермак", "Рейн", "Мотаро", "Шао Кан", "4"
            }
    };

    main(){
        init();

    }

    void init() {
        setTitle("Вход");
        setMinimumSize(new Dimension(250, 400));
        setPreferredSize(new Dimension(250,400));
        setResizable(false);
        setContentPane(rootPanel);
        cl = (CardLayout) rootPanel.getLayout();
        Users.put("Alenstoir", "1234");
        Users.put("NoobSaibot", "DU+A");
        Users.put("TobiasBoon", "MK3");

        DefaultTableModel sTModel = new DefaultTableModel(){};
        sTModel.setColumnIdentifiers(new Object[]{"Игрок","Количество очков"});
        sTable.setModel(sTModel);
        sTable.setName("Результаты");
        sTable.setDefaultEditor(Object.class,null);

        lLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String usr = lLoginField.getText().toString();
                String pswd = String.valueOf(lPasswordField.getPassword());
                System.out.println("Login: " + usr + "\nPassword: " + pswd);
                if (Users.containsKey(usr)) {
                    if (Users.get(usr).equals(pswd)) {
                        currentUser = usr;
                        setTitle("Меню");
                        mHelloLabel.setText("Привет, " + currentUser);
                        cl.show(rootPanel, "menu");
                    }
                    ;
                }
            }
        });
        mQuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentUser = null;
                setTitle("Вход");
                cl.show(rootPanel, "login");
            }
        });
        mStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setTitle("Викторина");
                answers.clear();
                cl.show(rootPanel, "quiz");
                operateQuiz();
            }
        });
        mResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setTitle("Результаты");
                sTModel.setRowCount(0);
                scoreBoard.forEach((k,v) -> {
                    sTModel.addRow(new Object[]{k,v});
                });
                cl.show(rootPanel, "scoreboard");
            }
        });

        rOkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setTitle("Меню");
                mHelloLabel.setText("Привет, " + currentUser);
                cl.show(rootPanel, "menu");
            }
        });

        sOkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setTitle("Меню");
                mHelloLabel.setText("Привет, " + currentUser);
                cl.show(rootPanel, "menu");
            }
        });

        qOneBtn.addActionListener(qButtonListener);
        qTwoBtn.addActionListener(qButtonListener);
        qThreeBtn.addActionListener(qButtonListener);
        qFourBtn.addActionListener(qButtonListener);
    }

    void operateQuiz(){
        for (int i = 0; i < questions.length; i++){
            String buffer[] = questions[i];
            int rnum = new Random().nextInt(questions.length);
            questions[i] = questions[rnum];
            questions[rnum] = buffer;
        }
        round = 0;
        setQuiz();
    };
    void setQuiz(){
        qQuestionArea.setText(questions[round][0]);
        qOneBtn.setText(questions[round][1]);
        qTwoBtn.setText(questions[round][2]);
        qThreeBtn.setText(questions[round][3]);
        qFourBtn.setText(questions[round][4]);
    };

    ActionListener qButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JButton source = (JButton)actionEvent.getSource();
            System.out.println(source.getName());
            answers.add(source.getName());
            round++;
            if (round < questions.length){
                setQuiz();
            } else {
                setTitle("Результаты");
                results.clear();
                for (int i = 0; i < questions.length; i++){
                    Boolean qres = answers.get(i).equals(questions[i][5]);
                    results.add("Вопрос №"+(i+1)+": "+qres);
                    answers.set(i,qres+"");
                };
                lastResult = 0;
                for (int i = 0; i < answers.size(); i++){
                    if (answers.get(i).equals("true")){
                        lastResult++;
                    }
                }
                rTextResult.setText("Вы правильно ответили на "+lastResult+" из "+answers.size()+"вопросов. \n Вы набрали "+lastResult+" баллов");
                cl.show(rootPanel, "result");
                rList.setListData(results);
                scoreBoard.put(currentUser, lastResult);
            };
        }
    };

    public static void main(String arghs[]){
        main app = new main();
        app.pack();
        app.setDefaultLookAndFeelDecorated(true);
        app.setVisible(true);
    }
}


