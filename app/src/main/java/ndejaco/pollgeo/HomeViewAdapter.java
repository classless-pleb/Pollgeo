package ndejaco.pollgeo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import ndejaco.pollgeo.Model.Poll;


/**
 * Created by Nicholas on 9/28/2015.
 */


public class HomeViewAdapter extends ParseQueryAdapter<Poll> {
    private static final String TAG = HomeViewAdapter.class.getSimpleName();
    public HomeViewAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Poll>() {
            public ParseQuery<Poll> create() {
                ParseQuery<Poll> polls = new ParseQuery<Poll>("Poll");
                polls.orderByDescending("createdAt");
                return polls;
            }
        });


    }

    @Override
    public View getItemView(Poll poll, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.home_list_item, null);
        }

        super.getItemView(poll, v, parent);

        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText((String) poll.getTitle());

        return v;

    }
}
