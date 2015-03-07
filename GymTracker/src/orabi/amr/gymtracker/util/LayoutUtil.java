package orabi.amr.gymtracker.util;

import java.io.FileInputStream;
import java.io.IOException;

import orabi.amr.gymtracker.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

public class LayoutUtil {
	
	public static Cursor prepareExerciseDataUI(Context context, int exeItemId, TextView exNameLabel,
			ImageView exPhotoDetails) throws IOException{
		DBHelper dbHelper = DBHelper.getHelperInstance(context);
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select exe_name, max_weight, avg_weight,"
				+ " notes, photo from exercises where id = ?",
				new String[]{"" + exeItemId});
		
		if(cursor.moveToFirst()){
			exNameLabel.setText(cursor.getString(0));
			
			String photoPath = cursor.getString(4);
			if(photoPath != null){
				 FileInputStream fi = context.openFileInput(photoPath);
				 exPhotoDetails.setImageBitmap(BitmapFactory.decodeFileDescriptor(fi.getFD()));
			}
		}
		return cursor;
//		cursor.close();
	}

}
