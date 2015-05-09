package library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {

    private JSONParser jsonParser;

    private static String loginURL = "http://10.0.2.2/social_network/android/user_activity.php";
    private static String registerURL = "http://10.0.2.2/social_network/android/user_activity.php";
    private static String profileURL = "http://10.0.2.2/social_network/android/data_activity.php";

    /*private static String loginURL = "http://dcetech.com/sagnik/vnb/android/user_activity.php";
    private static String registerURL = "http://dcetech.com/sagnik/vnb/android/user_activity.php";
    private static String noticesURL = "http://dcetech.com/sagnik/vnb/android/get_data.php";*/

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String profile_tag = "profile";

    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    public JSONObject loginUser(String email, String password){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);

        return json;
    }

    public JSONObject registerUser(String name, String email, String username, String password, String regId, String year){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("regId", regId));
        params.add(new BasicNameValuePair("year", year));

        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }

    public JSONObject getProfile(Context context, int role) {

        DatabaseHandler db = new DatabaseHandler(context);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", profile_tag));
        params.add(new BasicNameValuePair("username", db.getUsername()));
        params.add(new BasicNameValuePair("role", String.valueOf(role)));

        if(role == 0)
            params.add(new BasicNameValuePair("year", String.valueOf(db.getYear())));

        db.close();

        JSONObject json = jsonParser.getJSONFromUrl(profileURL, params);
        return json;

    }

    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        db.close();
        if(count > 0){
            return true;
        }
        return false;
    }

    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        db.close();
        return true;
    }
}
