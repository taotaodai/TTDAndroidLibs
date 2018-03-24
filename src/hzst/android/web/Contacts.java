package hzst.android.web;

import android.content.Context;

import com.google.gson.Gson;

import hzst.android.util.ContactUtil;

/**
 * Created by Administrator on 2016/6/7.
 */
public class Contacts extends BaseJsInterface{
    private ContactUtil contactUtil;

    public Contacts(Context context) {
        super(context);
        name = getClassName(this);
        contactUtil = new ContactUtil(context);
    }

    public void getContactsByPhone(String phone){
        Gson gson = new Gson();
        String contacts = gson.toJson(contactUtil.getContactByPhone(phone));
        getWebExample().executeJsFunction(getWebExample().baseJsInterface.operation.getOnSuccess(), contacts);
    }

//    public String getAllContacts(){
//        Gson gson = new Gson();
//        return gson.toJson(contactUtil.getAllContacts());
//    }
}
