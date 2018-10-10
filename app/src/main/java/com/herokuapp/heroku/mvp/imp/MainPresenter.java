package com.herokuapp.heroku.mvp.imp;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.herokuapp.heroku.model.Contact;
import com.herokuapp.heroku.mvp.ifc.MainView;
import com.herokuapp.heroku.network.ServiceApi;
import com.herokuapp.heroku.network.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by antonyhilary on 09 October 2018
 */
public class MainPresenter extends MvpBasePresenter<MainView> {
    private List<Contact> contactList = new ArrayList<>();
    private Subscription subs;

    public void getAllContacts() {
        getView().showProgress();
        contactList.clear();

        ServiceApi serviceApi = ServiceGenerator.createService(ServiceApi.class);
        subs = serviceApi.getAllContact()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    JSONObject jsonObject;

                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            if (jsonObject != null) {
                                getView().hideProgress();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideProgress();
                        }
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        if (responseBodyResponse.isSuccessful()) {
                            String sJson = null;
                            try {
                                sJson = responseBodyResponse.body().string();
                                jsonObject = new JSONObject(sJson);

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject contactObj = dataArray.getJSONObject(i);
                                    contactList.add(new Contact(
                                            contactObj.getString("id"),
                                            contactObj.getString("firstName"),
                                            contactObj.getString("lastName"),
                                            contactObj.getInt("age"),
                                            contactObj.getString("photo")));
                                }

                                getView().setContacts(contactList);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
