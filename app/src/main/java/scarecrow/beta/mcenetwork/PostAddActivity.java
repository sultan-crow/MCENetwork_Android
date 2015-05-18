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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import library.UserFunctions;


public class PostAddActivity extends ActionBarActivity {

    String username;
    int role;

    TextView spinner_text;
    Spinner spinner;

    EditText input_title, input_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.target);
        spinner_text = (TextView) findViewById(R.id.spinner_text);
        input_title = (EditText) findViewById(R.id.title);
        input_body = (EditText) findViewById(R.id.body);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        role = db.getRole();
        username = db.getUsername();

        if(role == 1) {

            spinner_text.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);

        }
    }

    public void buttonClick(View v) {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getApplicationContext(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();
            finish();

        } else {

            new SubmitPost().execute();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_add, menu);
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

        String title, body, year;

        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "";

        int error = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PostAddActivity.this);
            pDialog.setMessage("Fetching Profile Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();

            title = input_title.getText().toString();
            body = input_body.getText().toString();

            if(role == 1)
                year = spinner.getSelectedItem().toString();
            else
                //year =
            json = userFunctions.submitPost();

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
            } else if(error == 2) {
                Toast.makeText(getApplicationContext(),
                        "Please fill all fields.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Post has been posted successfully.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
