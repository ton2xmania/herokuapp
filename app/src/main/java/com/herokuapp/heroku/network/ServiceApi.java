package com.herokuapp.heroku.network;

import com.herokuapp.heroku.model.Contact;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

import static com.herokuapp.heroku.utilities.Constanta.CONTACT_BY_ID_URL;
import static com.herokuapp.heroku.utilities.Constanta.CONTACT_URL;

/**
 * Created by antonyhilary on 09 October 2018
 */
public interface ServiceApi {
    @GET(CONTACT_URL)
    Observable<Response<ResponseBody>> getAllContact();

    @POST(CONTACT_URL)
    Observable<Response<ResponseBody>> saveContact(@Body Contact contact);

    @PUT(CONTACT_BY_ID_URL)
    Observable<Response<ResponseBody>> saveContactById(@Path("id") String id, @Body Contact contact);

    @GET(CONTACT_BY_ID_URL)
    Observable<Response<ResponseBody>> getContact(@Path("id") String id);

    @DELETE(CONTACT_BY_ID_URL)
    Observable<Response<ResponseBody>> deleteContact(@Path("id") String id);
}