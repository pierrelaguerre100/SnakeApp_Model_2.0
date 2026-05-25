import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuWindowPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel subtitleLabel;

    private JLabel usernameLabel;
    private JTextField usernameField;

    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private JLabel difficultyLabel;
    private JComboBox<String> difficultyBox;

    private JButton loginButton;
    private JButton signUpButton;
    private JButton leaderboardButton;
    private JButton newGameButton;
    private JButton playButton;

    private JFrame frame;
    private int boardWidth;
    private int boardHeight;

    private int currentUserId = -1;
    private String currentUsername = "";

    private final Color backgroundColor = new Color(5, 20, 5);
    private final Color panelColor = new Color(10, 35, 10);
    private final Color neonGreen = new Color(75, 255, 75);
    private final Color darkGreen = new Color(20, 120, 20);
    private final Color yellow = new Color(255, 205, 40);
    private final Color blue = new Color(40, 140, 220);
    private final Color purple = new Color(135, 55, 200);
    private final Color red = new Color(210, 45, 45);

    public MenuWindowPanel(JFrame frame, int boardWidth, int boardHeight) {
        this.frame = frame;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(panelColor);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 3),
                new EmptyBorder(8, 25, 10, 25)
        ));

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createInputPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        connectButtons();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2, 1));
        titlePanel.setBackground(panelColor);
        titlePanel.setBorder(new EmptyBorder(0, 5, 8, 5));

        titleLabel = new JLabel("SNAKE GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(neonGreen);

        subtitleLabel = new JLabel("EAT  •  GROW  •  SURVIVE", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        subtitleLabel.setForeground(yellow);

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        return titlePanel;
    }

    private JPanel createInputPanel() {
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setBackground(panelColor);
        wrapperPanel.setLayout(new GridBagLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(panelColor);
        inputPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 8, 3, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameLabel = createRetroLabel("USERNAME");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(usernameLabel, gbc);

        usernameField = createRetroTextField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(usernameField, gbc);

        passwordLabel = createRetroLabel("PASSWORD");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(passwordLabel, gbc);

        passwordField = createRetroPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(passwordField, gbc);

        difficultyLabel = createRetroLabel("DIFFICULTY");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputPanel.add(difficultyLabel, gbc);

        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyBox.setFont(new Font("Monospaced", Font.BOLD, 15));
        difficultyBox.setBackground(new Color(15, 45, 15));
        difficultyBox.setForeground(Color.WHITE);
        difficultyBox.setSelectedItem("Medium");
        difficultyBox.setPreferredSize(new Dimension(330, 34));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(difficultyBox, gbc);

        loginButton = createRetroButton("LOGIN", darkGreen, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(155, 36));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        inputPanel.add(loginButton, gbc);

        signUpButton = createRetroButton("SIGN UP", yellow, Color.BLACK);
        signUpButton.setPreferredSize(new Dimension(155, 36));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        inputPanel.add(signUpButton, gbc);

        leaderboardButton = createRetroButton("LEADERBOARD", blue, Color.WHITE);
        leaderboardButton.setPreferredSize(new Dimension(330, 36));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 8, 2, 8);
        inputPanel.add(leaderboardButton, gbc);

        wrapperPanel.add(inputPanel);

        return wrapperPanel;
    }

    private JPanel createBottomButtonPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(panelColor);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        bottomPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        newGameButton = createRetroButton("NEW GAME", purple, Color.WHITE);
        playButton = createRetroButton("PLAY", red, Color.WHITE);

        newGameButton.setPreferredSize(new Dimension(175, 46));
        playButton.setPreferredSize(new Dimension(175, 46));

        bottomPanel.add(newGameButton);
        bottomPanel.add(playButton);

        return bottomPanel;
    }

    private JLabel createRetroLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(neonGreen);
        return label;
    }

    private JTextField createRetroTextField() {
        JTextField field = new JTextField(18);
        field.setFont(new Font("Monospaced", Font.BOLD, 15));
        field.setBackground(new Color(0, 10, 0));
        field.setForeground(Color.WHITE);
        field.setCaretColor(neonGreen);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                new EmptyBorder(3, 8, 3, 8)
        ));
        field.setPreferredSize(new Dimension(330, 34));
        return field;
    }

    private JPasswordField createRetroPasswordField() {
        JPasswordField field = new JPasswordField(18);
        field.setFont(new Font("Monospaced", Font.BOLD, 15));
        field.setBackground(new Color(0, 10, 0));
        field.setForeground(Color.WHITE);
        field.setCaretColor(neonGreen);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonGreen, 2),
                new EmptyBorder(3, 8, 3, 8)
        ));
        field.setPreferredSize(new Dimension(330, 34));
        return field;
    }

    private JButton createRetroButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 15));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                new EmptyBorder(5, 10, 5, 10)
        ));
        button.setPreferredSize(new Dimension(155, 36));
        return button;
    }

    private void connectButtons() {
        loginButton.addActionListener(e -> loginUser());
        signUpButton.addActionListener(e -> signUpUser());
        leaderboardButton.addActionListener(e -> showLeaderboard());

        playButton.addActionListener(e -> {
            if (currentUserId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please log in first.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            openGame();
        });

        newGameButton.addActionListener(e -> {
            if (currentUserId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please log in first before starting a new game.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            openGame();
        });
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        DatabaseSnakeGame db = new DatabaseSnakeGame();
        db.connect();

        int userId = db.loginUser(username, password);

        db.disconnect();

        if (userId != -1) {
            currentUserId = userId;
            currentUsername = username;

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful. Welcome, " + currentUsername + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            currentUserId = -1;
            currentUsername = "";

            JOptionPane.showMessageDialog(
                    this,
                    "Login failed. Check your username and password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void signUpUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        DatabaseSnakeGame db = new DatabaseSnakeGame();
        db.connect();

        boolean registered = db.registerUser(username, password);

        db.disconnect();

        if (registered) {
            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully. You can now log in.",
                    "Sign Up Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Sign up failed. That username may already exist.",
                    "Sign Up Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showLeaderboard() {
        DatabaseSnakeGame db = new DatabaseSnakeGame();
        db.connect();

        String[][] leaderboardData = db.getLeaderboardData();

        db.disconnect();

        String[] columns = {"Rank", "Player", "Score", "Level", "Difficulty", "Date", "Time"};

        DefaultTableModel model = new DefaultTableModel(leaderboardData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.BOLD, 13));
        table.setRowHeight(28);
        table.setBackground(new Color(5, 20, 5));
        table.setForeground(Color.WHITE);
        table.setGridColor(neonGreen);
        table.setSelectionBackground(new Color(20, 80, 20));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(10, 60, 10));
        table.getTableHeader().setForeground(neonGreen);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBackground(new Color(5, 20, 5));
        centerRenderer.setForeground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 330));
        scrollPane.getViewport().setBackground(new Color(5, 20, 5));
        scrollPane.setBorder(BorderFactory.createLineBorder(neonGreen, 3));

        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BorderLayout());
        leaderboardPanel.setBackground(backgroundColor);
        leaderboardPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        header.setFont(new Font("Monospaced", Font.BOLD, 30));
        header.setForeground(neonGreen);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel footer = new JLabel("TOP 10 SCORES", SwingConstants.CENTER);
        footer.setFont(new Font("Monospaced", Font.BOLD, 14));
        footer.setForeground(neonGreen);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));

        leaderboardPanel.add(header, BorderLayout.NORTH);
        leaderboardPanel.add(scrollPane, BorderLayout.CENTER);
        leaderboardPanel.add(footer, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(
                this,
                leaderboardPanel,
                "Leaderboard",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void openGame() {
        String selectedDifficulty = difficultyBox.getSelectedItem().toString();

        SnakeGamePanel gamePanel = new SnakeGamePanel(
                frame,
                boardWidth,
                boardHeight,
                currentUserId,
                currentUsername,
                selectedDifficulty
        );

        frame.setContentPane(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();

        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }
}