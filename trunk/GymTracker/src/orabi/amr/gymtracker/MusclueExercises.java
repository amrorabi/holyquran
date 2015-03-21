package orabi.amr.gymtracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import orabi.amr.gymtracker.util.DBUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusclueExercises extends Activity {
	private ListItem item;
	LinearLayout inScrollView;
	private Integer exeId;
	private String exeName;
	private String exePhotoPath;
	private DBHelper dbHelper;
	public static boolean shouldRefresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
//	        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
//	    }
		
		shouldRefresh = false;
		
		setContentView(R.layout.activity_musclue_exercises);
		
		item = (ListItem) getIntent().getSerializableExtra("muscleItem");
		TextView mName = (TextView) findViewById(R.id.muscleName);
		mName.setText(item.name);
		
		dbHelper = DBHelper.getHelperInstance(this);
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select id, exe_name, photo, last_time from exercises where muscle_id = ?", new String[]{"" + item.id});
		inScrollView = (LinearLayout)findViewById(R.id.inscrollview);
		
		//Exercises list
		if(cursor.getCount() > 0){
			
			TextView noItemsMsg = (TextView) findViewById(R.id.noItemsMsg);
			noItemsMsg.setVisibility(View.GONE);
			
			while(cursor.moveToNext()){
				
				exeId = cursor.getInt(0);
				exeName = cursor.getString(1);
				exePhotoPath = cursor.getString(2);
				
				GridLayout exeItem = new GridLayout(this);
				exeItem.setColumnCount(3);
				exeItem.setRowCount(1);
				exeItem.setMinimumHeight(180);
				exeItem.setId(exeId);
				
				String lastTime = cursor.getString(3);
				if(lastTime != null){
					try {
						Date lt = DBUtil.formatter.parse(lastTime);
						Calendar current = Calendar.getInstance();
						current.set(Calendar.HOUR, 0);
						current.set(Calendar.MINUTE, 0);
						current.set(Calendar.SECOND, 0);
						current.set(Calendar.MILLISECOND, 0);
						if(lt.compareTo(current.getTime()) == 0)
							exeItem.setBackgroundColor(Color.LTGRAY);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				int index = 0;
				
				if(exePhotoPath != null){
					try{
						 FileInputStream fi = openFileInput(exePhotoPath);
						 ImageView imageView = new ImageView(this);
					     imageView.setImageResource(R.drawable.ic_launcher);
					     imageView.setImageBitmap(BitmapFactory.decodeFileDescriptor(fi.getFD()));
					     imageView.setLayoutParams(new LayoutParams(150, 150));
					     imageView.setAdjustViewBounds(true);
					     imageView.setMaxHeight(150);
					     imageView.setMaxWidth(150);
					     exeItem.addView(imageView, index);
					}catch(IOException e){
						e.printStackTrace();
					}
				    index++;
				}
				if(exeName != null){
					TextView exName = new TextView(this);
					exName.setText(exeName);
					exName.setTextSize(20);
					exName.setLayoutParams(new LayoutParams(700, LayoutParams.WRAP_CONTENT));
					exName.setGravity(Gravity.CENTER_HORIZONTAL);
					exeItem.addView(exName, index);
					index++;
				}
				ImageButton imgBtn = new ImageButton(this);
				imgBtn.setId(exeId);
				imgBtn.setScaleType(ScaleType.CENTER_CROP);
				imgBtn.setPadding(0, 0, 0, 0);
				imgBtn.setImageResource(R.drawable.delete_icon);
				exeItem.addView(imgBtn, index);
				
				imgBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View btn) {
						new AlertDialog.Builder(MusclueExercises.this)
				          .setTitle("Warning")
				          .setMessage("deleting this exercise ?")
				          .setCancelable(false)
				          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				              @Override
				              public void onClick(DialogInterface dialog, int which) {
				            	  dbHelper.getWritableDatabase().execSQL("delete from exercises where id = " + btn.getId());
				            	  //refresh view
				            	  finish();
				            	  startActivity(getIntent());
				              }
				          })
				          .setNegativeButton("No", new DialogInterface.OnClickListener() {
				              @Override
				              public void onClick(DialogInterface dialog, int which) {
				            	  dialog.dismiss();
				              }
				          })
				         .create().show();
					}
				});
				
				exeItem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(MusclueExercises.this, ExerciseDetails.class);
						i.putExtra("exeItemId", arg0.getId());
						startActivity(i);
					}
				});
				inScrollView.addView(exeItem);
			
			}
		}
		cursor.close();
		
		//Add new button
		Button addNewEx = new Button(this);
		addNewEx.setText("Add New Exercise");
		addNewEx.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addNewEx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MusclueExercises.this, AddEditExercise.class);
				i.putExtra("muscleItem", item);
				startActivityForResult(i, Constants.ADD_NEW_EXERCISE);
			}
		});
		inScrollView.addView(addNewEx);
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
	
	@Override
	protected void onRestart() {
		super.onRestart();
		//refresh
		if(shouldRefresh){
			finish();
			startActivity(getIntent());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//refresh after add new exercise
		finish();
		startActivity(getIntent());
	}
}
