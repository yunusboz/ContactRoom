package com.yunus.contactroom.data;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.yunus.contactroom.model.Contact;
import com.yunus.contactroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application){
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData(){return allContacts;}

    public void insert(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(() ->{
            contactDao.insert(contact);
        });
    }
}
