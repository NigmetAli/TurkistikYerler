package nmt.turkistikyerler;

import android.app.Application;

/**
 * Created by NigmetAli on 15.05.2016.
 */
public class konumCek extends Application {

    Float konumX,konumY;

    String yerAdi;
   // Boolean yerSec = false;

    public void setKonumX(Float konumX){
        this.konumX = konumX;
    }
    public Float getKonumX(){
        return konumX;
    }

    public void setKonumY(Float konumY){
        this.konumY = konumY;
    }

    public Float getKonumY() {
        return konumY;
    }

    public void setYerAdi(String yerAdi) {
        this.yerAdi = yerAdi;
    }

    public String getYerAdi() {
        return yerAdi;
    }

   /* public void setYerSec(Boolean yerSec) {
        this.yerSec = yerSec;
    }

    public Boolean getYerSec() {
        return yerSec;
    }*/
}
