package at.jku.stockticker;

import java.util.HashMap;
 

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import at.jku.stockticker.ui.LoginActivity;
 
public class SessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
     
    private static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "StockTicker";
    private static final String IS_LOGIN = "IsLogged";
  
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "pwd";
     
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    public void createLoginSession(String name, String password){
        editor.putBoolean(IS_LOGIN, true);
         
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD, password);
        
        editor.commit();
    }   

    public void checkLogin(){
        if(!this.isLogged()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            _context.startActivity(i);
        }
         
    }
     
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
         
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    public boolean isLogged(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}