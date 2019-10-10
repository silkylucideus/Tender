package com.lucidlabs.tender.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lucidlabs.tender.R;

public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setTitle("Wallet");
            actionbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.logout));
            actionbar.inflateMenu(R.menu.login_menu);
            setSupportActionBar(actionbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }
}
