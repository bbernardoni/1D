package bennett.bernardoni;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame implements ComponentListener{
	Painting pan;
	Game game;
	int width;
	int height;
	
	public GUI(){
		setLayout(new BorderLayout());
		pan = new Painting();
		pan.setPreferredSize(new Dimension(1000, 250));
        pan.setOpaque(true);
        pan.setBackground(Color.BLACK);
		pan.addComponentListener(this);
		add("Center", pan);
		KeyManager km = new KeyManager();
		addKeyListener(km);
		game = new Game(km, pan);
	}

	public void startUpdating() {
        (new displayThread()).start();
	}
	
	private class displayThread extends Thread {
	    public void run() {
	    	while(true){
	    		game.update();
	    		pan.repaint();
	    		try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
	
	public class Painting extends JPanel implements MouseListener{
		public Painting(){
			addMouseListener(this);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			game.paint(g);
		}

		@Override 
		public void mousePressed(MouseEvent arg0) {
			game.le.mousePressed(arg0);
		}
		
		@Override public void mouseClicked(MouseEvent arg0) {}
		@Override public void mouseReleased(MouseEvent arg0) {}
		@Override public void mouseEntered(MouseEvent arg0) {}
		@Override public void mouseExited(MouseEvent arg0) {}
	}
	
	@Override
	public void componentResized(ComponentEvent arg0) {
		game.gr.gameResized(pan.getWidth(),pan.getHeight());
	}
	
	@Override public void componentHidden(ComponentEvent arg0) {}
	@Override public void componentMoved(ComponentEvent arg0) {}
	@Override public void componentShown(ComponentEvent arg0) {}
}