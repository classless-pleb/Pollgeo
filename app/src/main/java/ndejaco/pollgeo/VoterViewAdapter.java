package ndejaco.pollgeo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import java.util.List;

import ndejaco.pollgeo.Model.PollActivity;

/**
 * Created by Nicholas on 10/1/2015.
 */

public class VoterViewAdapter extends ArrayAdapter<ParseUser> {
    private List<ParseUser> mVoters;

    public VoterViewAdapter(Context context, List<ParseUser> voters) {
        super(context, R.layout.voter_view_item, voters);
        mVoters = voters;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.voter_view_item, null);
        }

        // Gets the username from the user that voted and sets the text to the username

        TextView userText = (TextView) v.findViewById(R.id.username);
        userText.setText((String) mVoters.get(position).getString("name"));

        ProfilePictureView fbPhoto = (ProfilePictureView) v.findViewById(R.id.fbPhoto);
        fbPhoto.setPresetSize(ProfilePictureView.SMALL);
        fbPhoto.setProfileId((String) mVoters.get(position).getString("facebookId"));


        return v;
    }
}
