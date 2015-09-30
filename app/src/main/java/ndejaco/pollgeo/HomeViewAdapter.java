package ndejaco.pollgeo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.util.List;

import ndejaco.pollgeo.Model.Poll;


/**
 * Created by Nicholas on 9/28/2015.
 */


public class HomeViewAdapter extends ArrayAdapter<Poll> {

    private Context mContext;
    private List<Poll> mPolls;
    private Poll poll;
    private int thePosition;


    public HomeViewAdapter(Context context, List<Poll> objects) {
        super(context, R.layout.home_list_item, objects);
        this.mContext = context;
        this.mPolls = objects;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.home_list_item, null);
        }

        poll = mPolls.get(position);
        thePosition = position;


        TextView title = (TextView) v.findViewById(R.id.title);
        TextView option1 = (TextView)v.findViewById(R.id.option1);
        TextView option2 = (TextView)v.findViewById(R.id.option2);
        TextView option3 = (TextView)v.findViewById(R.id.option3);
        TextView option4 = (TextView)v.findViewById(R.id.option4);

        Button option1button = (Button) v.findViewById(R.id.option1button);
        Button option2button = (Button) v.findViewById(R.id.option2button);
        Button option3button = (Button) v.findViewById(R.id.option3button);
        Button option4button = (Button) v.findViewById(R.id.option4button);

        title.setText((String) poll.getTitle());

        option1.setText((String)  poll.getOption(0));
        option2.setText((String)  poll.getOption(1));
        option3.setText((String)  poll.getOption(2));
        option4.setText((String)  poll.getOption(3));

        TextView votes1 = (TextView)v.findViewById(R.id.votes1);
        TextView votes2 = (TextView)v.findViewById(R.id.votes2);
        TextView votes3 = (TextView)v.findViewById(R.id.votes3);
        TextView votes4 = (TextView)v.findViewById(R.id.votes4);

        votes1.setText((String) (poll.getOptionVotes(0)  + ""));
        votes2.setText((String) (poll.getOptionVotes(1) + ""));
        votes3.setText((String) (poll.getOptionVotes(2) + ""));
        votes4.setText((String) (poll.getOptionVotes(3) + ""));

        option1button.setTag(position);
        option1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 0);
            }
        });

        option2button.setTag(position);
        option2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 1);
            }
        });

        option3button.setTag(position);
        option3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 2);
            }
        });


        option4button.setTag(position);
        option4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 3);
            }
        });



        return v;

    }




    private void addVote(Poll thePoll, int i) {
       thePoll.setOptionCount(i);
        thePoll.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    notifyDataSetChanged();
                }
            }
        });
    }
}
