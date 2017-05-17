package com.juliao.rm755833.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by schin on 17/05/2017.
 */

public final class ArquivoDB {

    private SharedPreferences sp;
    private final String ARQUIVO = "arquivoPreferencias";

    public ArquivoDB(Context context) throws Exception {
        if(context == null){
            throw new Exception("Informa o context para inicializar ArquivoDB");
        }else{
            sp = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        }
    }

    public void escreverChave(String key, String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String lerChave(String key){
        return sp.getString(key, "");
    }

    public boolean validarChave(String key){
        return sp.contains(key);
    }

    public void escreverTodasAsChaves(HashMap<String,String> map){
        for(Map.Entry<String, String> entry : map.entrySet()){
            escreverChave(entry.getKey(), entry.getValue());
        }
    }

    public HashMap<String, String> lerTodasAsChaves(String[] keys){
        HashMap<String, String> map = new HashMap<>();
        for(String chave : keys){
            map.put(chave, lerChave(chave));
        }
        return map;
    }

    public boolean validarTodasAsChaves(String[] keys){
        HashSet<Boolean> set = new HashSet<>();
        for(String chave : keys){
            if(validarChave(chave)){
                set.add(true);
            }else{
                set.add(false);
            }
        }
        return (set.size() == 1 && set.contains(true));
    }

}
