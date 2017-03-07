package br.com.trmasolucoes.cardiopapers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.trmasolucoes.cardiopapers.R;

public class SplashActivity extends Activity {

    private Thread mSplashThread;
    private boolean mblnClicou = false;
    ImageView imageView;
    /**************************************************************************************/
    /**             Evento chamado quando a activity é executada pela primeira vez			 */
    /**
     * ************************ *******************************************************
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.push_down_enter, R.anim.push_down_exit);
        setContentView(R.layout.activity_splash);

        //pega a imagem para girar
        imageView = (ImageView) findViewById(R.id.imgSplash);

        // Create an animation instance
        Animation an = AnimationUtils.loadAnimation(this, R.anim.fade_in_enter);

        // Set the animation's parameters
        an.setDuration(1500);     // keep rotation after animation

        imageView.setAnimation(an);

        /**************************************************************************************/
        /**            Thead que roda enquanto espera para abrir tela principal do App			 */
        /*************************** ********************************************************/
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                        wait(4000);
                        mblnClicou = true;
                    }
                } catch (InterruptedException ex) {
                }
                /**************************************************************************************/
                /**                        Se for clicado Carrega a Activity Principal					 */
                /*************************** ********************************************************/
                if (mblnClicou) {
                    finish();

                    Intent i = new Intent();
                    i.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        };

        mSplashThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        /**********************************************************************************************/
        /**     garante que quando o usuário clicar no botão "Voltar" o sistema deve finalizar a thread  */
        /*************************** ******************************************************************/
        mSplashThread.interrupt();
    }


    /**********************************************************************************************/
    /**     O método abaixo finaliza o comando wait mesmo que ele não tenha terminado sua espera	  */
    /**
     * ************************ *****************************************************************
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mblnClicou = true;
                mSplashThread.notifyAll();
            }
        }
        return true;
    }
}
