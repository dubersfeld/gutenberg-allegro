package com.dub.client.controller.users;

public class SelectableAndIndex {

	Selectable selectable;// marker interface not class
	int index;
	
	public SelectableAndIndex() {}
	
	public SelectableAndIndex(Selectable selectable, int index) {
		this.selectable = selectable;
		this.index = index;
	}

	public Selectable getSelectable() {
		return selectable;
	}

	public void setSelectable(Selectable selectable) {
		this.selectable = selectable;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
}