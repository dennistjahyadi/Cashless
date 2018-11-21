package com.dennistjahyadi.cashless.History.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Models.HistoryTransaction;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.TransactionDetail.TransactionDetailActivity;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Denn on 10/12/2017.
 */

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.MyViewHolder> {

    private List<HistoryTransaction> historyTransactionList;
    private String showHistory;
    private String userId;
    private SimpleDateFormat sdfDate, sdfDate2;
    private SimpleDateFormat sdfTime;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvTo, tvTransactionCategory, tvAmount, tvProfileIndicator;
        private View divider;
        private RelativeLayout layHistory;

        public MyViewHolder(View itemView) {
            super(itemView);
            layHistory = (RelativeLayout) itemView.findViewById(R.id.layHistory);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTo = (TextView) itemView.findViewById(R.id.tvTo);
            tvTransactionCategory = (TextView) itemView.findViewById(R.id.tvTransactionCategory);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            divider = (View) itemView.findViewById(R.id.divider);
            tvProfileIndicator = (TextView) itemView.findViewById(R.id.tvProfileIndicator);
        }
    }

    public HistoryRecyclerViewAdapter(List<HistoryTransaction> historyTransactionList, String showHistory, Context context) {
        this.historyTransactionList = historyTransactionList;
        this.showHistory = showHistory; // all, in , out
        this.userId = SharedPreferenceUtils.getUserId(context);
        this.sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        this.sdfDate2 = new SimpleDateFormat("dd MMM yyyy");
        this.sdfTime = new SimpleDateFormat("HH:mm:ss");
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_list_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HistoryTransaction historyTransaction = historyTransactionList.get(position);

        holder.tvProfileIndicator.setText("");

        holder.layHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TransactionDetailActivity.class);
                i.putExtra("data", historyTransaction);
                context.startActivity(i);
            }
        });

        if (position > 0) {
            if (historyTransactionList.get(position - 1).getDate().equals(historyTransaction.getDate())) {
                holder.tvDate.setVisibility(View.GONE);
                holder.tvDate.setText("");
            } else {
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvDate.setText(historyTransaction.getDate());
            }
        } else {
            holder.tvDate.setVisibility(View.VISIBLE);
            holder.tvDate.setText(historyTransaction.getDate());

        }

        if (position < (historyTransactionList.size() - 1)) {
            if (historyTransactionList.get((position + 1)).getDate().equals(historyTransaction.getDate())) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }

        if (showHistory.equals("all")) {
            if (historyTransaction.getMethod().equals("transaction")) {

                if (historyTransaction.getSenderUserId().equals(userId)) {
                    holder.tvProfileIndicator.setText(historyTransaction.getReceiverFullname().substring(0, 1).toUpperCase());
                    holder.tvTo.setText("To : " + historyTransaction.getReceiverFullname());
                    holder.tvTransactionCategory.setText("Pay");
                    holder.tvAmount.setText("- " + historyTransaction.getAmount());
                } else {
                    holder.tvProfileIndicator.setText(historyTransaction.getSenderFullname().substring(0, 1).toUpperCase());
                    holder.tvTo.setText("From : " + historyTransaction.getSenderFullname());
                    holder.tvTransactionCategory.setText("Receive");
                    holder.tvAmount.setText("+ " + historyTransaction.getAmount());
                }


            } else if (historyTransaction.getMethod().equals("withdraw")) {
                holder.tvProfileIndicator.setText(historyTransaction.getBankName().substring(0, 1).toUpperCase());
                holder.tvTo.setText("To : " + historyTransaction.getBankName());
                holder.tvTransactionCategory.setText("Withdraw " + historyTransaction.getStatus());
                if (historyTransaction.getStatus().equalsIgnoreCase("success")) {
                    holder.tvAmount.setText("- " + historyTransaction.getAmount());
                } else if (historyTransaction.getStatus().equalsIgnoreCase("pending")) {
                    holder.tvAmount.setText("- " + historyTransaction.getAmount());

                } else if (historyTransaction.getStatus().equalsIgnoreCase("rejected")) {
                    holder.tvAmount.setText("" + historyTransaction.getAmount());
                }

            } else if (historyTransaction.getMethod().equals("deposit")) {
                holder.tvProfileIndicator.setText("B");
                holder.tvTo.setText("From: Bank Account");
                holder.tvTransactionCategory.setText("Deposit " + historyTransaction.getStatus());
                if (historyTransaction.getStatus().equalsIgnoreCase("success")) {
                    holder.tvAmount.setText("+ " + historyTransaction.getAmount());
                } else if (historyTransaction.getStatus().equalsIgnoreCase("pending")) {
                    holder.tvAmount.setText("" + historyTransaction.getAmount());

                } else if (historyTransaction.getStatus().equalsIgnoreCase("rejected")) {
                    holder.tvAmount.setText("" + historyTransaction.getAmount());

                }

            }

        } else if (showHistory.equals("in")) {
            if (historyTransaction.getMethod().equals("transaction")) {
                holder.tvProfileIndicator.setText(historyTransaction.getSenderFullname().substring(0, 1).toUpperCase());
                holder.tvTo.setText("From : " + historyTransaction.getSenderFullname());
                holder.tvTransactionCategory.setText("Receive");
                holder.tvAmount.setText("+ " + historyTransaction.getAmount());


            } else if (historyTransaction.getMethod().equals("deposit")) {
                holder.tvProfileIndicator.setText("B");
                holder.tvTo.setText("From: Bank Account");
                holder.tvTransactionCategory.setText("Deposit " + historyTransaction.getStatus());
                if (historyTransaction.getStatus().equalsIgnoreCase("success")) {
                    holder.tvAmount.setText("+ " + historyTransaction.getAmount());
                } else if (historyTransaction.getStatus().equalsIgnoreCase("pending")) {
                    holder.tvAmount.setText("" + historyTransaction.getAmount());

                } else if (historyTransaction.getStatus().equalsIgnoreCase("rejected")) {
                    holder.tvAmount.setText("" + historyTransaction.getAmount());

                }
            }

        } else if (showHistory.equals("out")) {
            if (historyTransaction.getMethod().equals("transaction")) {
                holder.tvProfileIndicator.setText(historyTransaction.getReceiverFullname().substring(0, 1).toUpperCase());

                holder.tvTo.setText("To : " + historyTransaction.getReceiverFullname());
                holder.tvTransactionCategory.setText("Pay");
                holder.tvAmount.setText("- " + historyTransaction.getAmount());


            } else if (historyTransaction.getMethod().equals("withdraw")) {
                holder.tvProfileIndicator.setText(historyTransaction.getBankName().substring(0, 1).toUpperCase());
                holder.tvTo.setText("To : " + historyTransaction.getBankName());
                holder.tvTransactionCategory.setText("Withdraw " + historyTransaction.getStatus());
                if (historyTransaction.getStatus().equalsIgnoreCase("success")) {
                    holder.tvAmount.setText("- " + historyTransaction.getAmount());
                } else if (historyTransaction.getStatus().equalsIgnoreCase("pending")) {
                    holder.tvAmount.setText("- " + historyTransaction.getAmount());

                } else if (historyTransaction.getStatus().equalsIgnoreCase("rejected")) {
                    holder.tvAmount.setText("" + historyTransaction.getAmount());

                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return historyTransactionList.size();
    }
}
