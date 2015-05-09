package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import scarecrow.beta.mcenetwork.R;


public class FacultyFragment extends Fragment {

    TwoLineStructure[] faculty_data;
    ListView faculty_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_faculty, container, false);

        faculty_list = (ListView) rootView.findViewById(R.id.faculty_list);
        DatabaseHandler db = new DatabaseHandler(getActivity());

        try {
            JSONObject json = new JSONObject(db.getJSON());
            JSONArray faculty = json.getJSONArray("faculty");
            String name, designation;
            JSONObject faculty_row;

            if(faculty.length() > 0) {

                faculty_data = new TwoLineStructure[faculty.length()];
                for(int i = 0; i < faculty.length(); i ++) {

                    faculty_row = faculty.getJSONObject(i);
                    name = faculty_row.getString("name");
                    designation = faculty_row.getString("designation");

                    faculty_data[i] = new TwoLineStructure(name, designation, "");

                }

                TwoLineAdapter adapter = new TwoLineAdapter(getActivity(), R.layout.twolinelist_row, faculty_data);
                faculty_list.setAdapter(adapter);

                faculty_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(),
                                "You clicked item no." + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } else
                Toast.makeText(getActivity(),
                        "There are no faculty yet",
                        Toast.LENGTH_SHORT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();

        return rootView;

    }

}
