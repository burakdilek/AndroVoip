package android.msku.mskuproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;


public class Messaging extends ListFragment {

    protected static final String TAG = "ChatScreen";

    // Our list view adapter for chat messages
    private ChatAdapter mChatAdaper;
    private List<ChatItem> mChatList = new ArrayList<>();
    private boolean messageSent = false;


    public static EditText mToUsername, mMessageInput;
    private Button mSendButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_screen_layout, container, false);


        // The message you want to send
        mMessageInput = (EditText) view.findViewById(R.id.yourMessage);

        // The address of the person you want to send the message to.
        // DISCLAIMER: The address should include the service name as well (i.e. username@server.example.com/Smack)
        mToUsername = (EditText) view.findViewById(R.id.toUsername);

        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You should be checking for length and valid inputs here
                // In this basic example, we are just sending a message to the receipient.
                sendMessage(mToUsername.getText().toString(), mMessageInput.getText().toString());
            }
        });
        mMessageInput.requestFocus();
        return view;
    }

    // Instance is used here to allow adapter updates when an incoming message is received.
    private static Messaging inst;
    public static Messaging instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
        mChatAdaper = new ChatAdapter(getActivity(), mChatList);
        getListView().setAdapter(mChatAdaper);
    }

    /*
    * Adapter is updated when a message is sent or received.
    * Called in MyChatMessageListener (incoming messages) and when you send a message
    * */
    public void updateChatList(String sender, String message) {
        mChatList.add(new ChatItem(sender, message));
        mChatAdaper.notifyDataSetChanged();
    }

    /*
    * This method sends the message you have written to the receipient.
    * 'address' - the recipients address
    * DISCLAIMER: The address should include the service root as well (i.e. username@server.example.com/Smack)
    * */
    public void sendMessage(String address, String chat_message) {
        // Listview is updated with our new message
        updateChatList(ConnectionManager.mConnection.getUser(), chat_message);

        ChatManager chatManager = ChatManager.getInstanceFor(ConnectionManager.mConnection);

        final Chat newChat = chatManager.createChat(address+"@alpha-labs.net");

        // We're creating a message object. You can also use newChat.sendMessage() if needed
        // With a message object, you can also set the subject and a variety of other options
        final Message message = new Message();
        message.setBody(chat_message);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // We send the message here.
                // You should also check if the username is valid here.
                try {
                    newChat.sendMessage(message);
                    messageSent = true;
                } catch (SmackException.NotConnectedException e) {
                    Log.e(TAG, "Unable to send chat: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(messageSent) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.sent), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.unableToSent), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
