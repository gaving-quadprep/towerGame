package gui;

import java.awt.Graphics2D;

public class NumberPicker extends Input<Double> {
	double min;
	double max;
	double step;
	double value;
	@Override
	public Double getInput() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void render(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}

}
