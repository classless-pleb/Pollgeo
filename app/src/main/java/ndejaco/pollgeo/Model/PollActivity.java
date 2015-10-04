package ndejaco.pollgeo.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by Nicholas on 9/27/2015.
 */
@ParseClassName("PollActivity")
public class PollActivity extends ParseObject implements Serializable {

    public PollActivity() {
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

    public void setType(String t)
    {
        put("type", t);
    }

    public String getPollId() {
       return getString("PollId");
    }

    public void setPoll(Poll aPoll) {
        put("Poll", aPoll);
    }

    public void setOption(int i) {
        put("option", "option" + i);
    }



    public String getContent(){
        return getString("content");
    }

    public void setContent(String c){
        put("content", c);
    }

}