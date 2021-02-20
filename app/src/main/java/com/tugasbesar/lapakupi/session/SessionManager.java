package com.tugasbesar.lapakupi.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class SessionManager {
    private static final String ID_AKUN = "id_akun";
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void loginSession(String id_akun) {
        editor.putString(ID_AKUN, id_akun);
        editor.commit();
    }

    public String getSession() {
        String id_akun = sharedPreferences.getString(ID_AKUN, null);
        return id_akun;
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
