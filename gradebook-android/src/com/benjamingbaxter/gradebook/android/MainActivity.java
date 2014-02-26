package com.benjamingbaxter.gradebook.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.benjamingbaxter.gradebook.android.dao.ScreenDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.view.MasterDetailFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationBarFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationDrawerFragment;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Course;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MasterDetailFragment.Callbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private SqliteCourseDao courseDao;
    
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseDao = new SqliteCourseDao(new ScreenDbHelper(getApplicationContext()));
        //when calling setContentView, the phonelayout inflator tries to create 
        //the drawer fragment and in the oncreate of the drawer fragment
        //it tries to select the first item in the list. Thus, we need the 
        //dao to be created before the fragment
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        
        
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                createDrawerMenu());
    }

	private String[] createDrawerMenu() {
		List<String> titles = new ArrayList<String>();
        Query<Course> query = courseDao.findAll();
        while( query.next() ) {
        	titles.add(query.current().getTitle());
        }
        titles.add(getString(R.string.action_add_course));
		return titles.toArray(new String[titles.size()]);
	}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
//    	Fragment fragment = null;
    	if( position < courseDao.findAll().count() ) {
    		//course fragment with course loaded into context...
    	} else {
    		//must have selected to add new course...
    		String[] titles = createDrawerMenu();
    		List<String> ts = new ArrayList<String>();
    		ts.add("New course");
    		ts.addAll(Arrays.asList(titles));
    		titles = ts.toArray(new String[ts.size()]);
    		if( mNavigationDrawerFragment != null ) {
	    		mNavigationDrawerFragment.setUp(
	                    R.id.navigation_drawer,
	                    (DrawerLayout) findViewById(R.id.drawer_layout),
	                    titles);
    		}
    	}
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
    }

    @Override
    public void onSectionAttached(MasterDetailFragment fragment) {
    	if (fragment instanceof NavigationBarFragment) {
    		mTitle = ((NavigationBarFragment)fragment).getTitle();
    	} else {
    		mTitle = getString(R.string.app_name);
    	}
    }
    
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onBackPressed() {
    	if( ! getSupportFragmentManager().getFragments().isEmpty() ) {
    		//TODO: add a listener maybe for the master details frag
    		//to better handle this popping off stacks
    		//it will work for tablets because there is no stack...
    		//maybe it is fine the way it is and will handle all cases?
    		//too tired to finish thinking though...
    		//just first instinct thinks that the master details frag
    		//should handle the popping, not the weird way to access it
    		//from the main activity.
    		for( Fragment f : getSupportFragmentManager().getFragments() ) {
    			//the first one is the drawer... so kludgey :(
    			//what happens if there is more than 1 master detail frag 
    			//in the manager??
    			if( f instanceof MasterDetailFragment ) {
	    			FragmentManager fm = f.getChildFragmentManager();
	    			if (fm.getBackStackEntryCount() > 0) {
	    				Log.i("MainActivity", "popping backstack");
	    				fm.popBackStack();
	    			} else {
	    				Log.i("MainActivity", "nothing on backstack, calling super");
	    				super.onBackPressed();  
	    			}
	    			return;
    			}
    		}
    	} else {
    		super.onBackPressed();
    	}
    }
}
