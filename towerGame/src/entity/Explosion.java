package entity;

import java.awt.Color;
import java.awt.Rectangle;

import main.Main;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Position;

public class Explosion extends Entity {
	int size;
	int createdOn;
	Color color = new Color(237, 164, 5);
	Position[] circlePositions;
	double[] circleSizes;
	Color[] circleColors;
	public Explosion(Level level, int size) {
		super(level);
		this.size = size;
		this.createdOn = Main.frames;
		this.circlePositions = new Position[size+8];
		this.circleSizes = new double[size+8];
		this.circleColors = new Color[size+8];
		this.hitbox = new Rectangle(-8, -8, 16, 16);
		for(int i=0; i<size+8; i++) {
			circlePositions[i] = new Position(Main.random.nextGaussian() / 1.5, Main.random.nextGaussian() / 1.5);
			circleSizes[i] = Math.max(2 - Math.hypot(Math.abs(circlePositions[i].x), Math.abs(circlePositions[i].y)) * 1.2, 0.25);//(Main.random.nextDouble() / 2) + 1;
			System.out.println(circleSizes[i]);
			circleColors[i] = new Color(Math.min(color.getRed() + Main.random.nextInt(30), 255),Math.min(color.getGreen() +  Main.random.nextInt(60), 255),Math.min(color.getBlue() + Main.random.nextInt(25), 255));
		}
		// TODO Auto-generated constructor stub
	}
	public Explosion(Level level) {
		this(level, 100);
	}
	public void render(WorldRenderer wr) {
		for(int i=0; i<size+8; i++) {
			wr.fillEllipse(this.x + circlePositions[i].x + (this.circleSizes[i] * -0.5), this.x + circlePositions[i].x + (this.circleSizes[i] * 0.5), this.y + circlePositions[i].y + (this.circleSizes[i] * -0.5), this.y + circlePositions[i].y + (this.circleSizes[i] * 0.5), circleColors[i]);
		}
	}
	public String getDebugString() {
		return "Java is not a compiled language. I don't understand why people think it is compiled,\nit has it's own custom built MACHINE, which inside of it has custom built INTERPRETER for this horrible language.\nWhen you start your java \"program\", you actually boot up a whole new virtual machine,\nwhich then runs interpreter that actually runs your \"\"\"compiled java bytecode\"\"\n(read: glorified AST tree in binary format), why do you think it needs to start a whole new VIRTUAL MACHINE?\nIt is because this language is so horrible, it can't even run on REAL machines, using REAL interpreters, yet,\neven be compiled to anything reasonable, there's no such crazy vendor out there,\nwho would go out of their way to create nightmarish hardware that could run java NATIVELY, and so it will never happen,\nI don't understand why people go out of their way to CREATE IMAGINARY MACHINE,\njust so it can run their \"\"\"compiled\"\"\" language that uses 16GB RAM just to START,\nplease stop calling it compiled when you have no idea about programming.";
	}
}
