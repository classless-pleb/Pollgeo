package ndejaco.pollgeo.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Nicholas on 9/27/2015.
 */
@ParseClassName("Activity")
public class Activity extends ParseObject {

    public Activity() {
        // A default constructor is required.
    }

    public ParseUser getFromUser() {
        return getParseUser("fromUser");
    }

    public void setFromUser(ParseUser user) {
        put("fromUser", user);
    }

    public String getType(){
        return getString("type");
    }

    public void setType(String t){
        put("type", t);
    }

    public String getContent(){
        return getString("content");
    }

    public void setContent(String c){
        put("content", c);
    }

}