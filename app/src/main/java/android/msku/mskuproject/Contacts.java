package android.msku.mskuproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.utils.NgnUriUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class Contacts extends ListFragment {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    ContactDBHelper mydb;
    MainPage mp;
    Bundle dataBundle;
    FloatingActionButton callFab, deleteFab, updateFab, mesFab;
    Button updateButton;
    EditText UNText, CNText;
    ArrayList<HashMap<String, String>> contactList;
    private Dialog dialog;
    int id_To_Search;
    SimpleAdapter adapter;
    ArrayList array_list;
    public Contacts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_display_contact);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(CNText.isEnabled()){
                    UNText.setEnabled(false);
                    CNText.setEnabled(false);
                    updateButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        mp = new MainPage();
        callFab = (FloatingActionButton) dialog.findViewById(R.id.con_call_fab);
        deleteFab = (FloatingActionButton) dialog.findViewById(R.id.con_del_fab);
        updateFab = (FloatingActionButton) dialog.findViewById(R.id.con_update_fab);
        mesFab = (FloatingActionButton) dialog.findViewById(R.id.con_mes_fab);
        UNText = (EditText) dialog.findViewById(R.id.user_name_display);
        CNText = (EditText) dialog.findViewById(R.id.contact_name_display);
        updateButton = (Button) dialog.findViewById(R.id.con_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb.updateContact(id_To_Search,CNText.getText().toString(),UNText.getText().toString());
                notifyList();
                dialog.cancel();

            }
        });
        mesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TabLayout.Tab tab = MainPage.tabLayout.getTabAt(2);
                tab.select();
                Messaging.mToUsername.setText(UNText.getText());
                Messaging.mMessageInput.requestFocus();
                dialog.cancel();

            }
        });
        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                makeVoiceCall(UNText.getText().toString());
            }
        });
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                mydb.deleteContact(id_To_Search);
                notifyList();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getActivity().getApplicationContext(), AddContact.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        updateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UNText.setEnabled(true);
                CNText.setEnabled(true);
                updateButton.setVisibility(View.VISIBLE);
                CNText.setSelection(CNText.getText().length());


            }
        });

        return rootView;
    }
    private void notifyList(){
        array_list = mydb.getAllCotacts();
        adapter = new SimpleAdapter(
                getActivity(), array_list,
                R.layout.contact_list_item, new String[] { "CN", "UN"
        }, new int[] { R.id.contacText, R.id.numberText });
        obj.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        notifyList();

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
        i.putExtra(Call.EXTRAT_SIP_SESSION_ID, avSession.getId());
        startActivity(i);

        return avSession.makeCall(validUri);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        obj = getListView();
        mydb = new ContactDBHelper(getActivity());
        array_list = mydb.getAllCotacts();
        adapter = new SimpleAdapter(
                getActivity(), array_list,
                R.layout.contact_list_item, new String[] { "CN", "UN"
        }, new int[] { R.id.contacText, R.id.numberText });

        obj.setAdapter(adapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dialog.show();
                // TODO Auto-generated method stub
                id_To_Search = Integer.valueOf((String)((HashMap)array_list.get(arg2)).get("ID"));
                CNText.setText((String)((HashMap)array_list.get(arg2)).get("CN"));
                UNText.setText((String)((HashMap)array_list.get(arg2)).get("UN"));


                dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);
            }
        });
    }
}

