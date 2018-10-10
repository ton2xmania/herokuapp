package com.herokuapp.heroku.mvp.imp;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.herokuapp.heroku.model.Contact;
import com.herokuapp.heroku.mvp.ifc.ContactView;
import com.herokuapp.heroku.network.ServiceApi;
import com.herokuapp.heroku.network.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by antonyhilary on 09 October 2018
 */
public class ContactPresenter extends MvpBasePresenter<ContactView> {
    private Subscription subs;

    public void getContact(String id) {
        getView().showProgress();

        ServiceApi serviceApi = ServiceGenerator.createService(ServiceApi.class);
        subs = serviceApi.getContact(id)
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

                                JSONObject dataObj = jsonObject.getJSONObject("data");

                                getView().onLoadContact(
                                        dataObj.getString("id"),
                                        dataObj.getString("firstName"),
                                        dataObj.getString("lastName"),
                                        dataObj.getInt("age"),
                                        dataObj.getString("photo"));
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void deleteContact(String id) {
        getView().showProgress();

        ServiceApi serviceApi = ServiceGenerator.createService(ServiceApi.class);
        subs = serviceApi.deleteContact(id)
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

                                getView().onDeleteMessage(jsonObject.getString("message"), true);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String sJson = null;
                            try {
                                sJson = responseBodyResponse.errorBody().string();
                                jsonObject = new JSONObject(sJson);

                                getView().onDeleteMessage(jsonObject.getString("message"), false);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void addContact(String firstName, String lastName, int age, String photo) {
        getView().showProgress();

        ServiceApi serviceApi = ServiceGenerator.createService(ServiceApi.class);

        Contact contact;
        if (photo.isEmpty()) {
            contact = new Contact(firstName, lastName, age);
        } else {
            contact = new Contact(firstName, lastName, age, photo);
        }

        subs = serviceApi.saveContact(contact)
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

                                getView().onSaveMessage(jsonObject.getString("message"), true);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String sJson = null;
                            try {
                                sJson = responseBodyResponse.errorBody().string();
                                jsonObject = new JSONObject(sJson);

                                getView().onSaveMessage(jsonObject.getString("message"), false);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void editContact(String id, String firstName, String lastName, int age, String photo) {
        getView().showProgress();

        ServiceApi serviceApi = ServiceGenerator.createService(ServiceApi.class);

        Contact contact;
        if (photo.isEmpty()) {
            contact = new Contact(firstName, lastName, age);
        } else {
            contact = new Contact(firstName, lastName, age, photo);
        }

        subs = serviceApi.saveContactById(id, contact)
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

                                getView().onSaveMessage(jsonObject.getString("message"), true);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String sJson = null;
                            try {
                                sJson = responseBodyResponse.errorBody().string();
                                jsonObject = new JSONObject(sJson);

                                getView().onSaveMessage(jsonObject.getString("message"), false);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}