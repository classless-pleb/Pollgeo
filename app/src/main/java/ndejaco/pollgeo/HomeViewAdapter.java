package ndejaco.pollgeo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
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

        boolean chartIsEmpty = true;
        for(int i = 0; i < 4; i ++) {
            int votes = poll.getOptionVotes(i);
            if (votes != 0) {
                chartIsEmpty = false;
                entries.add(new Entry((float) votes, i));
                descriptions.add(poll.getOption(i));
            }
        }

        if(!chartIsEmpty){
        PieChart chart = (PieChart) v.findViewById(R.id.chart);
        PieDataSet ds = new PieDataSet(entries,"");
        int colors[] = {Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW};
        ds.setColors(colors);
        PieData pd = new PieData(descriptions,ds);
        chart.setData(pd);
            chart.invalidate();
        }

        // Creates buttons and textviews
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView option1 = (TextView)v.findViewById(R.id.option1);
        TextView option2 = (TextView)v.findViewById(R.id.option2);
        TextView option3 = (TextView)v.findViewById(R.id.option3);
        TextView option4 = (TextView)v.findViewById(R.id.option4);

        Button option1button = (Button) v.findViewById(R.id.option1button);
        Button option2button = (Button) v.findViewById(R.id.option2button);
        Button option3button = (Button) v.findViewById(R.id.option3button);
        Button option4button = (Button) v.findViewById(R.id.option4button);

        //Sets title of poll to current polls title
        title.setText((String) poll.getTitle());

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
        votes1.setText((String) (poll.getOptionVotes(0)  + ""));
        votes2.setText((String) (poll.getOptionVotes(1) + ""));
        votes3.setText((String) (poll.getOptionVotes(2) + ""));
        votes4.setText((String) (poll.getOptionVotes(3) + ""));

        // Sets option1 button tag to store its position in mPolls.
        // Then on click adds vote to correct poll and correct option
        // Also, adds vote activity to link current user with option with poll
        option1button.setTag(position);
        option1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                addVote(mPolls.get(position), 0);
                addVoteActivity(mPolls.get(position), 0);
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
                addVoteActivity(mPolls.get(position), 1);
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
                addVoteActivity(mPolls.get(position), 2);
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
                addVoteActivity(mPolls.get(position), 3);
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

        //returns view
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
        aVote.saveInBackground();
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
