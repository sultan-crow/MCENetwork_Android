package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import library.DatabaseHandler;
import library.UserFunctions;
import scarecrow.beta.mcenetwork.R;


public class ProfileFragment extends Fragment {

    private int role;

    TextView name_field;
    TextView year_field;
    TextView email_field;
    TextView dob_field;
    TextView gender_field;
    TextView designation_field;
    TextView qualification_field;
    ImageView image_field;

    private String KEY_EMAIL = "email";
    private String KEY_NAME = "name";
    private String KEY_GENDER = "gender";
    private String KEY_DOB = "dob";
    private String KEY_PICTURE = "picture";
    private String KEY_YEAR = "year";
    private String KEY_DESIGNATION = "designation";
    private String KEY_QUALIFICATION = "qualification";

    String name = "";
    String email = "";
    String dob = "";
    String year = "";
    String gender = "Male";
    String designation = "";
    String qualification = "";
    String url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DatabaseHandler db = new DatabaseHandler(getActivity());

        role = db.getRole();

        View rootView = null;

        if(role == 0)
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        else
            rootView = inflater.inflate(R.layout.fragment_profile_faculty, container, false);

        name_field = (TextView) rootView.findViewById(R.id.name);
        email_field = (TextView) rootView.findViewById(R.id.email);
        year_field = (TextView) rootView.findViewById(R.id.year);
        dob_field = (TextView) rootView.findViewById(R.id.dob);
        gender_field = (TextView) rootView.findViewById(R.id.gender);
        designation_field = (TextView) rootView.findViewById(R.id.designation);
        qualification_field = (TextView) rootView.findViewById(R.id.qualification);
        image_field = (ImageView) rootView.findViewById(R.id.picture);

        try {
            JSONObject json = new JSONObject(db.getJSON());
            JSONObject user = json.getJSONObject("user");

            name = user.getString(KEY_NAME);
            email = user.getString(KEY_EMAIL);
            dob = user.getString(KEY_DOB);
            url = user.getString(KEY_PICTURE);

            if(user.getString(KEY_GENDER).equals("f"))
                gender = "Female";

            if(role == 0) {

                year = user.getString(KEY_YEAR);

            } else {

                designation = user.getString(KEY_DESIGNATION);
                qualification = user.getString(KEY_QUALIFICATION);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        name_field.setText(name);
        dob_field.setText("Born on: " + dob);
        gender_field.setText(gender);
        email_field.setText(email);

        if(role == 0)
            year_field.setText(year);
        else {

            designation_field.setText(designation);
            qualification_field.setText("Qualification: " + qualification);

        }

        db.close();

        return rootView;

    }

}

/*if(!url.matches("")) {

                            URL urlConnection = new URL(url);
                            HttpURLConnection connection = (HttpURLConnection) urlConnection
                                    .openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            profile_pic = BitmapFactory.decodeStream(input);


                        }*/

/*if(profile_pic != null) {
                    image_field.setImageBitmap(profile_pic);
                }*/