package br.com.trmasolucoes.cardiopapers.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.trmasolucoes.cardiopapers.R;
import br.com.trmasolucoes.cardiopapers.model.ItemDrawer;

/**
 * Created by tairo on 04/03/17.
 */

public class DrawerAdapter extends BaseAdapter {
    private Context context;
    private List<ItemDrawer> list;

    public DrawerAdapter(Context context, List<ItemDrawer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Re-use the view if possible
        View holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.drawer_list_item, null);

            /* header */
            if (position == 0){
                convertView = inflater.inflate(R.layout.nav_header_main, null);
            }

            /* Social button */
            if (position == 6){
                convertView = inflater.inflate(R.layout.drawer_list_item_social, null);
            }

            holder = convertView;
            convertView.setTag(holder);
        } else {
            holder = (View) convertView.getTag();
        }

        if (position != 0 && position != 6){

            ImageView imageViewMenu = (ImageView) holder.findViewById(R.id.imageViewMenu);
            TextView textMenu = (TextView) holder.findViewById(R.id.textMenu);

            textMenu.setText(list.get(position).getTitle());
            imageViewMenu.setImageResource(list.get(position).getIcon());
            if (list.get(position).isShowIcon()){
                imageViewMenu.setVisibility(View.VISIBLE);
            }

            /* logout button */
            if (position == 8){
                textMenu.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }
        }

        /* Social icon click */
        if (position == 6){
            ImageView imageViewFacebook = (ImageView) convertView.findViewById(R.id.imageViewFacebook);
            ImageView imageViewTwiter = (ImageView) convertView.findViewById(R.id.imageViewTwiter);
            ImageView imageViewGoogle = (ImageView) convertView.findViewById(R.id.imageViewGoogle);
            ImageView imageViewYoutube = (ImageView) convertView.findViewById(R.id.imageViewYoutube);

            final Intent intent = new Intent(Intent.ACTION_VIEW);

            imageViewFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setData(Uri.parse("https://www.facebook.com/Cardiopapers-Blog-113480635393374/"));
                    context.startActivity(intent);
                }
            });
            imageViewTwiter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setData(Uri.parse("https://twitter.com/cardiopapers"));
                    context.startActivity(intent);
                }
            });
            imageViewGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setData(Uri.parse("https://plus.google.com/116750448870827193233/posts"));
                    context.startActivity(intent);
                }
            });
            imageViewYoutube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setData(Uri.parse("https://www.youtube.com/user/Cardiopapers"));
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
