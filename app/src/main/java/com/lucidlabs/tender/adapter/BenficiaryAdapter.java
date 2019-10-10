package com.lucidlabs.tender.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucidlabs.tender.R;
import com.lucidlabs.tender.api.Data_New;

import java.util.ArrayList;
import java.util.List;

public class BenficiaryAdapter extends RecyclerView.Adapter<BenficiaryAdapter.ViewHolder> {

    private List<String> values;
    private ArrayList<Data_New> dataModelArrayList;

    // Provide a reference to the views for each Data item
    // Complex Data items may need more than one view per item, and
    // you provide access to all the views for a Data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each Data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView acc_no, acc_type, ifsc_code, ben_name, email_id, status;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.t_date);
            txtFooter = (TextView) v.findViewById(R.id.t_date);
            acc_no = v.findViewById(R.id.acc_no);
            acc_type = v.findViewById(R.id.acc_type);
            ifsc_code = v.findViewById(R.id.ifsc_code);
            ben_name = v.findViewById(R.id.ben_name);
            email_id = v.findViewById(R.id.email_id);
            status = v.findViewById(R.id.status);
        }
    }

    public void add(int position, String item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BenficiaryAdapter(ArrayList<Data_New> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BenficiaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_benificary, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.ifsc_code.setText(dataModelArrayList.get(position).getIfsc_code());
        holder.email_id.setText(dataModelArrayList.get(position).getEmail());
        //holder.ben_name.setText(dataModelArrayList.get(position).getAlias());
        holder.acc_no.setText(dataModelArrayList.get(position).getAccount_number());

        //holder.txtHeader.setText(name);
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        //holder.txtFooter.setText("Footer: " + name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

}