/*
Dugum Nesnesi - Sehir sınıfına benzer fakat içerisinde en kısa yol bilsini tutar
! - Dijkstrada Dugum sınıfını kullan!
! - Bilgi edinmek için şehir sınıfını kullan ekstradan bellek harcama
 */

import java.util.HashMap;
import java.util.LinkedList;

class Dugum{
    String isim;
    float x;
    float y;
    int plaka;

    LinkedList<Dugum> enKisaYol = new LinkedList<Dugum>();
    Integer uzaklik = Integer.MAX_VALUE;

    HashMap<Dugum, Integer> komsuDugumler = new HashMap<Dugum, Integer>();

    void hedefEkle(Dugum hedef, int uzaklik){
        //Komsunun uzakligini guncelliyor.
        komsuDugumler.put(hedef, uzaklik);
    }

    Dugum(String isim, int plaka, float x, float y){
        this.isim = isim;
        this.plaka = plaka;
        this.x = x;
        this.y = y;
    }
}