package orabi.amr.gymtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "gymTrackerDB";
	private static  DBHelper HELPER;

	public static DBHelper getHelperInstance(Context context){
		if(HELPER == null)
			HELPER = new DBHelper(context);
		
		return HELPER;
	}
	
	public DBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table muscles (id integer primary key autoincrement, muscle_name text not null,"
				+ " day_order integer);"); 
		
		db.execSQL("create table exercises (id integer primary key autoincrement, exe_name text,"
				+ " max_weight integer, avg_weight integer, exc_times integer, muscle_id integer not null, notes text, photo text,"
				+ " foreign key (muscle_id) references muscles(id));");
		
		db.execSQL("insert into muscles values(1, \"Pi\", 1);");
		db.execSQL("insert into muscles values(2, \"Tri\", 2);");
		db.execSQL("insert into muscles values(3, \"Chest\", 2);");
		db.execSQL("insert into muscles values(4, \"Shoulders\", 3);");
		db.execSQL("insert into muscles values(5, \"Back\", 4);");
		db.execSQL("insert into muscles values(6, \"Legs\", 2);");
		db.execSQL("insert into muscles values(7, \"Belly\", 6);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
