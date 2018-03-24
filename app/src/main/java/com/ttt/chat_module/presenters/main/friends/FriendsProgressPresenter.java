package com.ttt.chat_module.presenters.main.friends;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.UserAuth;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.presenters.base_progress.BaseProgressFragmentPresenter;

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
        String userID = UserAuth.getUserID();
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .orderBy(User.IS_ONLINE, Query.Direction.DESCENDING)
                .orderBy(User.FIRST_NAME, Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<DocumentSnapshot> listDocumentSnapshots = documentSnapshots.getDocuments();
                    List<User> users = new ArrayList<>(listDocumentSnapshots.size());
                    for (DocumentSnapshot documentSnapshot : listDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        if (!user.getEmail().equals(userID)) {
                            users.add(user);
                        }
                    }
                    Bundle args = new Bundle();
                    args.putParcelableArrayList(Constants.KEY_USERS, (ArrayList<? extends Parcelable>) users);
                    listener.onFetchDataSuccess(args);
                })
                .addOnFailureListener(e -> {
                    listener.onFetchDataFailure(e.getMessage());
                });
    }
}
