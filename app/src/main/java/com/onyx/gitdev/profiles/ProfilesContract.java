package com.onyx.gitdev.profiles;

import com.onyx.gitdev.data.model.Person;

import java.util.ArrayList;

/**
 * Created by Morph-Deji on 15-Mar-17.
 */

public interface ProfilesContract {
    interface View{
        void showDevelopers(ArrayList<Person> persons);
        void showProgress(boolean show);
        void showEmptyContent(String message);

        void showError(String message);
    }

    interface UserActions{
        void loadDevelopers();
    }
}
