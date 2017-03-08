package br.com.trmasolucoes.cardiopapers.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.trmasolucoes.cardiopapers.R;
import br.com.trmasolucoes.cardiopapers.adapter.DrawerAdapter;
import br.com.trmasolucoes.cardiopapers.adapter.PostAdapter;
import br.com.trmasolucoes.cardiopapers.database.PostCommentDAO;
import br.com.trmasolucoes.cardiopapers.database.PostDAO;
import br.com.trmasolucoes.cardiopapers.model.ItemDrawer;
import br.com.trmasolucoes.cardiopapers.model.Post;
import br.com.trmasolucoes.cardiopapers.model.PostComment;
import br.com.trmasolucoes.cardiopapers.util.JsonUTF8Request;
import br.com.trmasolucoes.cardiopapers.util.TrackerUtils;

public class HomeActivity extends AppCompatActivity {

    private List<ItemDrawer> list = new ArrayList<>();
    private static final String TAG = "Script";
    private RecyclerView mRecyclerView;
    private List<Post> posts = new ArrayList<Post>();
    private List<Post> postsAux = new ArrayList<Post>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private PostDAO postDAO;
    private PostCommentDAO postCommentDAO;
    private ProgressDialog progressDialog;
    private String before_post = "";
    private FloatingActionButton fab;
    private AutoCompleteTextView edtSearch;
    private ListView mDrawerList;
    private DrawerLayout drawer;

    private static final String[] COUNTRIES = new String[]{
            "Sem categoria",
            "Coronariopatia",
            "Prevenção",
            "ECG",
            "Terapia Intensiva Cardiológica",
            "Perioperatório",
            "Arritmia",
            "Insuficiência Cardíaca",
            "Miscelânia",
            "Lípides",
            "Métodos complementares",
            "Hipertensão arterial sistêmica",
            "Congênitas",
            "Emergências",
            "Valvopatias",
            "Pericardiopatias",
            "Semiologia",
            "Hemodinâmica",
            "Manchetes da Semana",
            "Nefrologia",
            "Podcast",
            "Marcapasso",
            "Video Aulas",
            "Imagem cardiovascular"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transitions);

            getWindow().setSharedElementExitTransition(transition);
            getWindow().setSharedElementEnterTransition(transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edtSearch);

        ArrayAdapter<String> adapterAutoComplete = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        edtSearch.setAdapter(adapterAutoComplete);

        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;

