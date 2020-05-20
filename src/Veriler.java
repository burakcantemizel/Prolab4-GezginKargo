/*
Veriler - Programda çeşitli veriler bu sınıfta saklanıyor ve ihtiyaç halinde kullanılıyor.
 */
import java.util.LinkedList;

public class Veriler {
    static LinkedList<Sehir> sehirler;
    static int[][] uzakliklar;
    DosyaYukleyici dosyaYukleyici;

    static LinkedList< LinkedList< LinkedList < Dugum > > > tumKisaYollar;

    public Veriler(){
        dosyaYukleyici = new DosyaYukleyici();
        uzakliklar = new int[81][81];
        sehirler = dosyaYukleyici.sehirleriDosyadanYukle();
        dosyaYukleyici.komsuSehirleriDosyadanYukle();
        uzakliklar = dosyaYukleyici.uzakliklariDosyadanYukle();
        tumKisaYollar = new LinkedList < LinkedList < LinkedList <Dugum> > >();
    }
    static Sehir sehirBul(String isim){
        for(Sehir s : sehirler){
            if(s.isim.equals(isim) == true){
                return s;
            }
        }
        return null;
    }

}
