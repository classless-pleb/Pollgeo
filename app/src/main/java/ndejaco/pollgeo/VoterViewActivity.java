package ndejaco.pollgeo;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private VoterViewAdapter mVoterViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_view);
        ListView lv = getListView();

         mVoterViewAdapter = new VoterViewAdapter(this, new ArrayList<PollActivity>());
         setListAdapter(mVoterViewAdapter);

         updateData();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voter_view, menu);
        return true;
    }

    private void updateData() {
        ParseQuery<PollActivity> query = new ParseQuery<PollActivity>("PollActivity");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<PollActivity>() {

            @Override
            public void done(List<PollActivity> voters, com.parse.ParseException e) {
                if (voters != null) {
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
