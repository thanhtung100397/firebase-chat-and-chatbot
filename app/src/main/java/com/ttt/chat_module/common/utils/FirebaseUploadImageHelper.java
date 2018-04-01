package com.ttt.chat_module.common.utils;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by TranThanhTung on 25/11/2017.
 */

public class FirebaseUploadImageHelper {

    public static void uploadImageToStorage(String folder, String name, Uri uri,
                                            OnSuccessListener success,
                                            OnFailureListener failure,
                                            OnCompleteListener complete,
                                            OnProgressListener progress) {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(folder + "/" + name);
        UploadTask uploadTask = imageRef.putFile(uri);
        if (success != null) {
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl == null) {
                    if (failure != null) {
                        failure.onFailure(folder, name, new Exception("Download url is null"));
                    }
                } else {
                    success.onSuccess(folder, name, downloadUrl.toString());
                    if (complete != null) {
                        Map<String, String> urls = new HashMap<>(1);
                        urls.put(name, downloadUrl.toString());
                        complete.onComplete(folder, urls);
                    }
                }
            });
        }
        if (failure != null) {
            uploadTask.addOnFailureListener(e -> {
                failure.onFailure(folder, name, e);
            });
        }
        if (progress != null) {
            uploadTask.addOnProgressListener(taskSnapshot -> progress.onProgress(folder, taskSnapshot));
        }
    }

    public static void uploadImagesToStorage(String folder, Map<String, Uri> images,
                                             OnSuccessListener success,
                                             OnNextTaskListener next,
                                             OnFailureListener failure,
                                             OnCompleteListener complete,
                                             OnProgressListener progress) {
        int size = images.size();
        if (size == 0) {
            if (success != null) {
                success.onSuccess(folder, null, null);
            }
            if (complete != null) {
                complete.onComplete(folder, new HashMap<>(0));
            }
            return;
        }

        Map<String, String> urls = new HashMap<>(size);
        uploadImagesToStorage(folder, images.entrySet().iterator(), urls,
                success, next, failure, complete, progress);
    }

    private static void uploadImagesToStorage(String folder,
                                              Iterator<Map.Entry<String, Uri>> imageEntries,
                                              Map<String, String> urls,
                                              OnSuccessListener success,
                                              OnNextTaskListener next,
                                              OnFailureListener failure,
                                              OnCompleteListener complete,
                                              OnProgressListener progress) {
        Map.Entry<String, Uri> imageEntry = imageEntries.next();
        if (next != null) {
            next.onNextTask(folder, imageEntry.getKey());
        }
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(folder + "/" + imageEntry.getKey());
        UploadTask uploadTask = imageRef.putFile(imageEntry.getValue());
        if (success != null) {
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl == null) {
                    if (failure != null) {
                        failure.onFailure(folder, imageEntry.getKey(), new Exception("Download url is null"));
                    }
                } else {
                    String url = downloadUrl.toString();
                    urls.put(imageEntry.getKey(), url);
                    success.onSuccess(folder, imageEntry.getKey(), url);
                    if (imageEntries.hasNext()) {
                        uploadImagesToStorage(folder, imageEntries, urls, success,
                                next, failure, complete, progress);
                    } else {
                        if (complete != null) {
                            complete.onComplete(folder, urls);
                        }
                    }
                }
            });
        }
        if (failure != null) {
            uploadTask.addOnFailureListener(e -> {
                failure.onFailure(folder, imageEntry.getKey(), e);
            });
        }
        if (progress != null) {
            uploadTask.addOnProgressListener(taskSnapshot -> progress.onProgress(folder, taskSnapshot));
        }
    }

    public interface OnSuccessListener {
        void onSuccess(String folder, String name, String url);
    }

    public interface OnProgressListener {
        void onProgress(String folder, UploadTask.TaskSnapshot taskSnapshot);
    }

    public interface OnNextTaskListener {
        void onNextTask(String folder, String name);
    }

    public interface OnFailureListener {
        void onFailure(String folder, String name, Exception e);
    }

    public interface OnCompleteListener {
        void onComplete(String folder, Map<String, String> urls);
    }
}
