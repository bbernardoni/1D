import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Stage {
	Game game;

	double[][][] walls;// = {{{-10,10},{10,10},{10,-10},{-10,-10},{-10,-4},{-28,-4},{-28,10},{-20,10},{-20,4},{-10,4},{-10,10}}};
	
	public Stage(Game game) {
		this.game = game;
		File file = new File("level1.lvl");
        if (file.exists()){
	        try {
			    //get number of lines
			    Scanner scanLines = new Scanner(file);
			    int count = 0;
			    while (scanLines.hasNextLine()) {
			    	scanLines.nextLine();
			        count++;
			    }
			    walls = new double[count][][];
			    scanLines.close();
			    //get number of points on a line
			    Scanner scanPoints = new Scanner(file);
			    scanPoints.useDelimiter(",");
			    for(int i=0; i<walls.length; i++){
			    	count = 0;
			    	while (scanPoints.hasNextDouble()) {
			    		scanPoints.nextDouble();
				        count++;
				    }
			    	walls[i] = new double[count/2][2];
			    	scanPoints.nextLine();
			    }
			    scanPoints.close();
			    //propagate the array
			    Scanner scanArr = new Scanner(file);
			    scanArr.useDelimiter(",");
			    for(int i=0; i<walls.length; i++){
			    	for(int j=0; j<walls[i].length; j++){
			    		walls[i][j][0] = scanArr.nextDouble();
			    		walls[i][j][1] = scanArr.nextDouble();
				    }
			    	scanArr.nextLine();
			    }
			    scanArr.close();
			} catch (IOException e) {}
        }
	}

	public boolean collides(double x, double y, double hitboxSize) {
		double l = x-hitboxSize/2.0;
		double r = x+hitboxSize/2.0;
		double u = y-hitboxSize/2.0;
		double d = y+hitboxSize/2.0;
		for(int i=0; i<walls.length; i++){
			for(int j=1; j<walls[i].length; j++){
				if(Line2D.linesIntersect(l,u,l,d,walls[i][j-1][0],walls[i][j-1][1],walls[i][j][0],walls[i][j][1])||
						Line2D.linesIntersect(r,u,r,d,walls[i][j-1][0],walls[i][j-1][1],walls[i][j][0],walls[i][j][1])||
						Line2D.linesIntersect(r,u,l,u,walls[i][j-1][0],walls[i][j-1][1],walls[i][j][0],walls[i][j][1])||
						Line2D.linesIntersect(r,d,l,d,walls[i][j-1][0],walls[i][j-1][1],walls[i][j][0],walls[i][j][1])){
					return true;
				}
			}
		}
		return false;
	}

	public void drawGame(Color[] colorArray, double[] depthArray) {
		for(int i=0; i<walls.length; i++){
			for(int j=1; j<walls[i].length; j++){
				game.gr.drawLine(colorArray, depthArray, Color.GREEN, 
						walls[i][j-1][0],walls[i][j-1][1],walls[i][j][0],walls[i][j][1]);
			}
		}
	}

	public void drawDebug(Graphics g) {
		g.setColor(Color.GREEN);
		for(int i=0; i<walls.length; i++){
			for(int j=1; j<walls[i].length; j++){
				g.drawLine(game.gr.scaleX(walls[i][j-1][0]), game.gr.scaleY(-walls[i][j-1][1]),
						game.gr.scaleX(walls[i][j][0]), game.gr.scaleY(-walls[i][j][1]));
			}
		}
	}
	
}
