package com.example.ivansv.snapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    private static final int LOGIN = 0;
    private static final int FEED = 1;
    private static final int FRAGMENT_COUNT = FEED +1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        FacebookSdk.sdkInitialize(this);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (isResumed) {
                    FragmentManager manager = getSupportFragmentManager();
                    int backStackSize = manager.getBackStackEntryCount();
                    for (int i = 0; i < backStackSize; i++) {
                        manager.popBackStack();
                    }
                    if (currentAccessToken != null) {
                        showFragment(FEED, false);
//                        showFragment(LOGIN, false); // for logout
                    } else {
                        showFragment(LOGIN, false);
                    }
                }
            }
        };
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
        fragments[FEED] = fm.findFragmentById(R.id.feedFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (AccessToken.getCurrentAccessToken() != null) {
            showFragment(FEED, false);
        } else {
            showFragment(LOGIN, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
