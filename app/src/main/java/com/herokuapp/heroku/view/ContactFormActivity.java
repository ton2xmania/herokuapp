package com.herokuapp.heroku.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.herokuapp.heroku.R;
import com.herokuapp.heroku.mvp.ifc.ContactView;
import com.herokuapp.heroku.mvp.imp.ContactPresenter;
import com.herokuapp.heroku.utilities.Utils;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactFormActivity extends MvpActivity<ContactView, ContactPresenter> implements ContactView, IPickResult {
    private ProgressDialog progressDialog;
    private String base64Photo = "";
    private File photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initObjects();
    }

    @NonNull
    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void initObjects() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        if (getIntent().getStringExtra("mode").equals("edit")) {
            if (checkConnection()) {
                getPresenter().getContact(getIntent().getStringExtra("id"));
            } else {
                Toast.makeText(this, "Make sure you're connected to stable connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
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
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            photo = new File(pickResult.getPath());
            base64Photo = Utils.convert(pickResult.getBitmap());
            iv_contact.setImageBitmap(pickResult.getBitmap());

            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(photo));
                pickResult.getBitmap().compress(Bitmap.CompressFormat.JPEG, 80, os);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.btn_add_photo, R.id.btn_save_contact})
    void onClick(View view) {
        if (view.getId() == R.id.btn_add_photo) {
            PickImageDialog.build(new PickSetup()).show(this);
        }

        if (view.getId() == R.id.btn_save_contact) {
            if (getIntent().getStringExtra("mode").equals("add")) {
                getPresenter().addContact(
                        et_firstname.getText().toString(),
                        et_lastname.getText().toString(),
                        Integer.parseInt(et_age.getText().toString()),
                        base64Photo);
            } else {
                getPresenter().editContact(
                        getIntent().getStringExtra("id"),
                        et_firstname.getText().toString(),
                        et_lastname.getText().toString(),
                        Integer.parseInt(et_age.getText().toString()),
                        base64Photo);
            }
        }
    }


    @Override
    public void onLoadContact(String id, String firstName, String lastName, int age, String photo) {
        et_firstname.setText(firstName);
        et_lastname.setText(lastName);
        et_age.setText(String.valueOf(age));
        if (!photo.equals("N/A")) {
            Picasso.get().load(photo).into(iv_contact);
        }
    }

    @Override
    public void onSaveMessage(String message, final boolean isSuccess) {
        Utils.getDialog(this, "Info", message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isSuccess) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }).show();
    }

    @Override
    public void onDeleteMessage(String message, final boolean isDeleted) {
        Utils.getDialog(this, "Info", message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDeleted) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }).show();
    }

    @Override
    public void onValidationForm(String formType) {
        switch (formType) {
            case "firstname":
                Utils.getDialog(this, "Info", "Firstname cannot be blank")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_firstname.requestFocus();
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case "lastname":
                Utils.getDialog(this, "Info", "Lastname cannot be blank")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_lastname.requestFocus();
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case "age":
                Utils.getDialog(this, "Info", "Age cannot be blank")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                et_age.requestFocus();
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getStringExtra("mode").equals("edit")) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                Utils.getDialog(this, "Info", "Are you sure to delete this contact?")
                        .setPositiveButton("Yeah, I'm sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPresenter().deleteContact(getIntent().getStringExtra("id"));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @BindView(R.id.et_firstname)
    TextInputEditText et_firstname;
    @BindView(R.id.et_lastname)
    TextInputEditText et_lastname;
    @BindView(R.id.et_age)
    TextInputEditText et_age;
    @BindView(R.id.iv_contact)
    ImageView iv_contact;
}