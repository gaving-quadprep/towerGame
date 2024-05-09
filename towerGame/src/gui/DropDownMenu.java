package gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
/**
 * dropdown menu icon:
 * <pre>
 * ----
 * 0000
 * -00-
 * ----
 * </pre>
 * @param <T> type of object in the menu
 */
public class DropDownMenu<T> extends Input<T> {
	private List<T> items;
	private boolean dropped = false;
	public int selectedItem;
	@SafeVarargs
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
			for(T t : items) {
				
			}
		}
		GUI.fontRenderer.drawText(g2, this.items.get(selectedItem).toString(), this.x, this.y);
	}
	@Override
	public void onClicked(Point pos) {
		
		if(!dropped) {
			this.dropped = true;
		}else {
			
		}
	};

}
