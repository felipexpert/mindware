package br.com.mwdesenvolvimento.mylibrary.server;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by geppetto on 01/12/15.
 */
public class RequestTask extends AsyncTask<String, String, String> {
    private static final String TAG = PostRequestTask.class.getSimpleName();

    private Exception exception;

    private TaskListener taskListener;

    private Task task;

    public RequestTask(TaskListener taskListener) {
        this(taskListener, null);
    }

    public RequestTask(TaskListener taskListener, Task task) {
        this.taskListener = taskListener;
        this.task = task;
    }

    private static final Pattern escape = Pattern.compile("\\\\u[0-9a-fA-F]{4}");

    @Override
    protected String doInBackground(String... params) {
        if(params.length < 2) throw new IllegalArgumentException("Please send the http method and the url as a parameter");
        final String method = params[0].toUpperCase();
        final String url = params[1];
        String result = "";
        try {
            HttpParams httpParams = new BasicHttpParams();
            DefaultHttpClient client = new DefaultHttpClient();
            //HttpPost post = new HttpPost("http://192.168.0.10:8888/myerp/mobile/test.php");

            HttpRequestBase request = null;
            switch(method) {
                case "GET":
                    request = new HttpGet(url);
                    break;
                case "PUT":
                    request = new HttpPut(url);
                    break;
                case "DELETE":
                    request = new HttpDelete(url);
                    break;
                case "POST":
                    request = new HttpPost(url);
                    break;
                default:
                    throw new IllegalAccessException("Invalid HTTP method!");
            }
            if(params.length  > 2 && request instanceof HttpEntityEnclosingRequestBase) {
                HttpEntityEnclosingRequestBase enclosing = (HttpEntityEnclosingRequestBase) request;
                enclosing.setEntity(new ByteArrayEntity(params[1].toString().getBytes(
                        "UTF8")));
            }

            HttpResponse httpResponse = client.execute(request);

            InputStream inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
                inputStream.close();
            }
        } catch(Exception e) {
            exception = e;
        }
        Log.d(TAG, result);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(exception != null) Log.e(taskListener.getClass().getSimpleName(), "Error", exception);
        taskListener.act(s, task, exception);
    }

    private static String convertInputStreamToString(InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String line  = "", result = "";
        while((line =  bufferedReader.readLine()) != null)
            result += line;
        return scapeString(result);
    }

    /**
     * I created this method to escape unicode (e.i \u00e0  =  é)
     * This is useful for converting json data from server
     * @param string
     * @return
     */
    private static String scapeString(String string) {
        Matcher m = escape.matcher(string);
        while(m.find())  {
            String group = m.group();
            int start = m.start();
            String c = Character.toString((char) Integer.parseInt(group.substring(2, group.length()), 16));
            String regex = "\\" + group;
            string = string.replaceAll(regex, c);
        }
        return string;
    }

    public TaskListener getTaskListener() {
        return taskListener;
    }

    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}