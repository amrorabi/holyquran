package orabi.amr.gymtracker.util;

import orabi.amr.gymtracker.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

public class LayoutUtil {
	
	public static Cursor prepareExerciseDataUI(Context context, int exeItemId, TextView exNameLabel,
			ImageView exPhotoDetails){
		DBHelper dbHelper = DBHelper.getHelperInstance(context);
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select exe_name, max_weight, avg_weight,"
				+ " notes, photo from exercises where id = ?",
				new String[]{"" + exeItemId});
		
		if(cursor.moveToFirst()){
			exNameLabel.setText(cursor.getString(0));			
			byte[] photo = cursor.getBlob(4);
			if(photo != null)
				exPhotoDetails.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
		}
		return cursor;
//		cursor.close();
	}

}
