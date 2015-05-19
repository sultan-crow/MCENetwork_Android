package scarecrow.beta.mcenetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import library.DatabaseHandler;
import library.TwoLineAdapter;
import library.TwoLineStructure;
import library.UniqueFunctions;
import library.UserFunctions;


public class ChatActivity extends ActionBarActivity {

    String sender, receiver;

    ListView list_chats;
    EditText message_edittext;
    Button b;
    String message_body;

    TwoLineAdapter adapter;
    TwoLineStructure chats[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sender = getIntent().getStringExtra("sender");

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        receiver = db.getUsername();

        list_chats = (ListView) findViewById(R.id.list);
        message_edittext = (EditText) findViewById(R.id.chat_text);
        b = (Button) findViewById(R.id.chat_send);

        db.close();

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getApplicationContext(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();
            finish();

        } else {

            new LoadChats().execute();

        }
    }

    private void scrollToBottom() {
        list_chats.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                list_chats.setSelection(list_chats.getCount() - 1);
            }
        });
    }

    public void sendMessage(View v) {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        message_body = message_edittext.getText().toString();

        if(message_body.matches(""))
            return;

        if(ni == null) {

            Toast.makeText(getApplicationContext(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();
            finish();

        } else {

            new SendMessage().execute();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    class LoadChats extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;
        UserFunctions userFunctions;
        JSONObject json;

        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "error_message";

        int error = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChatActivity.this);
            pDialog.setMessage("Fetching Conversations ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.getChats(sender, receiver);
            String message, sender, time;

            try {
                if (json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                } else {

                    JSONArray chats_array = json.getJSONArray("chats");
                    chats = new TwoLineStructure[chats_array.length()];

                    for (int i = 0; i < chats_array.length(); i++) {

                        message = chats_array.getJSONObject(i)
                                .getString("message");
                        sender = chats_array.getJSONObject(i)
                                .getString("sender");
                        time = new UniqueFunctions().getFormattedDateTime(
                                chats_array.getJSONObject(i).getString("date"),
                                chats_array.getJSONObject(i).getString("time"));

                        chats[i] = new TwoLineStructure(message, sender, time);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String params) {

            pDialog.dismiss();

            if (error == 1) {
                Toast.makeText(getApplicationContext(), params, Toast.LENGTH_LONG).show();
            } else {

                if(chats.length > 0) {

                    adapter = new TwoLineAdapter(ChatActivity.this,
                            R.layout.twolinelist_row, chats);
                    list_chats.setAdapter(adapter);
                    scrollToBottom();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "There are no chats",
                            Toast.LENGTH_SHORT);
                }

            }
        }
    }

    class SendMessage extends AsyncTask<String, String, String> {

        UserFunctions userFunctions;
        JSONObject json;

        TwoLineStructure chat_class;

        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "error_message";

        int error = 0;

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.sendMessage(sender, receiver, message_body);
            String message, sender, date, time;

            try {
                if (json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                } else {

                    JSONObject chat = json.getJSONObject("chat");

                    message = chat.getString("message");
                    sender = chat.getString("sender");
                    date = chat.getString("date");
                    time = chat.getString("time");

                    chat_class = new TwoLineStructure(
                            message,
                            sender,
                            new UniqueFunctions().getFormattedDateTime(date, time));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String params) {
            if (error == 1) {
                Toast.makeText(getApplicationContext(), params, Toast.LENGTH_LONG).show();
            } else {

                List<TwoLineStructure> chats_list = Arrays.asList(chats);
                chats_list.add(chat_class);
                chats = new TwoLineStructure[chats.length];
                chats = chats_list.toArray(chats);

                adapter.notifyDataSetChanged();
                scrollToBottom();

            }
        }
    }
}
