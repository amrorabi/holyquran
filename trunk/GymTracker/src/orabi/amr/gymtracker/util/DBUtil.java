package orabi.amr.gymtracker.util;

import java.text.SimpleDateFormat;

import orabi.amr.gymtracker.DBHelper;
import android.database.Cursor;

public class DBUtil {
	
	public static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Cursor getExercise(DBHelper dbHelper, int exeItemId){
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select exe_name, max_weight, last_time,"
				+ " notes, photo from exercises where id = ?",
				new String[]{"" + exeItemId});
		
		return cursor;
	}

}
