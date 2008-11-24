package com.weltevree.ego.editors;

import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;

public class EgoFlyoutPreferences implements FlyoutPreferences {

	private int location = 0;

	private int state = 0;

	private int width = 200;

	public int getDockLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	public int getPaletteState() {
		// TODO Auto-generated method stub
		return state;
	}

	public int getPaletteWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	public void setDockLocation(int location) {

		this.location = location;

	}

	public void setPaletteState(int state) {
		this.state = state;

	}

	public void setPaletteWidth(int width) {
		this.width = width;

	}

}
