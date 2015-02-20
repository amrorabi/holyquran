package orabi.amr.gymtracker;

import java.io.Serializable;

public class ListItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	Integer id;
	String name;
	byte[] photo;
	
	@Override
	public String toString() {
		return name;
	}
}
