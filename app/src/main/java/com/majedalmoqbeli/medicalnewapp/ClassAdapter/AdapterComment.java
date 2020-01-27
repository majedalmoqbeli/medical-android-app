package com.majedalmoqbeli.medicalnewapp.ClassAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.ClassControl.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.ClassData.CommentData;
import com.majedalmoqbeli.medicalnewapp.ClassData.ConsultaionData;
import com.majedalmoqbeli.medicalnewapp.ConsultationCommentActivity;
import com.majedalmoqbeli.medicalnewapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {

    ArrayList<CommentData> commentData;
    Context ctx;
    ConsultaionData conData;

    public AdapterComment(ArrayList<CommentData> commentData, Context ctx,ConsultaionData conData) {
        this.commentData = commentData;
        this.ctx = ctx;
        this.conData=conData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.comment_templet, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(commentData.get(position));

    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView comment_userName,  comment, comment_data;
        CircleImageView comment_img;
        ImageView comImg;
        ImageView deleteComments;
        Map<String, String> map = new HashMap<String, String>();
        String url = "";
        CommentData mItem ;
        public ViewHolder(View itemView) {
            super(itemView);
            comment_img = itemView.findViewById(R.id.comment_img);
            comment = itemView.findViewById(R.id.comment);
            comment_userName = itemView.findViewById(R.id.comment_userName);
            comment_data = itemView.findViewById(R.id.comment_data);
            comImg=itemView.findViewById(R.id.comImg);
            deleteComments=itemView.findViewById(R.id.deleteComments);

        }

        public void bind(final CommentData commentData) {
            mItem = commentData;
            Picasso.with(ctx).load(SaveSetting.ServerURL + "user_image/" + commentData.getA_content()).into(comment_img);
            if(commentData.getHas_img().equals("1")){
                comment.setVisibility(View.GONE);
                comImg.setVisibility(View.VISIBLE);
                Picasso.with(ctx).load(SaveSetting.ServerURL + "comment_image/" + commentData.getComment()).into(comImg);
            } else {
                comment.setText(commentData.getComment());
            }
            if (commentData.getUser_id().equals(SaveSetting.USERID)){
                deleteComments.setVisibility(View.VISIBLE);
            } else deleteComments.setVisibility(View.GONE);
            comment_userName.setText(commentData.getUser_name());
            comment_data.setText(commentData.getComment_data());

            deleteComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                    builder1.setTitle("حذف التعليق");
                    builder1.setIcon(R.drawable.ic_cancel);
                    builder1.setMessage("سوف تقوم بحذف التعليق ! هل تريد المتابعة ؟");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("موافق",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    url = SaveSetting.ServerURL + "deleteComment.php";
                                    map.put("comment_id",commentData.getComment_id() );
                                    map.put("user_id",SaveSetting.USERID );
                                    map.put("Has_img",commentData.getHas_img());
                                    deleteComments();
                                    MyProgressDialog.show(ctx);
                                }
                            });
                    builder1.setNegativeButton("الغاء",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });

        }


        public void deleteComments() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            MyProgressDialog.hide();
                            Toast.makeText(ctx, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ctx,ConsultationCommentActivity.class);
                            intent.putExtra("ConData",conData);
                            ctx.startActivity(intent);
                            ConsultationCommentActivity.fa.finish();
                            Log.i("Response :", response.toString());
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hide();
                    Log.i("VolleyError :", error.toString());
                    Toast.makeText(ctx, ctx.getString(R.string.internetError), Toast.LENGTH_LONG).show();
                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
            requestQueue.add(stringRequest);
        }
    }
}
