package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import scarecrow.beta.mcenetwork.R;


public class ClassmatesFragment extends Fragment {

    ListView classmates_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_classmates, container, false);

        classmates_list = (ListView) rootView.findViewById(R.id.classmates_list);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        String[] students;

        try {

            JSONObject json = new JSONObject(db.getJSON());
            JSONArray classmates = json.getJSONArray("classmates");

            if(classmates.length() > 0) {

                students = new String[classmates.length()];

                for(int i = 0; i < classmates.length(); i ++)
                    students[i] = classmates.getJSONObject(i).getString("name");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        students );
                classmates_list.setAdapter(arrayAdapter);

                classmates_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(),
                                "You clicked item no." + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } else
                Toast.makeText(getActivity(),
                        "There are no registered students from your year",
                        Toast.LENGTH_SHORT);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();

        return rootView;

    }

}
