package de.voicehired.wachak.wachakchanges.api;

import de.voicehired.wachak.wachakchanges.ConstantValues;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Vetero on 03-03-2016.
 */
public class APIAdapter {

    public static APIService getApiService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantValues.EMAIL_REGISTER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }
}