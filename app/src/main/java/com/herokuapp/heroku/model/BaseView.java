package com.herokuapp.heroku.model;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by antonyhilary on 09 October 2018
 */
public interface BaseView extends MvpView {
    void initObjects();

    void showProgress();

    void hideProgress();

    boolean checkConnection();
}