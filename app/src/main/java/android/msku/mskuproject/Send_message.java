package android.msku.mskuproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Send_message extends AppCompatActivity {
    Toolbar toolbar;
    String receiverName;
    ArrayAdapter adapter;
    final ArrayList<String> messageList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        toolbar = (Toolbar) findViewById(R.id.toolbar_send_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                receiverName = null;
            } else {
                receiverName = extras.getString("name");
            }
        } else {
            receiverName = (String) savedInstanceState.getSerializable("name");
        }

        ListView listView = (ListView) findViewById(R.id.message_list);


        adapter = new ArrayAdapter<String>(this, R.layout.message_text_list_item, R.id.message_text, messageList);
        listView.setAdapter(adapter);
        System.out.println("Receivername : " + receiverName);
        toolbar.setTitle(receiverName);
        final EditText smsContent = (EditText) findViewById(R.id.messageContent);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageList.add(smsContent.getText().toString());
                adapter.notifyDataSetChanged();
                smsContent.setText(null);
            }
        });

    }
}
