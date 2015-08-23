package me.beartransit.apollojain.myapplication;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by apollojain on 8/17/15.
 */
public class Communicator {
    public String convert_id_to_url(String id){
        String base_url = "http://beartransit.daylen.com";
        String ms = Long.toString(System.currentTimeMillis());
        String url = base_url + "/api/v1/stop/" + id + "?time=" + ms;
        url = joinStr("%20", url.split(" "));
        return url;
    }

    public String joinStr(String seperator, String[] joined){
        String fin = joined[0];
        int i = 1;
        while(i < joined.length) {
            fin += seperator + joined[i];
            i++;
        }
        return fin;
    }

    public String executeHttpGet(String URL) throws Exception
    {
        BufferedReader in = null;
        try
        {
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
            HttpGet request = new HttpGet();
            request.setHeader("Content-Type", "text/plain; charset=utf-8");
            request.setURI(new URI(URL));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null)
            {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            //System.out.println(page);
            return page;
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    Log.d("BBB", e.toString());
                }
            }
        }
    }

    public JSONObject getJsonFromUrl(String URL){
        try {
            String s = executeHttpGet(URL);
            JSONObject jsonObj = new JSONObject(s);
            return(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getJsonArrayFromUrl(String URL){
        try {
            String s = executeHttpGet(URL);
            JSONArray jArray = new JSONArray(s);
            return(jArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}