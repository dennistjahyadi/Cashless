package com.dennistjahyadi.cashless.History.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dennistjahyadi.cashless.Components.EndlessRecyclerViewScrollListener;
import com.dennistjahyadi.cashless.History.adapter.HistoryRecyclerViewAdapter;
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
 * Created by Denn on 10/6/2017.
 */

public class FragmentHistoryAll extends Fragment {

    private List<HistoryTransaction> historyTransactionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private HistoryRecyclerViewAdapter adapter;
    private FrameLayout frameProgressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        frameProgressBar = (FrameLayout) view.findViewById(R.id.frameProgressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new HistoryRecyclerViewAdapter(this.historyTransactionList, "all", getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchAllHistoryTransaction(totalItemsCount);
            }
        });
        fetchAllHistoryTransaction(0);
    }

    private void fetchAllHistoryTransaction(int offset) {
        frameProgressBar.setVisibility(View.VISIBLE);
        String userId = SharedPreferenceUtils.getUserId(getContext());
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIHistoryTransactionService service = retrofit.create(APIHistoryTransactionService.class);
        Call<List<HistoryTransaction>> call = service.fetchAllHistoryTransaction(userId, offset, "a");
        call.enqueue(new Callback<List<HistoryTransaction>>() {
            @Override
            public void onResponse(Call<List<HistoryTransaction>> call, Response<List<HistoryTransaction>> response) {

                for (HistoryTransaction historyTransaction : response.body()) {
                    historyTransactionList.add(historyTransaction);
                }
                adapter.notifyDataSetChanged();
                frameProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<HistoryTransaction>> call, Throwable t) {
                Snackbar.make(coordinatorLayout, ConstantUtils.parseErrorMessage(t),
                        Snackbar.LENGTH_LONG)
                        .show();
                frameProgressBar.setVisibility(View.GONE);

            }
        });

    }

}
