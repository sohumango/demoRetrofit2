package com.example.demoretrofit2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private IWeatherAPI weatherApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://weather.livedoor.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        weatherApi = retrofit.create(IWeatherAPI.class);
        Button clickButton = (Button) findViewById(R.id.button10);
        clickButton.setOnClickListener(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Button	btn = (Button)v;
        if( btn.getId() == R.id.button10 )	// OK
        {
            TextView mTextView_Telop = (TextView) findViewById(R.id.textView2);
            TextView mEditText_Desc = (TextView) findViewById(R.id.textView3);
            final String TAG = "hello";

            Disposable d = weatherApi.getWhether("200010")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (response) -> {
                                mTextView_Telop.setText(response.forecasts.get(0).telop);
                                mEditText_Desc.setText(response.description.text);
                            },
                            (err) -> {
                                Log.d(TAG, err.toString());
                                Toast.makeText(MainActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                            }
                    );
            compositeDisposable.add(d);
        }
    }
}
