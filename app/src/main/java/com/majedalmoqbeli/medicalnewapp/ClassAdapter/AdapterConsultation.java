package com.majedalmoqbeli.medicalnewapp.ClassAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.ClassData.ConsultaionData;
import com.majedalmoqbeli.medicalnewapp.ConsultationCommentActivity;
import com.majedalmoqbeli.medicalnewapp.CreateAccountActivity;
import com.majedalmoqbeli.medicalnewapp.LoginActivity;
import com.majedalmoqbeli.medicalnewapp.MainActivity;
import com.majedalmoqbeli.medicalnewapp.ProfileDocActivity;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.SingleFragmentActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterConsultation extends RecyclerView.Adapter<AdapterConsultation.ViewHolder> {

    ArrayList<ConsultaionData> consultaionData;
    Context ctx;
    int myCon;

    public AdapterConsultation(ArrayList<ConsultaionData> consultaionData, Context ctx, int myCon) {
        this.consultaionData = consultaionData;
        this.ctx = ctx;
        this.myCon = myCon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.consultation_templet, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(consultaionData.get(position));
    }

    @Override
    public int getItemCount() {
        return consultaionData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName, cons_title, cons_content, cons_date_time, doc_name;
        ImageView is_public;
        CircleImageView cons_img;
        LinearLayout doc_nameLinear;
        ConsultaionData mItem ;
        public ViewHolder(View itemView) {
            super(itemView);
            cons_title = itemView.findViewById(R.id.cons_title);
            cons_img = itemView.findViewById(R.id.cons_img);
            cons_content = itemView.findViewById(R.id.cons_content);
            is_public = itemView.findViewById(R.id.is_public);
            userName = itemView.findViewById(R.id.userName);
            cons_date_time = itemView.findViewById(R.id.cons_date_time);
            doc_name = itemView.findViewById(R.id.doc_name);
            doc_nameLinear=itemView.findViewById(R.id.doc_nameLinear);
            if(myCon!=1 || SaveSetting.USERTYPE.equals("2")){
                doc_nameLinear.setVisibility(View.VISIBLE);
            }

            doc_nameLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  AlertDialog  alertDialog = new AlertDialog.Builder(ctx).create();
                    View view1 = LayoutInflater.from(ctx).inflate(R.layout.click_show_templet, null);
                    final CircleImageView img_doctor = view1.findViewById(R.id.img_doctor);
                    Picasso.with(ctx).load(SaveSetting.ServerURL + "user_image/user_"+mItem.getU_id_doc()+".jpg").into(img_doctor);
                    final TextView doc_name = view1.findViewById(R.id.doc_name);
                    doc_name.setText(mItem.getDoctor_name());
                    final Button btnShowProfile = view1.findViewById(R.id.btnShowProfile);
                    final Button btnSendCon = view1.findViewById(R.id.btnSendCon);
                    if (SaveSetting.USERTYPE.equals("1"))btnSendCon.setVisibility(View.GONE);

                    btnShowProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                             Intent intent = new Intent(ctx, ProfileDocActivity.class);
                             intent.putExtra("ID",mItem.getU_id_doc());
                             ctx.startActivity(intent);
                        }
                    });
                    btnSendCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (SaveSetting.USERID.equals("0"))
                                alertToGetLogin();
                            else {
                                Intent i = new Intent(ctx, SingleFragmentActivity.class);
                                i.putExtra("doc_id",mItem.getU_id_doc());
                                i.putExtra("to","send");
                                ctx.startActivity(i);
                            }
                        }
                    });

                    alertDialog.setView(view1);

                    alertDialog.show();

                }
            });
        itemView.setOnClickListener(this);
        }

        public void bind(ConsultaionData consData) {
            mItem=consData;
            Picasso.with(ctx).load(SaveSetting.ServerURL + "user_image/" + consData.getCo_a_content()).into(cons_img);
            userName.setText(consData.getUser_name());
            cons_title.setText(consData.getCons_title());
            cons_content.setText(consData.getCons_content());
            doc_name.setText(consData.getDoctor_name());
            if (consData.getIs_public().equals("1")) {
                is_public.setImageResource(R.drawable.ic_public);
            } else is_public.setImageResource(R.drawable.ic_privet);
            cons_date_time.setText(consData.getCons_date_time());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ctx, ConsultationCommentActivity.class);
            intent.putExtra("ConData",  mItem);
            ctx.startActivity(intent);
        }

        private void alertToGetLogin() {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
            builder1.setTitle("لم تقم بتسجيل الدخول");
            builder1.setIcon(R.drawable.ic_exit);
            builder1.setMessage("أنت لم تقم بتسجيل الدخول بعد ، يرجى تسجيل الدخول أولاً ثم اعد المحاولة !!");
            builder1.setCancelable(true);
            builder1.setPositiveButton("تسجيل الدخول",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ctx, LoginActivity.class);
                            ctx.startActivity(intent);
                            MainActivity.activity.finish();
                        }
                    });
            builder1.setNegativeButton("إنشاء حساب",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ctx,CreateAccountActivity.class);
                            ctx.startActivity(intent);

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }
}
