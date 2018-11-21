package com.dennistjahyadi.cashless.History;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dennistjahyadi.cashless.History.Fragment.FragmentHistoryAll;
import com.dennistjahyadi.cashless.History.Fragment.FragmentHistoryIn;
import com.dennistjahyadi.cashless.History.Fragment.FragmentHistoryOut;
import com.dennistjahyadi.cashless.Models.HistoryTransaction;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIHistoryTransactionService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/1/2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FragmentHistoryAll fragmentHistoryAll;
    FragmentHistoryIn fragmentHistoryIn;
    FragmentHistoryOut fragmentHistoryOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        //   fetchAllHistoryTransaction();
    }

    private void setupViewPager(ViewPager viewPager) {
        fragmentHistoryAll = new FragmentHistoryAll();
        fragmentHistoryIn = new FragmentHistoryIn();
        fragmentHistoryOut = new FragmentHistoryOut();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragmentHistoryAll, "All");
        adapter.addFragment(fragmentHistoryIn, "In");
        adapter.addFragment(fragmentHistoryOut, "Out");
        viewPager.setAdapter(adapter);
    }

    private void fetchAllHistoryTransaction() {
        String userId = SharedPreferenceUtils.getUserId(this);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIHistoryTransactionService service = retrofit.create(APIHistoryTransactionService.class);
        Call<List<HistoryTransaction>> call = service.fetchAllHistoryTransaction(userId, 0, "a");
        call.enqueue(new Callback<List<HistoryTransaction>>() {
            @Override
            public void onResponse(Call<List<HistoryTransaction>> call, Response<List<HistoryTransaction>> response) {
            }

            @Override
            public void onFailure(Call<List<HistoryTransaction>> call, Throwable t) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
