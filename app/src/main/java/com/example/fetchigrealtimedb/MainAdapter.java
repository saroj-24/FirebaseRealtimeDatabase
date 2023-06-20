package com.example.fetchigrealtimedb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myviewHolder> {


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, int position, @NonNull MainModel model) {

             holder.nametxt.setText(model.getName());
             holder.coursetxt.setText(model.getCourse());
             holder.email.setText(model.getEmail());

             Glide.with(holder.img.getContext())
                     .load(model.getPicurl())
                     .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                     .circleCrop()
                     .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                     .into(holder.img);

             holder.btnedit.setOnClickListener(new View.OnClickListener() { // editbtn funtion
                 @Override
                 public void onClick(View v) {
                     final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                             .setContentHolder(new ViewHolder(R.layout.update_popup)) // update_laayout
                             .setExpanded(true,1900)// set the apperence values
                             .create();


                     View view = dialogPlus.getHolderView(); // add all views in view instance so we can get edittext and image value.
                     EditText name = view.findViewById(R.id.nametxt);
                     EditText email = view.findViewById(R.id.emailtxt);
                     EditText course = view.findViewById(R.id.coursetxt);
                     EditText img = view.findViewById(R.id.imageurl);

                     Button upate_button = view.findViewById(R.id.updatebtn);

                     //this is for getting value in update_layout
                     name.setText(model.getName());
                     email.setText(model.getEmail());
                     course.setText(model.getCourse());
                     img.setText(model.getPicurl());

                     dialogPlus.show();

                     upate_button.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Map<String, Object> map = new HashMap<>(); // it will add the values that given in update_layout format
                             map.put("name",name.getText().toString());
                             map.put("email",email.getText().toString());
                             map.put("course",course.getText().toString());
                             map.put("picurl",img.getText().toString());

                             FirebaseDatabase.getInstance().getReference().child("Student").child(getRef(holder.getAdapterPosition()).getKey())
                                     .updateChildren(map)
                                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {
                                             Toast.makeText(holder.nametxt.getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                                             dialogPlus.dismiss();
                                         }
                                     })
                                     .addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Toast.makeText(holder.nametxt.getContext(), "Error while updating data", Toast.LENGTH_SHORT).show();
                                             dialogPlus.dismiss();
                                         }
                                     });

                         }
                     });

                 }
             });
             holder.btndelete.setOnClickListener(new View.OnClickListener() { //To Delete the data from firebase
                 @Override
                 public void onClick(View v) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(holder.nametxt.getContext());
                     builder.setTitle("Are You Sure?");
                     builder.setMessage("Deleted Data can't be undo");

                     builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("Student")
                                    .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                         }
                     });
                     builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             Toast.makeText(holder.nametxt.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                         }
                     });
                     builder.show();
                 }
             });

    }
    public MainModel getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new myviewHolder(view);
    }

    class myviewHolder extends RecyclerView.ViewHolder{

        TextView nametxt,coursetxt,email;
        CircleImageView img;

        Button btnedit,btndelete;
        public myviewHolder(@NonNull View itemView) {
            super(itemView);

           img = (CircleImageView)itemView.findViewById(R.id.imagepic);
           nametxt = (TextView)itemView.findViewById(R.id.name);
           email = (TextView)itemView.findViewById(R.id.emailtxt);
           coursetxt = (TextView)itemView.findViewById(R.id.coursetxt);

           btnedit = (Button) itemView.findViewById(R.id.editbtn);
           btndelete =(Button)itemView.findViewById(R.id.deletebtn);

        }
    }
}
