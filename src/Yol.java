/*
Yol Sınıfı - Yol listesini ve uzunluğunu bir arada tutan yapı.
 */

import java.util.LinkedList;

public class Yol {
    LinkedList<LinkedList<Dugum>> yolListesi;
    float yolUzunlugu;

    public Yol(LinkedList<LinkedList<Dugum>> yolListesi, float yolUzunlugu){
        this.yolListesi = yolListesi;
        this.yolUzunlugu = yolUzunlugu;
    }
}
