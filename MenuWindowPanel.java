import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuWindowPanel extends JPanel {

    private JLabel usernameLabel;
    private JTextField usernameField;

    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton signUpButton;
    private JButton leaderboardButton;
    private JButton newGameButton;
    private JButton playButton;

    public MenuWindowPanel() {

        // Panel settings
        setLayout(new BorderLayout());
        setBackground(new Color(35, 35, 35));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        //MAIN CENTER PANEL
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(35, 35, 35));
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        //USERNAME
        usernameLabel = new JLabel("Enter Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(16);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setPreferredSize(new Dimension(240, 40));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(usernameField, gbc);

        // PASSWORD 
        passwordLabel = new JLabel("Enter Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(16);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setPreferredSize(new Dimension(240, 40));

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(passwordField, gbc);

        //LOGIN AND SIGN UP PANEL
        JPanel accountButtonPanel = new JPanel();
        accountButtonPanel.setBackground(new Color(35, 35, 35));
        accountButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(140, 45));

        // Sign Up Button
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setPreferredSize(new Dimension(140, 45));

        accountButtonPanel.add(loginButton);
        accountButtonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(accountButtonPanel, gbc);

        //LEADERBOARD BUTTON
        leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 18));
        leaderboardButton.setPreferredSize(new Dimension(220, 45));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(leaderboardButton, gbc);

        //BOTTOM BUTTONS
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(35, 35, 35));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));

        // New Game Button
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        newGameButton.setPreferredSize(new Dimension(180, 50));

        // Play Button
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setPreferredSize(new Dimension(180, 50));

        buttonPanel.add(newGameButton);
        buttonPanel.add(playButton);

        // Add panels
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}