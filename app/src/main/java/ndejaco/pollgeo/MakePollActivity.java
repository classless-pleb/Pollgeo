package ndejaco.pollgeo;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import ndejaco.pollgeo.Model.Poll;

public class MakePollActivity extends AppCompatActivity {

    private EditText title;
    private EditText option1;
    private EditText option2;
    private EditText option3;
    private EditText option4;
    private Button submit;
    private Poll currentPoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_poll);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        }

        //Log this
        Log.i(MakePollActivity.class.getSimpleName(), currentUser.getUsername());


        title = (EditText) findViewById(R.id.userTitleText);
        option1 = (EditText) findViewById(R.id.option1);
        option2 = (EditText) findViewById(R.id.option2);
        option3 = (EditText) findViewById(R.id.option3);
        option4 = (EditText) findViewById(R.id.option4);


        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = title.getText().toString().trim();
                String o1 = option1.getText().toString().trim();
                String o2 = option2.getText().toString().trim();
                String o3 = option3.getText().toString().trim();
                String o4 = option4.getText().toString().trim();


                if (titleString.isEmpty() || o1.isEmpty() || o2.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MakePollActivity.this);
                    builder.setMessage(R.string.make_poll_error_msg).setTitle(R.string.make_poll_error_title).
                            setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    ArrayList<String> options = new ArrayList<String>();
                    options.add(o1);
                    options.add(o2);
                    options.add(o3);
                    options.add(o4);

                    currentPoll = createPoll(titleString, options);
                    currentPoll.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                Intent intent = new Intent(MakePollActivity.this, HomeListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MakePollActivity.this);
                                builder.setMessage(e.getMessage()).setTitle(R.string.make_poll_error_title).
                                        setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });

    }

    private Poll createPoll(String title, ArrayList<String> options) {
        Poll currentPoll = new Poll();
        currentPoll.setOptions(options);
        currentPoll.setUser(ParseUser.getCurrentUser());
        currentPoll.setTitle(title);


        return currentPoll;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_poll, menu);
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