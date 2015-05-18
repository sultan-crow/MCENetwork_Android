package scarecrow.beta.mcenetwork;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import library.DatabaseHandler;
import library.UserFunctions;
import library.config;


public class RegisterActivity extends ActionBarActivity {

    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputUsername;
    EditText inputPassword;
    Spinner inputYear;
    TextView registerErrorMsg;

    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public static final String REG_ID = "regId";
    //private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";

    private static String KEY_SUCCESS = "success";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_USERNAME = "username";
    private static String KEY_YEAR = "year";
    private static String KEY_ROLE = "role";
    private static String KEY_ERROR_MSG = "error_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputUsername = (EditText) findViewById(R.id.registerUsername);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputYear = (Spinner) findViewById(R.id.registerYear);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo ni = cm.getActiveNetworkInfo();

                if(ni == null) {

                    Toast.makeText(getApplicationContext(), "Can't Connect to the Internet", Toast.LENGTH_LONG).show();

                } else {
                    new AttemptRegister().execute();
                }
            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    class AttemptRegister extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {

            if(inputFullName.getText().toString().matches("") ||
                    inputEmail.getText().toString().matches("") ||
                    inputUsername.getText().toString().matches("") ||
                    inputPassword.getText().toString().matches("")) {
                return "Please fill all fields!";
            }

            String name = inputFullName.getText().toString();
            String email = inputEmail.getText().toString();
            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();
            String year = inputYear.getSelectedItem().toString();
            UserFunctions userFunction = new UserFunctions();

            String msg = "";
            context = getApplicationContext();
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regId = gcm.register(config.GOOGLE_PROJECT_ID);
                Log.d("RegisterActivity", "registerInBackground - regId: "
                        + regId);
                msg = "Device registered, registration ID=" + regId;

                //storeRegistrationId(context, regId);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.d("RegisterActivity", "Error: " + msg);
            }
            Log.d("RegisterActivity", "AsyncTask completed: " + msg);

            JSONObject json = userFunction.registerUser(name, email, username, password, regId, year);

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    //registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        userFunction.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_NAME),
                                json_user.getString(KEY_EMAIL),
                                json_user.getString(KEY_USERNAME),
                                json_user.getString(KEY_YEAR),
                                Integer.parseInt(json_user.getString(KEY_ROLE)));
                        db.close();

                        Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);

                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboard);

                    } else {
                        return json.getString(KEY_ERROR_MSG);
                    }
                } else {
                    return json.getString(KEY_ERROR_MSG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Registered Successfully";

        }

        @Override
        protected void onPostExecute(String msg) {

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

        }

    }
}
