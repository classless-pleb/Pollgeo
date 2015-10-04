package ndejaco.pollgeo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import ndejaco.pollgeo.Model.Poll;
import ndejaco.pollgeo.Model.PollActivity;


/**
 * Created by Nicholas on 9/28/2015.
 */


public class HomeViewAdapter extends ArrayAdapter<Poll> {

    private Context mContext;
    private List<Poll> mPolls;
    private Poll poll;


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
                addVoteActivity(mPolls.get(position), 0);
            }
        });

        option2button.setTag(position);
        option2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 1);
                addVoteActivity(mPolls.get(position), 1);
            }
        });

        option3button.setTag(position);
        option3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 2);
                addVoteActivity(mPolls.get(position), 2);
            }
        });


        option4button.setTag(position);
        option4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 3);
                addVoteActivity(mPolls.get(position), 3);
            }
        });

        votes1.setTag(position);
        votes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "0");
            }
        });

        votes2.setTag(position);
        votes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "1");
            }
        });

        votes3.setTag(position);
        votes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "2");
            }
        });

        votes4.setTag(position);
        votes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "3");
            }
        });


        return v;

    }

    private void navigateToVoterView(Poll aPoll, String optionNumber) {
        Intent intent = new Intent(mContext, VoterViewActivity.class);
        intent.putExtra("Poll", aPoll.getObjectId());
        intent.putExtra("option number", optionNumber);
        mContext.startActivity(intent);
    }


    private void addVote(Poll thePoll, int i) {
        thePoll.setOptionCount(i);
        thePoll.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    updateData();
                }
            }
        });
    }

    private void addVoteActivity(Poll thePoll, int i) {
        PollActivity aVote = new PollActivity();
        aVote.setFromUser(ParseUser.getCurrentUser());
        aVote.setOption(i);
        aVote.setPollId(thePoll.getObjectId());
        aVote.setType("Vote");
        aVote.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //updateData();
                }
            }
        });
    }


    private void updateData() {
        ParseQuery<Poll> query = new ParseQuery<Poll>("Poll");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Poll>() {

            @Override
            public void done(List<Poll> polls, com.parse.ParseException e) {
                if (polls != null) {
                    mPolls = polls;
                    notifyDataSetChanged();
                }
            }
        });
    }
}
