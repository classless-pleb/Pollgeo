package ndejaco.pollgeo.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nicholas on 9/20/2015.
 */

@ParseClassName("Poll")
public class Poll extends ParseObject implements Serializable {

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

    public void setOptionCount(int opt) {
        increment("option" + opt+ "count");

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







}
