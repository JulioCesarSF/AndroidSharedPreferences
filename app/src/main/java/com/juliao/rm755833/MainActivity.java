package com.juliao.rm755833;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.juliao.rm755833.utils.ArquivoDB;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail;
    private RadioGroup rgPagamento;
    private CheckBox cbAceite;

    //arquivoDB
    private ArquivoDB arquivoDB;
    private String[] chaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initArquivoDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregar();
    }

    private void initArquivoDB() {
        try {
            arquivoDB = new ArquivoDB(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        chaves = getResources().getStringArray(R.array.chaves);
    }

    private void initViews() {
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        rgPagamento = (RadioGroup) findViewById(R.id.rgPagamento);
        cbAceite = (CheckBox) findViewById(R.id.cbAceiteContrato);
    }

    public void salvar(View view) {
        if (!validarCampos()) {
            Toast.makeText(this, R.string.preencha, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(chaves[0], edtNome.getText().toString());
            map.put(chaves[1], edtEmail.getText().toString());
            RadioButton rb = (RadioButton) findViewById(rgPagamento.getCheckedRadioButtonId());
            map.put(chaves[2], rb.getText().toString());
            map.put(chaves[3], (cbAceite.isChecked()) ? getString(R.string.sim) : getString(R.string.nao));
            arquivoDB.escreverTodasAsChaves(map);
            Toast.makeText(this, R.string.salvo, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.erro).concat(e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    public void carregar() {
        if (!arquivoDB.validarTodasAsChaves(chaves)) {
            Toast.makeText(this, R.string.pref_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            HashMap<String, String> map = arquivoDB.lerTodasAsChaves(chaves);
            edtNome.setText(map.get(chaves[0]));
            edtEmail.setText(map.get(chaves[1]));
            for (int i = 0; i < rgPagamento.getChildCount(); i++) {
                if (rgPagamento.getChildAt(i) instanceof RadioButton) {
                    RadioButton rb = (RadioButton) rgPagamento.getChildAt(i);
                    if (rb.getText().toString().equals(map.get(chaves[2]))) {
                        rb.setChecked(true);
                    }
                }
            }

            if (map.get(chaves[3]).equals(getString(R.string.sim))) {
                cbAceite.setChecked(true);
            }

            Toast.makeText(this, R.string.sucesso, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.erro).concat(e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()
                && !TextUtils.isEmpty(edtNome.getText().toString())
                && rgPagamento.getCheckedRadioButtonId() != -1)
            return true;
        return false;
    }
}
