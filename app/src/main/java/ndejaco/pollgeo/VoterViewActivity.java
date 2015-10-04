package ndejaco.pollgeo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import ndejaco.pollgeo.Model.Poll;
import ndejaco.pollgeo.Model.PollActivity;


public class VoterViewActivity extends ListActivity {

    private static final String TAG = VoterViewActivity.class.getSimpleName();

    private VoterViewAdapter mVoterViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_view);
        ListView lv = getListView();

         // Gets intent passed to this activity
         Intent passed = getIntent();

         // Intent includes poll id of poll and option number of option we wish to return votes from
         String thePoll = (String) passed.getStringExtra("Poll");
         String optionNumber = passed.getStringExtra("option number");

         //Log.i(TAG, thePoll.getTitle());
         Log.i(TAG, optionNumber);

        // Sets empty voter view adapter but then calls updateData to query for data.
        mVoterViewAdapter = new VoterViewAdapter(this, new ArrayList<PollActivity>());
        setListAdapter(mVoterViewAdapter);
        updateData(thePoll, optionNumber);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voter_view, menu);
        return true;
    }

    private void updateData(String thePoll, String optionNumber) {
        // Query returns all votes of the option number matching the optionNumber that was passed on the
        // poll matching the poll id that was passed.
        // i.e, we should return all votes on the poll with objectID = thePoll with option = to optionNumber

        ParseQuery<PollActivity> query = new ParseQuery<PollActivity>("PollActivity");
        query.whereMatches("Poll", thePoll);
        query.whereMatches("option", "option" + optionNumber);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<PollActivity>() {

            @Override
            public void done(List<PollActivity> voters, com.parse.ParseException e) {
                if (voters != null) {
                    // Adds the data to the VoterViewList
                    mVoterViewAdapter.clear();
                    mVoterViewAdapter.addAll(voters);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
