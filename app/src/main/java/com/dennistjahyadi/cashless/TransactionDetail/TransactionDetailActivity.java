package com.dennistjahyadi.cashless.TransactionDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Models.HistoryTransaction;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.Utils.SharedPreferenceUtils;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;

/**
 * Created by Denn on 10/6/2017.
 */

public class TransactionDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvEmailIcon, tvDate, tvTime, tvMethod, tvAmount, tvFullname,
            tvMethod2, tvToOrFrom, tvFullname2, tvAmount2, tvContact, tvEmailContact,
            tvWithdrawAmount, tvWithdrawFee, tvAmountTotal, tvTransactionId, tvBankName,
            tvBankAccountName, tvBankAccountNumber, tvDepositAmount, tvFor;
    private LinearLayout layDetailFee, layContact, layWithdrawTo, layDeposit;
    private RelativeLayout layTo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        tvEmailIcon.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));

        //check method: transaction, deposit, or withdraw;
        HistoryTransaction historyTransaction = (HistoryTransaction) getIntent().getSerializableExtra("data");
        checkMethod(historyTransaction);
    }

    private void init() {

        layTo = (RelativeLayout) findViewById(R.id.layTo);
        layWithdrawTo = (LinearLayout) findViewById(R.id.layWithdrawTo);
        layDetailFee = (LinearLayout) findViewById(R.id.layDetailFee);
        layContact = (LinearLayout) findViewById(R.id.layContact);
        layDeposit = (LinearLayout) findViewById(R.id.layDeposit);
        tvFor = (TextView) findViewById(R.id.tvFor);
        tvBankName = (TextView) findViewById(R.id.tvBankName);
        tvBankAccountName = (TextView) findViewById(R.id.tvBankAccountName);
        tvBankAccountNumber = (TextView) findViewById(R.id.tvBankAccountNumber);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvMethod = (TextView) findViewById(R.id.tvMethod);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvFullname = (TextView) findViewById(R.id.tvFullname);
        tvMethod2 = (TextView) findViewById(R.id.tvMethod2);
        tvToOrFrom = (TextView) findViewById(R.id.tvlabelBank);
        tvFullname2 = (TextView) findViewById(R.id.tvFullname2);
        tvAmount2 = (TextView) findViewById(R.id.tvAmount2);
        tvEmailContact = (TextView) findViewById(R.id.tvEmailContact);
        tvEmailIcon = (TextView) findViewById(R.id.tvEmailIcon);
        tvWithdrawAmount = (TextView) findViewById(R.id.tvWithdrawAmount);
        tvWithdrawFee = (TextView) findViewById(R.id.tvWithdrawFee);
        tvAmountTotal = (TextView) findViewById(R.id.tvAmountTotal);
        tvTransactionId = (TextView) findViewById(R.id.tvTransactionID);
        tvContact = (TextView) findViewById(R.id.tvContact);
        tvDepositAmount = (TextView) findViewById(R.id.tvDepositAmount);

    }


    private void checkMethod(HistoryTransaction historyTransaction) {
        String userId = SharedPreferenceUtils.getUserId(getApplicationContext());
        if (historyTransaction.getMethod().equals("transaction")) {
            initTransactionMethod(historyTransaction, userId);
        } else if (historyTransaction.getMethod().equals("withdraw")) {
            initWithdrawMethod(historyTransaction);
        } else if (historyTransaction.getMethod().equals("deposit")) {
            initDepositMethod(historyTransaction);
        }
    }

    private void initTransactionMethod(HistoryTransaction historyTransaction, String userId) {
        layDetailFee.setVisibility(View.GONE);
        layWithdrawTo.setVisibility(View.GONE);
        layDeposit.setVisibility(View.GONE);

        tvDate.setText(historyTransaction.getDate());
        tvTime.setText(historyTransaction.getTime());
        tvAmount.setText("Rp " + historyTransaction.getAmount());
        tvAmount2.setText("Rp " + historyTransaction.getAmount());
        tvContact.setText("Contact:");
        tvTransactionId.setText(historyTransaction.getId());
        if (TextUtils.isEmpty(historyTransaction.getDescription())) {
            tvFor.setText("-");

        } else {
            tvFor.setText(historyTransaction.getDescription());
        }
        if (historyTransaction.getSenderUserId().equals(userId)) {
            tvMethod.setText("Transfer " + historyTransaction.getStatus());
            tvFullname.setText("to : " + historyTransaction.getReceiverFullname());
            tvMethod2.setText("Money Transfer");
            tvToOrFrom.setText("To:");
            tvFullname2.setText(historyTransaction.getReceiverFullname());
            tvEmailContact.setText(historyTransaction.getReceiverEmail());
        } else {
            tvMethod.setText("You Receive");
            tvFullname.setText("from : " + historyTransaction.getSenderFullname());
            tvMethod2.setText("Money Receive");
            tvToOrFrom.setText("From:");
            tvFullname2.setText(historyTransaction.getSenderFullname());
            tvEmailContact.setText(historyTransaction.getSenderEmail());
        }
    }

    private void initWithdrawMethod(HistoryTransaction historyTransaction) {
        layContact.setVisibility(View.GONE);
        layTo.setVisibility(View.GONE);
        layDeposit.setVisibility(View.GONE);
        tvDate.setText(historyTransaction.getDate());
        tvTime.setText(historyTransaction.getTime());
        tvAmount.setText("Rp " + historyTransaction.getAmount());
        tvAmount2.setText(historyTransaction.getAmount());
        tvTransactionId.setText(historyTransaction.getId());
        tvBankName.setText(historyTransaction.getBankName());
        tvBankAccountName.setText(historyTransaction.getBankAccountName());
        tvBankAccountNumber.setText(historyTransaction.getBankAccountNumber());
        tvWithdrawAmount.setText("Rp " + historyTransaction.getAmount());
        tvWithdrawFee.setText("Rp " + historyTransaction.getFee());
        tvAmountTotal.setText("Rp " + historyTransaction.getTotalAmount());
        tvMethod.setText("Withdraw " + historyTransaction.getStatus());
        tvMethod2.setText("WITHDRAW");
        tvMethod.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

    }

    private void initDepositMethod(HistoryTransaction historyTransaction) {
        layWithdrawTo.setVisibility(View.GONE);
        layContact.setVisibility(View.GONE);
        layTo.setVisibility(View.GONE);
        layDetailFee.setVisibility(View.GONE);
        tvDate.setText(historyTransaction.getDate());
        tvTime.setText(historyTransaction.getTime());
        tvMethod.setText("Deposit " + historyTransaction.getStatus());
        tvMethod2.setText("DEPOSIT");
        tvAmount.setText("Rp " + historyTransaction.getTotalAmount());
        tvDepositAmount.setText("Rp " + historyTransaction.getTotalAmount());
        tvMethod.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
