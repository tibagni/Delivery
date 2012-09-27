package com.delivery.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.delivery.android.R;

public class OrderDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(OrderDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID, 0));
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.order_detail_container, fragment)
                    .commit();
        }
    }

    public void navigateUp() {
        NavUtils.navigateUpTo(this, new Intent(this, OrderListActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	navigateUp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    }
}
