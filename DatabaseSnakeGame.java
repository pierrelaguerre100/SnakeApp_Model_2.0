import java.sql.*;

public class DatabaseSnakeGame {
    private Connection connection;

    //database information
    private static final String URL = "jdbc:mysql://localhost:3306/snakegame";
    private static final String USER = "root";
    private static final String PASSWORD = "ComputerScience380";

    //connect to database
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }

    //disconnect from the database
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected");
            }
        } catch (SQLException e) {
            System.out.println("Disconnect failed");
        }
    }

    //registering new users
    public boolean registerUser(String username, String password) {
        try {
            //check if the user already exists
            String check = "SELECT * FROM USERS WHERE USERNAME =?";
            PreparedStatement ps = connection.prepareStatement(check);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Username already exists");
                return false;
            }

            //insert new user
            String sql = "INSERT INTO USERS (USERNAME, PASSWORD, DATE_REGISTERED) VALUES (?, ?, NOW())";
            PreparedStatement insert = connection.prepareStatement(sql);

            insert.setString(1, username);
            insert.setString(2, password);

            insert.executeUpdate();

            System.out.println("User registered");
            return true;

        } catch (SQLException e) {
            System.out.println("Register failed");
            return false;

        }
    }
    //login user
    public int loginUser(String username, String password) {
        try {
            String sql = "SELECT USER_ID FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login was a success");
                return rs.getInt("USER_ID");
            }

        } catch (SQLException e) {
            System.out.println("Login error");
        }
        return -1;
    }


    //save score after the game ends
    public void saveScore(int userId, int score, int level, String difficulty, int durationSec) {
        try {
            String sql = "INSERT INTO SCORES (USER_ID, SCORE, LEVEL, DIFFICULTY, DATE_PLAYED, DURATION_SEC)" + "VALUES (?, ?, ?, ?, NOW(), ?)";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, score);
            ps.setInt(3, level);
            ps.setString(4, difficulty);
            ps.setInt(5, durationSec);

            ps.executeUpdate();

            System.out.println("Score saved successfully");
        } catch (SQLException e) {
            System.out.println("Save score has failed");
        }
    }

    //get highest score in game
    public int getHighScore() {
        try {
            String sql = "SELECT MAX(SCORE) AS HIGH_SCORE FROM SCORES";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("HIGH_SCORE");
            }

        } catch (SQLException e) {
            System.out.println("Get high score failed");
        }
        return 0;
    }
}

