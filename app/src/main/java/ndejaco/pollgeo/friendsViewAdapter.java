package ndejaco.pollgeo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import ndejaco.pollgeo.Model.Group;

/**
 * Created by Matthew on 10/25/2015.
 * used in MakeGroupActivity for bringing up the friends list of user
 */
public class friendsViewAdapter extends ArrayAdapter<ParseUser> {


    // Private Context that will be used to do an intent
    private Context mContext;
    // Private List of users friends to hold our data
    private List<ParseUser> friends;
//    private TextView friendName;
//    private ProfilePictureView fbPhoto;
    private Group currGroup;
//    // ParseUser variable for current friend
//    private ParseUser friend;
    private boolean clicked;


    private static final String TAG = friendsViewAdapter.class.getSimpleName();


    // Sets instance variables and calls super class constructor
    public friendsViewAdapter(Context context, List<ParseUser> users, Group group) {
        super(context, R.layout.friends_list_item, users);
        this.mContext = context;
        this.friends = users;
        Log.d(TAG, "FRIENDS VIEW ADAPTER REACHED");
        for(ParseUser pu: friends) {
            Log.d(TAG, "Adding " + pu.get("name") + " In ADAPTER");
        }
        this.currGroup = group;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        // If the view passed is null it inflates the home list item view to create a new one
        if (v == null) {
            v = View.inflate(getContext(), R.layout.friends_list_item, null);
            Log.d(TAG, "FRIENDS VIEW being inflated");
        }

        Log.d(TAG, "GROUP NAME: " + currGroup.getName());




        // ParseUser variable for current friend
        ParseUser friend;

        boolean clicked = false;
        // set up elements from layout
        TextView friendName = (TextView) v.findViewById(R.id.userName);
        ProfilePictureView fbPhoto = (ProfilePictureView) v.findViewById(R.id.fbPhoto);
        fbPhoto.setPresetSize(ProfilePictureView.SMALL);
        final Button addButton = (Button) v.findViewById(R.id.addFriend);
        addButton.setEnabled(true);
        addButton.setClickable(true);

        try {
            friend = friends.get(position).fetchIfNeeded();
            if (friend != null) {
                Log.d(TAG, "ADAPTER, friend name: " + friend.getString("name"));
                if (friendName != null) {
                    Log.d(TAG, "ADAPTER, friend name not null");
                    friendName.setText((String) friend.getString("name"));
                }
                String profileId = friend.getString("facebookId");
                if (profileId != null && fbPhoto != null){
                    Log.d(TAG, "ADAPTER, profile Id and fbPhoto not null");
                    fbPhoto.setProfileId((String) friend.getString("facebookId"));
                }
                // set up addMember button
                addButton.setTag(position);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag(); // get where the friend is
                        ParseUser user = friends.get(position); // friend being added
                        insertMember(user); // insert friend to group
                        addButton.setEnabled(false); //dont want user to spam add same friend
                        addButton.setClickable(false);
                        addButton.setText(user.getString("name") + " Added");


                    }
                });



            }

        } catch (ParseException e) {
            e.printStackTrace();
        }





        return v;
    }

    /*
    addMember adds a ParseUser to the group
     */
    public void insertMember(ParseUser user) {
        Log.d(TAG, user.getString("name") + " added to Group");
        Log.d(TAG, currGroup.getName() + " is the Group");
        currGroup.addMember(user);
        currGroup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });

    }

}
