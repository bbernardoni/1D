import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

public class CompGraphics {
	Game game;
	int width;
	int height;

	double aCoef;
	double bCoef;
	double cCoef;
	double dCoef;
	double eCoef;
	double fCoef;

	static final double fov = 0.4;//distance to screen/width
	static final double scalar = 10.0;
	
	public CompGraphics(Game game) {
		this.game = game;
	}

	public void gameResized(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void updateTransforms() {
		double ang = game.pl.ca;
		double x = game.pl.cx;
		double y = game.pl.cy;
		aCoef = Math.cos(ang);
		bCoef = Math.sin(ang);
		cCoef = (-x*Math.cos(ang)-y*Math.sin(ang));
		dCoef = -Math.sin(ang);
		eCoef = Math.cos(ang);
		fCoef = (x*Math.sin(ang)-y*Math.cos(ang));
	}

	public void drawGame(Graphics g) {
		Color colorArray[] = new Color[width];
		double depthArray[] = new double[width];
		Arrays.fill(depthArray, Double.MAX_VALUE);
		game.st.drawGame(colorArray, depthArray);
		game.pl.drawGame(colorArray, depthArray);
		for(int x=0; x<colorArray.length; x++){
			if(colorArray[x]==null){
				g.setColor(Color.BLACK);
			}else{
				g.setColor(colorArray[x]);
			}
			g.drawLine(x, 0, x, height-1);
		}
	}
	
	public void drawDebug(Graphics g) {
		game.pl.drawDebug(g);
		game.st.drawDebug(g);
	}
	
	public void drawEditor(Graphics g) {
		game.le.draw(g);
	}

	public void drawPrinter(Graphics g) {
		//Polygon h = new Polygon(new int[]{2,2,4,4,7,7,9,9,7,7,4,4},new int[]{0,10,10,6,6,10,10,0,0,4,4,0}, 12);
		for(int x=0; x<width; x++){
			if(x==2)
			{
				g.setColor(Color.GREEN);
			}else{
				g.setColor(Color.BLACK);
			}
			g.drawLine(x, 0, x, height-1);
		}
	}
	
	public int scale(double s) {
		return (int) (s*scalar);
	}

	public int scaleX(double x) {
		return scale(x)+width/2;
	}

	public int scaleY(double y) {
		return scale(y)+height/2;
	}

	public double invScale(double s) {
		return s/scalar;
	}

	public double invScaleX(double x) {
		return invScale(x-width/2);
	}

	public double invScaleY(double y) {
		return invScale(y-height/2);
	}
	
	public void drawLineAtAng(Graphics g, int pcx, int pcy, double ang) {
		if(ang<=Math.PI){
			g.drawLine(pcx, pcy, (int)(pcy/Math.tan(ang)+pcx), 0);
		}else{
			g.drawLine(pcx, pcy, (int)((pcy-height)/Math.tan(ang)+pcx), height-1);
		}
	}
	
	public void drawLine(Color[] colorArray, double[] depthArray, Color c, double x0, double y0, double x1, double y1){
		double dy0 = aCoef*x0+bCoef*y0+cCoef;
		double dx0 = dCoef*x0+eCoef*y0+fCoef;
		double dy1 = aCoef*x1+bCoef*y1+cCoef;
		double dx1 = dCoef*x1+eCoef*y1+fCoef;
		double ddx = dx0 - dx1;
		double ddy = dy0 - dy1;
		double slope = ddy/ddx;
		if(dy0<=dx0*slope || dx0*dx1>=0 || ddx==0){
			if(dy0<=2*fov*Math.abs(dx0)){
				if(dy1<=2*fov*Math.abs(dx1)){
					return;
				}
			}
		}
		int startP;
		int endP;
		if(dy0<=0){
			if(dx0>dy0*ddx/ddy){
				startP = 0;
				endP = (int) (fov*width*dx1/-dy1) + width/2;
			}else{
				startP = (int) (fov*width*dx1/-dy1) + width/2;
				endP = width-1;
			}
		}else if(dy1<=0){
			if(dx0>dy0*ddx/ddy){
				startP = 0;
				endP = (int) (fov*width*dx0/-dy0) + width/2;
			}else{
				startP = (int) (fov*width*dx0/-dy0) + width/2;
				endP = width-1;
			}
		}else{
			startP = (int) (fov*width*dx0/-dy0) + width/2;
			endP = (int) (fov*width*dx1/-dy1) + width/2;
			if(endP<startP){
				int temp = endP;
				endP = startP;
				startP = temp;
			}
		}
		if(startP<0){
			startP = 0;
		}
		if(endP>=width){
			endP = width-1;
		}
		double depthCoef = fov*width*(dx0*slope-dy0);
		double lineAng = Math.atan2(ddy, ddx);
		double angCoef = (Math.cos(lineAng*2)+5)/6;
		for(int i=startP; i<=endP; i++){
			int px = width/2-i;
			double depthY;
			if(ddx!=0){
				depthY = depthCoef/(px*slope-fov*width);
			}else{
				depthY = fov*width*dx0/px;
			}
			double depthX = px*depthY/fov/width;
			double depth = Math.sqrt(depthX*depthX+depthY*depthY);
			if(depthArray[i] > depth){
				double color = 1-Math.sqrt(depth)/10;
				if(color<0){
					color = 0;
				}
				color *= angCoef;
				int red = (int) (c.getRed()*color);
				int green = (int) (c.getGreen()*color);
				int blue = (int) (c.getBlue()*color);
				colorArray[i] = new Color(red,green,blue);
				depthArray[i] = depth;
			}
		}
	}
}
