package com.ttt.chat_module.presenters.main;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.models.ChatRoomInfo;
import com.ttt.chat_module.models.User;

import java.util.List;
import java.util.Map;

public class MainActivityInteractorImpl implements MainActivityInteractor {
    private ListenerRegistration usersOnlineStateListenerRegistration;
    private ListenerRegistration chatRoomLastMessageChangeListenerRegistration;

    @Override
    public void onViewDestroy() {

    }

    @Override
    public void registerUsersOnlineStateChangeListener(OnUsersOnlineStateChangeListener listener) {
        usersOnlineStateListenerRegistration = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        listener.onRequestError(e.getMessage());
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    for (DocumentChange documentChange : documentChanges) {
                        switch (documentChange.getType()) {
                            case ADDED:
                            case MODIFIED: {
                                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                                listener.onUserOnlineStateChanged(documentSnapshot.getId(), documentSnapshot.getBoolean(User.IS_ONLINE));
                            }
                            break;

                            default: {
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void unregisterUsersOnlineStateChangeListener() {
        if (usersOnlineStateListenerRegistration != null) {
            usersOnlineStateListenerRegistration.remove();
        }
    }

    @Override
    public void registerChatRoomLastMessageChangeListener(OnChatRoomLastMessageChangeListener listener) {
        chatRoomLastMessageChangeListenerRegistration = FirebaseFirestore.getInstance()
                .collection(Constants.CHAT_ROOMS_COLLECTION)
                .addSnapshotListener((documentSnapshots, e) -> {
                    if (e != null) {
                        listener.onRequestError(e.getMessage());
                        return;
                    }
                    List<DocumentChange> documentChanges = documentSnapshots.getDocumentChanges();
                    for (DocumentChange documentChange : documentChanges) {
                        switch (documentChange.getType()) {
                            case MODIFIED: {
                                DocumentSnapshot chatRoomSnapshot = documentChange.getDocument();
                                Map<String, Object> lastMessageMap = (Map<String, Object>) chatRoomSnapshot.get(ChatRoomInfo.LAST_MESSAGE);
                                if (lastMessageMap == null) {
                                    continue;
                                }
                                listener.onChatRoomLastMessageChanged(chatRoomSnapshot.getId(), lastMessageMap);
                            }
                            break;
                        }
                    }
                });
    }

    @Override
    public void unregisterChatRoomLastMessageChangeListener() {
        if (chatRoomLastMessageChangeListenerRegistration != null) {
            chatRoomLastMessageChangeListenerRegistration.remove();
        }
    }
}
