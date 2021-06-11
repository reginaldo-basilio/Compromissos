package com.example.compromissos.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.compromissos.Adapter.CompromissoAdapter;
import com.example.compromissos.Entidades.Compromisso;
import com.example.compromissos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassadosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassadosFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CompromissoAdapter adapter;
    private List<Compromisso> compromissosList;
    private DatabaseReference databaseReference;
    private FirebaseUser userAlth;
    private Compromisso compromisso;
    private LinearLayoutManager mLinearLayoutManager;

/*

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassadosFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassadosFragment.



    // TODO: Rename and change types and number of parameters
    public static PassadosFragment newInstance(String param1, String param2) {
        PassadosFragment fragment = new PassadosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passados, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        popularListaCompromisso();
        return view;
    }

    private void popularListaCompromisso(){

        userAlth = FirebaseAuth.getInstance().getCurrentUser();
        String uid = userAlth.getUid();

        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        compromissosList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("compromissos").orderByChild("status").equalTo("Passado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                compromissosList.clear();
                for(DataSnapshot compromissoSnapshot : snapshot.getChildren()){
                    compromisso = compromissoSnapshot.getValue(Compromisso.class);

                    if(compromisso.getUid() != null && compromisso.getUid().equals(uid)){
                    //if(compromisso.getUid().equals(uid)){
                        compromissosList.add(compromisso);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
        adapter = new CompromissoAdapter(compromissosList, getContext());
        mRecyclerView.setAdapter(adapter);
    }
}