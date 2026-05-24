
import java.awt.Component;

import javax.swing.*;
public class App {


	public static void main(String[] args) throws Exception {
		int boardWidth = 600;
		int boardHeight = boardWidth;
		
		JFrame frame = new JFrame("Snake");
		frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SnakeGamePanel snakeGame = new SnakeGamePanel(boardWidth, boardHeight);
		MenuWindowPanel menu = new MenuWindowPanel();
		
		//frame.add(menu);
		frame.add(snakeGame);
		frame.pack();
		snakeGame.requestFocus();
	}
	

}
