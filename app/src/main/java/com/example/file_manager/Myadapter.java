package com.example.file_manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;


public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {

    Context context;
    File[] filesAndFolders;

    public Myadapter(Context context, File[] filesAndFolders) {
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        File selectedFile = filesAndFolders[position];
        holder.textView.setText(selectedFile.getName());

        if(selectedFile.isDirectory()){
            holder.imageView.setImageResource(R.drawable.foldericon);
        }else{
            holder.imageView.setImageResource(R.drawable.docicon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFile.isDirectory()) {
                    Intent intent = new Intent(context, FileListActivity.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    //open this file
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e){
                        Toast.makeText(context.getApplicationContext(),"Cannot open the file",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("Delete");
                popupMenu.getMenu().add("Move");
                popupMenu.getMenu().add("Rename");
                popupMenu.getMenu().add("Save Locally");
                popupMenu.getMenu().add("Save As Confidential");


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Delete")){
                           boolean deleted =  selectedFile.delete();
                           if (deleted){
                               Toast.makeText(context.getApplicationContext(),"DELETED",Toast.LENGTH_SHORT).show();
                               v.setVisibility(View.GONE);
                           }

                        }
                        if (item.getTitle().equals("Move")){
                            Toast.makeText(context.getApplicationContext(),"MOVED",Toast.LENGTH_SHORT).show();

                        }
                        if (item.getTitle().equals("Rename")){
                            Toast.makeText(context.getApplicationContext(),"RENAME",Toast.LENGTH_SHORT).show();

                        }
                        if (item.getTitle().equals("Save Locally")){
                            Toast.makeText(context.getApplicationContext(),"Saved Locally",Toast.LENGTH_SHORT).show();

                        }

                        if (item.getTitle().equals("Save As Confidential")){
                            Toast.makeText(context.getApplicationContext(),"Saved to Confidential",Toast.LENGTH_SHORT).show();

                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_view);
        }
    }
}



