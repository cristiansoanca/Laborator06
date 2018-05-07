package clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;

import clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.general.Constants;
import clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.general.Utilities;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private WeakReference<TextView> textViewWeakReference;

    public ClientAsyncTask(TextView serverMessageTextView) {
        textViewWeakReference = new WeakReference<>(serverMessageTextView);
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            // TODO exercise 6b
            // - get the connection parameters (serverAddress and serverPort from parameters - on positions 0 and 1)
            // - open a socket to the server
            // - get the BufferedReader in order to read from the socket (use Utilities.getReader())
            // - while the line that has read is not null (EOF was not sent), append the content to serverMessageTextView
            // by publishing the progress - with the publishProgress(...) method - to the UI thread
            // - close the socket to the server
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);
            socket = new Socket(serverAddress, serverPort);

            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            BufferedReader bufferedReader = Utilities.getReader(socket);
            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {
                publishProgress(currentLine);
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // TODO exercise 6b
        // - reset the content of the serverMessageTextView
        textViewWeakReference.get().setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // TODO exercise 6b
        // - append the content to serverMessageTextView
        textViewWeakReference.get().append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}
}
