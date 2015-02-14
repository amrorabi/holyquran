package orabi.amr.gymtracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Path.Direction;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import orabi.amr.gymtracker.R;

public class MusclueExercises extends Activity {
	private ListItem item;
	LinearLayout inScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musclue_exercises);
		
		item = (ListItem) getIntent().getSerializableExtra("muscleItem");
		TextView mName = (TextView) findViewById(R.id.muscleName);
		mName.setText(item.name);
		
		DBHelper dbHelper = DBHelper.getHelperInstance(this);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select id, exe_name, photo from exercises where muscle_id = ?", new String[]{"" + item.id}); 
		
		//Exercises list
		if(cursor.getCount() > 0){
	        inScrollView = (LinearLayout)findViewById(R.id.inscrollview);
			
			TextView noItemsMsg = (TextView) findViewById(R.id.noItemsMsg);
			noItemsMsg.setVisibility(View.GONE);
			
			while(cursor.moveToNext()){
				int id = cursor.getInt(0);
				String name = cursor.getString(1);
				byte[] photo = cursor.getBlob(2);
				
				LinearLayout exeItem = new LinearLayout(this);
				if(photo != null){
					 ImageView imageView = new ImageView(this);
				     imageView.setImageResource(R.drawable.ic_launcher);
				     imageView.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
				     exeItem.addView(imageView);
				}
				if(name != null){
					TextView exName = new TextView(this);
					exName.setText(name);
					exeItem.addView(exName);
				}
				inScrollView.addView(exeItem);
			}
			cursor.close();
		}
		
		//Add new button
		Button addNewEx = (Button) findViewById(R.id.addNewEx);
		addNewEx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MusclueExercises.this, AddExerciseActivity.class);
				i.putExtra("muscleItem", item);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.musclue_exercises, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
