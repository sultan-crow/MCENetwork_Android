package library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.ClassmatesFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.FacultyFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.PostsFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.ProfileFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.ResearchFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    int role, year;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void assignData(int mRole, int mYear) {

        role = mRole;
        year = mYear;

    }

    @Override
    public Fragment getItem(int position) {

        if(role == 0) {

            switch (position) {
                case 0:
                    return new PostsFragment();
                case 1:
                    return new ClassmatesFragment();
                case 2:
                    return new FacultyFragment();
                case 3:
                    return new ProfileFragment();
            }
        }

        else {
            switch (position) {
                case 0:
                    return new PostsFragment();
                case 1:
                    return new PostsFragment();
                case 2:
                    return new PostsFragment();
                case 3:
                    return new PostsFragment();
                case 4:
                    return new PostsFragment();
                case 5:
                    return new FacultyFragment();
                case 6:
                    return new ResearchFragment();
                case 7:
                    return new ProfileFragment();
            }
        }

        return null;

    }

    @Override
    public int getCount() {

        if(role == 0)
            return 4;
        else
            return 8;

    }
}
