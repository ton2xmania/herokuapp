package com.herokuapp.heroku.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.herokuapp.heroku.R;
import com.herokuapp.heroku.model.Contact;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by antonyhilary on 09 October 2018
 */
public class ContactAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {
    private Context context;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(R.layout.row_contact, contacts);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Contact contact) {
        baseViewHolder.setText(R.id.tv_name, contact.getFirstName().concat(" ").concat(contact.getLastName()));
        baseViewHolder.setText(R.id.tv_age, "Usia " + contact.getAge() + " tahun");

        ImageView iv_contact = baseViewHolder.getView(R.id.iv_contact);
        Picasso.get().load(contact.getPhoto()).into(iv_contact);
    }
}