import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LevelEditor {
	Game game;
	ArrayList<Double> wallsX0;
	ArrayList<Double> wallsY0;
	ArrayList<Double> wallsX1;
	ArrayList<Double> wallsY1;
	Point2D.Double tempP;
	Point2D.Double lastP;
	boolean straight;
	boolean connected;
	
	static final int radius = 3;
	
	public LevelEditor(Game game) {
		this.game = game;
		wallsX0 = new ArrayList<Double>();
		wallsY0 = new ArrayList<Double>();
		wallsX1 = new ArrayList<Double>();
		wallsY1 = new ArrayList<Double>();
		tempP = null;
		lastP = null;
		straight = false;
		connected = false;
	}

	public void mousePressed(MouseEvent arg0) {
		double px = game.gr.invScaleX(arg0.getX());
		double py = -game.gr.invScaleY(arg0.getY());
		if(straight){
			px = Math.round(px);
			py = Math.round(py);
			if(lastP != null){
				if(Math.abs(px-lastP.x) < Math.abs(py-lastP.y)){
					px=lastP.x;
				}else{
					py=lastP.y;
				}
			}
		}
		int size = wallsX0.size();
		if(size>0 && connected && tempP == null){
			wallsX0.add(wallsX1.get(size-1));
			wallsY0.add(wallsY1.get(size-1));
			wallsX1.add(px);
			wallsY1.add(py);
		}else if(tempP == null){
			tempP = new Point2D.Double(px, py);
		}else{
			wallsX0.add(tempP.x);
			wallsY0.add(tempP.y);
			wallsX1.add(px);
			wallsY1.add(py);
			tempP = null;
		}
		lastP = new Point2D.Double(px, py);
	}

	public void draw(Graphics g) {
		int size = wallsX0.size();
		//draw axes
		g.setColor(Color.RED);
		g.drawLine(game.gr.width/2, 0, game.gr.width/2, game.gr.height);
		g.drawLine(0, game.gr.height/2, game.gr.width, game.gr.height/2);
		//draw walls
		g.setColor(Color.GREEN);
		for(int i=0; i<size; i++){
			g.drawLine(game.gr.scaleX(wallsX0.get(i)), game.gr.scaleY(-wallsY0.get(i)),
					game.gr.scaleX(wallsX1.get(i)), game.gr.scaleY(-wallsY1.get(i)));
		}
		//get mouse info
		Point m = MouseInfo.getPointerInfo().getLocation();
		Point j = game.pan.getLocationOnScreen();
		double px = game.gr.invScaleX(m.x-j.x);
		double py = -game.gr.invScaleY(m.y-j.y);
		if(straight){
			px = Math.round(px);
			py = Math.round(py);
			if(lastP != null){
				if(Math.abs(px-lastP.x) < Math.abs(py-lastP.y)){
					px=lastP.x;
				}else{
					py=lastP.y;
				}
			}
		}
		//draw points
		g.setColor(Color.BLUE);
		for(int i=0; i<size; i++){
			g.fillOval(game.gr.scaleX(wallsX0.get(i))-radius-1, game.gr.scaleY(-wallsY0.get(i))-radius-1,2*radius+1,2*radius+1);
			g.fillOval(game.gr.scaleX(wallsX1.get(i))-radius-1, game.gr.scaleY(-wallsY1.get(i))-radius-1,2*radius+1,2*radius+1);
		}
		if(tempP != null){
			g.fillOval(game.gr.scaleX(tempP.x)-radius-1, game.gr.scaleY(-tempP.y)-radius-1,2*radius+1,2*radius+1);
		}
		g.fillOval(game.gr.scaleX(px)-radius-1, game.gr.scaleY(-py)-radius-1,2*radius+1,2*radius+1);
		//draw text
		g.setColor(Color.RED);
		g.drawString("X="+px, 5, 15);
		g.drawString("Y="+py, 5, 30);
	}

	public void deletePoly() {
		wallsX0.clear();
		wallsY0.clear();
		wallsX1.clear();
		wallsY1.clear();
		tempP = null;
		lastP = null;
	}

	public void removeLastPoint() {
		int size = wallsX0.size();
		if(tempP != null){
			tempP = null;
		}else if(size>0){
			size--;
			removeLine(size);
		}
		if(size>0){
			lastP = new Point2D.Double(wallsX1.get(size-1), wallsY1.get(size-1));
		}else{
			lastP = null;
		}
	}
	
	public void printPoly() {
		if(wallsX0.size()<1){
			return;
		}
		String str;
		Point2D.Double startPoint;
		Point2D.Double endPoint;
		ArrayList<Double> wallsX0c = new ArrayList<Double>(wallsX0);
		ArrayList<Double> wallsY0c = new ArrayList<Double>(wallsY0);
		ArrayList<Double> wallsX1c = new ArrayList<Double>(wallsX1);
		ArrayList<Double> wallsY1c = new ArrayList<Double>(wallsY1);
		while(wallsX0.size()>0){
			startPoint = new Point2D.Double(wallsX0.get(0),wallsY0.get(0));
			str = wallsX0.get(0)+","+wallsY0.get(0)+",";
			endPoint = new Point2D.Double(wallsX1.get(0),wallsY1.get(0));
			removeLine(0);
			for(int i=0; i<wallsX0.size(); i++){
				if(endPoint.x==wallsX0.get(i) && endPoint.y==wallsY0.get(i)){
					str += wallsX0.get(i)+","+wallsY0.get(i)+",";
					endPoint = new Point2D.Double(wallsX1.get(i),wallsY1.get(i));
					removeLine(i);
					i=-1;
				}else if(endPoint.x==wallsX1.get(i) && endPoint.y==wallsY1.get(i)){
					str += wallsX1.get(i)+","+wallsY1.get(i)+",";
					endPoint = new Point2D.Double(wallsX0.get(i),wallsY0.get(i));
					removeLine(i);
					i=-1;
				}
			}
			for(int i=0; i<wallsX0.size(); i++){
				if(startPoint.x==wallsX1.get(i) && startPoint.y==wallsY1.get(i)){
					str = wallsX0.get(i)+","+wallsY0.get(i)+","+str;
					startPoint = new Point2D.Double(wallsX0.get(i),wallsY0.get(i));
					removeLine(i);
					i=-1;
				}else if(startPoint.x==wallsX0.get(i) && startPoint.y==wallsY0.get(i)){
					str = wallsX1.get(i)+","+wallsY1.get(i)+","+str;
					startPoint = new Point2D.Double(wallsX1.get(i),wallsY1.get(i));
					removeLine(i);
					i=-1;
				}
			}
			str += endPoint.x+","+endPoint.y+",";
			System.out.println(str);
		}
		wallsX0 = new ArrayList<Double>(wallsX0c);
		wallsY0 = new ArrayList<Double>(wallsY0c);
		wallsX1 = new ArrayList<Double>(wallsX1c);
		wallsY1 = new ArrayList<Double>(wallsY1c);
	}

	private void removeLine(int i){
		wallsX0.remove(i);
		wallsY0.remove(i);
		wallsX1.remove(i);
		wallsY1.remove(i);
	}

	public void straightLines(boolean key) {
		straight = key;
	}

	public void connectedLines(boolean key) {
		connected = key;
	}
}
