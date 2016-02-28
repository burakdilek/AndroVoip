package android.msku.mskuproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Message extends ListFragment implements AdapterView.OnItemClickListener {
    String[] contacts = {"ekozon","elift"};
    public Message() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.message_contacts_list_item,R.id.Text, contacts);
        ListView listView = getListView();
        listView.setAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Intent i = new Intent();
        i.setClass(this.getContext(), Send_message.class);
        String name = contacts[position];
        i.putExtra("name", contacts[position]);
        startActivity(i);
    }
}