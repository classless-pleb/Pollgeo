package ndejaco.pollgeo;

import android.app.ListActivity;
import android.graphics.SweepGradient;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ndejaco.pollgeo.Model.*;

public class HomeListActivity extends ListActivity {

    // TAG used for debugging
    private static final String TAG = HomeListActivity.class.getSimpleName();
    // Button used to create new poll
    private Button create_button;
    //Refresh layout swipe
    private PullRefreshLayout swipeLayout;
    //HomeViewAdapter responsible for setting contents of listView
    private HomeViewAdapter mHomeViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);

        // Gets current user, if null goes to login screen, if not logs current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }


        // Button listener will navigate to screen where user can make poll
        create_button = (Button) findViewById(R.id.makePoll);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMakePoll();
            }
        });

        // Sets a blank homeView Adapter with no data
        mHomeViewAdapter = new HomeViewAdapter(this, new ArrayList<Poll>());
        setListAdapter(mHomeViewAdapter);

        // Set up the PullRefreshLayout and add the listener.
        swipeLayout = (PullRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                swipeLayout.setRefreshing(false);
            }
        });

        // Updates the data of homeViewAdapter by querying database.
        updateData();

    }

    // Private method will navigate to login screen if current user is null
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Updates the data and sets HomeViewAdapter as data
    public void updateData() {

        // Queries poll data and orders by most recent polls. Finds in background.
        ParseQuery<Poll> query = new ParseQuery<Poll>("Poll");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Poll>() {

            @Override
            public void done(List<Poll> polls, com.parse.ParseException e) {
                if (polls != null) {
                    mHomeViewAdapter.clear();
                    mHomeViewAdapter.addAll(polls);
                }
            }
        });
    }


    // Private method moves to MakePollActivity when make poll button was clicked.
    private void navigateToMakePoll() {
        Intent intent = new Intent(this, MakePollActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            navigateToLogin();
        }

        return super.onOptionsItemSelected(item);
    }

}
