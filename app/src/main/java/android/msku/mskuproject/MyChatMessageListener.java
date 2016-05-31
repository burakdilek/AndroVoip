package android.msku.mskuproject;

import android.util.Log;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/*
* MyChatMessageListener directs the incoming messages to the appropriate container.
* In this case, messages are contained in the ChatList
* */
public class MyChatMessageListener implements ChatMessageListener {

    protected static final String TAG = "MyChatMessageListener";

    @Override
    public void processMessage(Chat chat, Message message) {
        final String mChatSender = message.getFrom();
        final String mChatMessage = message.getBody();

        Log.e(TAG, mChatSender + ": " + mChatMessage);

        // We add the chat messages to the list and update the adapter on receive
        MainPage.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Messaging.instance().updateChatList(mChatSender, mChatMessage);
            }
        });
    }
}
