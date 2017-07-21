package com.dugsolutions.weatherhunt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dugsolutions.weatherhunt.data.WeatherLocal;
import com.dugsolutions.weatherhunt.data.WeatherLocalResult;
import com.dugsolutions.weatherhunt.data.WeatherSearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mWhereView.setText(mResult.requestLocation);
            mAdapter.setResult(mResult);
        }
    }

    @BindView(R.id.search_entry) EditText mSearchEntry;
    @BindView(R.id.search_execute) Button mSearchExecute;
    @BindView(R.id.where) TextView mWhereView;
    @BindView(R.id.main_list) RecyclerView mMainList;

    MyApplication mApp;
    Snackbar snackbar;
    DividerItemDecoration mDivider;
    MainAdapter mAdapter;
    MyHandler mHandler = new MyHandler();
    WeatherLocalResult mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (MyApplication) getApplicationContext();

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EventBus.getDefault().register(this);

        mSearchExecute.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String loc = mSearchEntry.getText().toString().trim();
                if (loc.length() == 0) {
                    showMessage(getString(R.string.search_help));
                } else {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    Timber.d("HUNTING FOR " + loc);
                    mApp.huntForWeather(loc);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMainList.setLayoutManager(linearLayoutManager);
        mDivider = new DividerItemDecoration(mMainList.getContext(), linearLayoutManager.getOrientation());
        mMainList.addItemDecoration(mDivider);
        mAdapter = new MainAdapter(this);
        mMainList.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(WeatherLocalResult event) {
        mResult = event;
        mHandler.sendEmptyMessage(0);
    }

    void showMessage(String msg) {
        CoordinatorLayout top = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        snackbar = Snackbar.make(top, msg, Snackbar.LENGTH_INDEFINITE);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4);
        snackbar.show();
    }
}
