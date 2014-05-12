package com.amrorabi.holyquran;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.amrorabi.holyquran.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SuraActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	private int suraNumber;
	private ImageView suraView;
	private float xOld, xNew, yOld, yNew, deltaX, deltaY;
	private float suraX;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private void setupSuraView() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException{
		
		Intent myIntent = getIntent();
		suraNumber = myIntent.getIntExtra("suraNumber", 0);		// 0 index
		suraNumber += 1;
		
		suraView = (ImageView) findViewById(R.id.suraView);
		Class cl = R.drawable.class;
		Field f = cl.getDeclaredField("sura_" + suraNumber);
		suraView.setImageResource(f.getInt(null));
		
		suraView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()){
				
					case MotionEvent.ACTION_DOWN:
						xOld = event.getX();
//						yOld = event.getY();
						suraX = suraView.getX();
						break;
						
					case MotionEvent.ACTION_UP:
						xNew = event.getX();
//						yNew = event.getY();
						deltaX = xNew - xOld;
//						deltaY = yNew - yOld;						
						updateSuraNumber(deltaX, deltaY);
						
						Class cl = R.drawable.class;
						try {
							Field f = cl.getDeclaredField("sura_" + suraNumber);
							suraView.setImageResource(f.getInt(null));
							return true;
							
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
						
						break;
						
				    case MotionEvent.ACTION_MOVE:
						float deltaMoving = event.getX() - xOld;
						suraView.setX(suraX + deltaMoving);
						suraX = suraView.getX();
						break;
				}
				
				return false;
			}
		});
	}

	protected void updateSuraNumber(float deltaX, float deltaY) {
		
		if(Math.abs(deltaX) > Math.abs(deltaY)) {
            if(deltaX > 0) 
            	suraNumber -= 1;
            else 
            	suraNumber += 1;
        } 
//		else {
//            if(deltaY > 0) 
//            	suraNumber -= 1;
//            else 
//            	suraNumber += 1;
//        }
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sura);
		setupActionBar();

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		
		try {
			setupSuraView();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
//		delayedHide(100);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
//				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};


}
