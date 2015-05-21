package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import library.DatabaseHandler;
import library.TwoLineAdapter;
import library.TwoLineStructure;
import library.UniqueFunctions;
import library.UserFunctions;
import scarecrow.beta.mcenetwork.PostActivity;
import scarecrow.beta.mcenetwork.R;


public class PostsFragment extends Fragment {

    private int role;
    private ListView post_listview;
    private int year;

    View rootView;

    UniqueFunctions uniqueFunctions;

    public PostsFragment() {
        super();
        uniqueFunctions = new UniqueFunctions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_posts, container, false);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        role = db.getRole();
        if(role == 0)
            year = db.getYear();
        else
            year = 5;

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getActivity(),
                    "Can't Connect to the Internet",
                    Toast.LENGTH_LONG).show();

        } else {

            if(!db.checkJSON())
                new getData().execute();

        }

        if(db.checkJSON()) {

            try {
                populate_listview(new JSONObject(db.getJSON()));
            } catch (JSONException e) {
                Log.e("JSON Error!", e.toString());
                Toast.makeText(getActivity(),
                        "There was an error fetching data. Please try again later",
                        Toast.LENGTH_LONG).show();
            }
        }

        db.close();

        return rootView;

    }

    public void populate_listview(JSONObject json) {

        TwoLineStructure post_list[];

        try {

            JSONObject post;
            final JSONArray posts;
            String title, author, time;

            if(role == 0)
                posts = json.getJSONArray("posts");
             else
                posts = json.getJSONObject("posts").getJSONArray(String.valueOf(year));

            if(posts.length() > 0) {

                post_list = new TwoLineStructure[posts.length()];

                for (int i = 0; i < posts.length(); i++) {

                    post = posts.getJSONObject(i);
                    title = post.getString("post_title");
                    author = post.getString("posted_by");
                    time = uniqueFunctions.getFormattedDateTime(post.getString("date"),
                            post.getString("time"));

                    post_list[i] = new TwoLineStructure(title, author, time);

                }

                TwoLineAdapter adapter = new TwoLineAdapter(getActivity(), R.layout.twolinelist_row, post_list);
                post_listview = (ListView) rootView.findViewById(R.id.post_list);
                post_listview.setAdapter(adapter);

                post_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String post_id = "";
                        try {
                            post_id = posts.getJSONObject(position).getString("post_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent post_intent = new Intent(getActivity(), PostActivity.class);
                        post_intent.putExtra("id", post_id);

                        startActivity(post_intent);

                    }
                });

            } else
                Toast.makeText(getActivity(),
                        "No Posts are available",
                        Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.e("JSON Error!", e.toString());;
            Toast.makeText(getActivity(),
                    "There was an error fetching data. Please try again later",
                    Toast.LENGTH_LONG).show();
        }

    }

    class getData extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        UserFunctions userFunctions;
        int error = 0;

        private JSONObject json;
        private String KEY_SUCCESS = "success";
        private String KEY_ERROR_MSG = "error_message";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching Posts ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            userFunctions = new UserFunctions();
            json = userFunctions.getUserData(getActivity(), role);
            int count;

            try {
                if(json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                } else {
                    DatabaseHandler db = new DatabaseHandler(getActivity());
                    db.putJSON(json.toString());
                    db.close();

                    String pic = json.getJSONObject("user").getString("picture");

                    File check = new File(Environment.getExternalStorageDirectory()
                            + "scarecrow.beta.mcenetwork/pic.jpg");
                    if (!check.exists()) {
                        if (pic.length() > 2) {

                            if (!pic.substring(0, 4).matches("http")) {
                                if (role == 0)
                                    pic = "http://dcetech.com/sagnik/social_network/web/students/upload/"
                                            + pic;
                                else
                                    pic = "http://dcetech.com/sagnik/social_network/web/upload/" + pic;
                            }
                        }

                        URL url = new URL((String) pic);
                        URLConnection conexion = url.openConnection();
                        conexion.connect();
                        String targetFileName = "pic" + ".jpg";//Change name and subname
                        int lenghtOfFile = conexion.getContentLength();
                        String PATH = Environment.getExternalStorageDirectory()
                                + "scarecrow.beta.mcenetwork/";
                        File folder = new File(PATH);
                        if (!folder.exists()) {
                            folder.mkdir();//If there is no folder it will be created.
                        }
                        InputStream input = new BufferedInputStream(url.openStream());
                        OutputStream output = new FileOutputStream(PATH + targetFileName);
                        byte data[] = new byte[1024];
                        while ((count = input.read(data)) != -1) {
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            pDialog.dismiss();

            if(error == 1) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            } else
                populate_listview(json);
        }
    }

}
