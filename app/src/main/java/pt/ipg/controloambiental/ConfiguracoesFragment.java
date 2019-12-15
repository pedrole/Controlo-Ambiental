package pt.ipg.controloambiental;


import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguracoesFragment extends DialogFragment implements View.OnClickListener {



    public static final String TITULO = "titulo";
    private static final String LOCAL = "local";
    private EditText editTextFrequencia;
    private EditText editTextTemperaturaMinima, editTextTemperaturaMaxima;

    public ConfiguracoesFragment() {
        // Required empty public constructor
    }
    public interface ConfiguracoesDialogListener {
        void onFinishConfiguracoesDialog();
    }

    public static ConfiguracoesFragment newInstance(Local local, String titulo) {
        ConfiguracoesFragment frag = new ConfiguracoesFragment();
        Bundle args = new Bundle();
        args.putParcelable(LOCAL,local);
        args.putString(TITULO,titulo);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getArguments().getString(TITULO, "Enter Name"));
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String title = getArguments().getString(TITULO, "Enter Name");
        getDialog().setTitle(title);
        return inflater.inflate(R.layout.fragment_configuracoes, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextFrequencia = (EditText) view.findViewById(R.id.editTextFrequencia);
        Local local = getArguments().getParcelable(LOCAL);
        editTextFrequencia.setText(String.valueOf(local.getFrequencia()));
        editTextTemperaturaMinima = (EditText) view.findViewById(R.id.editTextTemperaturaMinima);
        editTextTemperaturaMaxima = (EditText) view.findViewById(R.id.editTextTemperaturaMaxima);
        editTextTemperaturaMinima.setText(String.valueOf( local.getTemperaturaMinima()));
        editTextTemperaturaMaxima.setText(String.valueOf( local.getTemperaturaMaxima()));
        view.findViewById(R.id.botaoCancelar).setOnClickListener(this);
        view.findViewById(R.id.botaoGuardar).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.botaoCancelar) dismiss();
        else{
            Local local = getArguments().getParcelable(LOCAL);
            local.setTemperaturaMinima(Double.parseDouble(editTextTemperaturaMinima.getText().toString()));
            local.setTemperaturaMaxima(Double.parseDouble(editTextTemperaturaMaxima.getText().toString()));
            local.setFrequencia(Integer.parseInt(editTextFrequencia.getText().toString()));
            new GuardaConfiguracoesTask(local).execute();
        }

    }

    private class GuardaConfiguracoesTask extends AsyncTask<Object, Object, Integer> {

        private final Local local;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"","Guardando Configuracoes");
        }

        public GuardaConfiguracoesTask(Local local) {
            this.local = local;
        }



        @Override
        protected Integer doInBackground(Object... params) {

            try {
                String json = new Gson().toJson(local);
                return NetworkUtils.doOutputOperation("PUT", NetworkUtils.buildUrlLocais(local.getId()), json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Integer resposta) {
            super.onPostExecute(resposta);
            progressDialog.dismiss();
            dismiss();
            if(resposta==null || resposta > 300 )
                Toast.makeText(getActivity(), "Não foi Possivel guardar dados", Toast.LENGTH_SHORT).show();
            ConfiguracoesDialogListener listener = (ConfiguracoesDialogListener) getActivity();
            listener.onFinishConfiguracoesDialog();



        }
    }

}
