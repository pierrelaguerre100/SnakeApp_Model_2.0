//This is the test we used for the database to see if its working
public class TestDB {
    public static void main(String[] args) {

        DatabaseSnakeGame db = new DatabaseSnakeGame();

        //connect first
        db.connect();

        //register user
        db.registerUser("Homer", "1234");

        //login user
        int userId = db.loginUser("Homer", "1234");
        System.out.println("User ID: " + userId);

        //save score
        db.saveScore(userId, 250, 2, "MEDIUM", 75);

        //high score
        int highScore = db.getHighScore();
        System.out.println("Highest Score: " + highScore);

        //disconnect
        db.disconnect();
    }
}
