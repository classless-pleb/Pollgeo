package ndejaco.pollgeo;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import ndejaco.pollgeo.Model.Group;
import ndejaco.pollgeo.Model.Poll;

/**
 * Created by Matthew on 10/13/2015.
 * MakeGroupActivity is responsible for making a group
 */
public class MakeGroupActivity extends AppCompatActivity {

    private EditText groupTitle;
    private EditText member1;
    private EditText member2;
    private EditText member3;
    private EditText member4;
    private EditText member5;
    private EditText member6;
    private EditText member7;
    private EditText member8;
    private EditText member9;
    private Button createGroupButton;
    private Button addMemberButton;
    private Group currentGroup;
    private int memberCount = 3; //memberCount holds how many visible member entries there are

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_group_poll);

        // Gets current user
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Set up textFields
        groupTitle = (EditText) findViewById(R.id.userTitleText);
        member1 = (EditText) findViewById(R.id.user1);
        member2 = (EditText) findViewById(R.id.user2);
        member3 = (EditText) findViewById(R.id.user3);
        //The rest of the EditText elements will be set to invisible and only made visible when clicked on
        member4 = (EditText) findViewById(R.id.user4);
        member4.setVisibility(View.GONE);
        member5 = (EditText) findViewById(R.id.user5);
        member5.setVisibility(View.GONE);
        member6 = (EditText) findViewById(R.id.user6);
        member6.setVisibility(View.GONE);
        member7 = (EditText) findViewById(R.id.user7);
        member7.setVisibility(View.GONE);
        member8 = (EditText) findViewById(R.id.user8);
        member8.setVisibility(View.GONE);
        member9 = (EditText) findViewById(R.id.user9);
        member9.setVisibility(View.GONE);


        createGroupButton = (Button) findViewById(R.id.makeGroupButton);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCreateGroup();
            }
        });

        //Initialize the addMemberButton
        addMemberButton = (Button) findViewById(R.id.makeGroupButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });

    }


    /*
    navigateToCreateCroup is called when the create group button is clicked. This method will set up creating a group
     */
    public void navigateToCreateGroup() {
        //Grab the values of the text fields
        String groupTitleString = groupTitle.getText().toString().trim();
        String member1Name = member1.getText().toString().trim();
        String member2Name = member2.getText().toString().trim();
        String member3Name = member3.getText().toString().trim();
        String member4Name = member4.getText().toString().trim();
        String member5Name = member5.getText().toString().trim();
        String member6Name = member6.getText().toString().trim();
        String member7Name = member7.getText().toString().trim();
        String member8Name = member8.getText().toString().trim();
        String member9Name = member9.getText().toString().trim();

        // If either is title, member1, or member2 is empty, pop and alert dialog
        if (groupTitleString.isEmpty() || member1Name.isEmpty() || member2Name.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroupActivity.this);
            builder.setMessage("A Group must have a name and at least three members").setTitle("Oops1").
                    setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else { //create a group with the users specs
            //Add users to a group

            ArrayList<String> members = new ArrayList<String>();
            //Add member to members list is a string was put into the box
            if ( !(member1Name.isEmpty()) ){ members.add(member1Name);}
            if ( !(member2Name.isEmpty()) ){ members.add(member2Name);}
            if ( !(member3Name.isEmpty()) ){ members.add(member3Name);}
            if ( !(member4Name.isEmpty()) ){ members.add(member4Name);}
            if ( !(member5Name.isEmpty()) ){ members.add(member5Name);}
            if ( !(member6Name.isEmpty()) ){ members.add(member6Name);}
            if ( !(member7Name.isEmpty()) ){ members.add(member7Name);}
            if ( !(member8Name.isEmpty()) ){ members.add(member8Name);}

            //create the group
            currentGroup = createGroup(groupTitleString, members);

            currentGroup.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }
            });

        }
    }


    /*
        createGroup takes in a string which is to be the name of a group and list of usernames for the users to be in the
        group and creates a group, and returns that group
     */
    private Group createGroup(String groupName, ArrayList<String> members){
        Group group = new Group();
        group.setTitle(groupName);
        group.addMembers(members);
        return group;
    }

    /*
    addMember is called when the add member button is clicked. Initially there are only 3 visible text field options for adding
    members to a group (the other 6 are invisible), but when addMember is called, it makes another group member text field visible
    for the user to add. addMember just makes the textFields visible
     */
    private void addMember() {
        if (memberCount < 9){ //increment member count if there is room to do so
            memberCount++;
        }
        else {
            //Alert user if they try to add more than nine users
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeGroupActivity.this);
            builder.setMessage("Group full! You can only invite up to nine members right now").setTitle("Oops!").
                    setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        // switch statement on what EditText element to make visible
        switch (memberCount){
            case 4: member4.setVisibility(View.VISIBLE);
                break;
            case 5: member5.setVisibility(View.VISIBLE);
                break;
            case 6: member6.setVisibility(View.VISIBLE);
                break;
            case 7: member7.setVisibility(View.VISIBLE);
                break;
            case 8: member8.setVisibility(View.VISIBLE);
                break;
            case 9: member9.setVisibility(View.VISIBLE);
                break;
            default: break;
        }
    }
}