package com.dennistjahyadi.cashless.Bank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Models.BankAccount;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;

import java.util.List;

/**
 * Created by Denn on 10/22/2017.
 */

public class BankAccountRecyclerViewAdapter extends RecyclerView.Adapter<BankAccountRecyclerViewAdapter.MyViewHolder> {
    private List<BankAccount> bankAccountList;
    private Context con;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvlabelBank, tvAtasNama, tvBankAccountNumber, tvBtnRemove;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvlabelBank = (TextView) itemView.findViewById(R.id.tvlabelBank);
            tvAtasNama = (TextView) itemView.findViewById(R.id.tvAtasNama);
            tvBankAccountNumber = (TextView) itemView.findViewById(R.id.tvBankAccountNumber);
            tvBtnRemove = (TextView) itemView.findViewById(R.id.tvBtnRemove);
            tvBtnRemove.setTypeface(TypeFaceManager.getFontAwesomeTypeface(con));

        }
    }

    public BankAccountRecyclerViewAdapter(Context context, List<BankAccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
        this.con = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_bank_account, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BankAccount bankAccount = bankAccountList.get(position);

        holder.tvlabelBank.setText(bankAccount.getBank_name());
        holder.tvAtasNama.setText(bankAccount.getAtas_nama());
        holder.tvBankAccountNumber.setText(bankAccount.getNo_rekening());
        holder.tvBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return bankAccountList.size();
    }
}
