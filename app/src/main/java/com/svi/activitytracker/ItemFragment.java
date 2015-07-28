package com.svi.activitytracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import com.svi.activitytracker.common.Constants;
import com.svi.activitytracker.common.InMemoryLog;

import java.util.ArrayList;

/**
 * A fragment representing a list of string items
 */
public class ItemFragment extends ListFragment {

    private static final String LOG = "LOG";
    private ArrayList<String> data = new ArrayList<>();
    private int fragId;

    public interface LogProvider {
        InMemoryLog getLog();
    }
    LogProvider logProvider = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            logProvider = (LogProvider)activity;
        } catch (Exception e) {
            throw  new ClassCastException(activity.getClass().getName() + " must implement " + LogProvider.class.getName() + " interface");
        }
    }

    private BroadcastReceiver receiver;
    private class ActionReceiver extends BroadcastReceiver {
        private Handler handler;

        public ActionReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final int broadcastId = intent.getIntExtra(Constants.FRAG_ID, 0);
            if (broadcastId == Constants.NO_ID) {
                return;
            }

            final Intent finalIntent = intent;
            handler.post(new Runnable() {
                @Override
                public void run() {
                        if (broadcastId == fragId) {
                        String action = finalIntent.getAction();
                        if (action == Constants.Action.ADD) {
                            String msg = finalIntent.getStringExtra(Constants.DATA);
                            adapter.add(msg);
                        } else if (action == Constants.Action.ADD_ALL) {
                            data = finalIntent.getStringArrayListExtra(Constants.DATA);
                            adapter.addAll(data);
                        } else if (action == Constants.Action.CLEAR) {
                            adapter.clear();
                        }
                    }
                }
            });
        }
    };
    private ArrayAdapter<String> adapter;

    public static ItemFragment newInstance(int fragId) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.FRAG_ID, fragId);
        fragment.setArguments(args);
        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Action.ADD);
        intentFilter.addAction(Constants.Action.ADD_ALL);
        intentFilter.addAction(Constants.Action.CLEAR);
        receiver = new ActionReceiver(new Handler());
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.fragId = getArguments().getInt(Constants.FRAG_ID);

        data = logProvider.getLog().filter(this.fragId);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, data);
        setListAdapter(adapter);
    }
}

