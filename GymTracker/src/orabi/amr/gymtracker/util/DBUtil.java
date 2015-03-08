package orabi.amr.gymtracker.util;

import orabi.amr.gymtracker.DBHelper;
import android.database.Cursor;

public class DBUtil {
	
	public static Cursor getExercise(DBHelper dbHelper, int exeItemId){
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select exe_name, max_weight, avg_weight,"
				+ " notes, photo from exercises where id = ?",
				new String[]{"" + exeItemId});
		
		return cursor;
	}

}
