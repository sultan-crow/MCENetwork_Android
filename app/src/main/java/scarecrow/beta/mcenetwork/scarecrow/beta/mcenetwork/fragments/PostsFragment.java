package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import library.UserFunctions;
import scarecrow.beta.mcenetwork.R;


public class PostsFragment extends Fragment {

    private int role;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        role = db.getRole();

        if(!db.checkJSON())
           new getData().execute();

        db.close();


        return rootView;

    }

    class getData extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        UserFunctions userFunctions;
        int error = 0;

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
            JSONObject json = userFunctions.getProfile(getActivity(), role);
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
            }
        }
    }

}
