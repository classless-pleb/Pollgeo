package ndejaco.pollgeo;

import android.app.Application;


import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;


import ndejaco.pollgeo.Model.Poll;
import ndejaco.pollgeo.Model.PollActivity;

/**
 * Created by Nicholas on 9/25/2015.
 */
// Application initializes parse sdk usage and registers subclasses.Need to register subclass here everytime create a new
// Parse Object type that we want to store on Parse
public class PollgeoApplication extends Application {

    public static final boolean APPDEBUG = true;
    // Debugging tag for the application
    public static final String APPTAG = "Pollgeo Locations";
    public static final String INTENT_EXTRA_LOCATION = "location";

    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Poll.class);
        ParseObject.registerSubclass(PollActivity.class);

        Parse.initialize(this, "efCVhlYGapbFMccQ3dEk8V02KewoO74TGZaaVoiQ", "ymQXFhW4m96V6RV3l8Ymrg3oDsab2ZLKYSv4MZPu");
        ParseFacebookUtils.initialize(this);



    }
}
