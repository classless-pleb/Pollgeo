
package ndejaco.pollgeo;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ndejaco.pollgeo.Model.Group;
import ndejaco.pollgeo.Model.Poll;

/**
 * Created by Matthew on 10/11/2015.
 * <p/>
 * GroupActivity is the activity that will handle Group Poll functionality
 * It will display group polls are in and the option to make a group poll
 */
public class GroupActivity extends ListActivity {

    // TAG used for debugging
    private static final String TAG = GroupActivity.class.getSimpleName();
    private Button create_button;
    private Button logOutButton;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mSections;
    private PullRefreshLayout swipeLayout;
    private GroupViewAdapter mGroupViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);

        create_button = (Button) findViewById(R.id.makeGroup);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMakeGroup();
            }
        });

        logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mGroupViewAdapter = new GroupViewAdapter(this, new ArrayList<Group>());
        setListAdapter(mGroupViewAdapter);

        swipeLayout = (PullRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                swipeLayout.setRefreshing(false);
            }
        });


        // set up the drawer's list view with items and click listener
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mSections = getResources().getStringArray(R.array.sections_array);
        mDrawerList.setAdapter(new DrawerAdapter(this, mSections));

        updateData();


    }

    private void updateData() {
        ParseUser current = ParseUser.getCurrentUser();
        ArrayList<ParseUser> users = new ArrayList<ParseUser>();
        users.add(current);
        ParseQuery<Group> query = new ParseQuery<Group>("Group");
        query.whereContainedIn("members", users);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (objects != null) {
                    mGroupViewAdapter.clear();
                    mGroupViewAdapter.addAll(objects);
                }

            }
        });
    }

    /*
    navigateToMakeGroup is called when the makeGroupButton is pressed.
    This button will take the user to the make_group.xmlut to make a group
     */
    private void navigateToMakeGroup() {
        Intent intent = new Intent(this, MakeGroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
