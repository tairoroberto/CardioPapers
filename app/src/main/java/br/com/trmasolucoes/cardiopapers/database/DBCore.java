package br.com.trmasolucoes.cardiopapers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCore extends SQLiteOpenHelper {
    private static final String NOME_DB = "cardiopapers_db";
    private static final int VERSAO_DB = 1;

    public DBCore(Context context) {
        super(context, NOME_DB, null, VERSAO_DB);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("create table posts("
                + "_id integer primary key autoincrement,"
                + "postid text not null,"
                + "author text not null,"
                + "date text not null,"
                + "title text not null,"
                + "content text not null,"
                + "content_filtered text,"
                + "status text,"
                + "guid text,"
                + "commmentcount text,"
                + "display_name text,"
                + "metaimage text,"
                + "tumbnail text,"
                + "tumbnailmedium text,"
                + "tumbnaillarge text,"
                + "userimage text,"
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP );");


        db.execSQL("create table comments("
                + "_id integer primary key autoincrement,"
                + "comment_id integer not null,"
                + "post_id text not null,"
                + "author text not null,"
                + "author_email text not null,"
                + "date text null,"
                + "content text not null,"
                + "parent integer );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table posts");
        db.execSQL("drop table comments");
        onCreate(db);
    }
}
