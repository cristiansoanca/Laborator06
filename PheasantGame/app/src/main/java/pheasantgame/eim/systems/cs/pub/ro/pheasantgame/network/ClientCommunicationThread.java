package pheasantgame.eim.systems.cs.pub.ro.pheasantgame.network;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.general.Constants;
import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.general.Utilities;

public class ClientCommunicationThread extends Thread {

    private Socket socket = null;

    private boolean first = true;

    private String mostRecentWordSent = "";
    private String mostRecentValidPrefix = "";

    private Context context;
    private Handler handler;
    private EditText wordEditText;
    private Button sendButton;
    private TextView clientHistoryTextView;

    public ClientCommunicationThread(Socket socket, Context context, Handler handler, EditText wordEditText, Button sendButton, TextView clientHistoryTextView) {
        this.socket = socket;
        this.context = context;
        this.handler = handler;
        this.wordEditText = wordEditText;
        this.sendButton = sendButton;
        this.clientHistoryTextView = clientHistoryTextView;

        if (socket == null) {
            try {
                socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
            } catch (UnknownHostException unknownHostException) {
                Log.e(Constants.TAG, "An exception has occurred: " + unknownHostException.getMessage());
                if (Constants.DEBUG) {
                    unknownHostException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        if (socket != null) {
            Log.d(Constants.TAG, "[CLIENT] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
        }
    }

    public void run() {
        try {
            BufferedReader responseReader = Utilities.getReader(socket);
            PrintWriter requestPrintWriter = Utilities.getWriter(socket);

            // TODO exercise 7b
            final String word = wordEditText.getText().toString();

            // Verify word length
            if (word.length() > 2) {
                Log.d(Constants.TAG, "[CLIENT] Sent \"" + word + "\" on socket " + socket);

                // Send to server
                requestPrintWriter.println(word);

                // most recent word send to server
                mostRecentWordSent = word;

                // Update history
                clientHistoryTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        String text = "Client sent word " + word + " to server\n" + clientHistoryTextView.getText().toString();
                        clientHistoryTextView.setText(text);
                    }
                });
            } else { // Error message for incorrect word length
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Word must be at least 3 characters long!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            // Server response
            final String response = responseReader.readLine();
            Log.d(Constants.TAG, "[CLIENT] Received \"" + response + "\", most recent word was \"" + mostRecentWordSent + "\" on socket " + socket);

            // Update history
            clientHistoryTextView.post(new Runnable() {
                @Override
                public void run() {
                    String text = "Client received word " + response + " from server\n" + clientHistoryTextView.getText().toString();
                    clientHistoryTextView.setText(text);
                }
            });

            if (Constants.END_GAME.equals(response)) { // Received End Game
                // Invalidate graphic control
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordEditText.setText("");
                        wordEditText.setEnabled(false);
                        sendButton.setEnabled(false);

                        String text = "Communication ended!\n" + clientHistoryTextView.getText().toString();
                        clientHistoryTextView.setText(text);
                    }
                });
            } else if (mostRecentWordSent.isEmpty() || !mostRecentWordSent.equals(response)) {
                // Find new valid prefix
                mostRecentValidPrefix = response.substring(response.length() - 2, response.length());

                // Set wordEditText with the new valid prefix
                wordEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        wordEditText.setText(mostRecentValidPrefix);
                        wordEditText.setSelection(2);
                    }
                });
            } else { // Received the same word as previously
                mostRecentValidPrefix = response.substring(0, 2);

                wordEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        wordEditText.setText(mostRecentValidPrefix);

                        if ((mostRecentValidPrefix != null) && (mostRecentValidPrefix.length() == 2)) {
                            wordEditText.setSelection(2);
                        }
                    }
                });

            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
