import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class QuizApp extends JFrame {
    // Main components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Panels for different screens
    private JPanel loginPanel;
    private JPanel adminPanel;
    private JPanel studentPanel;
    private JPanel addQuestionPanel;
    private JPanel addStudentPanel;
    private JPanel takeQuizPanel;
    private JPanel resultsPanel;
    
    // Data structures
    private ArrayList<User> users;
    private ArrayList<Question> questions;
    private ArrayList<Result> results;
    private User currentUser;
    
    // Components for login panel
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    
    // Current quiz state
    private int currentQuestionIndex;
    private int score;
    private ArrayList<Question> currentQuizQuestions;
    
    // Custom colors for the application
    private Color bgColor = new Color(235, 245, 251); // Light blue background
    private Color accentColor = new Color(70, 130, 180); // Steel blue accent
    private Color textColor = new Color(44, 62, 80); // Dark slate for text
    private Color buttonColor = new Color(52, 152, 219); // Bright blue for buttons
    
    public QuizApp() {
        // Initialize data structures
        users = new ArrayList<>();
        questions = new ArrayList<>();
        results = new ArrayList<>();
        
        // Add default admin user
        users.add(new User("admin", "admin", "Admin", true));
        
        // Add some sample users and questions
        addSampleData();
        
        // Set up the main frame
        setTitle("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with card layout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Initialize panels
        createLoginPanel();
        createAdminPanel();
        createStudentPanel();
        createAddQuestionPanel();
        createAddStudentPanel();
        createTakeQuizPanel();
        createResultsPanel();
        
        // Add panels to main panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(studentPanel, "student");
        mainPanel.add(addQuestionPanel, "addQuestion");
        mainPanel.add(addStudentPanel, "addStudent");
        mainPanel.add(takeQuizPanel, "takeQuiz");
        mainPanel.add(resultsPanel, "results");
        
        // Set login panel as default
        cardLayout.show(mainPanel, "login");
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private void addSampleData() {
        // Add some sample students
        users.add(new User("student1", "pass1", "Student One", false));
        users.add(new User("student2", "pass2", "Student Two", false));
        
        // Add some sample questions
        questions.add(new Question("What is the capital of France?", 
                                   new String[]{"London", "Paris", "Berlin", "Rome"}, 
                                   1));
        questions.add(new Question("Which planet is known as the Red Planet?", 
                                   new String[]{"Venus", "Jupiter", "Mars", "Saturn"}, 
                                   2));
        questions.add(new Question("What is 2 + 2?", 
                                   new String[]{"3", "4", "5", "6"}, 
                                   1));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?", 
                                   new String[]{"Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"}, 
                                   1));
        questions.add(new Question("What is the largest ocean on Earth?", 
                                   new String[]{"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"}, 
                                   3));
    }
    
    // Create login panel
    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setBackground(bgColor);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create title
        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(accentColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);
        
        // Login button
        JButton loginButton = createStyledButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);
        
        // Add form panel to login panel
        loginPanel.add(formPanel, BorderLayout.CENTER);
        
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                User user = authenticateUser(username, password);
                if (user != null) {
                    currentUser = user;
                    statusLabel.setText(" ");
                    usernameField.setText("");
                    passwordField.setText("");
                    
                    if (user.isAdmin()) {
                        cardLayout.show(mainPanel, "admin");
                    } else {
                        cardLayout.show(mainPanel, "student");
                    }
                } else {
                    statusLabel.setText("Invalid username or password");
                }
            }
        });
    }
    
    // Create admin panel
    private void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBackground(bgColor);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBackground(bgColor);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton addQuestionButton = createStyledButton("Add Question");
        JButton addStudentButton = createStyledButton("Add Student");
        JButton viewResultsButton = createStyledButton("View Results");
        
        menuPanel.add(addQuestionButton);
        menuPanel.add(addStudentButton);
        menuPanel.add(viewResultsButton);
        
        // Button actions
        addQuestionButton.addActionListener(e -> cardLayout.show(mainPanel, "addQuestion"));
        addStudentButton.addActionListener(e -> cardLayout.show(mainPanel, "addStudent"));
        viewResultsButton.addActionListener(e -> {
            updateResultsPanel();
            cardLayout.show(mainPanel, "results");
        });
        
        // Add components to panel
        adminPanel.add(headerPanel, BorderLayout.NORTH);
        adminPanel.add(menuPanel, BorderLayout.CENTER);
    }
    
    // Create student panel
    private void createStudentPanel() {
        studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(bgColor);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        menuPanel.setBackground(bgColor);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton takeQuizButton = createStyledButton("Take Quiz");
        JButton viewResultsButton = createStyledButton("View My Results");
        
        menuPanel.add(takeQuizButton);
        menuPanel.add(viewResultsButton);
        
        // Button actions
        takeQuizButton.addActionListener(e -> startQuiz());
        viewResultsButton.addActionListener(e -> {
            updateResultsPanel();
            cardLayout.show(mainPanel, "results");
        });
        
        // Add components to panel
        studentPanel.add(headerPanel, BorderLayout.NORTH);
        studentPanel.add(menuPanel, BorderLayout.CENTER);
    }
    
    // Create add question panel
    private void createAddQuestionPanel() {
        addQuestionPanel = new JPanel(new BorderLayout());
        addQuestionPanel.setBackground(bgColor);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel("Add Question");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "admin"));
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Question text
        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(questionLabel, gbc);
        
        JTextField questionField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(questionField, gbc);
        
        // Options
        JLabel[] optionLabels = new JLabel[4];
        JTextField[] optionFields = new JTextField[4];
        
        for (int i = 0; i < 4; i++) {
            optionLabels[i] = new JLabel("Option " + (i + 1) + ":");
            optionLabels[i].setForeground(textColor);
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            formPanel.add(optionLabels[i], gbc);
            
            optionFields[i] = new JTextField(30);
            gbc.gridx = 1;
            gbc.gridy = i + 1;
            formPanel.add(optionFields[i], gbc);
        }
        
        // Correct answer dropdown
        JLabel correctLabel = new JLabel("Correct Answer:");
        correctLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(correctLabel, gbc);
        
        String[] choices = {"Option 1", "Option 2", "Option 3", "Option 4"};
        JComboBox<String> correctComboBox = new JComboBox<>(choices);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(correctComboBox, gbc);
        
        // Status message
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);
        
        // Submit button
        JButton submitButton = createStyledButton("Add Question");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);
        
        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String questionText = questionField.getText().trim();
                String[] options = new String[4];
                for (int i = 0; i < 4; i++) {
                    options[i] = optionFields[i].getText().trim();
                }
                int correctIndex = correctComboBox.getSelectedIndex();
                
                // Validate inputs
                if (questionText.isEmpty()) {
                    statusLabel.setText("Question text cannot be empty");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                for (int i = 0; i < 4; i++) {
                    if (options[i].isEmpty()) {
                        statusLabel.setText("Option " + (i + 1) + " cannot be empty");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }
                }
                
                // Add question
                questions.add(new Question(questionText, options, correctIndex));
                
                // Clear fields
                questionField.setText("");
                for (JTextField field : optionFields) {
                    field.setText("");
                }
                correctComboBox.setSelectedIndex(0);
                
                // Show success message
                statusLabel.setText("Question added successfully!");
                statusLabel.setForeground(Color.GREEN);
            }
        });
        
        // Add components to panel
        addQuestionPanel.add(headerPanel, BorderLayout.NORTH);
        addQuestionPanel.add(formPanel, BorderLayout.CENTER);
    }
    
    // Create add student panel
    private void createAddStudentPanel() {
        addStudentPanel = new JPanel(new BorderLayout());
        addStudentPanel.setBackground(bgColor);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel("Add Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "admin"));
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);
        
        // Full name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(nameField, gbc);
        
        // Status message
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.GREEN);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);
        
        // Submit button
        JButton submitButton = createStyledButton("Add Student");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);
        
        // Student list panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(bgColor);
        listPanel.setBorder(BorderFactory.createTitledBorder("Existing Students"));
        
        DefaultListModel<String> studentListModel = new DefaultListModel<>();
        for (User user : users) {
            if (!user.isAdmin()) {
                studentListModel.addElement(user.getUsername() + " - " + user.getFullName());
            }
        }
        
        JList<String> studentList = new JList<>(studentListModel);
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String fullName = nameField.getText().trim();
                
                // Validate inputs
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                    statusLabel.setText("All fields are required");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                // Check if username already exists
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        statusLabel.setText("Username already exists");
                        statusLabel.setForeground(Color.RED);
                        return;
                    }
                }
                
                // Add student
                users.add(new User(username, password, fullName, false));
                
                // Update student list
                studentListModel.addElement(username + " - " + fullName);
                
                // Clear fields
                usernameField.setText("");
                passwordField.setText("");
                nameField.setText("");
                
                // Show success message
                statusLabel.setText("Student added successfully!");
                statusLabel.setForeground(Color.GREEN);
            }
        });
        
        // Create a split panel to show form and list side by side
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, listPanel);
        splitPane.setDividerLocation(400);
        
        // Add components to panel
        addStudentPanel.add(headerPanel, BorderLayout.NORTH);
        addStudentPanel.add(splitPane, BorderLayout.CENTER);
    }
    
    // Create take quiz panel
    private void createTakeQuizPanel() {
        takeQuizPanel = new JPanel(new BorderLayout());
        takeQuizPanel.setBackground(bgColor);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel("Take Quiz");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("Exit Quiz");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "student"));
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Quiz panel (will be populated dynamically)
        JPanel quizContentPanel = new JPanel(new BorderLayout());
        quizContentPanel.setBackground(bgColor);
        
        // Add components to panel
        takeQuizPanel.add(headerPanel, BorderLayout.NORTH);
        takeQuizPanel.add(quizContentPanel, BorderLayout.CENTER);
    }
    
    // Create results panel
    private void createResultsPanel() {
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(bgColor);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(accentColor);
        
        JLabel titleLabel = new JLabel("Quiz Results");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (currentUser.isAdmin()) {
                cardLayout.show(mainPanel, "admin");
            } else {
                cardLayout.show(mainPanel, "student");
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Results content (will be populated dynamically)
        JPanel resultsContentPanel = new JPanel(new BorderLayout());
        resultsContentPanel.setBackground(bgColor);
        
        // Add components to panel
        resultsPanel.add(headerPanel, BorderLayout.NORTH);
        resultsPanel.add(resultsContentPanel, BorderLayout.CENTER);
    }
    
    // Update the results panel with current results
    private void updateResultsPanel() {
        JPanel resultsContentPanel = new JPanel(new BorderLayout());
        resultsContentPanel.setBackground(bgColor);
        
        // Create table model for results
        String[] columnNames = {"Student", "Date", "Score", "Total Questions"};
        Object[][] data;
        
        if (currentUser.isAdmin()) {
            // Show all results for admin
            data = new Object[results.size()][4];
            for (int i = 0; i < results.size(); i++) {
                Result result = results.get(i);
                data[i][0] = result.getUsername();
                data[i][1] = result.getDate();
                data[i][2] = result.getScore();
                data[i][3] = result.getTotalQuestions();
            }
        } else {
            // Filter results for current student
            List<Result> studentResults = new ArrayList<>();
            for (Result result : results) {
                if (result.getUsername().equals(currentUser.getUsername())) {
                    studentResults.add(result);
                }
            }
            
            data = new Object[studentResults.size()][4];
            for (int i = 0; i < studentResults.size(); i++) {
                Result result = studentResults.get(i);
                data[i][0] = result.getUsername();
                data[i][1] = result.getDate();
                data[i][2] = result.getScore();
                data[i][3] = result.getTotalQuestions();
            }
        }
        
        // Create table and scroll pane
        JTable resultsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        // Add to panel
        resultsContentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Replace the content in the results panel
        resultsPanel.remove(1); // Remove the old content panel
        resultsPanel.add(resultsContentPanel, BorderLayout.CENTER);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    // Start a new quiz for the student
    private void startQuiz() {
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for the quiz.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Reset quiz state
        currentQuestionIndex = 0;
        score = 0;
        
        // Shuffle questions for the quiz (use all questions for now)
        currentQuizQuestions = new ArrayList<>(questions);
        Collections.shuffle(currentQuizQuestions);
        
        // Limit to 5 questions if there are more
        if (currentQuizQuestions.size() > 5) {
            currentQuizQuestions = new ArrayList<>(currentQuizQuestions.subList(0, 5));
        }
        
        // Display the first question
        displayCurrentQuestion();
        
        // Show the quiz panel
        cardLayout.show(mainPanel, "takeQuiz");
    }
    
    // Display the current question
    private void displayCurrentQuestion() {
        // Get the content panel
        JPanel quizContentPanel = (JPanel) takeQuizPanel.getComponent(1);
        quizContentPanel.removeAll();
        quizContentPanel.setLayout(new BorderLayout());
        
        if (currentQuestionIndex < currentQuizQuestions.size()) {
            Question question = currentQuizQuestions.get(currentQuestionIndex);
            
            // Create question panel
            JPanel questionPanel = new JPanel(new BorderLayout());
            questionPanel.setBackground(bgColor);
            questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Question text
            JLabel questionLabel = new JLabel("Question " + (currentQuestionIndex + 1) + " of " + currentQuizQuestions.size() + ":");
            questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
            questionLabel.setForeground(textColor);
            questionPanel.add(questionLabel, BorderLayout.NORTH);
            
            JTextArea questionText = new JTextArea(question.getText());
            questionText.setFont(new Font("Arial", Font.PLAIN, 14));
            questionText.setForeground(textColor);
            questionText.setBackground(bgColor);
            questionText.setWrapStyleWord(true);
            questionText.setLineWrap(true);
            questionText.setEditable(false);
            questionText.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
            questionPanel.add(questionText, BorderLayout.CENTER);
            
            // Options panel
            JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            optionsPanel.setBackground(bgColor);
            
            ButtonGroup buttonGroup = new ButtonGroup();
            JRadioButton[] optionButtons = new JRadioButton[4];
            
            for (int i = 0; i < 4; i++) {
                optionButtons[i] = new JRadioButton(question.getOptions()[i]);
                optionButtons[i].setBackground(bgColor);
                optionButtons[i].setForeground(textColor);
                buttonGroup.add(optionButtons[i]);
                optionsPanel.add(optionButtons[i]);
            }
            
            // Navigation panel
            JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            navPanel.setBackground(bgColor);
            
            JButton nextButton = new JButton("Next");
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Check if an option is selected
                    boolean selected = false;
                    int selectedOption = -1;
                    
                    for (int i = 0; i < 4; i++) {
                        if (optionButtons[i].isSelected()) {
                            selected = true;
                            selectedOption = i;
                            break;
                        }
                    }
                   
if (!selected) {
                        JOptionPane.showMessageDialog(QuizApp.this, "Please select an answer", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Check answer
                    if (selectedOption == question.getCorrectOptionIndex()) {
                        score++;
                    }
                    
                    // Move to next question
                    currentQuestionIndex++;
                    
                    // Display next question or show results
                    if (currentQuestionIndex < currentQuizQuestions.size()) {
                        displayCurrentQuestion();
                    } else {
                        displayQuizResults();
                    }
                }
            });
            
            navPanel.add(nextButton);
            
            // Add all panels to quiz content panel
            quizContentPanel.add(questionPanel, BorderLayout.NORTH);
            quizContentPanel.add(optionsPanel, BorderLayout.CENTER);
            quizContentPanel.add(navPanel, BorderLayout.SOUTH);
            
        } else {
            displayQuizResults();
        }
        
        quizContentPanel.revalidate();
        quizContentPanel.repaint();
    }
    
    // Display quiz results after completion
    private void displayQuizResults() {
        // Get the content panel
        JPanel quizContentPanel = (JPanel) takeQuizPanel.getComponent(1);
        quizContentPanel.removeAll();
        quizContentPanel.setLayout(new BorderLayout());
        quizContentPanel.setBackground(bgColor);
        
        // Create results panel
        JPanel resultsPanel = new JPanel(new GridBagLayout());
        resultsPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Results title
        JLabel titleLabel = new JLabel("Quiz Results");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(accentColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        resultsPanel.add(titleLabel, gbc);
        
        // Score message
        JLabel scoreLabel = new JLabel("Your score: " + score + " out of " + currentQuizQuestions.size());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        resultsPanel.add(scoreLabel, gbc);
        
        // Percentage
        double percentage = (double) score / currentQuizQuestions.size() * 100;
        JLabel percentLabel = new JLabel(String.format("Percentage: %.1f%%", percentage));
        percentLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        percentLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        resultsPanel.add(percentLabel, gbc);
        
        // Pass/Fail message
        JLabel resultLabel = new JLabel(percentage >= 60 ? "Passed!" : "Failed!");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setForeground(percentage >= 60 ? new Color(46, 204, 113) : new Color(231, 76, 60));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        resultsPanel.add(resultLabel, gbc);
        
        // Back to dashboard button
        JButton backButton = createStyledButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "student"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        resultsPanel.add(backButton, gbc);
        
        // Add the results to the quiz content panel
        quizContentPanel.add(resultsPanel, BorderLayout.CENTER);
        
        // Save result
        results.add(new Result(currentUser.getUsername(), new Date().toString(), score, currentQuizQuestions.size()));
        
        quizContentPanel.revalidate();
        quizContentPanel.repaint();
    }
    
    // Authenticate user
    private User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    // Create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }
    
    // Main method
    public static void main(String[] args) {
        try {
            // Set look and feel to system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show application
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                QuizApp app = new QuizApp();
                app.setVisible(true);
            }
        });
    }
    
    // User class
    class User {
        private String username;
        private String password;
        private String fullName;
        private boolean admin;
        
        public User(String username, String password, String fullName, boolean admin) {
            this.username = username;
            this.password = password;
            this.fullName = fullName;
            this.admin = admin;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public String getFullName() {
            return fullName;
        }
        
        public boolean isAdmin() {
            return admin;
        }
    }
    
    // Question class
    class Question {
        private String text;
        private String[] options;
        private int correctOptionIndex;
        
        public Question(String text, String[] options, int correctOptionIndex) {
            this.text = text;
            this.options = options;
            this.correctOptionIndex = correctOptionIndex;
        }
        
        public String getText() {
            return text;
        }
        
        public String[] getOptions() {
            return options;
        }
        
        public int getCorrectOptionIndex() {
            return correctOptionIndex;
        }
    }
    
    // Result class
    class Result {
        private String username;
        private String date;
        private int score;
        private int totalQuestions;
        
        public Result(String username, String date, int score, int totalQuestions) {
            this.username = username;
            this.date = date;
            this.score = score;
            this.totalQuestions = totalQuestions;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getDate() {
            return date;
        }
        
        public int getScore() {
            return score;
        }
        
        public int getTotalQuestions() {
            return totalQuestions;
        }
    }
}