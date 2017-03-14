package br.com.trmasolucoes.cardiopapers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.trmasolucoes.cardiopapers.model.Category;


/**
 * Created by tairo on 13/04/16.
 */
public class CategoryDAO {

    private SQLiteDatabase db;
    private Context context;
    private String[] columns;

    public CategoryDAO(Context context) {
        DBCore dbCore = new DBCore(context);
        db = dbCore.getWritableDatabase();
        this.context = context;
        this.columns = new String[]{"_id", "post_id", "name"};
    }

    public void insert(Category category) {
        ContentValues values = new ContentValues();
        values.put("post_id", category.getPostId());
        values.put("name", category.getName());

        db.insert("categories", null, values);
    }

    public void update(Category category) {
        ContentValues values = new ContentValues();
        values.put("post_id", category.getPostId());
        values.put("name", category.getName());

        db.update("categories", values, "_id = ?", new String[]{"" + category.getId()});
    }

    public void delete(Category category) {
        db.delete("categories", "_id = ?", new String[]{"" + category.getId()});
    }


    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        Cursor cursor = db.query("categories", columns, null, null, null, null, "_id");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Category category = new Category();
                category.setId(cursor.getLong(0));
                category.setPostId(cursor.getLong(1));
                category.setName(cursor.getString(2));

                list.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (list);
    }


    public List<Category> getByPostId(long id) {
        List<Category> list = new ArrayList<>();
        String where = "post_id = ?";
        Cursor cursor = db.query("categories", columns, where, new String[]{"" + id}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Category category = new Category();
                category.setId(cursor.getLong(0));
                category.setPostId(cursor.getLong(1));
                category.setName(cursor.getString(2));

                list.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (list);
    }
}