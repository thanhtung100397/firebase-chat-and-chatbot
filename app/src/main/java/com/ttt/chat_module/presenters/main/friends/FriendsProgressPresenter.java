package com.ttt.chat_module.presenters.main.friends;

import android.os.Bundle;
import android.os.Parcelable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.base_progress_fragment.BaseProgressFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class FriendsProgressPresenter extends BaseProgressFragmentPresenter {

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void fetchData(OnFetchDataProgressListener listener) {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<User> users = documentSnapshots.toObjects(User.class);
                    Bundle args = new Bundle();
                    args.putParcelableArrayList(Constants.KEY_USERS, (ArrayList<? extends Parcelable>) users);
                    listener.onFetchDataSuccess(args);
                })
                .addOnFailureListener(e -> listener.onFetchDataFailure(e.getMessage()));
    }
}
