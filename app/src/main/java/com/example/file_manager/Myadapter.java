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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {

    Uri uri;
    Context context;
    File[] filesAndFolders;

    Uri ur;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

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
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("MOVE");
                popupMenu.getMenu().add("RENAME");
                popupMenu.getMenu().add("Upload");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("DELETE")){
                           boolean deleted =  selectedFile.delete();
                           if (deleted){
                               Toast.makeText(context.getApplicationContext(),"DELETED",Toast.LENGTH_SHORT).show();
                               v.setVisibility(View.GONE);
                           }

                        }
                        if (item.getTitle().equals("MOVE")){
                            Toast.makeText(context.getApplicationContext(),"MOVED",Toast.LENGTH_SHORT).show();

                        }
                        if (item.getTitle().equals("RENAME")){
                            Toast.makeText(context.getApplicationContext(),"RENAME",Toast.LENGTH_SHORT).show();

                        }

                        if (item.getTitle().equals("UPLOAD")){
                            Toast.makeText(context.getApplicationContext(),"UPLOADED",Toast.LENGTH_SHORT).show();
                            final StorageReference reference = firebaseStorage.getReference()
                                    .child("images")
                                    .child(System.currentTimeMillis()+"");
                            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Model model = new Model();
                                            model.setImage(uri.toString());

                                            firebaseDatabase.getReference().child("data")
                                                    .push()
                                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(Myadapter.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(MainActivity.this, "upload Error", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                }
                            });
                        }

                        if (item.getTitle().equals("Confidential")){
                            Toast.makeText(context.getApplicationContext(),"UPLOADED",Toast.LENGTH_SHORT).show();

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



