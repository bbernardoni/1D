import java.awt.Color;
import java.awt.Graphics;

public class Player {
	Game game;
	
	double cx=0;
	double cy=0;
	double ca=0;

	static final double transSpeed = 0.25;
	static final double angSpeed = 0.05;
	static final double hitboxSize = 1.0;
	static final double m_2pi = 2*Math.PI;
	
	public Player(Game game){
		this.game = game;
	}

	public void move(boolean kRight, boolean kLeft, boolean kW, boolean kS, boolean kD, boolean kA) {
		if(kRight){
			ca-=angSpeed;
		}
		if(kLeft){
			ca+=angSpeed;
		}
		int x = 0;
		int y = 0;
		if(kW){
			x++;
		}
		if(kS){
			x--;
		}
		if(kD){
			y--;
		}
		if(kA){
			y++;
		}
		if(x!=0 || y!=0){
			double transAng = Math.atan2(y, x)+ca;
			double dcx=transSpeed*Math.cos(transAng)+cx;
			double dcy=transSpeed*Math.sin(transAng)+cy;
			if(!game.st.collides(dcx, dcy, hitboxSize)){
				cx=dcx;
				cy=dcy;
			}else if(!game.st.collides(dcx, cy, hitboxSize)){
				cx=dcx;
			}else if(!game.st.collides(cx, dcy, hitboxSize)){
				cy=dcy;
			}
		}
	}

	public void drawGame(Color[] colorArray, double[] depthArray) {
		//don't really need to draw anything
	}

	public void drawDebug(Graphics g) {
		int pcx = game.gr.scaleX(cx);
		int pcy = game.gr.scaleY(-cy);
		g.setColor(Color.BLUE);
		int hbWidth = game.gr.scale(hitboxSize);
		g.fillRect(pcx-hbWidth/2, pcy-hbWidth/2, hbWidth, hbWidth);
		g.setColor(Color.RED);
		double offset = Math.atan(1.0/2.0/CompGraphics.fov);
		double ang = ((ca % m_2pi) + m_2pi) % m_2pi;
		game.gr.drawLineAtAng(g, pcx, pcy, ang);
		game.gr.drawLineAtAng(g, pcx, pcy, (ang+offset) % m_2pi);
		game.gr.drawLineAtAng(g, pcx, pcy, (ang-offset+m_2pi) % m_2pi);
	}
}
