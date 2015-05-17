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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.UniqueFunctions;
import library.UserFunctions;


public class PostActivity extends ActionBarActivity {

    String id;
    TextView title_textview, text_textview, footer_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        id = getIntent().getStringExtra("id");

        title_textview = (TextView) findViewById(R.id.title);
        text_textview = (TextView) findViewById(R.id.text);
        footer_textview = (TextView) findViewById(R.id.footer);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getApplicationContext(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();
            finish();

        } else {

            new LoadPost().execute();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
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

    class LoadPost extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        UserFunctions userFunctions;
        JSONObject json;

        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "";

        int error = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PostActivity.this);
            pDialog.setMessage("Fetching Post ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.getPost(id);

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

                    JSONObject post = json.getJSONObject("post");
                    String title = post.getString("title");
                    String author = post.getString("author");
                    String text = post.getString("text");
                    String date = new UniqueFunctions()
                            .getFormattedDateTime(post.getString("date"),
                                    post.getString("time"));

                    title_textview.setText(title);
                    text_textview.setText(text);
                    footer_textview.setText(author + " " + date);

                } catch (JSONException e) {
                    Log.d("Error!", e.toString());
                }
            }
        }
    }
}
