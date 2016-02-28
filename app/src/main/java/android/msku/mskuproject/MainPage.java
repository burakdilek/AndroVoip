package android.msku.mskuproject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.events.NgnInviteEventTypes;
import org.doubango.ngn.events.NgnMessagingEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.sip.NgnInviteSession;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.ngn.utils.NgnStringUtils;
import org.doubango.tinyWRAP.tdav_codec_id_t;

import java.util.ArrayList;
import java.util.List;

//Todo: Should be handled empty username and password

public class MainPage extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Dialog incoming_dialog;
    private boolean is_in_call = false;


    EditText number;
    Button callb,acceptButton,rejectButton;
    TextView incoming_info;

    String publicId, realm, port, proxy, username, password;

    private static String TAG = LoginActivity.class.getCanonicalName();
    private BroadcastReceiver broadcastReceiver;

    public final INgnSipService service;
    private final NgnEngine engine;
    private final INgnConfigurationService confService;

    private NgnAVSession mAVSession;

    public MainPage() {
        engine = NgnEngine.getInstance();
        confService = engine.getConfigurationService();
        service = engine.getSipService();

        // Enable G.711, RED, T.140
        confService.putInt(NgnConfigurationEntry.MEDIA_CODECS,
                tdav_codec_id_t.tdav_codec_id_pcma.swigValue() |
                        tdav_codec_id_t.tdav_codec_id_pcmu.swigValue() |
                        tdav_codec_id_t.tdav_codec_id_red.swigValue() |
                        tdav_codec_id_t.tdav_codec_id_t140.swigValue()
        );
        confService.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        incoming_dialog = new Dialog(MainPage.this);
        incoming_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        incoming_dialog.setContentView(R.layout.incoming_dialog);
        incoming_dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        acceptButton = (Button)incoming_dialog.findViewById(R.id.accept_dialog);
        rejectButton = (Button)incoming_dialog.findViewById(R.id.reject_dialog);
        incoming_info = (TextView)incoming_dialog.findViewById(R.id.info_dialog);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAVSession.acceptCall();
                Intent i = new Intent();
                i.setClass(MainPage.this, CallScreen.class);
                i.putExtra(Call.EXTRAT_SIP_SESSION_ID, mAVSession.getId());
                startActivity(i);
                incoming_dialog.dismiss();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAVSession.hangUpCall();
                incoming_dialog.dismiss();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        port = getIntent().getStringExtra("port");
        realm = getIntent().getStringExtra("domain");
        proxy = getIntent().getStringExtra("proxy");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        System.out.println(username + " " + password + " " + realm + " " + port + " " + proxy + " " + publicId);


        engine.start();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                final String action = intent.getAction();

                //Registration Part
                if (NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT.equals(action)) {
                    NgnRegistrationEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
                    if (args == null) {
                        Log.e(TAG, "Invalid event args");
                        return;
                    }
                    toolbar.setTitle(username);
                    switch (args.getEventType()) {
                        case REGISTRATION_NOK:
                            tabLayout.setBackgroundColor(Color.RED);
                            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                            toolbar.setBackgroundColor(Color.RED);
                            toolbar.setTitleTextColor(Color.LTGRAY);
                            break;
                        case UNREGISTRATION_OK:
                            tabLayout.setBackgroundColor(Color.RED);
                            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                            toolbar.setBackgroundColor(Color.RED);
                            toolbar.setTitleTextColor(Color.LTGRAY);
                            System.out.println(username + " " + password + " " + realm + " " + port + " " + proxy + " " + publicId);
                            break;
                        case REGISTRATION_OK:
                            //    status.setText("You are now registered");
                            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            toolbar.setTitleTextColor(Color.GREEN);
                            break;
                        case REGISTRATION_INPROGRESS:
                            //  status.setText("Trying to register...");
                            tabLayout.setBackgroundColor(getResources().getColor(R.color.tryingcolor));
                            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
                            toolbar.setBackgroundColor(getResources().getColor(R.color.tryingcolor));
                            break;
                        case UNREGISTRATION_INPROGRESS:
                            //  status.setText("Trying to unregister...");
                            break;
                        case UNREGISTRATION_NOK:
                            //  status.setText("Failed to unregister :(");
                            break;
                    }
                } else if (NgnInviteEventArgs.ACTION_INVITE_EVENT.equals(action)) {
                    NgnInviteEventArgs args = intent.getParcelableExtra(NgnInviteEventArgs.EXTRA_EMBEDDED);
                    if (args == null) {
                        Log.e(TAG, "Invalid event args");
                        return;
                    }
                    if (mAVSession == null && args.getEventType() == NgnInviteEventTypes.INCOMING) {
                        mAVSession = NgnAVSession.getSession(args.getSessionId());
                    }

                    if (mAVSession == null || args.getSessionId() != mAVSession.getId()) {
                        return;
                    }
                    final NgnInviteSession.InviteState callState = mAVSession.getState();
                    switch (callState) {
                        case REMOTE_RINGING:
                            engine.getSoundService().startRingBackTone();
                            break;
                        case INCOMING:
                            incoming_info.setText(mAVSession.getRemotePartyDisplayName());
                            incoming_dialog.show();
                            engine.getSoundService().startRingTone();

                            break;
                        case EARLY_MEDIA:
                        case INCALL:
                            is_in_call = true;
                            engine.getSoundService().stopRingTone();
                            engine.getSoundService().stopRingBackTone();
                            mAVSession.setSpeakerphoneOn(false);
                            break;
                        case TERMINATING:
                        case TERMINATED:
                            engine.getSoundService().stopRingTone();
                            engine.getSoundService().stopRingBackTone();
                            if(is_in_call){
                                CallScreen.This.finish();
                                is_in_call = false;
                            }
                            mAVSession = null;
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
        intentFilter.addAction(NgnInviteEventArgs.ACTION_INVITE_EVENT);
        intentFilter.addAction(NgnMessagingEventArgs.ACTION_MESSAGING_EVENT);
        registerReceiver(broadcastReceiver, intentFilter);
        registerAuth();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Call(), "CALL");
        adapter.addFragment(new Contacts(), "CONT");
        adapter.addFragment(new Message(), "MSG");
        viewPager.setAdapter(adapter);
    }

    public void registerAuth() {
        if (engine.isStarted()) {
            if (!service.isRegistered()) {
                //Set User Credentials
                String userId = "sip:" + username + "@sip2sip.info";

                confService.putString(NgnConfigurationEntry.IDENTITY_IMPU,
                        userId);
                confService.putString(NgnConfigurationEntry.IDENTITY_IMPI,
                        username);
                confService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD,
                        password);
                confService.putString(NgnConfigurationEntry.NETWORK_PCSCF_HOST,
                        proxy);
                confService.putInt(NgnConfigurationEntry.NETWORK_PCSCF_PORT,
                        NgnStringUtils.parseInt(port, 5060));
                confService.putString(NgnConfigurationEntry.NETWORK_REALM,
                        realm);

                //Commiting is important when you change the parameters.
                confService.commit();

                service.register(MainPage.this);
            } else {
                // unregister (log out)
                service.unRegister();
            }
        } else {
            // status.setText("Engine not started yet");
        }
    }
    private String getStateDesc(NgnInviteSession.InviteState state) {
        switch (state) {
            case NONE:
            default:
                return "Unknown";
            case INCOMING:
                return "Incoming";
            case INPROGRESS:
                return "Inprogress";
            case REMOTE_RINGING:
                return "Ringing";
            case EARLY_MEDIA:
                return "Early media";
            case INCALL:
                return "In Call";
            case TERMINATING:
                return "Terminating";
            case TERMINATED:
                return "termibated";
        }
    }
    @Override
    protected void onDestroy() {
        // Stops the engine
        if (engine.isStarted()) {
            engine.stop();
        }
        // hangup the call
        if (mAVSession != null) {
            mAVSession.setContext(null);
            if (mAVSession.isConnected()) {
                mAVSession.hangUpCall();
            }
        }
        // release the listener
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Starts the engine
        if (!engine.isStarted()) {
            if (engine.start()) {
                // status.setText("Engine started");
            } else {
                // status.setText("Failed to start the engine");
            }
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

