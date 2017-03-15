package br.com.trmasolucoes.cardiopapers.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import br.com.trmasolucoes.cardiopapers.R;
import br.com.trmasolucoes.cardiopapers.adapter.CommentAdapter;
import br.com.trmasolucoes.cardiopapers.database.PostCommentDAO;
import br.com.trmasolucoes.cardiopapers.model.Post;


public class PostDetalheActivity extends AppCompatActivity {
    private static final String TAG = "Script";
    private ScrollView content_feed;
    private Toolbar mToolbar;
    private Post post;
    private TextView tvDescription;
    private ImageView img_feed;
    private TextView txt_title;
    private TextView txt_date;
    private TextView txtContent;
    private ViewGroup mRoot;
    private ProgressDialog progressDialog;
    private WebView webviewFeed;
    private PostCommentDAO postCommentDAO;
    private ListView commentList;
    private String element;
    private Document document;
    private Elements elements;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transitions);
            getWindow().setSharedElementEnterTransition(transition);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detalhe);

        /* Roda o progressbar */
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        img_feed = (ImageView) findViewById(R.id.img_post_detalhe);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_date = (TextView) findViewById(R.id.txt_date);
        webviewFeed = (WebView) findViewById(R.id.webviewcontent);
        mRoot = (ViewGroup) findViewById(R.id.root);
        commentList = (ListView) findViewById(R.id.list_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getParcelable("post") != null) {
            post = getIntent().getExtras().getParcelable("post");
        } else {
            Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
            finish();
        }

        /* Se tem conexão com a internet apenas mostro um webview */
        /* Controle de cache para imagem*/
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });

        okHttpClient.setCache(new Cache(PostDetalheActivity.this.getCacheDir(), Integer.MAX_VALUE));
        OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
        Picasso picasso = new Picasso.Builder(PostDetalheActivity.this).downloader(okHttpDownloader).build();
        //Carrega a imagem
        picasso.load(post.getTumbnailLarge()).into(img_feed);

        if (img_feed.getDrawable() == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(PostDetalheActivity.this.getResources(), R.drawable.logo_3);
            img_feed.setImageBitmap(bitmap);
        }

        txt_title.setText(post.getTitle());
        txt_date.setText(post.getDate());

        webviewFeed.getSettings().setJavaScriptEnabled(true);
        webviewFeed.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webviewFeed.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        webviewFeed.getSettings().setAppCacheEnabled(true);
        webviewFeed.getSettings().setJavaScriptEnabled(true);
        webviewFeed.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webviewFeed.getSettings().setPluginState(WebSettings.PluginState.ON);
        webviewFeed.getSettings().setLoadWithOverviewMode(true);
        webviewFeed.getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webviewFeed.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webviewFeed.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        webviewFeed.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                finish();
            }
        });


        if (post.getContent().contains("slideshare") || post.getContent().contains("https://www.youtube.com/watch?")){
            PostHtmlRequest postHtmlRequest = new PostHtmlRequest();
            webviewFeed.getSettings().setUseWideViewPort(true);
            webviewFeed.getSettings().setLoadWithOverviewMode(true);
            postHtmlRequest.execute(post.getGuid());
        }else {
            String style = "<style>img{display: inline;height: auto;max-width: 100%;}</style>";
            webviewFeed.loadData( style + post.getContent(), "text/html; charset=UTF-8", null);
        }


        if (post.getComments().size() > 0) {
            commentList.setVisibility(View.VISIBLE);
            CommentAdapter commentAdapter = new CommentAdapter(PostDetalheActivity.this, post.getComments());
            commentList.setAdapter(commentAdapter);

            /* Mede o tamnahdo do listview de comentarios para aplicar no layout */
            int totalHeight = 0;
            ListAdapter adapter = commentList.getAdapter();
            int lenght = adapter.getCount();
            for (int i = 0; i < lenght; i++) {
                View listItem = adapter.getView(i, null, commentList);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = commentList.getLayoutParams();
            params.height = totalHeight + (commentList.getDividerHeight() * (adapter.getCount() - 1));
            commentList.setLayoutParams(params);
            commentList.requestLayout();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("post", post);
    }

    @Override
    public void onPause() {
        super.onPause();
        webviewFeed.onPause();
        webviewFeed.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        webviewFeed.resumeTimers();
        webviewFeed.onResume();
    }


    @Override
    protected void onDestroy() {
        webviewFeed.destroy();
        webviewFeed = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(mRoot, new Slide());
            tvDescription.setVisibility(View.INVISIBLE);
            txtContent.setVisibility(View.INVISIBLE);
        }
        super.onBackPressed();
    }

    /**
     * Implementação de botão de voltar
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webviewFeed.canGoBack()) {
                        webviewFeed.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public boolean getConnection() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }

    private class PostHtmlRequest extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                document = Jsoup.connect(params[0]).get();

                elements = document.select(".entry-content iframe");

                element =  elements.toString();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            webviewFeed.loadData(post.getContent().concat(element), "text/html; charset=UTF-8", null);
        }
    }
}
