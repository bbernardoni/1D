import javax.swing.JFrame;

public class Main {
	
	public static void main (String args[]){
		GUI gui = new GUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.pack();
		//gui.setResizable(false);
		gui.setTitle("1D");
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
		gui.requestFocusInWindow();
		gui.startUpdating();
	}
}
