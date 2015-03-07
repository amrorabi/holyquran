package orabi.amr.gymtracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import orabi.amr.gymtracker.util.LayoutUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddEditExercise extends Activity {
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_IMAGE_File = 2;

	private int exeItemId;
	private DBHelper dbHelper;
	private ListItem item;
	private Bitmap imageBitmap;
	private TextView exName;
	private ImageView exPhoto;
	private boolean isUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_exercise);
		
		imageBitmap = null;
		dbHelper = DBHelper.getHelperInstance(this);
		exName = (TextView) findViewById(R.id.exName);		//name
		exPhoto = (ImageView) findViewById(R.id.exPhoto);   //photo
		
		exeItemId = getIntent().getIntExtra("exeItemId", 0);
		
		if(exeItemId != 0){
			isUpdate = true;
			try {
				LayoutUtil.prepareExerciseDataUI(this, exeItemId, exName, exPhoto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			isUpdate = false;
			item = (ListItem) getIntent().getSerializableExtra("muscleItem");
		}
		
		//Take photo action
		Button cameraBtn = (Button) findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
		});
		
		//Browse action
		Button browseBtn = (Button) findViewById(R.id.browseBtn);
		browseBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AddEditExercise.this, ListFileActivity.class);
				startActivityForResult(i, REQUEST_IMAGE_File);
			}
		});
		
		//save all button action
		Button saveAllBtn = (Button) findViewById(R.id.saveAllBtn);
		saveAllBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				String fileName = null;
				try {
					fileName = writeBitmapToStroage(imageBitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				if(isUpdate){
					updateExercise(fileName);
				}
				else{
					insertNewExercise(fileName);
				}
			}
		});
	}


	protected void updateExercise(String filePath) {
		ContentValues params = new ContentValues();
		params.put("exe_name", exName.getText().toString());
		
		if(filePath != null)
			params.put("photo", filePath);
		
		long result = dbHelper.getWritableDatabase().update("exercises", params, "id = ?", new String[]{"" + exeItemId});
		Log.e("updated row #:", "" + result);

		MusclueExercises.shouldRefresh = true;
		
		setResult(RESULT_OK, null);
		finish();
	}


	protected void insertNewExercise(String filePath) {
		ContentValues params = new ContentValues();
		params.put("exe_name", exName.getText().toString());
		params.put("muscle_id", item.id);
		params.put("photo", filePath);
		params.put("exc_times", 0);
		params.put("avg_weight", 0);
		params.put("max_weight", 0);
		params.put("notes", "");
		long result = dbHelper.getWritableDatabase().insert("exercises", null, params);
		Log.e("inserted row #:", "" + result);
		
		if(result > 0){
			new AlertDialog.Builder(AddEditExercise.this)
	          .setTitle("Done")
	          .setMessage("added successfully")
	          .setCancelable(false)
	          .setPositiveButton("Add another", new DialogInterface.OnClickListener() {
	              @Override
	              public void onClick(DialogInterface dialog, int which) {
	            	  //TODO clear UI fields
	            	  dialog.dismiss();
	              }
	          })
	          .setNegativeButton("Back to List", new DialogInterface.OnClickListener() {
	              @Override
	              public void onClick(DialogInterface dialog, int which) {
	            	  setResult(RESULT_OK, null);
	  				  finish();
	              }
	          })
	         .create().show();
		}
	}


	private String writeBitmapToStroage(Bitmap bitmap) throws FileNotFoundException {
		if(bitmap == null)
			return null;
 
		String fileName = "gt_" + new Date().getTime();
	    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);		//used android method to create file in internal storage
	    bitmap.compress(CompressFormat.PNG, 0, outputStream);
	    
	    return fileName;
	}
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.e("photooooooooo resultttt ", "");
		
		if(resultCode == RESULT_OK){
			if (requestCode == REQUEST_IMAGE_CAPTURE) {
		        Bundle extras = data.getExtras();
		        imageBitmap = (Bitmap) extras.get("data");
		    }
			else{											// if (requestCode == REQUEST_IMAGE_File) {
				Bundle extras = data.getExtras();
		        String photoPath = (String) extras.get("photoPath");
		        imageBitmap = getImageFromFile(photoPath);
			}
			
			Log.e("photooooooooo", "" + imageBitmap.getByteCount());
	        exPhoto.setImageBitmap(imageBitmap);
		}
	}
	
	private Bitmap getImageFromFile(String path){
		File imgFile = new  File(path);
		if(imgFile.exists()){
		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    return myBitmap;
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_exercise, menu);
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
