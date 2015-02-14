package orabi.amr.gymtracker;

import java.io.Serializable;

public class ListItem implements Serializable{
	int id;
	String name;
	byte[] photo;
	
	@Override
	public String toString() {
		return name;
	}
}
