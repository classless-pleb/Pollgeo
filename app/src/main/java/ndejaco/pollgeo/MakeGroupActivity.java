package ndejaco.pollgeo;

        import android.app.AlertDialog;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.parse.FindCallback;
        import com.parse.ParseException;
        import com.parse.ParseQuery;
        import com.parse.ParseUser;
        import com.parse.SaveCallback;

        import java.util.List;

        import ndejaco.pollgeo.Model.Group;

/**
 * Created by Matthew on 10/13/2015.
 * MakeGroupActivity is responsible for making a group
 */
public class MakeGroupActivity extends AppCompatActivity {

    // set up element variables for user interactions with make_group.xml
    private TextView groupCreateMessage; // text that lets the user know if they have created a group successfully or unsuccessfuly
    private TextView inviteMemberText; // text field that helps guide the user of how to work activity
    private TextView memberInviteStatus; // this text field lets the user know if they have successfully added a user to the group
    private EditText groupTitle; // groupTitle will be the users choice for the group name
    private EditText userNameEntry; // this text bar will take in the username of a user that is to be added to the group
    private Button createGroupButton; // button when once clicked, creates a group
    private Button addMemberButton; // button will add the username given is userNameEntry to the group as a member
    private Group currentGroup; // the current group being created in this certain activity
    private Button doneButton; // button that will return the user to the group view activity
    // make private string variables for the groupCreateMessage.
    private String groupCreateMessageSuccess = "You have successfully created group";
    private String getGroupCreateMessageFail = "Your group could not be created";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_group);

        // Gets current user
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Set up textFields
        groupTitle = (EditText) findViewById(R.id.userTitleText);
        groupCreateMessage = (TextView) findViewById(R.id.groupCreateMessage);
        groupCreateMessage.setVisibility(View.INVISIBLE);
        inviteMemberText = (TextView) findViewById(R.id.inviteMemberText);
        inviteMemberText.setVisibility(View.INVISIBLE);
        userNameEntry = (EditText) findViewById(R.id.userNameEntry);
        userNameEntry.setVisibility(View.INVISIBLE);
        memberInviteStatus = (TextView) findViewById(R.id.memberInviteStatus);
        memberInviteStatus.setVisibility(View.INVISIBLE);
        //The rest of the elements will be set to invisible and only made visible when group successfully created
        String groupCreateMessageSuccess = "You have successfully created a group";
        groupCreateMessage = (TextView) findViewById(R.id.groupCreateMessage);




        // set listener for make group button
        createGroupButton = (Button) findViewById(R.id.makeGroupButton);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupButton.setEnabled(false); //dont want user to spam click the button
                createGroupButton.setClickable(false);
                navigateToCreateGroup();
            }
        });

        //Initialize the addMemberButton
        addMemberButton = (Button) findViewById(R.id.makeGroupButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMemberButton.setEnabled(false); //dont want user to spam click the button
                addMemberButton.setClickable(false);
                addMember();

            }
        });
        addMemberButton.setVisibility(View.INVISIBLE); // addMemberButton becomes visible once the group is created

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton.setEnabled(false); //dont want user to spam click the button
                doneButton.setClickable(false);
                navigateToGroupActivity(); // go to method to change windows


            }
        });


    }


    /*
    navigateToGroupActivity exits the make group activity and its view and moves the user back to the group view
     */
    public void navigateToGroupActivity(){
        Intent intent = new Intent(this, GroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*
    navigateToCreateCroup is called when the create group button is clicked. This method will set up creating a group
     */
    public void navigateToCreateGroup() {
        //Grab the name the user gave for the group name
        String groupTitleString = groupTitle.getText().toString().trim();

        // If either title name is empty, pop up an alert to the user
        if (groupTitleString.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroupActivity.this);
            builder.setMessage("A Group must have a name! Please enter a name for the group before creating").setTitle("Oops!").
                    setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else { //create a group, and add the user who created the group to the group
            //create the group
            ParseUser creator = ParseUser.getCurrentUser(); //get the user who created the group
            currentGroup = createGroup(groupTitleString, creator);

            // save the group to the database
            currentGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                }
            });

            //make visible the rest of the elements of make_group
            groupCreateMessage.setText(groupCreateMessageSuccess + ": " + groupTitleString);
            groupCreateMessage.setVisibility(View.VISIBLE);
            inviteMemberText.setVisibility(View.VISIBLE);
            userNameEntry.setVisibility(View.VISIBLE);
            addMemberButton.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.VISIBLE);


        }
    }


    /*
        createGroup takes in a string which is to be the name of a group and list of usernames for the users to be in the
        group and creates a group, and returns that group
     */
    private Group createGroup(String groupName, ParseUser user){
        Group group = new Group();
        group.setTitle(groupName);
        group.addMember(user);
        return group;
    }

    /*
    addMember is called when the add member button is clicked. The method attempts to find a registered PollGeo user in the
    Parse database based of the username given in the userNameEntry text box from make_group.xml
     */
    private void addMember() {

        // grab the username the user provides us in the userNameEntry element
        String userName = userNameEntry.getText().toString().trim();

        // newMember will be used if a query successfully finds the user specified to add
        //query the string representing the user name in the _user table in Parse
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    //query was successful

                    ParseUser newMember = objects.get(0); // newMember represents the new ParsUser being added to the group

                    // check if the user is already in the group first
                    List<ParseUser> members = currentGroup.getMembers();
                    for (ParseUser user: members) {
                        if (newMember == user){
                            // member trying to be added is already in the group
                            memberInviteStatus.setText("The person you are trying to add is already in the group, try a different username");
                            addMemberButton.setEnabled(true);
                            addMemberButton.setClickable(true);
                            return; // exit the addMember() method
                        }
                    }
                    currentGroup.addMember(newMember); // add newMember to the group
                    // display success in the view
                    memberInviteStatus.setText(newMember.getUsername() + " successfully added to " + currentGroup.getTitle() + "!");

                    //update the view
                    addMemberButton.setEnabled(true);
                    addMemberButton.setClickable(true);

                } else {
                    // Something went wrong.
                    // display failure in the view
                    memberInviteStatus.setText("The username given does not exist! Try a different username");

                    addMemberButton.setEnabled(true);
                    addMemberButton.setClickable(true);

                }
            }
        }); // end of query


    } //end of addMember()
}
