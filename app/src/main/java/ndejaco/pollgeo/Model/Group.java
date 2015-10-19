package ndejaco.pollgeo.Model;

//import java.text.ParseException;
import java.util.ArrayList;
import com.parse.ParseException;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 10/12/2015.
 * This class represents a Group object.
 * A group object can have multiple viewers who are able to see and vote on the poll
 * A group poll can only be seen in The Group view, only visible to those invited or apart
 * of the group
 */

@ParseClassName("Group")
public class Group extends ParseObject {

    private int memberCount = 1;

    // addMember takes in strings user names and looks for the username in the parse database to add the member to the group
    public void addMembers(ArrayList<String> members)  {

        // memberCount will hold how many users are in the group, the max will be 10 and the minimum 3, for now

        //first add the user who create the group as the first member
        ParseUser user = ParseUser.getCurrentUser();
        put("member1", user);
        memberCount++;
        // Loop through the string user names in members, query the strings if they actually exist, and if they do, add them to the group
        for (String member : members) {
            ParseUser tempMember;
            //query the string representing the user name in the _user table in Parse
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", member);
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        // The query was successful
                        String memberPosition = "member" + memberCount;
                        inviteMember(objects.get(0),memberPosition );
                    } else {
                        // Something went wrong.
                    }
                }
            });

        }
    }

    // inviteMember takes in a user and the member count and attempts to invite the user to the group if there is room
    public void inviteMember(ParseUser user, String memberPosition){
        if (memberCount >= 10){ //Group is full, do not add user to group
            return;
        }
        else {
            // put the user in the right Parse field and increment memberCount
            put(memberPosition, user);
            memberCount++;
        }

    }
    // set the title name of the group
    public void setTitle(String title) {
        put("title", title);
    }

    // return the title name of the group
    public String getTitle() {
        return getString("title");
    }
}