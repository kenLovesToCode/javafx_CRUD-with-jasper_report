package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Position {
	
	private final StringProperty pPosition;
	
	public Position(String pPosition) {
		this.pPosition = new SimpleStringProperty(pPosition);
	}

	public StringProperty getpPosition() {
		return pPosition;
	}

}
