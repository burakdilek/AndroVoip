package android.msku.mskuproject;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


import de.greenrobot.event.EventBus;

public class ConnectionManager extends Service {
    protected static final String TAG = "ConnectionManager";

    /*
    * SERVICE_NAME and HOST_NAME are your server details.
    * Make sure you edit this with your own
    * */
    protected static final String SERVICE_NAME = "alpha-labs.net";
    protected static final String HOST_NAME = "alpha-labs.net";

    public static AbstractXMPPConnection mConnection;
    private XMPPTCPConnectionConfiguration mConnectionConfiguration;

    private boolean startConnected = false;

    private final IBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {
        ConnectionManager mService() {
            return ConnectionManager.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return START_STICKY;
    }

    // Handles incoming events
    protected void onHandleIntent(Intent intent) {
        if(intent == null) {
            Log.e(TAG, "Stopped service");
            return;
        }

        // The event received is communicated via numbers
        // i.e. 0 signifies logging in, 1 disconnecting, etc.
        int event = intent.getIntExtra("event", 1);
        switch(event) {
            // login
            case 0:
                String username = intent.getStringExtra("username");
                String password = intent.getStringExtra("password");

                if(username != null && password != null) {
                    startLogin(username, password);
                }
                break;
            default:
                disconnect();
                break;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /*
    * startLogin creates the connection for the log in process.
    * First, a connection to the server must be established. After the connection
    *   is established, then only can you process the login details.
    * */
    private void startLogin(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnectionConfiguration = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(username, password) // The username and password supplied by the user
                        .setServiceName(SERVICE_NAME) // Service name
                        .setHost(HOST_NAME) // Server host name
                        .setPort(5222) // Incoming port (might depend on your XMPP server software)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.required) // Security mode is disabled for example purposes
                        .build();

                mConnection = new XMPPTCPConnection(mConnectionConfiguration);

                try {
                    mConnection.connect();
                    startConnected = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // If the connection is successful, we begin the login process
                if(startConnected) {
                    connectionLogin();
                    Log.e(TAG, "Connected");
                } else {
                    Log.e(TAG, "Unable to connect");
                }
            }
        }).start();
    }
    private boolean loggedIn = true;
    private void connectionLogin() {
        try {
            mConnection.login();
        } catch (Exception e) {
            loggedIn = false;
        }

        // If the login fails, we disconnect from the server
        if(!loggedIn) {
            Log.e(TAG, "Unable to login");

            disconnect();
            loggedIn = true;
        } else {
            // If the login succeeds, we implement the chat listener.
            // It's important to implement the listener here so we can receive messages sent to us
            //      when we're offline.
            createChatListener();
            // Callback to LoginScreen to change the UI to the ChatScreen listview
            EventBus.getDefault().post(new LoggedInEvent(true));
            Log.e(TAG, "Logged in");
        }
    }
    private MyChatMessageListener mChatMessageListener;
    private void createChatListener() {
        if(mConnection != null) {
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            chatManager.setNormalIncluded(false); // Eliminates a few debug messages
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    if (!createdLocally) {
                        mChatMessageListener = new MyChatMessageListener();
                        chat.addMessageListener(mChatMessageListener);
                        Log.e(TAG, "ChatListener created");
                    }
                }
            });
        }
    }
    private void disconnect() {
        if(mConnection != null && mConnection.isConnected()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mConnection.disconnect();
                    Log.e(TAG, "Connection disconnected");
                    return null;
                }
            }.execute();
        }
    }
}
