package ndejaco.pollgeo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import ndejaco.pollgeo.Model.Poll;
import ndejaco.pollgeo.Model.PollActivity;


/**
 * Created by Nicholas on 9/28/2015.
 */


public class HomeViewAdapter extends ArrayAdapter<Poll> {

    // Private Context that will be used to do an intent
    private Context mContext;
    // Private List of polls to hold our data
    private List<Poll> mPolls;
    // Private Poll for current poll
    private Poll poll;
    private static String CHART_ID = "chart";

    private Poll votedPoll;
    private int votedOption;

    private static final String TAG = HomeViewAdapter.class.getSimpleName();


    // Sets instance variables and calls super class constructor
    public HomeViewAdapter(Context context, List<Poll> objects) {
        super(context, R.layout.home_list_item, objects);
        this.mContext = context;
        this.mPolls = objects;
    }


    @Override
    public View getView(int position, View v, ViewGroup parent) {

        // If the view passed is null it inflates the home list item view to create a new one
        if (v == null) {
            v = View.inflate(getContext(), R.layout.home_list_item, null);
        }

        // Saves current poll at the index in the List of Polls
        poll = mPolls.get(position);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for(int i = 0; i < 4; i ++) {
            int votes = poll.getOptionCount(i);
            if (votes != 0) {
                entries.add(new Entry((float) votes, i));
                descriptions.add(poll.getOption(i));
            }
        }

        PieChart chart = (PieChart) v.findViewById(R.id.chart);
        chart.setNoDataText("");
        PieDataSet ds = new PieDataSet(entries,"");
        int colors[] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW};
        ds.setColors(colors);
        PieData pd = new PieData(descriptions,ds);
        chart.setData(pd);
        chart.setDescription("");
        chart.invalidate();

        // Creates buttons and textviews
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView option1 = (TextView)v.findViewById(R.id.option1);
        TextView option2 = (TextView)v.findViewById(R.id.option2);
        TextView option3 = (TextView)v.findViewById(R.id.option3);
        TextView option4 = (TextView)v.findViewById(R.id.option4);

        ImageButton option1button = (ImageButton) v.findViewById(R.id.option1button);
        ImageButton option2button = (ImageButton) v.findViewById(R.id.option2button);
        ImageButton option3button = (ImageButton) v.findViewById(R.id.option3button);
        ImageButton option4button = (ImageButton) v.findViewById(R.id.option4button);

        option1button.setEnabled(true);
        option2button.setEnabled(true);
        option3button.setEnabled(true);
        option4button.setEnabled(true);

        option1button.setBackgroundResource(R.drawable.not_voted);
        option2button.setBackgroundResource(R.drawable.not_voted);
        option3button.setBackgroundResource(R.drawable.not_voted);
        option4button.setBackgroundResource(R.drawable.not_voted);


        //Sets title of poll to current polls title
        title.setText((String) poll.getTitle());
        title.setGravity(Gravity.CENTER);
        ParseUser pu = poll.getUser();
        try{
            pu.fetchIfNeeded();
            title.setText(title.getText() + "\nby " + poll.getUser().getUsername());
        }catch(Exception e){

        }


        // Sets option texts
        option1.setText((String)  poll.getOption(0));
        option2.setText((String)  poll.getOption(1));
        option3.setText((String)  poll.getOption(2));
        option4.setText((String)  poll.getOption(3));

        TextView votes1 = (TextView)v.findViewById(R.id.votes1);
        TextView votes2 = (TextView)v.findViewById(R.id.votes2);
        TextView votes3 = (TextView)v.findViewById(R.id.votes3);
        TextView votes4 = (TextView)v.findViewById(R.id.votes4);

        // Sets votes texts
        votes1.setText((String) (poll.getOptionCount(0)  + ""));
        votes2.setText((String) (poll.getOptionCount(1) + ""));
        votes3.setText((String) (poll.getOptionCount(2) + ""));
        votes4.setText((String) (poll.getOptionCount(3) + ""));

