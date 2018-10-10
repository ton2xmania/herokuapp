package com.herokuapp.heroku.mvp.ifc;

import com.herokuapp.heroku.model.BaseView;
import com.herokuapp.heroku.model.Contact;

import java.util.List;

/**
 * Created by antonyhilary on 09 October 2018
 */
public interface MainView extends BaseView {
    void setContacts(List<Contact> contacts);
}