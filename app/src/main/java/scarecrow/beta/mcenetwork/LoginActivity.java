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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import library.UserFunctions;


public class LoginActivity extends ActionBarActivity {

    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;

    private static String KEY_SUCCESS = "success";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_USERNAME = "username";
    private static String KEY_YEAR = "year";
    private static String KEY_ROLE = "role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo ni = cm.getActiveNetworkInfo();

                if(ni == null) {

                    Toast.makeText(getApplicationContext(), "Can't Connect to the Internet", Toast.LENGTH_LONG).show();
                    return;

                } else {
                    new AttemptLogin().execute();
                }
            }
        });

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = null;
            json = userFunction.loginUser(email, password);

            try {
                Log.d("KK", "P");
                if (json.getString(KEY_SUCCESS) != null) {
                    //loginErrorMsg.setText("");
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
                        Log.d("Error!", "Incorrect Username/Password");
                        return "Incorrect Username/Password";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Login Successful";
        }

        @Override
        protected void onPostExecute(String args) {
            Toast.makeText(getApplicationContext(), args, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
