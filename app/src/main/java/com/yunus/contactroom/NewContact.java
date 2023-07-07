package com.yunus.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.yunus.contactroom.model.Contact;
import com.yunus.contactroom.model.ContactViewModel;

public class NewContact extends AppCompatActivity {

    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION = "occupation";
    private int contactId = 0;
    private Boolean isEdit = false;
    private EditText enterName, enterOccupation;
    private Button saveButton, updateButton, deleteButton;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveButton = findViewById(R.id.save_button);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this.getApplication())
                .create(ContactViewModel.class);

        Bundle retrievedData = getIntent().getExtras();
        if (getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);

            contactViewModel.getOneContact(contactId).observe(this, contact -> {
                if (contact != null) {
                    enterName.setText(contact.getName());
                    enterOccupation.setText(contact.getOccupation());
                }
            });
            isEdit = true;
        }

        if (isEdit) {
            saveButton.setVisibility(View.GONE);
        } else {
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }


        saveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (!TextUtils.isEmpty(enterName.getText())
                    && !TextUtils.isEmpty(enterOccupation.getText())) {

                String name = enterName.getText().toString();
                String occupation = enterOccupation.getText().toString();

                replyIntent.putExtra(NAME_REPLY, name);
                replyIntent.putExtra(OCCUPATION, occupation);
                setResult(RESULT_OK, replyIntent);

                //ContactViewModel.insert(contact);
            } else {
                setResult(RESULT_CANCELED, replyIntent);
                //Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
            }

            finish();

        });

        updateButton.setOnClickListener(view -> {
            edit(false);
        });

        deleteButton.setOnClickListener(view -> {
            edit(true);
        });


    }

    private void edit(Boolean isDelete) {
        int id = contactId;
        String name = enterName.getText().toString().trim();
        String occupation = enterOccupation.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)) {
            Snackbar.make(enterName, R.string.empty, Snackbar.LENGTH_SHORT).show();
        } else {
            Contact contact = new Contact(id, name, occupation);
            if (isDelete)
                ContactViewModel.deleteContact(contact);
            else
                ContactViewModel.updateContact(contact);
            finish();
        }
    }
}