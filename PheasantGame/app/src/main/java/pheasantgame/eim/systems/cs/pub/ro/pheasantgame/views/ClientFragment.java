package pheasantgame.eim.systems.cs.pub.ro.pheasantgame.views;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.R;
import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.general.Constants;
import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.network.ClientCommunicationThread;

public class ClientFragment extends Fragment {

    private Socket socket;

    private Handler handler;

    private EditText wordEditText;
    private TextView clientHistoryTextView;
    private Button sendButton;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            ClientCommunicationThread clientCommunicationThread = new ClientCommunicationThread(socket, getActivity(), handler, wordEditText, sendButton, clientHistoryTextView);
            clientCommunicationThread.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();

        handler = new Handler();

        wordEditText = getActivity().findViewById(R.id.word_edit_text);
        sendButton = getActivity().findViewById(R.id.send_button);
        sendButton.setOnClickListener(buttonClickListener);
        clientHistoryTextView = getActivity().findViewById(R.id.client_history_text_view);
    }
}
