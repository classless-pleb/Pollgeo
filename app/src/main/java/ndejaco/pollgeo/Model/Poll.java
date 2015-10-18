package ndejaco.pollgeo.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nicholas on 9/20/2015.
 */

@ParseClassName("Poll")
public class Poll extends ParseObject {

    public Poll() {

    }
    public ParseUser getUser() {
        return getParseUser("user");
    }
    public void setUser(ParseUser user) {
        put("user", user);
    }

    public void setOptions(ArrayList<String> options) {
       for (int i = 0; i < options.size(); i++) {
           put("option" + i, options.get(i));
           put("option" + i + "count", 0);
       }
    }

    /* setOptionCount, takes in an int representing which option is being voted on and a value by how much to increment the vote count
      value will always be 1 or -1 for we only want to increment or decrement by one
    */
    public void setOptionCount(int opt, int value) {
        increment("option" + opt + "count", value);

    }
    public void setTitle(String title) {
        put("title", title);
    }

    public String getTitle() {
        return getString("title");
    }

    public String getOption(int opt){
        return getString("option"+opt);
    }

    public int getOptionVotes(int opt) { return getInt("option" + opt + "count");}

    public ParseFile getImage() {
        return getParseFile("image");
    }

    public void setImage(ParseFile file) {
        put("image", file);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }


}
