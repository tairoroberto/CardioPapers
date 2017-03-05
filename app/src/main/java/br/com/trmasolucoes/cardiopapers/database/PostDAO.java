package br.com.trmasolucoes.cardiopapers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.trmasolucoes.cardiopapers.model.Post;


/**
 * Created by tairo on 13/04/16.
 */
public class PostDAO {

    private SQLiteDatabase db;
    private Context context;
    private String[] columns;

    public PostDAO(Context context) {
        DBCore dbCore = new DBCore(context);
        db = dbCore.getWritableDatabase();
        this.context = context;
        this.columns = new String[]{"_id", "postid", "author", "date", "title", "content", "content_filtered", "status", "guid", "commmentcount", "display_name", "metaimage", "tumbnail", "tumbnailmedium", "tumbnaillarge", "userimage", "created_at"};
    }

    public void insert(Post post) {
        ContentValues values = new ContentValues();

        values.put("postid", post.getPostId());
        values.put("author", post.getAuthor());
        values.put("date", post.getDate());
        values.put("title", post.getTitle());
        values.put("content", post.getContent());
        values.put("content_filtered", post.getContent_filtered());
        values.put("status", post.getStatus());
        values.put("guid", post.getGuid());
        values.put("commmentcount", post.getCommmentCount());
        values.put("display_name", post.getDisplay_name());
        values.put("metaimage", post.getMetaImage());
        values.put("tumbnail", post.getTumbnail());
        values.put("tumbnailmedium", post.getTumbnailMedium());
        values.put("tumbnaillarge", post.getTumbnailLarge());
        values.put("userimage", post.getUserImage());
        values.put("created_at", getDateTime());

        db.insert("posts", null, values);
    }


    public void update(Post post) {
        ContentValues values = new ContentValues();

        values.put("postid", post.getPostId());
        values.put("author", post.getAuthor());
        values.put("date", post.getDate());
        values.put("title", post.getTitle());
        values.put("content", post.getContent());
        values.put("content_filtered", post.getContent_filtered());
        values.put("status", post.getStatus());
        values.put("guid", post.getGuid());
        values.put("commmentcount", post.getCommmentCount());
        values.put("display_name", post.getDisplay_name());
        values.put("metaimage", post.getMetaImage());
        values.put("tumbnail", post.getTumbnail());
        values.put("tumbnailmedium", post.getTumbnailMedium());
        values.put("tumbnaillarge", post.getTumbnailLarge());
        values.put("userimage", post.getUserImage());
        values.put("created_at", getDateTime());

        db.update("posts", values, "_id = ?", new String[]{"" + post.getPostId()});
    }


    public void delete(Post post) {
        db.delete("posts", "_id = ?", new String[]{"" + post.getPostId()});
    }

    public void deleteAll() {
        db.delete("posts", "_id <> ?", new String[]{""});
    }

    public void deleteDate() {
        db.delete("posts", "created_at < ?", new String[]{getDateTime()});
    }

    public ArrayList<Post> getAll() {
        ArrayList<Post> list = new ArrayList<Post>();
        Cursor cursor = db.query("posts", columns, null, null, null, null, "_id", "10");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Post post = new Post();

                post.setId(cursor.getInt(0));
                post.setPostId(cursor.getInt(1));
                post.setAuthor(cursor.getString(2));
                post.setDate(cursor.getString(3));
                post.setTitle(cursor.getString(4));
                post.setContent(cursor.getString(5));
                post.setContent_filtered(cursor.getString(6));
                post.setStatus(cursor.getString(7));
                post.setGuid(cursor.getString(8));
                post.setCommmentCount(cursor.getInt(9));
                post.setDisplay_name(cursor.getString(10));
                post.setMetaImage(cursor.getString(11));
                post.setTumbnail(cursor.getString(12));
                post.setTumbnailMedium(cursor.getString(13));
                post.setTumbnailLarge(cursor.getString(14));
                post.setUserImage(cursor.getString(15));
                post.setCreated_at(formataData(cursor.getString(16)));

                list.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }

    public Post getById(long id) {
        Post post = new Post();
        String where = "_id = ?";

        Cursor cursor = db.query("posts", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            post.setId(cursor.getInt(0));
            post.setPostId(cursor.getInt(1));
            post.setAuthor(cursor.getString(2));
            post.setDate(cursor.getString(3));
            post.setTitle(cursor.getString(4));
            post.setContent(cursor.getString(5));
            post.setContent_filtered(cursor.getString(6));
            post.setStatus(cursor.getString(7));
            post.setGuid(cursor.getString(8));
            post.setCommmentCount(cursor.getInt(9));
            post.setDisplay_name(cursor.getString(10));
            post.setMetaImage(cursor.getString(11));
            post.setTumbnail(cursor.getString(12));
            post.setTumbnailMedium(cursor.getString(13));
            post.setTumbnailLarge(cursor.getString(14));
            post.setUserImage(cursor.getString(15));
            post.setCreated_at(formataData(cursor.getString(16)));

            cursor.close();
            return post;
        } else {
            cursor.close();
            return post;
        }
    }

    private Date formataData(String data) {
        if (data == null || data.equals(""))
            return null;

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = (Date) formatter.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
