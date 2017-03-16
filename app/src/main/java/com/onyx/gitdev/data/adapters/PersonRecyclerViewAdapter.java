package com.onyx.gitdev.data.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onyx.gitdev.R;
import com.onyx.gitdev.data.model.Person;

import java.util.List;

/**
 * Created by Morph-Deji on 7/25/2016.
 */

public class PersonRecyclerViewAdapter extends RecyclerView.Adapter<PersonRecyclerViewAdapter.ViewHolder> {

    private List<Person> mPersons;
    private Context context;
    private PersonInteractionListener mPersonInteractionListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Person mBoundPerson;

        public final View mView;
        public final TextView mPersonName;
        public final ImageView mUserAvatar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPersonName = (TextView) view.findViewById(R.id.dev_name);
            mUserAvatar = (ImageView) view.findViewById(R.id.dev_image);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPersonName.getText();
        }
    }

    public Person getValueAt(int position) {
        return mPersons.get(position);
    }

    public PersonRecyclerViewAdapter(Context context, List<Person> items, PersonInteractionListener listener) {
        mPersons = items;
        this.context = context;
        mPersonInteractionListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.developer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBoundPerson = mPersons.get(position);
        holder.mPersonName.setText(String.format("%s", holder.mBoundPerson.getLogin()));
        Glide.with(context).load(holder.mBoundPerson.getAvatarUrl()).into(holder.mUserAvatar);



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                Person person = getValueAt(adapterPosition);
                mPersonInteractionListener.onOrderClick(view, adapterPosition, person);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public void setData(List<Person> orders) {
        mPersons = orders;
        notifyDataSetChanged();
    }

    public interface PersonInteractionListener {
        void onOrderClick(View view, int position, Person order);
    }
}