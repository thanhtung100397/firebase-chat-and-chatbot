package com.ttt.chat_module.presenters.main.friends;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.presenters.BaseInteractor;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface FriendsInteractor extends BaseInteractor {
    void getFriends(DocumentSnapshot startAt, int pageSize, OnGetFriendsCompleteListener listener);
}
