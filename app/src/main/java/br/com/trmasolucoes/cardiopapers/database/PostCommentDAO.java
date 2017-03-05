package br.com.trmasolucoes.cardiopapers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.trmasolucoes.cardiopapers.model.PostComment;


/**
 * Created by tairo on 13/04/16.
 */
public class PostCommentDAO {

    private SQLiteDatabase db;
    private Context context;
    private String[] columns;

    public PostCommentDAO(Context context) {
        DBCore dbCore = new DBCore(context);
        db = dbCore.getWritableDatabase();
        this.context = context;
        this.columns = new String[]{"_id", "comment_id", "post_id", "author", "author_email", "date", "content", "parent"};
    }

    public void insert(PostComment postComment) {
        ContentValues values = new ContentValues();
        values.put("comment_id", postComment.getCommentID());
        values.put("post_id", postComment.getPostID());
        values.put("author", postComment.getAuthor());
        values.put("author_email", postComment.getAuthorEmail());
        values.put("date", postComment.getDate());
        values.put("content", postComment.getContent());
        values.put("parent", postComment.getParent());

        db.insert("comments", null, values);
    }

    public void update(PostComment postComment) {
        ContentValues values = new ContentValues();
        values.put("comment_id", postComment.getCommentID());
        values.put("post_id", postComment.getPostID());
        values.put("author", postComment.getAuthor());
        values.put("author_email", postComment.getAuthorEmail());
        values.put("date", postComment.getDate());
        values.put("content", postComment.getContent());
        values.put("parent", postComment.getParent());

        db.update("comments", values, "_id = ?", new String[]{"" + postComment.getId()});
    }

    public void delete(PostComment postComment) {
        db.delete("comments", "_id = ?", new String[]{"" + postComment.getId()});
    }


    public List<PostComment> getAll() {
        List<PostComment> list = new ArrayList<PostComment>();
        Cursor cursor = db.query("comments", columns, null, null, null, null, "_id");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PostComment postComment = new PostComment();
                postComment.setId(cursor.getLong(0));
                postComment.setCommentID(cursor.getString(1));
                postComment.setPostID(cursor.getString(2));
                postComment.setAuthor(cursor.getString(3));
                postComment.setAuthorEmail(cursor.getString(4));
                postComment.setDate(cursor.getString(5));
                postComment.setContent(cursor.getString(6));

                list.add(postComment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (list);
    }

    public PostComment getById(long id) {
        PostComment postComment = new PostComment();
        String where = "_id = ?";

        Cursor cursor = db.query("comments", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            postComment.setId(cursor.getLong(0));
            postComment.setCommentID(cursor.getString(1));
            postComment.setPostID(cursor.getString(2));
            postComment.setAuthor(cursor.getString(3));
            postComment.setAuthorEmail(cursor.getString(4));
            postComment.setDate(cursor.getString(5));
            postComment.setContent(cursor.getString(6));

            cursor.close();
            return postComment;
        } else {
            cursor.close();
            return postComment;
        }
    }

    public List<PostComment> getByPostId(long id) {
        List<PostComment> list = new ArrayList<PostComment>();
        String where = "post = ?";
        Cursor cursor = db.query("comments", columns, where, new String[]{"" + id}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                PostComment postComment = new PostComment();
                postComment.setId(cursor.getLong(0));
                postComment.setCommentID(cursor.getString(1));
                postComment.setPostID(cursor.getString(2));
                postComment.setAuthor(cursor.getString(3));
                postComment.setAuthorEmail(cursor.getString(4));
                postComment.setDate(cursor.getString(5));
                postComment.setContent(cursor.getString(6));

                list.add(postComment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (list);
    }
}