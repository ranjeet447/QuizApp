package myapp.com.quizz.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import myapp.com.quizz.R;
import myapp.com.quizz.adapter.TopicsAdapter;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularTopicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularTopicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseDatabase mFirebaseDatabase;

    RecyclerView mRecyclerView;


    public PopularTopicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularTopicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularTopicsFragment newInstance(String param1, String param2) {
        PopularTopicsFragment fragment = new PopularTopicsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_topics, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.popularTopicRecycler);
        ArrayList<String> mArrayList = new ArrayList<>();

        final TopicsAdapter topicsAdapter = new TopicsAdapter(getContext(), mArrayList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(topicsAdapter);

        mFirebaseDatabase.getReference("Quizz").child("Topics").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String topicName = dataSnapshot.getValue(String.class);
                topicsAdapter.addTopic(topicName);
                topicsAdapter.notifyItemInserted(topicsAdapter.getItemCount());  //position=topicsAdapter.getItemCount() -> new Topic will be added at the last.

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}