package ndejaco.pollgeo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import ndejaco.pollgeo.Model.Poll;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the stupid action bar
        ActionBar bar = getSupportActionBar();
        bar.hide();
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        String user = "";
        if(extras != null){
            user = (String)extras.get("target");
        }

        Log.e("In profile activity","Username = " + user);

        ParseUser pu = null;
        if(user.equals(ParseUser.getCurrentUser().getUsername())){
            pu = ParseUser.getCurrentUser();
        }
        else if(!user.equals("")){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username",user);
            List<ParseUser> userList = null;
            try {
                userList = query.find();
            }catch(Exception e){
            }

            if(userList != null){
                try {
                    pu = userList.get(0);
                }catch(Exception e){
                    Log.e("In profile activity","The list is empty.");
                }
            }
            else{
                Log.e("In profile activity","The returned list is null");
            }
        }

        if(pu == null){
            Log.e("in profile activity","exception happened.");
        }


        ProfilePictureView fbPhoto = (ProfilePictureView) findViewById(R.id.profilePicture);
        fbPhoto.setPresetSize(ProfilePictureView.LARGE);
        fbPhoto.setProfileId((String) pu.get("facebookId"));

        TextView userName = (TextView)findViewById(R.id.profileName);
        userName.setText((String)pu.get("name"));

        TextView scoreText = (TextView)findViewById(R.id.scoreText);
        Integer userScore = (Integer)pu.get("score");
        if(userScore == null){
            userScore = 0;
        }
        scoreText.setText("Score: " + userScore);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}