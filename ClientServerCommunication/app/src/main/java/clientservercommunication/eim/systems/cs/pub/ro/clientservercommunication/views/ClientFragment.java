package clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.R;
import clientservercommunication.eim.systems.cs.pub.ro.clientservercommunication.network.ClientAsyncTask;

public class ClientFragment extends Fragment {

    private EditText serverAddressEditText, serverPortEditText;
    private TextView serverMessageTextView;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (!serverAddressEditText.getText().toString().isEmpty() && !serverPortEditText.getText().toString().isEmpty()) {
                ClientAsyncTask clientAsyncTask = new ClientAsyncTask(serverMessageTextView);
                clientAsyncTask.execute(serverAddressEditText.getText().toString(), serverPortEditText.getText().toString());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_client, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        serverAddressEditText = getActivity().findViewById(R.id.server_address_edit_text);
        serverPortEditText = getActivity().findViewById(R.id.server_port_edit_text);

        Button displayMessageButton = getActivity().findViewById(R.id.display_message_button);
        displayMessageButton.setOnClickListener(buttonClickListener);

        serverMessageTextView = getActivity().findViewById(R.id.server_message_text_view);
    }
}
