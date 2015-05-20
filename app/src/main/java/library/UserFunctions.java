package library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UserFunctions {

    private JSONParser jsonParser;
    private UniqueFunctions uniqueFunctions;

    private static String loginURL = "http://dcetech.com/sagnik/social_network/android/user_activity.php";
    private static String registerURL = "http://dcetech.com/sagnik/social_network/android/user_activity.php";
    private static String userURL = "http://dcetech.com/sagnik/social_network/android/data_activity.php";
    private static String postURL = "http://dcetech.com/sagnik/social_network/android/detailed_data.php";
    private static String profileURL = "http://dcetech.com/sagnik/social_network/android/detailed_data.php";
    private static String addPostURL = "http://dcetech.com/sagnik/social_network/android/detailed_data.php";
    private static String chatURL = "http://dcetech.com/sagnik/social_network/android/detailed_data.php";
    private static String chatSendURL = "http://dcetech.com/sagnik/social_network/android/detailed_data.php";

    /*private static String loginURL = "http://dcetech.com/sagnik/vnb/android/user_activity.php";
    private static String registerURL = "http://dcetech.com/sagnik/vnb/android/user_activity.php";
    private static String noticesURL = "http://dcetech.com/sagnik/vnb/android/get_data.php";*/

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String profile_tag = "profile";
    private static String post_tag = "post";
    private static String add_post_tag = "add_post";
    private static String chat_tag = "chat";
    private static String chat_send_tag = "chat_send";

    public UserFunctions() {

        jsonParser = new JSONParser();
        uniqueFunctions = new UniqueFunctions();

    }

    public JSONObject loginUser(String email, String password, String regId){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("regId", regId));
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

    public JSONObject getUserData(Context context, int role) {

        DatabaseHandler db = new DatabaseHandler(context);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", profile_tag));
        params.add(new BasicNameValuePair("username", db.getUsername()));
        params.add(new BasicNameValuePair("role", String.valueOf(role)));

        if(role == 0)
            params.add(new BasicNameValuePair("year", String.valueOf(db.getYear())));

        db.close();

        JSONObject json = jsonParser.getJSONFromUrl(userURL, params);
        return json;

    }

    public JSONObject getPost(String id) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", post_tag));
        params.add(new BasicNameValuePair("id", id));

        JSONObject json = jsonParser.getJSONFromUrl(postURL, params);

        return json;

    }

    public JSONObject getChats(String sender, String receiver) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", chat_tag));
        params.add(new BasicNameValuePair("sender", sender));
        params.add(new BasicNameValuePair("receiver", receiver));

        JSONObject json = jsonParser.getJSONFromUrl(chatURL, params);

        return json;

    }

    public JSONObject sendMessage(String sender, String receiver, String message,
                                  String role_sender, String role_receiver) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", chat_send_tag));
        params.add(new BasicNameValuePair("sender", sender));
        params.add(new BasicNameValuePair("receiver", receiver));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("role_sender", message));
        params.add(new BasicNameValuePair("role_receiver", message));

        JSONObject json = jsonParser.getJSONFromUrl(chatSendURL, params);

        return json;

    }

    public JSONObject getProfile(String id, String role) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", profile_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("role", role));

        JSONObject json = jsonParser.getJSONFromUrl(profileURL, params);

        return json;

    }

    public JSONObject submitPost(String title, String body, String year, String username) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", add_post_tag));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("body", body));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("year", uniqueFunctions.getShortYear(year)));

        JSONObject json = jsonParser.getJSONFromUrl(addPostURL, params);

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
