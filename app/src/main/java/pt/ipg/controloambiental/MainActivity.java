package pt.ipg.controloambiental;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConfiguracoesFragment.ConfiguracoesDialogListener {

    public static final DecimalFormat FORMATO_TEMPERATURA = new DecimalFormat("#,##0.##");
    public static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat();
    public static final int GUARDA = 1, VISEU = 2;
    private ProgressBar progressBarGuarda;
    private ProgressBar progressBarViseu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBarGuarda = (ProgressBar) findViewById(R.id.progressBarGuarda);
        progressBarViseu = (ProgressBar) findViewById(R.id.progressBarViseu);



        Intent intent = new Intent(this, EditorNotificacoes.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),1*60*1000,alarmIntent);


    }



    private void atualizaInterface() {
        handler.removeCallbacks(runnableCode);
        handler.post(runnableCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableCode);
    }

    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            getDadosMeteorologicos();
            Log.d("Handlers", "Called on main thread");
            handler.postDelayed(runnableCode, 60000);
        }
    };
// Run the above code block on the main thread after 2 seconds


    private void getDadosMeteorologicos() {
        new MeteorologiaTask(progressBarGuarda, 1).execute();
        new MeteorologiaTask(progressBarViseu, VISEU).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaInterface();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickConfiguracoes(View view) {
        int local;
        if (view.getId() == R.id.botaoConfiguracoesGuarda)
            local = GUARDA;
        else
            local = VISEU;

        new CarregaConfiguracoesTask(local).execute();

    }

    public void atualizaGuardaClick(View view) {
                new MeteorologiaTask(progressBarGuarda,GUARDA).execute();
    }

    public void atualizaViseuClick(View view) {
        new MeteorologiaTask(progressBarViseu,VISEU).execute();
    }

    @Override
    public void onFinishConfiguracoesDialog() {
        atualizaInterface();
    }


    public class CarregaConfiguracoesTask extends AsyncTask<Object, Object, Local> {
        ProgressDialog progressDialog;
        private int localId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Carregando Configurações");
            progressDialog.show();
        }

        public CarregaConfiguracoesTask(int localId) {
            this.localId = localId;
        }

        @Override
        protected Local doInBackground(Object... params) {
            try {
                String resposta = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrlLocais(localId));
                Local local = new Gson().fromJson(resposta, Local.class);
                return local;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Local local) {
            super.onPostExecute(local);
            progressDialog.dismiss();
            if (local != null) {
                local.setId(localId);
                mostraDialogoConfiguracoes(local);
            }
        }
    }

    private void mostraDialogoConfiguracoes(Local local) {

        FragmentManager fm = getSupportFragmentManager();
        ConfiguracoesFragment configuracoesFragment =
                ConfiguracoesFragment.newInstance(local,
                        "Configurações " + (local.getId() == GUARDA ? "Guarda" : "Viseu"));

        configuracoesFragment.show(fm, "fragment_configuracoes");

    }

    public class MeteorologiaTask extends AsyncTask<Object, Object, ArrayList<Meteorologia>> {
        ProgressBar progressBar;
        private int localId;

        public MeteorologiaTask(ProgressBar progressBar, int localId) {
            this.progressBar = progressBar;
            this.localId = localId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          if(progressBar!=null)  progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Meteorologia> doInBackground(Object... params) {

            try {
                String conteudo = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl(localId));
                ArrayList<Meteorologia> meteorologia = new Gson().fromJson(conteudo, new TypeToken<List<Meteorologia>>() {
                }.getType());

                return meteorologia;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Meteorologia> meteorologia) {
            super.onPostExecute(meteorologia);
            if (meteorologia != null) {
                getTextViewTemperatura(localId).setText(FORMATO_TEMPERATURA.format(meteorologia.get(0).getTemperatura()) + "º");
                getTextViewHumidade(localId).setText(FORMATO_TEMPERATURA.format(meteorologia.get(0).getHumidade()) + "%");
                getTextViewData(localId).setText(FORMATO_DATA.format(meteorologia.get(0).getData()));

                setEstado(meteorologia.get(0));

            } else
                Toast.makeText(MainActivity.this, "Não foi possivel obter dados", Toast.LENGTH_SHORT).show();
            this.progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private void setEstado(Meteorologia meteorologia) {
        double temperaturaMinima = meteorologia.getLocal().getTemperaturaMinima();
        double temperaturaMaxima = meteorologia.getLocal().getTemperaturaMaxima();
        TextView textViewEstado = (TextView)(meteorologia.getLocal().getId() == GUARDA ?  findViewById(R.id.textViewEstado):
      findViewById(R.id.textViewEstadoViseu));
        if(meteorologia.getTemperatura() > temperaturaMinima && meteorologia.getTemperatura()<temperaturaMaxima){
            textViewEstado.setCompoundDrawablesWithIntrinsicBounds(0, 0,android.R.drawable.presence_online, 0);
        }else
            textViewEstado.setCompoundDrawablesWithIntrinsicBounds(0, 0,android.R.drawable.ic_notification_overlay, 0);
    }

    private TextView getTextViewData(int localId) {
        if (localId == GUARDA)
            return (TextView) findViewById(R.id.textViewDataGuarda);
        else
            return (TextView) findViewById(R.id.textViewDataViseu);
    }

    private TextView getTextViewHumidade(int localId) {
        if (localId == GUARDA)
            return (TextView) findViewById(R.id.textViewHumidadeGuarda);
        else
            return (TextView) findViewById(R.id.textViewHumidadeViseu);
    }

    private TextView getTextViewTemperatura(int localId) {
        if (localId == GUARDA)
            return (TextView) findViewById(R.id.textViewTemperaturaGuarda);
        else
            return (TextView) findViewById(R.id.textViewTemperaturaViseu);

    }


}

