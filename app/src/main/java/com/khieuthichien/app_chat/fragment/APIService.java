package com.khieuthichien.app_chat.fragment;

import com.khieuthichien.app_chat.notifications.MyResponse;
import com.khieuthichien.app_chat.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAsadVcQ4:APA91bHvBPGoLnSVQYj-VA151mzUfRBBcPcOudE8wLD4drMag-O9S6VaK8kwmBINTFgDCeoURaz6l7VRpwFmtjVASL93R5bLOZ1exTZiXEtvczoxouiAbPF6r7OOprOss5BP4HDnq3mP"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
