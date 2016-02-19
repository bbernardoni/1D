import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Game {
	KeyManager km;
	GUI.Painting pan;
	Player pl;
	Stage st;
	CompGraphics gr;
	LevelEditor le;
	enum MenuStates {game, debug, editor, printer}
	MenuStates menuState;
	
	public Game(KeyManager km, GUI.Painting pan) {
		this.km = km;
		this.pan = pan;
		pl = new Player(this);
		st = new Stage(this);
		gr = new CompGraphics(this);
		le = new LevelEditor(this);
		menuState = MenuStates.game;
	}

	public void update() {
		if(km.getKey(KeyEvent.VK_1)){
			menuState = MenuStates.game;
		}
		if(km.getKey(KeyEvent.VK_2)){
			menuState = MenuStates.debug;
		}
		if(km.getKey(KeyEvent.VK_3)){
			menuState = MenuStates.editor;
		}
		if(km.getKey(KeyEvent.VK_4)){
			menuState = MenuStates.printer;
		}
		if(menuState == MenuStates.game || menuState == MenuStates.debug){
			pl.move(km.getKey(KeyEvent.VK_RIGHT), km.getKey(KeyEvent.VK_LEFT), km.getKey(KeyEvent.VK_W), 
					km.getKey(KeyEvent.VK_S), km.getKey(KeyEvent.VK_D), km.getKey(KeyEvent.VK_A));
			gr.updateTransforms();
		}else if(menuState == MenuStates.editor){
			if(km.getKeyPressed(KeyEvent.VK_DELETE)){
				le.deletePoly();
			}
			if(km.getKeyPressed(KeyEvent.VK_BACK_SPACE)){
				le.removeLastPoint();
			}
			if(km.getKeyPressed(KeyEvent.VK_ENTER)){
				le.printPoly();
			}
			le.straightLines(km.getKey(KeyEvent.VK_SHIFT));
			le.connectedLines(km.getKey(KeyEvent.VK_CONTROL));
		}
	}

	public void paint(Graphics g) {
		switch(menuState){
		case game:
			gr.drawGame(g);
			break;
		case debug:
			gr.drawDebug(g);
			break;
		case editor:
			gr.drawEditor(g);
			break;
		case printer:
			gr.drawPrinter(g);
			break;
		default:
			break;
		}
	}
}
