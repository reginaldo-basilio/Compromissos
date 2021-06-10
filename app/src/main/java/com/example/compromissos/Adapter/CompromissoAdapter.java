package com.example.compromissos.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.compromissos.Activity.LoginActivity;
import com.example.compromissos.Entidades.Compromisso;
import com.example.compromissos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompromissoAdapter extends RecyclerView.Adapter<CompromissoAdapter.ViewHolder> {

    private List<Compromisso> mCompromissoList;
    private Context context;
    private Dialog dialog;

    private BootstrapButton btnEditCompromisso, btnUpdateStatus, btnDelete;
    private TextView txtDescription;


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
                abrirDialog();
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

    private void abrirDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_update_status_compromisso);

        btnEditCompromisso = (BootstrapButton) dialog.findViewById(R.id.btnEditCompromisso);
        btnUpdateStatus = (BootstrapButton) dialog.findViewById(R.id.btnUpdateStatus);
        btnDelete = (BootstrapButton) dialog.findViewById(R.id.btnDelete);
        txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);

        btnEditCompromisso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
