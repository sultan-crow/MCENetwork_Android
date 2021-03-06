package scarecrow.beta.mcenetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.WindowManager;
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
import library.TwoLineAdapterWithList;
import library.TwoLineStructure;
import library.UniqueFunctions;
import library.UserFunctions;


public class ChatActivity extends ActionBarActivity {

    String sender_global, receiver_global, role_sender, role_receiver;

    ListView list_chats;
    EditText message_edittext;
    Button b;
    String message_body;

    int num;

    TwoLineAdapterWithList adapter;
    ArrayList<TwoLineStructure> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sender_global = getIntent().getStringExtra("sender");
        role_sender = getIntent().getStringExtra("role");

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        receiver_global = db.getUsername();
        role_receiver = String.valueOf(db.getRole());

        list_chats = (ListView) findViewById(R.id.list);
        message_edittext = (EditText) findViewById(R.id.chat_text);
        b = (Button) findViewById(R.id.chat_send);

        db.close();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(chat_receiver, new IntentFilter("unique_name"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(chat_receiver);
        finish();
    }

    private BroadcastReceiver chat_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String sender = intent.getStringExtra("sender");
            String time = intent.getStringExtra("time");

            if(sender.equals(sender_global)) {

                chats.add(new TwoLineStructure(message, sender, time));
                adapter.notifyDataSetChanged();
                scrollToBottom();

            }
        }
    };

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
            pDialog.setMessage("Fetching Conversations With " + sender_global + " ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.getChats(sender_global, receiver_global);
            String message, sender, time;

            try {
                if (json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                } else {

                    JSONArray chats_array = json.getJSONArray("chats");
                    num = json.getInt("num");

                    chats = new ArrayList<TwoLineStructure>();

                    for (int i = num - 1; i >= 0; i--) {

                        message = chats_array.getJSONObject(i)
                                .getString("message");
                        sender = chats_array.getJSONObject(i)
                                .getString("sender");
                        time = new UniqueFunctions().getFormattedDateTime(
                                chats_array.getJSONObject(i).getString("date"),
                                chats_array.getJSONObject(i).getString("time"));

                        chats.add(new TwoLineStructure(message, sender, time));

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

                adapter = new TwoLineAdapterWithList(ChatActivity.this,
                        R.layout.twolinelist_row, sender_global, chats);
                list_chats.setAdapter(adapter);
                if(chats.size() > 0) {

                    scrollToBottom();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "There are no chats",
                            Toast.LENGTH_SHORT).show();
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
        String check;

        int error = 0;

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.sendMessage(receiver_global, sender_global, message_body,
                    role_receiver, role_sender);

            String message, sender, time;
            try {
                if (json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                } else {

                    JSONObject chat = json.getJSONObject("chat");

                    message = chat.getString("message");
                    sender = chat.getString("sender");
                    time = new UniqueFunctions().getFormattedDateTime(chat.getString("date"),
                            chat.getString("time"));
                    check = chat.getString("check");

                    chat_class = new TwoLineStructure(message, sender, time);

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

                message_edittext.setText("");
                chats.add(chat_class);
                adapter.notifyDataSetChanged();
                scrollToBottom();

            }
        }
    }
}
