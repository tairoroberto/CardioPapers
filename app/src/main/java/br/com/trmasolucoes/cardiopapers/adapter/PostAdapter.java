package br.com.trmasolucoes.cardiopapers.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import br.com.trmasolucoes.cardiopapers.R;
import br.com.trmasolucoes.cardiopapers.model.Post;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> mList;
    private LayoutInflater mLayoutInflater;
    private float scale;
    private int width;
    private int height;

    private boolean withAnimation;
    private boolean withCardLayout;

    private static final String TAG = "Script";


    public PostAdapter(Context c, List<Post> l){
        this(c, l, true, true);
    }
    public PostAdapter(Context c, List<Post> l, boolean wa, boolean wcl){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        withAnimation = wa;
        withCardLayout = wcl;

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        v = mLayoutInflater.inflate(R.layout.item_post_card, viewGroup, false);

        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public Random rand = new Random();

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        myViewHolder.txtTitle.setText(mList.get(position).getTitle());
        myViewHolder.txtDate.setText(mList.get(position).getDate());
        myViewHolder.txtCategories.setText("Insuficiência Cardíaca • Semiologia • Valvopatias");

        /** Controle de cache para imagem*/
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });

        okHttpClient.setCache(new Cache(mContext.getCacheDir(), Integer.MAX_VALUE));
        OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
        Picasso picasso = new Picasso.Builder(mContext).downloader(okHttpDownloader).build();

        /** User image */
        if (mList.get(position).getUserImage() != null){
            picasso.load(mList.get(position).getUserImage()).into(myViewHolder.userImage);
        }else {
            picasso.load(R.drawable.foto_user).into(myViewHolder.userImage);
        }

        if (myViewHolder.userImage.getDrawable() == null){
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.foto_user);
            myViewHolder.userImage.setImageBitmap(bitmap);
        }

        /** Post image */
        if (mList.get(position).getTumbnailMedium() != null && !mList.get(position).getTumbnailMedium().equalsIgnoreCase("")){
            picasso.load(mList.get(position).getTumbnailMedium()).into(myViewHolder.imgPost);
        }

        if (myViewHolder.imgPost.getDrawable() == null){
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo_3);
            myViewHolder.imgPost.setImageBitmap(bitmap);
        }

        if(withAnimation){
            try{
                YoYo.with(Techniques.Landing)
                        .duration(700)
                        .playOn(myViewHolder.itemView);
            }
            catch(Exception e){}
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void addListItem(Post post, int position){
        mList.add(post);
        notifyItemInserted(position);
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AvatarImageView userImage;
        public ImageView imgPost;
        public TextView txtTitle;
        public TextView txtDate;
        public TextView txtCategories;

        public MyViewHolder(View itemView) {
            super(itemView);

            userImage = (AvatarImageView) itemView.findViewById(R.id.user_image);
            imgPost = (ImageView) itemView.findViewById(R.id.img_post_detalhe);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtCategories = (TextView) itemView.findViewById(R.id.categories);
        }
    }
}
