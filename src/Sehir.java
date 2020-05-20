/*
Sehir Nesnesi - Sehirlere ait bilgileri tutuyor.
!- Genel olarak şehir bilgilerini edinmek için kullanılıyor.
!- Graf yapısında kullanılan hali için Dugum classını kullancaz
 */

import java.util.LinkedList;

public class Sehir{
    int plaka;
    float x;
    float y;
    String isim;
    LinkedList<Sehir> komsuSehirler = new LinkedList<Sehir>();

    Sehir(int plaka, float x, float y, String isim){
        this.plaka = plaka;
        this.x = x;
        this.y = y;
        this.isim = isim;
    }

    void komsulariYazdir(){
        for(Sehir s : komsuSehirler){
            System.out.println(s.isim);
        }
    }


}