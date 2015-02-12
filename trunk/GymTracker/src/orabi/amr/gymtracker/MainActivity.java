package orabi.amr.gymtracker;

import java.util.ArrayList;
import java.util.List;

import com.example.gymtracker.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
		
		//my code
		SQLiteDatabase db = openOrCreateDatabase("gymTrackerDB", MODE_PRIVATE, null);
		db.execSQL("create table if not exists muscles (id integer primary key autoincrement, muscle_name text not null,"
				+ " day_order integer);"); 
		
		db.execSQL("create table if not exists exercises (id integer primary key autoincrement, description text,"
				+ " max_weight integer, avg_weight integer, exc_times integer, muscle_id integer, "
				+ " foreign key (muscle_id) references muscles(id));");
		
		Cursor query = db.rawQuery("select muscle_name from muscles", null);
		if(query.getCount() == 0){
			db.execSQL("insert into muscles values(1, \"Pi\", 1);");
			db.execSQL("insert into muscles values(2, \"Tri\", 2);");
			db.execSQL("insert into muscles values(3, \"Shoulders\", 3);");
			db.execSQL("insert into muscles values(4, \"Back\", 4);");
			db.execSQL("insert into muscles values(5, \"Legs\", 2);");
			db.execSQL("insert into muscles values(6, \"Belly\", 6);");
			
			query = db.rawQuery("select muscle_name from muscles", null);
		}
		
		List<MuscleListItem> muscleNames = new ArrayList<MuscleListItem>();
		while(query.moveToNext()){
			MuscleListItem item = new MuscleListItem();
			item.id = query.getInt(0);
			item.name = query.getString(1);
			muscleNames.add(item);
		}
		
		MuscleArrayAdapter adapter = new MuscleArrayAdapter(this, android.R.layout.simple_list_item_1, muscleNames);
		ListView mainList = (ListView) findViewById(R.id.musclesList);
		mainList.setAdapter(adapter);
		
		mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(MainActivity.this, MusclueExercises.class);
				i.putExtra("muscleId", id);
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
	
	private class MuscleListItem{
		int id;
		String name;
	}
	
	private class MuscleArrayAdapter extends ArrayAdapter<MuscleListItem> {
		
		private List<MuscleListItem> objects;

	    public MuscleArrayAdapter(Context context, int textViewResourceId,
	        List<MuscleListItem> objects) {
	      super(context, textViewResourceId, objects);
	      this.objects = objects;
	    }

	    @Override
	    public long getItemId(int position) {
	    	return objects.get(position).id;
	    }
	    
	    @Override
	    public MuscleListItem getItem(int position) {
	    	return super.getItem(position);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }

}
