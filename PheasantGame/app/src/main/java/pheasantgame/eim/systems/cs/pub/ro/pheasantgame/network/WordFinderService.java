package pheasantgame.eim.systems.cs.pub.ro.pheasantgame.network;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.general.Constants;

public class WordFinderService extends AsyncTask<String, Object, List<String>> {

    @Override
    protected List<String> doInBackground(String... params) {
        HttpClient httpClient = HttpClientBuilder.create().build();;
        HttpGet httpGet = new HttpGet(Constants.WORD_FINDER_SERVICE_INTERNET_ADDRESS + params[0].replaceAll("\\s+","") + ".html");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String content = null;

        try {
            content = httpClient.execute(httpGet, responseHandler);
        } catch (ClientProtocolException clientProtocolException) {
            Log.d(Constants.TAG, "An exception has occurred: " + clientProtocolException.getMessage());
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace();
            }
        } catch (IOException ioException) {
            Log.d(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }


        Document htmlPage = Jsoup.parse(content);
        List<String> wordList = new LinkedList<>();

        for (Element element : htmlPage.getElementsByTag("a")) {
            if (element.html().startsWith(params[0]) && element.html().length() > 2) {
                wordList.add(element.html());
            }
        }
        Log.d(Constants.TAG, wordList.toString());



        return wordList;
    }

}
