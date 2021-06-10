package com.example.compromissos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compromissos.Entidades.Compromisso;
import com.example.compromissos.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CompromissoAdapter extends RecyclerView.Adapter<CompromissoAdapter.ViewHolder> {

    private List<Compromisso> mCompromissoList;
    private Context context;

    public CompromissoAdapter(List<Compromisso> l, Context c){
        context = c;
        mCompromissoList = l;
    }

    @NotNull
    @Override
    public CompromissoAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todos_compromissos, parent, false);

        return new CompromissoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CompromissoAdapter.ViewHolder holder, int position) {

        Compromisso item = mCompromissoList.get(position);

        holder.txtName.setText(item.getTitulo());
        holder.txtDate.setText(item.getDate());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Short click", Toast.LENGTH_SHORT).show();
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCompromissoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView txtName;
        protected TextView txtDate;
        protected LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }

}