        // Sets option1 button tag to store its position in mPolls.
        // Then on click adds vote to correct poll and correct option
        // Also, adds vote activity to link current user with option with poll
        option1button.setTag(position);
        option1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 0);
            }
        });

        // Sets option2 button tag to store its position in mPolls.
        // Then on click adds vote to correct poll and correct option
        // Also, adds vote activity to link current user with option with poll

        option2button.setTag(position);
        option2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 1);
            }
        });

        // Sets option3 button tag to store its position in mPolls.
        // Then on click adds vote to correct poll and correct option
        // Also, adds vote activity to link current user with option with poll

        option3button.setTag(position);
        option3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 2);
            }
        });

        // Sets option4 button tag to store its position in mPolls.
        // Then on click adds vote to correct poll and correct option
        // Also, adds vote activity to link current user with option with poll

        option4button.setTag(position);
        option4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 3);
            }
        });


        // Sets vote1 tag position in order to get correct poll onClick
        // On click navigates to voter view to show which users voted on that option in this poll
        votes1.setTag(position);
        votes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "0");
            }
        });

        // Sets vote2 tag position in order to get correct poll onClick
        // On click navigates to voter view to show which users voted on that option in this poll
        votes2.setTag(position);
        votes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "1");
            }
        });

        // Sets votes3 tag position in order to get correct poll onClick
        // On click navigates to voter view to show which users voted on that option in this poll

        votes3.setTag(position);
        votes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "2");
            }
        });


        // Sets vote4 tag position in order to get correct poll onClick
        // On click navigates to voter view to show which users voted on that option in this poll
        votes4.setTag(position);
        votes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                navigateToVoterView(mPolls.get(position), "3");
            }
        });

        /*
        if(pa != null){
            int pos = determinePositionVoted(pa);
            switch(pos){
                case 0:
                    option1button.setEnabled(false);
                    option1button.setBackgroundResource(R.drawable.voted);
                    break;
                case 1:
                    option2button.setEnabled(false);
                    option2button.setBackgroundResource(R.drawable.voted);
                    break;
                case 2:
                    option3button.setEnabled(false);
                    option3button.setBackgroundResource(R.drawable.voted);
                    break;
                case 3:
                    option4button.setEnabled(false);
                    option4button.setBackgroundResource(R.drawable.voted);
                    break;
            }
        }
        */

        //returns view
        return v;

    }

    private void navigateToVoterView(Poll aPoll, String optionNumber) {
        Intent intent = new Intent(mContext, VoterViewActivity.class);
        intent.putExtra("Poll", aPoll.getObjectId());
        intent.putExtra("option number", optionNumber);
        mContext.startActivity(intent);
    }


    /*
    addVote takes in the poll which is being voted and i, which rperesnts the option which was chosen.
    The option that was voted on gets incremented and if the user has already voted on the poll, any of their previous votes
    are deleted and decremented from their respective option count
     */
    private void addVote(Poll thePoll, int i) {
        //Check to see if the current user has already voted on this poll
        votedPoll = thePoll;
        votedOption = i;

        //determinePositionVoted does our null checking.
        //int pastOption = determinePositionVoted(activity);
        boolean flag = removePreviousVotes(votedPoll, votedPoll.getTotalOptions(), votedOption);
        if (flag) {
            votedPoll.setOptionCount(votedOption, ParseUser.getCurrentUser());
        }
        votedPoll.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                updateData();
            }
        });


    }

    private boolean removePreviousVotes(Poll votedPoll, int optionCount, int ignore) {
        for (int i = 0; i < optionCount; i++) {
            if (votedPoll.getOptionVoters(i) != null) {
                if (votedPoll.getOptionVoters(i).contains(ParseUser.getCurrentUser())) {
                    if (i == ignore) {
                        return false;
                    }
                    else {
                        votedPoll.removeUser(i, ParseUser.getCurrentUser());
                    }
                }
            }


        }

        return true;
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
