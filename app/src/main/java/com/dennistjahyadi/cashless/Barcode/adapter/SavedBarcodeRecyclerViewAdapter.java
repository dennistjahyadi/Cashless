package com.dennistjahyadi.cashless.Barcode.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadi.cashless.Barcode.BarcodeActivity;
import com.dennistjahyadi.cashless.Models.BarcodeBusiness;
import com.dennistjahyadi.cashless.R;
import com.dennistjahyadi.cashless.RetrofitServices.APIBusinessListService;
import com.dennistjahyadi.cashless.Utils.ConstantUtils;
import com.dennistjahyadi.cashless.Utils.TypeFaceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Denn on 10/12/2017.
 */

public class SavedBarcodeRecyclerViewAdapter extends RecyclerView.Adapter<SavedBarcodeRecyclerViewAdapter.MyViewHolder> {

    private List<BarcodeBusiness> sourceMapList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBarcodeName, tvDescription, tvBtnTrash;
        private RelativeLayout layoutButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            layoutButton = (RelativeLayout) itemView.findViewById(R.id.layoutButton);
            tvBarcodeName = (TextView) itemView.findViewById(R.id.tvBarcodeName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvBtnTrash = (TextView) itemView.findViewById(R.id.tvBtnTrash);
            tvBtnTrash.setTypeface(TypeFaceManager.getFontAwesomeTypeface(context));

        }
    }

    public SavedBarcodeRecyclerViewAdapter(List<BarcodeBusiness> sourceMapList, Context context) {
        this.sourceMapList = sourceMapList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_barcode_list_business, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BarcodeBusiness obj = sourceMapList.get(position);
        final String businessName = (String) obj.getBusiness_name();
        final String businessDesc = (String) obj.getBusiness_desc();
        final Integer amount = obj.getAmount();
        holder.tvBarcodeName.setText(businessName);
        holder.tvDescription.setText(businessDesc);
        holder.layoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BarcodeActivity.class);
                i.putExtra("type", "business");
                i.putExtra("businessId", obj.getId());
                i.putExtra("businessName", businessName);
                i.putExtra("businessDesc", businessDesc);
                i.putExtra("businessAmount", amount);
                context.startActivity(i);
            }
        });

        holder.tvBtnTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
                builder.setTitle("Verification")
                        .setMessage("Delete " + businessName + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteBusinessById(obj);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing


                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sourceMapList.size();
    }

    private void deleteBusinessById(final BarcodeBusiness obj) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ConstantUtils.webserviceurl)
                .build();
        APIBusinessListService service = retrofit.create(APIBusinessListService.class);
        Call<String> call = service.deleteBusinessById(obj.getId(), "a");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("1")) {
                    sourceMapList.remove(obj);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