                Toast.makeText(HomeActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        list.add(new ItemDrawer("", R.drawable.vector_drawable_icon_manchetes, false));
        list.add(new ItemDrawer("Manchetes da Semana", R.drawable.vector_drawable_icon_manchetes, true));
        list.add(new ItemDrawer("Vídeos", R.drawable.vector_drawable_icon_videos, true));
        list.add(new ItemDrawer("Sobre nós", R.drawable.vector_drawable_icon_sobrenos, true));
        list.add(new ItemDrawer("Contato", R.drawable.vector_drawable_icon_contato, true));
        list.add(new ItemDrawer("Patrocinado", R.drawable.vector_drawable_icon_avatar, true));
        list.add(new ItemDrawer("", R.drawable.vector_drawable_icon_manchetes, false));
        list.add(new ItemDrawer("Configuração da conta", R.drawable.vector_drawable_icon_manchetes, false));
        list.add(new ItemDrawer("Sair", R.drawable.vector_drawable_icon_logout, true));

        // Set the adapter for the list view
        DrawerAdapter adapter = new DrawerAdapter(this, list);
        mDrawerList.setAdapter(adapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onBackPressed();

                if (position == 8) {
                    finish();
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState != null) {
            posts = savedInstanceState.getParcelableArrayList("posts");
        }

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);


        postDAO = new PostDAO(HomeActivity.this);
        postCommentDAO = new PostCommentDAO(HomeActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        PostAdapter postAdapter = new PostAdapter(HomeActivity.this, posts);
        mRecyclerView.setAdapter(postAdapter);

        /** Atualiza a lista de registros */
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                loadPosts("limit=10", "&start=1", "", "", "", true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = mLayoutManager.findLastVisibleItemPosition();
                if (position <= 5) {
                    fab.setVisibility(View.GONE);
                }

                if (position > 5 && fab.getVisibility() == View.GONE) {
                    fab.setVisibility(View.VISIBLE);
                }

                if (dy > 0 && getConnection()) { //check for scroll down

                    /** Verifico se é o ultimos post pra carrregar mais do servidor */
                    if (posts.size() == mLayoutManager.findFirstCompletelyVisibleItemPosition() + 1) {

                        if (before_post.equals("")) {
                            before_post = posts.get(posts.size() - 1).getDate();
                        }

                        loadPosts("limit=10", "", "&before=" + before_post, "", "", false);
                        final PostAdapter adapter = (PostAdapter) recyclerView.getAdapter();
                        for (int i = 0; i < postsAux.size(); i++) {
                            posts.add(postsAux.get(i));
                            recyclerView.post(new Runnable() {
                                public void run() {
                                    adapter.notifyItemInserted(posts.size());
                                }
                            });
                            before_post = postsAux.get(i).getDate();
                        }
                        postsAux.clear();
                    }
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(HomeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this, PostDetalheActivity.class);
                intent.putExtra("post", posts.get(position));

                if (posts != null) {
                    // TRANSITIONS
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        View post_img = findViewById(R.id.img_post_detalhe);
                        View post_title = findViewById(R.id.txt_title);
                        View post_date = findViewById(R.id.txt_date);

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this,
                                Pair.create(post_img, "post_img"),
                                Pair.create(post_title, "post_title"),
                                Pair.create(post_date, "post_date"));

                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        }));

        this.loadPosts("limit=10", "&start=1", "", "", "", true);


        /** Set Google Analitics */
        TrackerUtils.send(this, "Posts");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("posts", (ArrayList<Post>) posts);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadPosts(String limit, final String start, final String before, String category, String id, final boolean delete) {

        progressDialog.show();
        /** Se tem conexão copm a intertnet eu busco os posts no servidor */
        if (getConnection()) {
            RequestQueue mRequestQueue = Volley.newRequestQueue(HomeActivity.this);
            final String url = "https://cardiopapers.com.br/control/wp-feed-api.php?" + limit + start + before + category + id;
            JsonArrayRequest JSONArrayRequest = new JsonUTF8Request(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i(TAG, "COUNT: " + response.length() + "URL: " + url);

                    /** Verifico se é pra deletar os posts */
                    if (delete) {
                        postDAO.deleteDate();
                    }

                    for (int i = 0; i < response.length(); i++) {
                        Post post = new Post();
                        ArrayList<PostComment> listComments = new ArrayList<>();
                        try {
                            post.setPostId(response.getJSONObject(i).getInt("ID"));
                            post.setAuthor(response.getJSONObject(i).getString("post_author"));
                            post.setDate(response.getJSONObject(i).getString("post_date"));
                            post.setTitle(response.getJSONObject(i).getString("post_title"));
                            post.setContent(response.getJSONObject(i).getString("post_content"));
                            post.setContent_filtered(response.getJSONObject(i).getString("post_content_filtered"));
                            post.setStatus(response.getJSONObject(i).getString("post_status"));
                            post.setGuid(response.getJSONObject(i).getString("guid"));
                            post.setCommmentCount(response.getJSONObject(i).getInt("comment_count"));
                            post.setDisplay_name(response.getJSONObject(i).getString("display_name"));
                            post.setMetaImage(response.getJSONObject(i).getString("post_meta_image"));
                            post.setTumbnail(response.getJSONObject(i).getString("post_tumbnail"));
                            post.setTumbnailMedium(response.getJSONObject(i).getString("post_tumbnail_medium"));
                            post.setTumbnailLarge(response.getJSONObject(i).getString("post_tumbnail_large"));
                            post.setUserImage(response.getJSONObject(i).getString("userImage"));

                            JSONArray comments = response.getJSONObject(i).getJSONArray("comments");
                            for (int j = 0; j < comments.length(); j++) {
                                PostComment postComment = new PostComment();
                                postComment.setCommentID(comments.getJSONObject(j).getString("comment_ID"));
                                postComment.setPostID(comments.getJSONObject(j).getString("comment_post_ID"));
                                postComment.setAuthor(comments.getJSONObject(j).getString("comment_author"));
                                postComment.setAuthorEmail(comments.getJSONObject(j).getString("comment_author_email"));
                                postComment.setDate(comments.getJSONObject(j).getString("comment_date"));
                                postComment.setContent(comments.getJSONObject(j).getString("comment_content"));
                                postComment.setParent(comments.getJSONObject(j).getInt("comment_parent"));

                                postCommentDAO.insert(postComment);
                                listComments.add(postComment);
                            }
                            post.setComments(listComments);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /** Se for buscar pela primeira vez eu limpo os posts */
                        if (!before.equals("")) {
                            postsAux.add(post);
                        } else {
                            posts.add(post);
                        }

                        postDAO.insert(post);
                    }


                    if (!start.equals("")) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse:");
                    error.printStackTrace();
                }
            });

            // Access the RequestQueue through your singleton class.
            mRequestQueue.add(JSONArrayRequest);
        } else {

            /** carrego os posts da base de dados */
            posts = postDAO.getAll();
            PostAdapter postAdapter = new PostAdapter(HomeActivity.this, posts);
            mRecyclerView.setAdapter(postAdapter);
            progressDialog.dismiss();
        }
    }

    public boolean getConnection() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }
}
