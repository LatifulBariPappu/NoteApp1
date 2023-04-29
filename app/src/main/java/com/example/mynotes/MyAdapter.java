package com.example.mynotes;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    static Context context;
    RealmResults<Note> noteList;
    public MyAdapter(Context context, RealmResults<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_view,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note=noteList.get(position);
        holder.titleOutput.setText((CharSequence) note.getTitle());
        holder.descriptionOutput.setText((CharSequence) note.getDescription());

        String dateTime= DateFormat.getDateTimeInstance().format(note.createdTime);
        holder.timeOutput.setText(dateTime);



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu=new PopupMenu(context,v);
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Delete")){
                            //delete the note
                            Realm realm=Realm.getDefaultInstance();
                            realm.beginTransaction();
                            note.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(context,"Note deleted",Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                menu.show();
                return true;
            }
        });


    }
    @Override
    public int getItemCount() {

        return noteList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput=itemView.findViewById(R.id.titleoutput);
            descriptionOutput=itemView.findViewById(R.id.descriptionoutput);
            timeOutput=itemView.findViewById(R.id.timeoutput);

        }



    }
}
