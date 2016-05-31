package android.msku.mskuproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.utils.NgnUriUtils;


public class Call extends Fragment {
    static EditText no;
    Button callb, bosb;
    MainPage mp;


    public final static String EXTRAT_SIP_SESSION_ID = "SipSession";

    public Call() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_call, container, false);


        callb = (Button) rootView.findViewById(R.id.callButton);
        no = (EditText) rootView.findViewById(R.id.editText);
        bosb = (Button) rootView.findViewById(R.id.bos);


        mp = new MainPage();

        callb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(no.length() <= 2)) {
                    makeVoiceCall(no.getText().toString());
                }
            }
        });
        bosb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no.setEnabled(true);
                no.requestFocus();

            }
        });


        return rootView;
    }

    boolean makeVoiceCall(String phoneNumber) {

        final String validUri = NgnUriUtils.makeValidSipUri(String.format("sip:%s@%s", phoneNumber, MainPage.realm));
        if (validUri == null) {
            Log.i("TAG", "failed to normalize sip uri '" + phoneNumber + "'");
            return false;
        }
        NgnAVSession avSession = NgnAVSession.createOutgoingSession(mp.service.getSipStack(), NgnMediaType.Audio);

        Intent i = new Intent();
        i.setClass(this.getContext(), CallScreen.class);
        i.putExtra(EXTRAT_SIP_SESSION_ID, avSession.getId());
        startActivity(i);

        return avSession.makeCall(validUri);
    }
}