package gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class DropDownMenu<T> extends Input<T> {
	private List<T> items;
	private boolean dropped = false;
	public int selectedItem;
	public DropDownMenu(T... ts) {
		this.items = new ArrayList<T>();
		for(T t : ts) {
			this.items.add(t);
		}
	}
	
	@Override
	public T getInput() {
		return items.get(selectedItem);
	}

	@Override
	public void render(Graphics2D g2) {
		if(dropped) {
			
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClicked() {
		
	};

}
