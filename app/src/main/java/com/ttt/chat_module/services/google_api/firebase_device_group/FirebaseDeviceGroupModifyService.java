package com.ttt.chat_module.services.google_api.firebase_device_group;

import com.ttt.chat_module.models.FirebaseDeviceGroupApiResponse;
import com.ttt.chat_module.models.FirebaseDeviceGroupDTO;

import io.reactivex.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseDeviceGroupModifyService {
    @Headers({
            "Authorization: key=AAAAD6KPTcY:APA91bHF6AhplaDRU3uHlr9HkIpP8ZBuoiA72HWvVbf5WicJRgvbWUyQUMQzZrokWD5skBdG52MWVcUvWl1gDdsf1K_K2aF-bVv1fNBdOl6018pt9Xi2BQ3OtACKpMFKEsdT-OunRgD9",
            "project_id: 67151809990"
    })
    @POST("notification")
    Observable<FirebaseDeviceGroupApiResponse> modifyGroupDevice(FirebaseDeviceGroupDTO deviceGroupDTO);
}
