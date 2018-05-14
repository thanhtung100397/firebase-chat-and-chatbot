package com.ttt.chat_module.presenters.edit_profile;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.ttt.chat_module.common.Constants;
import com.ttt.chat_module.common.utils.FirebaseUploadImageHelper;
import com.ttt.chat_module.models.ChatRoomUsersInfo;
import com.ttt.chat_module.models.Path;
import com.ttt.chat_module.models.User;
import com.ttt.chat_module.models.UserInfo;
import com.ttt.chat_module.models.UserProfile;

import java.util.List;
import java.util.Map;

/**
 * Created by TranThanhTung on 03/02/2018.
 */

public class EditProfileInteractorImpl implements EditProfileInteractor {
    @Override
    public void onViewDestroy() {
    }

    @Override
    public void updateUserProfile(UserProfile userProfile, OnUpdateUserProfileCompleteListener listener) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firebaseFirestore.collection(Constants.USERS_COLLECTION)
                .document(userProfile.getId());
        userRef.collection(User.PATHS)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    firebaseFirestore.runTransaction((Transaction.Function<Void>) transaction -> {
                        List<DocumentSnapshot> listDocumentSnapshot = documentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : listDocumentSnapshot) {
                            Path path = documentSnapshot.toObject(Path.class);
                            switch (path.getType()){
                                case Path.CHAT_ROOM_TYPE:{
                                    ChatRoomUsersInfo chatRoomUsersInfo = new ChatRoomUsersInfo(userProfile.getId(), new UserInfo(userProfile));
                                    transaction.set(firebaseFirestore.document(path.getPath()), chatRoomUsersInfo, SetOptions.merge());
                                }
                                break;

                                default:{
                                    break;
                                }
                            }
                        }
                        transaction.set(userRef, userProfile, SetOptions.merge());
                        return null;
                    }).addOnSuccessListener(success -> listener.onUpdateUserProfileSuccess(userProfile))
                            .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
                })
                .addOnFailureListener(e -> listener.onRequestError(e.getMessage()));
    }

    @Override
    public void uploadImages(Map<String, Uri> images,
                             String dir,
                             FirebaseUploadImageHelper.OnCompleteListener completeListener,
                             FirebaseUploadImageHelper.OnFailureListener failureListener) {
        FirebaseUploadImageHelper.uploadImagesToStorage(Constants.FIREBASE_USER_IMAGES_FOLDER_PATH + "/" + dir, images,
                (name, url) -> {//on success

                }, next -> {//on next

                }, failureListener,
                completeListener,
                progress -> {//on progress

                });
    }
}
