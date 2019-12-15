/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pt.ipg.controloambiental;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String SERVIDOR =
            "http://185.203.118.18:8080";

    private static final String METEOROLOGIA = SERVIDOR + "/ControloAmbiental/webresources/pt.ipg.meteorologia";
    private static final String LOCAIS = SERVIDOR + "/ControloAmbiental/webresources/pt.ipg.locais";


    // private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;


    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";
    /* The number of days we want our API to return */
    private static final int numDays = 14;

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";

    public static URL buildUrl(int local) {
        Uri builtUri = Uri.parse(METEOROLOGIA).buildUpon()
                .appendQueryParameter("local", String.valueOf(local))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlLocais(int local) {
        Uri builtUri = Uri.parse(LOCAIS).buildUpon()
                .appendPath(String.valueOf(local))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    final static String DAYS_PARAM = "cnt";

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    public static URL buildUrl(Double lat, Double lon) {
        /** This will be implemented in a future lesson **/
        return null;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.addRequestProperty("Content-Type", "application/json");
        urlConnection.addRequestProperty("Accept", "application/json");

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static int doOutputOperation(String metodo, URL url, String jsonLocal) throws IOException {


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(metodo);
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream output = new BufferedOutputStream(conn.getOutputStream());
            output.write(jsonLocal.toString().getBytes());
            output.flush();

            return conn.getResponseCode();
        } finally {
            conn.disconnect();
        }


    }
}