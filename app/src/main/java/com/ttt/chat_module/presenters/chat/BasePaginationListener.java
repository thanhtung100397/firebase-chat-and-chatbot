package com.ttt.chat_module.presenters.chat;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by TranThanhTung on 21/03/2018.
 */

public interface BasePaginationListener {
    void onLastDocumentSnapshotFetched(DocumentSnapshot lastDocumentSnapshot);
}
