package br.com.trmasolucoes.cardiopapers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.trmasolucoes.cardiopapers.R;
import br.com.trmasolucoes.cardiopapers.model.PostComment;

/**
 * Created by tairo on 30/10/16.
 */

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PostComment> list;
    private LayoutInflater layoutInflater;

    public CommentAdapter(Context context, ArrayList<PostComment> list){
        this.context = context;
        this.list = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_post_comment, viewGroup, false);

            viewHolder.user_comment_image = (ImageView) view.findViewById(R.id.user_comment_image);
            viewHolder.txt_comment_author = (TextView) view.findViewById(R.id.txt_comment_author);
            viewHolder.txt_comment_date = (TextView) view.findViewById(R.id.txt_comment_date);
            viewHolder.txt_comment_content = (TextView) view.findViewById(R.id.txt_comment_content);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PostComment postComment = list.get(i);
        viewHolder.txt_comment_author.setText(postComment.getAuthor());
        viewHolder.txt_comment_date.setText(postComment.getDate());
        viewHolder.txt_comment_content.setText(postComment.getContent());

        return view;
    }

    private static class ViewHolder {
        ImageView user_comment_image;
        TextView txt_comment_author;
        TextView txt_comment_date;
        TextView txt_comment_content;
    }
}
