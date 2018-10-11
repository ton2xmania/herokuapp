package com.herokuapp.heroku.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.herokuapp.heroku.R;
import com.herokuapp.heroku.adapter.ContactAdapter;
import com.herokuapp.heroku.model.Contact;
import com.herokuapp.heroku.mvp.ifc.MainView;
import com.herokuapp.heroku.mvp.imp.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initObjects();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            getPresenter().getAllContacts();
        }
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void setContacts(final List<Contact> contacts) {
        rv_contact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_contact.setAdapter(new ContactAdapter(this, contacts));

        rv_contact.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, ContactFormActivity.class);
                intent.putExtra("mode", "edit");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", contacts.get(position).getId());
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void initObjects() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_contact.setLayoutManager(linearLayoutManager);

        if (checkConnection()) {
            getPresenter().getAllContacts();
        } else {
            Toast.makeText(this, "Make sure you're connected to stable connection", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkConnection()) {
                    getPresenter().getAllContacts();
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @OnClick(R.id.btn_add_contact)
    void onClick() {
        Intent intent = new Intent(this, ContactFormActivity.class);
        intent.putExtra("mode", "add");
        startActivityForResult(intent, 1);
    }

    @BindView(R.id.rv_contact)
    RecyclerView rv_contact;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
}