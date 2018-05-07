package pheasantgame.eim.systems.cs.pub.ro.pheasantgame.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.R;
import pheasantgame.eim.systems.cs.pub.ro.pheasantgame.network.ServerThread;

public class ServerFragment extends Fragment {

    private ServerThread serverThread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_server, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView serverHistoryTextView = getActivity().findViewById(R.id.server_history_text_view);

        serverThread = new ServerThread(serverHistoryTextView);
        serverThread.start();
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }

        super.onDestroy();
    }

}
