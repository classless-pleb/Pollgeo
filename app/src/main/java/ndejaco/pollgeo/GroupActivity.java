
package ndejaco.pollgeo;

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.parse.ParseUser;

/**
 * Created by Matthew on 10/11/2015.

 GroupActivity is the activity that will handle Group Poll functionality
 It will display group polls are in and the option to make a group poll
 */
public class GroupActivity extends ListActivity {

    // TAG used for debugging
    private static final String TAG = GroupActivity.class.getSimpleName();

    private Button myGroupsButton; // Button to show or hide user's Group list view
    private Button groupInvitesButton; //Button to hide or show users invitations to groups
    private Button makeGroupButton; // Button to make a group
    private RelativeLayout groupsLayout; // Layout that displays users groups
    private RelativeLayout invitesLayout; //Layout the displays users invites to join groups



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // Gets current user, if null goes to login screen, if not logs current user
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Set Relative Layouts
        groupsLayout = (RelativeLayout) findViewById(R.id.groupListLayout);
        invitesLayout = (RelativeLayout) findViewById(R.id.groupInvitesLayout);
        invitesLayout.setVisibility(View.GONE); //Set to GONE(invisible and doesnt take up space) user has to click on invite button to make visible

        // Button listener will navigate to screen where user can make poll
        makeGroupButton = (Button) findViewById(R.id.makeGroupButton);
        makeGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMakeGroup();
            }
        });

        // Button listener will change the screen to where the user can view invites
        groupInvitesButton = (Button) findViewById(R.id.makeGroupButton);
        groupInvitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateGroupInvites();
            }
        });


    }

    /*
    navigateToMakeGroup is called when the makeGroupButton is pressed.
    This button will take the user to the make_group.xmlut to make a group
     */
    private void navigateToMakeGroup() {
        Intent intent = new Intent(this, MakeGroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*
    navigateToGroupInvites() is called on when the group Invites button is clicked, this method will
    show the users his group invites by making the groupInvites list visible, if the invites are already visible
    then the button will hide the visibility of the group invites layout.
     */
    private void navigateGroupInvites() {

        if (invitesLayout.getVisibility() == View.GONE) {
            // Its visible
        } else {
            // Either gone or invisible
        }

    }

}
