package com.herokuapp.heroku.mvp.ifc;

import com.herokuapp.heroku.model.BaseView;

/**
 * Created by antonyhilary on 09 October 2018
 */
public interface ContactView extends BaseView {
    void onLoadContact(String id, String firstName, String lastName, int age, String photo);

    void onSaveMessage(String message, boolean isSuccess);

    void onDeleteMessage(String message, boolean isDeleted);

    void onValidationForm(String formType);
}