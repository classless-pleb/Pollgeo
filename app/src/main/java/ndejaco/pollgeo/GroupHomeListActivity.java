package ndejaco.pollgeo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ndejaco.pollgeo.Model.Poll;

/**
 * Created by Nicholas on 10/31/2015.
 */
public class GroupHomeListActivity extends ListActivity {

    // TAG used for debugging
    private static final String TAG = GroupHomeListActivity.class.getSimpleName();
    // Button used to create new poll
    private Button create_button;
    private Button logOutButton;
    private DrawerLayout mDrawerLayout;
    private Context mContext;
    private ListView mDrawerList;
    private String[] mSections;

    //Refresh layout swipe
    private PullRefreshLayout swipeLayout;
    //HomeViewAdapter responsible for setting contents of listView
    private HomeViewAdapter mGroupHomeViewAdapter;


    private ParseUser currentUser;
    private ProfilePictureView fbPhoto;
    private String objectId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);

        Intent intent = getIntent();
        objectId = intent.getStringExtra("Group");

        // Gets current user, if null goes to login screen, if not logs current user
        ParseUser current = ParseUser.getCurrentUser();
        if (current == null) {
            navigateToLogin();
        } else {
            currentUser = current;
        }


        // Button listener will navigate to screen where user can make poll
        create_button = (Button) findViewById(R.id.makePoll);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMakePoll();
            }
        });

        logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                navigateToLogin();
            }
        });

        // Sets a blank homeView Adapter with no data
        mGroupHomeViewAdapter = new HomeViewAdapter(this, new ArrayList<Poll>());
        setListAdapter(mGroupHomeViewAdapter);

        // Set up the PullRefreshLayout and add the listener.
        swipeLayout = (PullRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                swipeLayout.setRefreshing(false);
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        fbPhoto = (ProfilePictureView) findViewById(R.id.thumbnail);

        if (currentUser != null) {
            fbPhoto.setPresetSize(ProfilePictureView.LARGE);
            String profileId = currentUser.getString("facebookId");
            if (profileId != null) {
                fbPhoto.setProfileId(currentUser.getString("facebookId"));
            } else {

            }
        }

        // set up the drawer's list view with items and click listener
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mSections = getResources().getStringArray(R.array.sections_array);
        mDrawerList.setAdapter(new DrawerAdapter(this, mSections));

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
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo == null || !activeNetworkInfo.isConnected()){
            create_button.setText("No Internet Connection Detected :(");
        }


        Log.i(TAG, "Starting query");
        ParseQuery<Poll> query = new ParseQuery<Poll>("GroupPoll");
        query.orderByDescending("createdAt");
        query.whereEqualTo("group", objectId);
        query.findInBackground(new FindCallback<Poll>() {

            @Override
            public void done(List<Poll> polls, com.parse.ParseException e) {
                if (polls != null) {
                    mGroupHomeViewAdapter.clear();
                    mGroupHomeViewAdapter.addAll(polls);
                }
            }
        });
    }


    // Private method moves to MakePollActivity when make poll button was clicked.
    private void navigateToMakePoll() {
        Intent intent = new Intent(this, MakePollActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", "group");
        intent.putExtra("groupId", objectId);
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

        if (id == R.id.logOutButton) {
            ParseUser.logOut();
            navigateToLogin();
        }

        return super.onOptionsItemSelected(item);
    }

}
