package hzst.android.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import hzst.android.entity.Contact;


/**
 * Created by Administrator on 2016/6/7.
 */
public class ContactUtil {
    private Context context;

    public ContactUtil(Context context) {
        this.context = context;
    }


    /**
     * 根据联系人电话查询联系人信息
     * @param phone
     * @return
     */
    public List<Contact> getContactByPhone(String phone) {
        String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + phone + "'";
        return query(selection);
    }

    /**
     * 根据联系人姓名查询联系人信息
     * @param name
     * @return
     */
    public List<Contact> getContactByName(String name){
        String selection = ContactsContract.PhoneLookup.DISPLAY_NAME + " = '" + name + "'";
        return query(selection);
    }

    private List<Contact> query(String selection){
        List<Contact> list = new ArrayList<>();
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};


        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,    // Which columns to return.
                selection, // WHERE clause.
                null,          // WHERE clause value substitution
                null);   // Sort order.

        if( cursor == null ) {
            return list;
        }
        for( int i = 0; i < cursor.getCount(); i++ )
        {
            Contact contact = new Contact();
            cursor.moveToPosition(i);

            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            contact.setName(name);
            contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            list.add(contact);
        }
        return list;
    }
}
