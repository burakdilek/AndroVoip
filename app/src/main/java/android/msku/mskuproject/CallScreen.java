package android.msku.mskuproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.sip.NgnInviteSession;

public class CallScreen extends AppCompatActivity {

    private final NgnEngine engine;
    private static final String TAG = CallScreen.class.getCanonicalName();
    public static CallScreen This;
    private NgnAVSession session;
    private TextView mTvInfo;
    private TextView mTvRemote;
    private Button hangUp,speakerButton, muteButton;
    private boolean is_in_call = false;

    private BroadcastReceiver broadcastReceiver;


    public CallScreen(){
        super();
        engine = NgnEngine.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);
        mTvInfo = (TextView) findViewById(R.id.sta_accepted);
        hangUp = (Button) findViewById(R.id.hngUpbutton);
        mTvRemote = (TextView) findViewById(R.id.callscreen_remote);
        speakerButton = (Button) findViewById(R.id.speaker);
        muteButton = (Button)findViewById(R.id.mute);
        This = this;

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            session = NgnAVSession.getSession(extras.getLong(Call.EXTRAT_SIP_SESSION_ID));
        }
        if(session == null){
            Log.e(TAG, "Null session");
            finish();
            return;
        }
        session.incRef();
        session.setContext(this);
        speakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isSpeakerOn()) {
                    session.setSpeakerphoneOn(false);
                } else {
                    session.setSpeakerphoneOn(true);
                }
            }
        });
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isMicrophoneMute()){
                    session.setMicrophoneMute(false);
                } else {
                    session.setMicrophoneMute(true);
                }
            }
        });
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleSipEvent(intent);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NgnInviteEventArgs.ACTION_INVITE_EVENT);
        registerReceiver(broadcastReceiver, intentFilter);

        hangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session != null) {
                    session.hangUpCall();
                }
                finish();
            }
        });
        mTvRemote.setText(session.getRemotePartyDisplayName());
        mTvInfo.setText(getStateDesc(session.getState()));

    }


    private String getStateDesc(NgnInviteSession.InviteState state){
        switch(state){
            case NONE:
            default:
                return "Unknown";
            case INCOMING:

                return "Gelen Arama";
            case INPROGRESS:
                return "Aranıyor";
            case REMOTE_RINGING:
                return "Çalıyor";
            case EARLY_MEDIA:
                return "Early media";
            case INCALL:
                return "Arama";
            case TERMINATING:
                return "Terminating";
            case TERMINATED:
                return "Sonlandırıldı";
        }
    }



    private void handleSipEvent(Intent intent){
        if(session == null){
            Log.e(TAG, "Invalid session object");
            return;
        }
        final String action = intent.getAction();
        if(NgnInviteEventArgs.ACTION_INVITE_EVENT.equals(action)){
            NgnInviteEventArgs args = intent.getParcelableExtra(NgnInviteEventArgs.EXTRA_EMBEDDED);
            if(args == null){
                Log.e(TAG, "Invalid event args");
                return;
            }
            if(args.getSessionId() != session.getId()){
                return;
            }

            final NgnInviteSession.InviteState callState = session.getState();
            mTvInfo.setText(getStateDesc(callState));
            switch(callState){
                case REMOTE_RINGING:
                    engine.getSoundService().startRingBackTone();
                    break;
                case INCOMING:
                    break;
                case EARLY_MEDIA:
                case INCALL:
                    is_in_call = true;
                    engine.getSoundService().stopRingTone();
                    engine.getSoundService().stopRingBackTone();
                    session.setSpeakerphoneOn(false);
                    break;
                case INPROGRESS:
                    engine.getSoundService().startRingBackTone();
                case TERMINATING:

                case TERMINATED:
                    if(is_in_call){
                        finish();
                        is_in_call = false;
                    }
                    engine.getSoundService().stopRingTone();
                    engine.getSoundService().stopRingBackTone();

                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        if(session != null){
            final NgnInviteSession.InviteState callState = session.getState();
            mTvInfo.setText(getStateDesc(callState));
            if(callState == NgnInviteSession.InviteState.TERMINATING || callState == NgnInviteSession.InviteState.TERMINATED){
                finish();
            }
        }
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy()");
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }

        if(session != null){
            session.setContext(null);
            session.decRef();
        }
        super.onDestroy();
    }

}
