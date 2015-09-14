package bennett.bernardoni;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

	static final int keyMap = 128;
	
	private boolean keys[] = new boolean[keyMap];
	private boolean keysPressed[] = new boolean[keyMap];
	
	public boolean getKey(int k){
		return keys[k];
	}
	
	public boolean getKeyPressed(int k){
		boolean kp = keysPressed[k];
		keysPressed[k] = false;
		return kp;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k<keyMap){
			keys[k] = true;
			keysPressed[k] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if(k<keyMap){
			keys[k] = false;
			keysPressed[k] = false;
		}
	}

	@Override public void keyTyped(KeyEvent arg0) {}
}
