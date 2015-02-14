package orabi.amr.gymtracker;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddExerciseActivity extends Activity {
	
	static final int REQUEST_IMAGE_CAPTURE = 1;

	private DBHelper dbHelper;
	private ListItem item;
	private Bitmap imageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_exercise);
		
		imageBitmap = null;
		dbHelper = DBHelper.getHelperInstance(this);
		
		item = (ListItem) getIntent().getSerializableExtra("muscleItem");
		
		//Take photo action
		Button cameraBtn = (Button) findViewById(R.id.cameraBtn);
		cameraBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();
			}
		});
		
		//save all button action
		Button saveAllBtn = (Button) findViewById(R.id.saveAllBtn);
		saveAllBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView exName = (TextView) findViewById(R.id.exName);		//name
				byte[] data = getBitmapAsByteArray(imageBitmap);			//photo
				
//				dbHelper.getWritableDatabase().execSQL(
//						"insert into exercises(exe_name, muscle_id, photo) values(?,?,?)",
//						new String[]{exName.getText().toString(), "" + item.id, "" + data});
				
				ContentValues params = new ContentValues();
				params.put("exe_name", exName.getText().toString());
				params.put("muscle_id", item.id);
				params.put("photo", data);
				long result = dbHelper.getWritableDatabase().insert("exercises", null, params);
				Log.e("inserted row #:", "" + result);
				
				if(result > 0){
					new AlertDialog.Builder(AddExerciseActivity.this)
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
			            	  Intent i = new Intent(AddExerciseActivity.this, MusclueExercises.class);
			  				  i.putExtra("muscleItem", item);
			  				  startActivity(i);
			              }
			          })
			         .create().show();
				}
			}
		});
	}
	
	private byte[] getBitmapAsByteArray(Bitmap bitmap) {
			if(bitmap == null)
				return new byte[]{};
			
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    bitmap.compress(CompressFormat.PNG, 0, outputStream);       
		    return outputStream.toByteArray();
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
		
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        imageBitmap = (Bitmap) extras.get("data");
	        ImageView exPhoto = (ImageView) findViewById(R.id.exPhoto);
	        exPhoto.setImageBitmap(imageBitmap);
	        
	        Log.e("photooooooooo", "" + imageBitmap.getByteCount());
	    }

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
