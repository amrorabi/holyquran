package orabi.amr.gymtracker;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private ListView mainList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
//	        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
//	    }
		
		setContentView(R.layout.activity_main);
		
		DBHelper dbHelper = DBHelper.getHelperInstance(this);
		
		String query1 = "select id, muscle_name from muscles";
		
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query1, null);
		List<ListItem> muscleNames = new ArrayList<ListItem>();
		while(cursor.moveToNext()){
			ListItem item = new ListItem();
			item.id = cursor.getInt(0);
			item.name = cursor.getString(1);
			muscleNames.add(item);
		}
		
		MuscleArrayAdapter adapter = new MuscleArrayAdapter(this, android.R.layout.simple_list_item_1, muscleNames);
		mainList = (ListView) findViewById(R.id.musclesList);
		mainList.setAdapter(adapter);
		
		mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ListItem muscle = (ListItem) mainList.getItemAtPosition(position);
				Intent i = new Intent(MainActivity.this, MusclueExercises.class);
				i.putExtra("muscleItem", muscle);
				startActivity(i);
				 
			}

		});
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container,
					false);
			return rootView;
		}
	}
	
	private class MuscleArrayAdapter extends ArrayAdapter<ListItem> {
		
		private List<ListItem> objects;

	    public MuscleArrayAdapter(Context context, int textViewResourceId,
	        List<ListItem> objects) {
	      super(context, textViewResourceId, objects);
	      this.objects = objects;
	    }

	    @Override
	    public long getItemId(int position) {
	    	return objects.get(position).id;
	    }
	    
	    @Override
	    public ListItem getItem(int position) {
	    	return super.getItem(position);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }

}
