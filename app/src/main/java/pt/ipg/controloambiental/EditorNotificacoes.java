package pt.ipg.controloambiental;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lealp on 05/07/2017.
 */

public class EditorNotificacoes extends BroadcastReceiver {
    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
        new MeteorologiaTask(MainActivity.GUARDA).execute();
        new MeteorologiaTask(MainActivity.VISEU).execute();
    }

    private class MeteorologiaTask extends AsyncTask<Object, Object, Meteorologia> {
        private int localId;

        public MeteorologiaTask(int localId) {
            this.localId = localId;
        }

        @Override
        protected Meteorologia doInBackground(Object... params) {
            try {
                String resposta = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl(localId));
                ArrayList<Meteorologia> meteorologia = new Gson().fromJson(resposta, new TypeToken<List<Meteorologia>>() {
                }.getType());
                return meteorologia != null ? meteorologia.get(0) : null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Meteorologia meteorologia) {
            super.onPostExecute(meteorologia);
            if (meteorologia != null && meteorologia.getLocal() != null) {

                if(getEstado(meteorologia)!=null)
                    adicionaNotificacao(meteorologia);

                /*Intent i=new Intent(context,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(i);*/
            }

        }
        private String getEstado(Meteorologia meteorologia){
            double temperaturaMinima = meteorologia.getLocal().getTemperaturaMinima();
            double temperaturaMaxima = meteorologia.getLocal().getTemperaturaMaxima();
            if (meteorologia.getTemperatura() < temperaturaMinima  ) {
                return "Atenção temperatura demasiado baixa!";
            }else if (meteorologia.getTemperatura() > temperaturaMaxima)
                return "Atenção temperatura demasiada alta!";
            return null;

        }

        private void adicionaNotificacao(Meteorologia meteorologia) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(meteorologia.getLocal().getDescricao());
            builder.setContentText(getEstado(meteorologia));
            builder.setSubText(MainActivity.FORMATO_DATA.format(meteorologia.getData()));
            builder.setContentInfo(MainActivity.FORMATO_TEMPERATURA.format(meteorologia.getTemperatura())+ "º") ;
            //builder.setNumber((int) meteorologia.getTemperatura());
            builder.setSmallIcon(R.mipmap.ic_ipg);
            builder.setOnlyAlertOnce(true);
            //Bitmap imagem = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_notification_overlay);
            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.alerta, null);
            //DrawableCompat.setTint(drawable, ContextCompat.getColor(context,android.R.color.holo_red_dark));
            //drawable.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark), PorterDuff.Mode.SRC_IN);
           //drawable.setColorFilter(new
             //      PorterDuffColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark),PorterDuff.Mode.MULTIPLY));


            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
      //     bitmap = mudaCorBitmap(bitmap);
            //setIconGrande(bitmap, builder);
            builder.setLargeIcon(bitmap);


            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);
            builder.setContentIntent(intent);

            //builder.setLargeIcon(resultBitmap);

            Intent buttonIntent = new Intent(context, ButaoRemoverReceiver.class);
            buttonIntent.putExtra("notificationId", meteorologia.getLocal().getId());
            //builder.addAction(android.R.drawable.ic_notification_clear_all,"Remover", PendingIntent.getBroadcast(context, 0, buttonIntent,0));
            NotificationCompat.Action remover = new NotificationCompat.Action.Builder(android.R.drawable.ic_notification_clear_all, "Remover", PendingIntent.getBroadcast(context, meteorologia.getLocal().getId(), buttonIntent, 0)).build();
            builder.addAction(remover);
            Notification notificacao = builder.build();


            notificationManager.notify(meteorologia.getLocal().getId(), notificacao);
        }
    }

    private void setIconGrande(Bitmap bitmap, NotificationCompat.Builder builder) {
        float[] colorTransform = {
                0, 1f, 0, 0, 0,
                0, 0, 0f, 0, 0,
                0, 0, 0, 0f, 0,
                0, 0, 0, 1f, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); //Remove Colour
        colorMatrix.set(colorTransform); //Apply the Red

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics( new DisplayMetrics());
        Display display = windowManager.getDefaultDisplay();

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, (int)(display.getHeight() * 0.15), display.getWidth(), (int)(display.getHeight() * 0.75));
        builder.setLargeIcon(resultBitmap);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);

    }

    private Bitmap mudaCorBitmap(Bitmap bitmap) {
        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_4444, true);
        int [] allpixels = new int [bitmap1.getHeight()*bitmap1.getWidth()];

        bitmap1.getPixels(allpixels, 0, bitmap1.getWidth(), 0, 0, bitmap1.getWidth(), bitmap1.getHeight());

        for(int i = 0; i < allpixels.length; i++)
        {
           // if(allpixels[i] == Color.BLACK)
           // {
                allpixels[i] = Color.RED;
            //}
        }

        bitmap1.setPixels(allpixels, 0, bitmap1.getWidth(), 0, 0, bitmap1.getWidth(), bitmap1.getHeight());

        return bitmap;



    }


}
