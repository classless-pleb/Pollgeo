package ndejaco.pollgeo;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Nicholas on 9/25/2015.
 */
public class PollgeoApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "efCVhlYGapbFMccQ3dEk8V02KewoO74TGZaaVoiQ", "ymQXFhW4m96V6RV3l8Ymrg3oDsab2ZLKYSv4MZPu");

    }
}
