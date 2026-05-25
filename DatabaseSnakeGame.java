import java.sql.*;

public class DatabaseSnakeGame {
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/snakegame";
    private static final String USER = "root";

    // Change this if your MySQL password is different
    private static final String PASSWORD = "cs380";

    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected");
            }
        } catch (SQLException e) {
            System.out.println("Disconnect failed");
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        try {
            String check = "SELECT * FROM users WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(check);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Username already exists");
                return false;
            }

            String sql = "INSERT INTO users (username, password, date_registered) VALUES (?, ?, NOW())";
            PreparedStatement insert = connection.prepareStatement(sql);

            insert.setString(1, username);
            insert.setString(2, password);

            insert.executeUpdate();

            System.out.println("User registered");
            return true;

        } catch (SQLException e) {
            System.out.println("Register failed");
            e.printStackTrace();
            return false;
        }
    }

    public int loginUser(String username, String password) {
        try {
            String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login was a success");
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            System.out.println("Login error");
            e.printStackTrace();
        }

        return -1;
    }

    public void saveScore(int userId, int score, int level, String difficulty, int durationSec) {
        try {
            String sql = "INSERT INTO scores (user_id, score, level, difficulty, date_played, duration_sec) "
                    + "VALUES (?, ?, ?, ?, NOW(), ?)";

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
            e.printStackTrace();
        }
    }

    public int getHighScore() {
        try {
            String sql = "SELECT MAX(score) AS high_score FROM scores";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("high_score");
            }

        } catch (SQLException e) {
            System.out.println("Get high score failed");
            e.printStackTrace();
        }

        return 0;
    }

    public String getLeaderboard() {
        StringBuilder leaderboard = new StringBuilder();

        leaderboard.append("Top 10 Scores\n");
        leaderboard.append("--------------------------------------------------\n");

        String sql =
                "SELECT users.username, scores.score, scores.level, scores.difficulty, " +
                        "scores.date_played, scores.duration_sec " +
                        "FROM scores " +
                        "JOIN users ON scores.user_id = users.user_id " +
                        "ORDER BY scores.score DESC, scores.date_played ASC " +
                        "LIMIT 10";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int rank = 1;
            boolean hasScores = false;

            while (rs.next()) {
                hasScores = true;

                String username = rs.getString("username");
                int score = rs.getInt("score");
                int level = rs.getInt("level");
                String difficulty = rs.getString("difficulty");
                Timestamp datePlayed = rs.getTimestamp("date_played");
                int durationSec = rs.getInt("duration_sec");

                leaderboard.append(rank).append(". ");
                leaderboard.append(username).append(" | ");
                leaderboard.append("Score: ").append(score).append(" | ");
                leaderboard.append("Level: ").append(level).append(" | ");
                leaderboard.append("Difficulty: ").append(difficulty).append(" | ");
                leaderboard.append("Duration: ").append(durationSec).append(" sec | ");
                leaderboard.append("Date: ").append(datePlayed);
                leaderboard.append("\n");

                rank++;
            }

            if (!hasScores) {
                leaderboard.append("No scores found yet.");
            }

        } catch (SQLException e) {
            System.out.println("Get leaderboard failed");
            e.printStackTrace();
            return "Could not load leaderboard from database.";
        }

        return leaderboard.toString();
    }

    public String[][] getLeaderboardData() {
        String[][] data = new String[10][7];

        for (int i = 0; i < data.length; i++) {
            data[i][0] = "";
            data[i][1] = "";
            data[i][2] = "";
            data[i][3] = "";
            data[i][4] = "";
            data[i][5] = "";
            data[i][6] = "";
        }

        String sql =
                "SELECT users.username, scores.score, scores.level, scores.difficulty, " +
                        "DATE(scores.date_played) AS play_date, " +
                        "TIME(scores.date_played) AS play_time " +
                        "FROM scores " +
                        "JOIN users ON scores.user_id = users.user_id " +
                        "ORDER BY scores.score DESC, scores.date_played ASC " +
                        "LIMIT 10";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int row = 0;
            int rank = 1;

            while (rs.next() && row < 10) {
                data[row][0] = String.valueOf(rank);
                data[row][1] = rs.getString("username").toUpperCase();
                data[row][2] = String.valueOf(rs.getInt("score"));
                data[row][3] = String.valueOf(rs.getInt("level"));
                data[row][4] = rs.getString("difficulty");
                data[row][5] = String.valueOf(rs.getDate("play_date"));
                data[row][6] = String.valueOf(rs.getTime("play_time"));

                row++;
                rank++;
            }

        } catch (SQLException e) {
            System.out.println("Get leaderboard table data failed");
            e.printStackTrace();

            data[0][0] = "!";
            data[0][1] = "ERROR";
            data[0][2] = "";
            data[0][3] = "";
            data[0][4] = "";
            data[0][5] = "";
            data[0][6] = "";
        }

        return data;
    }
}