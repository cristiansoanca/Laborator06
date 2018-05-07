package clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.network;

import android.util.Log;
import android.widget.EditText;

import java.io.PrintWriter;
import java.net.Socket;

import clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.general.Constants;
import clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.general.Utilities;


public class CommunicationThread extends Thread {
    private Socket socket;
    private EditText serverTextEditText;

    CommunicationThread(Socket socket, EditText serverTextEditText) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());

            // TODO exercise 6a
            // - get the PrintWriter object in order to write on the socket (use Utilities.getWriter())
            // - print a line containing the text in the serverTextEditText edit text
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(serverTextEditText.getText().toString());

            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (Exception exception) {
            Log.e(Constants.TAG, "An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }
}
