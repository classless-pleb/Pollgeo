package ndejaco.pollgeo;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
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

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ndejaco.pollgeo.Model.Poll;

public class HomeListActivity extends ListActivity {

    private static final String TAG = HomeListActivity.class.getSimpleName();
    private Button create_button;
    private HomeViewAdapter mHomeViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);
        ListView lv = getListView();


        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        } else {
            Log.i(TAG, currentUser.getUsername());
        }


        create_button = (Button) findViewById(R.id.makePoll);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMakePoll();
            }
        });


        mHomeViewAdapter = new HomeViewAdapter(this, new ArrayList<Poll>());
        setListAdapter(mHomeViewAdapter);

        updateData();

    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void updateData() {
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


    private void navigateToMakePoll() {
        Intent intent = new Intent(this, MakePollActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void updateHomeList() {
        mHomeViewAdapter.notifyDataSetChanged();

    }


}