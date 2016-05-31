package android.msku.mskuproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddContact extends AppCompatActivity {
    EditText contactNameText, userNameText;
    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        contactNameText = (EditText) findViewById(R.id.contact_name);
        userNameText = (EditText) findViewById(R.id.user_name);
        addButton = (Button) findViewById(R.id.add_to_contacts_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = contactNameText.getText().toString();
                String userName = userNameText.getText().toString();
                ContactDBHelper mydb = new ContactDBHelper(getApplicationContext());
                mydb.insertContact(contactName,userName);
                finish();
            }
        });

    }
}
