package nmt.turkistikyerler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by NigmetAli on 6.05.2016.
 */
public class GirisSayfasi extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris);

        if(!internetErisimi())
        {
            final AlertDialog.Builder internetAc = new AlertDialog.Builder(GirisSayfasi.this);
            internetAc.setTitle("İnternet Erişiminiz açık Değil!")
                    .setMessage("Lütfen İnternet Bağlantınızı Açın ve Uygulamayı Tekrar Başlatın.")
                    .setCancelable(false)
                    .setPositiveButton("Tekrar Başlat", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GirisSayfasi.this.recreate();
                        }
                    })
                    .setNegativeButton("Çıkış" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                              finish();
                         }
                    }).show();
        }
        else if(internetErisimi())
        menuEkraninaGec();

    }

    public Boolean internetErisimi(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {

            return false;

        }
    }

    private void menuEkraninaGec() {
        Animation anim_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView girisImage = (ImageView) findViewById(R.id.girisTurkistikLogo);
        anim_in.reset();
        girisImage.clearAnimation();
        girisImage.startAnimation(anim_in);


        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(GirisSayfasi.this, MainActivity.class);
                GirisSayfasi.this.finish();
                startActivity(intent);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
