package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public PostsFragment(int year) {
        uniqueFunctions = new UniqueFunctions();
        this.year = year;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_posts, container, false);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        role = db.getRole();

        if(!db.checkJSON())
           new getData().execute();
        else {

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
                    time = uniqueFunctions.getFormattedDateTime(post.getString("date"), post.getString("time"));

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
            json = userFunctions.getProfile(getActivity(), role);
            int error = 0;

            try {
                if(json.getInt(KEY_SUCCESS) != 1) {
                    error = 1;
                    return json.getString(KEY_ERROR_MSG);
                } else {
                    DatabaseHandler db = new DatabaseHandler(getActivity());
                    db.putJSON(json.toString());
                    db.close();
                }
            } catch (JSONException e) {
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
