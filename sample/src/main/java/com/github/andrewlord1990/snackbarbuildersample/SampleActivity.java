package com.github.andrewlord1990.snackbarbuildersample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarCallback;
import com.github.andrewlord1990.toastbuilder.ToastBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class SampleActivity extends AppCompatActivity {

    private static final String TAG = SampleActivity.class.getSimpleName();

    private ListView listView;
    private Map<String, OnClickListener> samples;

    private int red;
    private int green;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        red = ContextCompat.getColor(this, R.color.red);
        green = ContextCompat.getColor(this, R.color.green);

        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFab();
        setupData();
        setupListView();
    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(createAdapter());
    }

    private void setupData() {
        samples = new LinkedHashMap<>();
        samples.put("Message", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message")
                        .build()
                        .show();
            }
        });
        samples.put("Action", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message")
                        .actionText("Action")
                        .actionClickListener(getActionClickListener())
                        .build()
                        .show();
            }
        });
        samples.put("Custom Text Colours Using Resources", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message in different color")
                        .actionText("Action")
                        .actionClickListener(getActionClickListener())
                        .messageTextColorRes(R.color.red)
                        .actionTextColorRes(R.color.green)
                        .build()
                        .show();
            }
        });
        samples.put("Custom Text Colours Using Colors", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message in different color")
                        .actionText("Action")
                        .actionClickListener(getActionClickListener())
                        .messageTextColor(green)
                        .actionTextColor(red)
                        .build()
                        .show();
            }
        });
        samples.put("Standard callback", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message")
                        .actionText("Action")
                        .callback(createCallback())
                        .build()
                        .show();
            }
        });
        samples.put("SnackbarBuilder callback", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message")
                        .actionText("Action")
                        .snackbarCallback(createSnackbarCallback())
                        .build()
                        .show();
            }
        });
        samples.put("Lowercase action", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Message")
                        .actionText("Action")
                        .lowercaseAction()
                        .build()
                        .show();
            }
        });
        samples.put("Custom timeout", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("This has a custom timeout")
                        .duration(10000)
                        .build()
                        .show();
            }
        });
        samples.put("Toast with red text", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastBuilder(SampleActivity.this)
                        .message("Custom toast")
                        .duration(Toast.LENGTH_LONG)
                        .messageTextColor(Color.RED)
                        .build()
                        .show();
            }
        });
        samples.put("Toast with custom position", new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastBuilder(SampleActivity.this)
                        .message("Positioned toast")
                        .duration(Toast.LENGTH_LONG)
                        .gravity(Gravity.TOP)
                        .gravityOffsetX(100)
                        .gravityOffsetY(300)
                        .build()
                        .show();
            }
        });
    }

    private SnackbarCallback createSnackbarCallback() {
        return new SnackbarCallback() {
            @Override
            public void onSnackbarShown(Snackbar snackbar) {
                super.onSnackbarShown(snackbar);
            }

            @Override
            public void onSnackbarDismissed(Snackbar snackbar) {
                super.onSnackbarDismissed(snackbar);
            }

            @Override
            public void onSnackbarActionPressed(Snackbar snackbar) {
                showToast("Action pressed");
            }

            @Override
            public void onSnackbarSwiped(Snackbar snackbar) {
                showToast("Swiped");
            }

            @Override
            public void onSnackbarTimedOut(Snackbar snackbar) {
                showToast("Timed out");
            }

            @Override
            public void onSnackbarManuallyDismissed(Snackbar snackbar) {
                super.onSnackbarManuallyDismissed(snackbar);
            }

            @Override
            public void onSnackbarDismissedAfterAnotherShown(Snackbar snackbar) {
                super.onSnackbarDismissedAfterAnotherShown(snackbar);
            }
        };
    }

    private Callback createCallback() {
        return new Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                switch (event) {
                    case DISMISS_EVENT_SWIPE:
                        showToast("Swiped");
                        break;
                    case DISMISS_EVENT_ACTION:
                        showToast("Action dismissed");
                        break;
                    case DISMISS_EVENT_TIMEOUT:
                        showToast("Timed out");
                        break;
                    case DISMISS_EVENT_MANUAL:
                        Log.v(TAG, "Snackbar manually dismissed");
                        break;
                    case DISMISS_EVENT_CONSECUTIVE:
                        Log.v(TAG, "Snackbar dismissed consecutive");
                        break;
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
                Log.v(TAG, "Snackbar shown");
            }
        };
    }

    private OnClickListener getActionClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Action pressed");
            }
        };
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new SnackbarBuilder(SampleActivity.this)
                        .message("Primary action pressed")
                        .duration(Snackbar.LENGTH_SHORT)
                        .build()
                        .show();
            }
        });
    }

    private ListAdapter createAdapter() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                samples.get(item).onClick(view);
            }
        });
        for (String sample : samples.keySet()) {
            adapter.add(sample);
        }
        return adapter;
    }

    private void showToast(String message) {
        new ToastBuilder(SampleActivity.this)
                .message(message)
                .duration(Toast.LENGTH_LONG)
                .build()
                .show();
    }

}