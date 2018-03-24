package com.ttt.chat_module.presenters.chat.fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ttt.chat_module.presenters.BaseInteractor;
import com.ttt.chat_module.presenters.OnRequestCompleteListener;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public interface ChatFragmentInteractor extends BaseInteractor {
    void sendMessage(String roomID, String message, OnRequestCompleteListener listener);
    void registerOnMessageChangedListener(String roomID, OnMessageChangedListener listener);
    void unregisterOnMessageChangedListener();
    void getMessages(String roomID, DocumentSnapshot startAtDocument, int pageSize, OnEachMessagesFetchedListener listener);
}
