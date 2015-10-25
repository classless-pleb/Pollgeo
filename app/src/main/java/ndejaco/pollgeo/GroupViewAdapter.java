package ndejaco.pollgeo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import java.util.List;

import ndejaco.pollgeo.Model.Group;

/**
 * Created by Nicholas on 10/25/2015.
 */
public class GroupViewAdapter extends ArrayAdapter<Group> {
    private List<Group> mGroups;

    public GroupViewAdapter(Context context, List<Group> voters) {
        super(context, R.layout.voter_view_item, voters);
        mGroups = voters;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.group_list_item, null);
        }

        // Gets the username from the user that voted and sets the text to the username

        TextView userText = (TextView) v.findViewById(R.id.GroupName);

        if (mGroups.get(position) == null) {
            userText.setText((String) mGroups.get(position).getName());
        }


        return v;
    }
}
