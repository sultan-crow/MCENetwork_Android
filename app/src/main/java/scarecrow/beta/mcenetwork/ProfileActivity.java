package scarecrow.beta.mcenetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.UniqueFunctions;
import library.UserFunctions;


public class ProfileActivity extends ActionBarActivity {

    String id, role;
    TextView name_textview, gender_textview, email_textview, dob_textview,
            group_designation_textview, year_qualification_textview;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(role.equals("0"))
            setContentView(R.layout.activity_profile_student);
        //else
            //setContentView(R.layout.activity_profile_faculty);

        id = getIntent().getStringExtra("id");
        role = getIntent().getStringExtra("role");

        Log.d("Bye", String.valueOf(role.equals('0')));

        name_textview = (TextView) findViewById(R.id.name);
        gender_textview = (TextView) findViewById(R.id.gender);
        email_textview = (TextView) findViewById(R.id.email);
        dob_textview = (TextView) findViewById(R.id.dob);
        imageView = (ImageView) findViewById(R.id.image);

        /*if(role.equals("0")) {
            group_designation_textview = (TextView) findViewById(R.id.group);
            year_qualification_textview = (TextView) findViewById(R.id.year);
        } else {
            //group_designation_textview = (TextView) findViewById()
        }*/

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getApplicationContext(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();
            finish();

        } else {

            Toast.makeText(getApplicationContext(),
                    "Role is " + role,
                    Toast.LENGTH_SHORT);
            new LoadProfile().execute();

        }
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

    class LoadProfile extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        UserFunctions userFunctions;
        JSONObject json;

        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "";

        int error = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Fetching Profile Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.getProfile(id, role);

            try {
                if(json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String params) {

            pDialog.dismiss();

            if(error == 1) {
                Toast.makeText(getApplicationContext(), params, Toast.LENGTH_LONG).show();
            } else {
                try {

                    JSONObject profile = json.getJSONObject("profile");
                    String name = profile.getString("name");
                    String email = profile.getString("email");
                    String gender = profile.getString("gender");
                    String dob = profile.getString("dob");
                    String pic = profile.getString("pic");

                    name_textview.setText(name);
                    email_textview.setText(email);
                    dob_textview.setText(dob);
                    gender_textview.setText(gender);

                    if(role.equals("0")) {

                        year_qualification_textview.setText(profile.getString("year"));
                        group_designation_textview.setText(profile.getString("group"));

                    }

                } catch (JSONException e) {
                    Log.d("Error!", e.toString());
                }
            }
        }
    }
}
