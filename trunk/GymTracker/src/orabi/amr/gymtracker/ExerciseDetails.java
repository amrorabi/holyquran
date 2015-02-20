package orabi.amr.gymtracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseDetails extends Activity {

	private ListItem exeItem;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_details);
		
		Intent i = getIntent();
		
		exeItem = (ListItem) i.getSerializableExtra("exeItem");
		
		TextView exNameLabel = (TextView) findViewById(R.id.exNameLabel);
		exNameLabel.setText(exeItem.name);
		
		ImageView exPhotoDetails = (ImageView) findViewById(R.id.exPhotoDetails);
		exPhotoDetails.setImageBitmap(BitmapFactory.decodeByteArray(exeItem.photo, 0, exeItem.photo.length));
		
		dbHelper = DBHelper.getHelperInstance(ExerciseDetails.this);
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select max_weight from exercises where id = ?", new String[]{"" + exeItem.id});
		TextView maxWeight = (TextView) findViewById(R.id.maxWeight);
		if(cursor.moveToFirst()){
			maxWeight.setText("" + cursor.getInt(0));
		}
		cursor.close();
		
		//events
		ImageButton addMaxBtn = (ImageButton) findViewById(R.id.addMaxBtn);
		addMaxBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TextView maxInputValue = (TextView) findViewById(R.id.maxInputValue);
				if(maxInputValue.getText() != null && maxInputValue.getText().toString().trim().length() > 0){
					dbHelper.getWritableDatabase().execSQL("update exercises set max_weight = " + maxInputValue.getText().toString().trim() + 
							" where id = " + exeItem.id);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_details, menu);
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
