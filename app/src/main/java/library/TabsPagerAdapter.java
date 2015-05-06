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

    Fragment postsFragment = new PostsFragment();
    Fragment classmatesFragment = new ClassmatesFragment();
    Fragment facultyFragment = new FacultyFragment();
    Fragment profileFragment = new ProfileFragment();
    Fragment researchFragment = new ResearchFragment();

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
                    return postsFragment;
                case 1:
                    return classmatesFragment;
                case 2:
                    return facultyFragment;
                case 3:
                    return profileFragment;
            }
        }

        else {
            switch (position) {
                case 0:
                    return postsFragment;
                case 1:
                    return facultyFragment;
                case 2:
                    return researchFragment;
                case 3:
                    return profileFragment;
            }
        }

        return null;

    }

    @Override
    public int getCount() {
        return 4;
    }
}
