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

    int role;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void assignRole(int mRole) {
        role = mRole;
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
                    return new ProfileFragment();
                case 1:
                    return new FacultyFragment();
                case 2:
                    return new ResearchFragment();
                case 3:
                    return new PostsFragment();
            }
        }

        return null;

    }

    @Override
    public int getCount() {
        return 4;
    }
}
