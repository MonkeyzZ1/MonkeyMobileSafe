package com.cskaoyan.mobilesafe.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.monkeyzzi.monkeymobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends ActionBarActivity {
    private ListView lv_list;
    private ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        lv_list = (ListView) findViewById(R.id.lv_list);
        readContact = readContact();
        lv_list.setAdapter(new SimpleAdapter(this, readContact,
                R.layout.contact_list_item,
                new String[]{"name", "phone"},
                new int[]{R.id.tv_name, R.id.tv_phone}));
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = readContact.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(0,intent);//将数据放在intent中返回给上一个页面

                finish();
            }
        });

    }
    private ArrayList<HashMap<String, String>> readContact(){
        //首先，从raw_contacts中读取联系人的id
        //再从data中读取联系人的手机号码
        //然后根据mimetype哪个是联系人，哪个是电话号码
        Uri rawContacts = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        Cursor rawContactsCursor = getContentResolver().query(rawContacts, new String[]{"contact_id"}, null, null, null);
        if (rawContactsCursor!=null){
            while (rawContactsCursor.moveToNext()){
                String contactId = rawContactsCursor.getString(0);
                //Log.i("联系人:",contactId+"");
                Cursor dataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);

                if (dataCursor!=null){
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()){
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);

                        Log.i("data1+mimetype:", data1 + mimetype);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                            map.put("phone",data1);

                        }else if ("vnd.android.cursor.item/name".equals(mimetype)){
                            map.put("name",data1);
                        }
                    }
                    list.add(map);
                    dataCursor.close();
                }

            }
            rawContactsCursor.close();
        }
        return list;
    }
}
