package orabi.amr.gymtracker;

import java.io.FileInputStream;
import java.io.IOException;

import orabi.amr.gymtracker.util.DBUtil;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseDetails extends Activity {

	private int exeItemId;
	private DBHelper dbHelper;
	private TextView maxWeight;
	private EditText maxInputValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_details);
		
		Intent i = getIntent();
		
		exeItemId = i.getIntExtra("exeItemId", 0);
		
		TextView exNameLabel = (TextView) findViewById(R.id.exNameLabel);
		ImageView exPhotoDetails = (ImageView) findViewById(R.id.exPhotoDetails);
		
		dbHelper = DBHelper.getHelperInstance(this);
		
		Cursor cursor = null;
		cursor = DBUtil.getExercise(dbHelper, exeItemId);
		
		maxWeight = (TextView) findViewById(R.id.maxWeight);
		TextView avgWeight = (TextView) findViewById(R.id.avgWeight);
		EditText notes = (EditText) findViewById(R.id.notes);
		maxInputValue = (EditText) findViewById(R.id.maxInputValue);
		maxInputValue.requestFocus();
		
		if(cursor != null && cursor.moveToFirst()){
			exNameLabel.setText(cursor.getString(0));
			maxWeight.setText("" + cursor.getInt(1));
			avgWeight.setText("" + cursor.getInt(2));
			notes.setText(cursor.getString(3));
			String photoPath = cursor.getString(4);
			if(photoPath != null){
				try {
					 FileInputStream fi = openFileInput(photoPath);
					 exPhotoDetails.setImageBitmap(BitmapFactory.decodeFileDescriptor(fi.getFD()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		cursor.close();
		
		//events
		Button addMaxBtn = (Button) findViewById(R.id.addMaxBtn);
		addMaxBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditText notes = (EditText) findViewById(R.id.notes);
				if(maxInputValue.getText() != null && maxInputValue.getText().toString().trim().length() > 0){
					String mw = maxInputValue.getText().toString().trim();
					int currentMax = (int) Float.parseFloat(mw);
					int prevMax = Integer.parseInt(maxWeight.getText().toString());
					int max = prevMax > currentMax? prevMax : currentMax;
					
					dbHelper.getWritableDatabase().execSQL("update exercises set max_weight = " + max + 
							" , exc_times = (exc_times + 1), avg_weight = ((avg_weight * exc_times +" + currentMax + ") / (exc_times + 1)), "
							+ " notes = '" + notes.getText() + "'" +
							" where id = " + exeItemId);
					
					//refresh view
					finish();
					startActivity(getIntent());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_details, menu);
		menu.clear();
		menu.add("Edit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == 0) {
			Intent i = new Intent(this, AddEditExercise.class);
			i.putExtra("exeItemId", exeItemId);
			startActivityForResult(i, Constants.EDIT_EXERCISE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//refresh after edit info
		finish();
		startActivity(getIntent());
	}
}
