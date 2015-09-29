package ndejaco.pollgeo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button option1button;

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
        TextView option1 = (TextView)v.findViewById(R.id.option1);
        TextView option2 = (TextView)v.findViewById(R.id.option2);
        TextView option3 = (TextView)v.findViewById(R.id.option3);
        TextView option4 = (TextView)v.findViewById(R.id.option4);

        option1button = (Button) v.findViewById(R.id.option1button);

        title.setText((String) poll.getTitle());

        option1.setText((String)  poll.getOption(0));
        option2.setText((String)  poll.getOption(1));
        option3.setText((String)  poll.getOption(2));
        option4.setText((String)  poll.getOption(3));

        TextView votes1 = (TextView)v.findViewById(R.id.votes1);
        TextView votes2 = (TextView)v.findViewById(R.id.votes2);
        TextView votes3 = (TextView)v.findViewById(R.id.votes3);
        TextView votes4 = (TextView)v.findViewById(R.id.votes4);

        votes1.setText(poll.getOptionVotes(0)  + "");
        votes2.setText(poll.getOptionVotes(1) + "");
        votes3.setText(poll.getOptionVotes(2) + "");
        votes4.setText(poll.getOptionVotes(3) + "");
        

        return v;

    }
}
