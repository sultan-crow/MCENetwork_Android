package scarecrow.beta.mcenetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import library.UniqueFunctions;
import library.UserFunctions;


public class ProfileActivity extends ActionBarActivity {

    String id, role;
    TextView name_textview, gender_textview, email_textview, dob_textview,
            group_qualification_textview, year_designation_textview,
            username_textview, research_header_textview, research_textview;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getIntent().getStringExtra("id");
        role = getIntent().getStringExtra("role");

        if(role.equals("0"))
            setContentView(R.layout.activity_profile_student);
        else
            setContentView(R.layout.activity_profile_faculty);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name_textview = (TextView) findViewById(R.id.name);
        gender_textview = (TextView) findViewById(R.id.gender);
        email_textview = (TextView) findViewById(R.id.email);
        dob_textview = (TextView) findViewById(R.id.dob);
        imageView = (ImageView) findViewById(R.id.image);
        username_textview = (TextView) findViewById(R.id.username);

        if(role.equals("0")) {
            group_qualification_textview = (TextView) findViewById(R.id.group);
            year_designation_textview = (TextView) findViewById(R.id.year);
        } else {
            group_qualification_textview = (TextView) findViewById(R.id.qualification);
            year_designation_textview = (TextView) findViewById(R.id.designation);
            research_header_textview = (TextView) findViewById(R.id.research_header);
            research_textview = (TextView) findViewById(R.id.research);
        }

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getApplicationContext(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();
            finish();

        } else {

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

    public void chat(View v) {

        String username = username_textview.getText().toString();

        Intent i = new Intent(ProfileActivity.this, ChatActivity.class);
        i.putExtra("sender", username);
        i.putExtra("role", role);

        startActivity(i);
    }

    class LoadProfile extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        UserFunctions userFunctions;
        JSONObject json;

        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "";
        UniqueFunctions uniqueFunctions;

        int error = 0;

        String name, email, gender, dob, pic, username, year, group, designation,
                qualification, research_string = "";

        Bitmap b;

        boolean image_bool = false;

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
                } else {
                    JSONObject profile = json.getJSONObject("profile");
                    name = profile.getString("name");
                    email = profile.getString("email");
                    gender = profile.getString("gender");
                    dob = profile.getString("dob");
                    pic = profile.getString("pic");
                    username = profile.getString("username");

                    if(role.equals("0")) {
                        year = profile.getString("year");
                        group = profile.getString("group");
                    } else {
                        designation = profile.getString("designation");
                        qualification = profile.getString("qualification");
                    }

                    if(pic.length() > 2) {

                        if(!pic.substring(0, 4).matches("http")) {
                            if(role.equals("0"))
                                pic = "http://dcetech.com/sagnik/social_network/web/students/upload/"
                                        + pic;
                            else
                                pic = "http://dcetech.com/sagnik/social_network/web/upload/" + pic;
                        }
                        image_bool = true;
                        InputStream in = new java.net.URL(pic).openStream();
                        b = BitmapFactory.decodeStream(in);
                    }

                    JSONArray research = json.getJSONArray("research");
                    for(int i = 0; i < research.length(); i ++)
                        research_string = research_string
                                + research.getString(i) + "\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String params) {

            pDialog.dismiss();
            uniqueFunctions = new UniqueFunctions();
            if(error == 1) {
                Toast.makeText(getApplicationContext(), params, Toast.LENGTH_LONG).show();
            } else {

                name_textview.setText(name);
                email_textview.setText(email);
                dob_textview.setText("B'Day: " + uniqueFunctions.getFormattedDate(dob));
                gender_textview.setText(uniqueFunctions.getFullGender(gender));
                username_textview.setText(username);

                if(role.equals("0")) {

                    year_designation_textview.setText("Year: " +
                            uniqueFunctions.getNumberWithSubscript(year));
                    group_qualification_textview.setText("Group: " + group);

                } else {
                    year_designation_textview.setText(designation);
                    group_qualification_textview.setText(qualification);
                    if(!research_string.matches("")) {
                        research_header_textview.setVisibility(View.VISIBLE);
                        research_textview.setText(research_string);
                    }
                }

                if(image_bool)
                    imageView.setImageBitmap(b);
                else
                    imageView.setImageResource(R.drawable.anonymous);

            }
        }
    }
}
