package com.dugsolutions.weatherhunt.act;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.dugsolutions.weatherhunt.R;
import com.dugsolutions.weatherhunt.app.MyApplication;
import com.dugsolutions.weatherhunt.event.WeatherResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search_entry) EditText mSearchEntry;
    @BindView(R.id.search_execute) Button mSearchExecute;

    MyApplication mApp;

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
                String loc = mSearchEntry.getText().toString();
                Timber.d("HUNTING FOR " + loc);
                mApp.huntForWeather(loc);
            }
        });
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

    public void onEvent(WeatherResult event) {
        Timber.d("MYDEBUG: onEvent(" + event.getResult());
    }
}
