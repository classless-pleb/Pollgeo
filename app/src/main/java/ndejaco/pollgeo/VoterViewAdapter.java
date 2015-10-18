package ndejaco.pollgeo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

import ndejaco.pollgeo.Model.PollActivity;

/**
 * Created by Nicholas on 10/1/2015.
 */

public class VoterViewAdapter extends ArrayAdapter<PollActivity> {
    private List<PollActivity> mVoters;

    public VoterViewAdapter(Context context, List<PollActivity> voters) {
        super(context, R.layout.voter_view_item, voters);
        mVoters = voters;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.voter_view_item, null);
        }

        // Gets the username from the user that voted and sets the text to the username

        ParseUser user = mVoters.get(position).getFromUser();
        TextView userText = (TextView) v.findViewById(R.id.username);

        try {
            user = user.fetchIfNeeded();
            userText.setText(user.getUsername());
        }catch(Exception e){

        }


        return v;
    }
}
