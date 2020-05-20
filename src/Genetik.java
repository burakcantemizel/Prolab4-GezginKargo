/*
https://natureofcode.com/book/chapter-9-the-evolution-of-code/ Daniel Shiffmanın Nature Of Code kitabının 9. chapterından
yardım alınarak uygunluk fonksiyonu oluşturuldu. Genetik Algoritma Collection Nesneleri ve metodları kullanılarak en baştan
yazıldı temel olarak Daniel Shiffmanın Javascript implementasyonu referans alındı. Populasyonlar dijkstra sınıfında yapılan
tüm hedef şehirlerin uzaklık hesaplamasından dönen değerin uygunluğuna göre oluşturuluyor.

F(u) = (float)(1/(Math.pow(mesafe,10))); // Üstel fonksiyonun artış hızı kullanılarak distancea göre paydanın
//artış hızını arttırıyoruz. böylece daha yüksek uzaklıklarda uygunluk daha düşük oluyor.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Genetik{
    ArrayList<Sehir> kargoSehirleri;
    Sehir[] sehirler;
    int toplamSehirler;

    int populasyonBuyuklugu = 500;
    List<List<Integer>> populasyon;
    float[] uygunluk;

    float mevcutUzaklik = Float.POSITIVE_INFINITY;
    List<Integer> enIyıSonuc;
    List<Integer> mevcutEnIyıSonuc;

    Random r;

    float mevcutHesaplananUzaklik = Float.POSITIVE_INFINITY;

    public Genetik(ArrayList<Sehir> kargoSehirleri){
        if(kargoSehirleri.size() > 8 && kargoSehirleri.size() < 13){
            populasyonBuyuklugu = 200;
        }
        /*
        Sınıf Değişkenlerinin atanması ve başlangıç ayarları.
         */
        r = new Random();
        this.kargoSehirleri = kargoSehirleri;
        toplamSehirler = this.kargoSehirleri.size();
        ayarla();

    }

    void ayarla(){
        List<Integer> siraliGirdi = new ArrayList<Integer>();
        sehirler = new Sehir[toplamSehirler];
        populasyon = new ArrayList<List<Integer>>();
        uygunluk = new float[populasyonBuyuklugu];

        for(int i = 0; i < toplamSehirler; i++){
            sehirler[i] = kargoSehirleri.get(i);
            siraliGirdi.add(i);
        }

        List<Integer> siraliGirdiKopya = new ArrayList<Integer>();
        siraliGirdiKopya.removeAll(siraliGirdiKopya);
        siraliGirdiKopya.addAll(siraliGirdi);
        //Collections.copy(siraliGirdiKopya, siraliGirdi);

        for (int i = 0; i < populasyonBuyuklugu; i++) {
            Collections.shuffle(siraliGirdiKopya);
            populasyon.add(siraliGirdiKopya);
        }
    }

    //Uygunluk fonksiyonu ile temel uygunluk değerleri hesaplanıyor.
    void uygunluguHesapla() {
        for (int i = 0; i < populasyon.size(); i++) {
            float d = mesafeHesapla(sehirler, populasyon.get(i));
            if (d < mevcutUzaklik) {
                mevcutUzaklik = d;
                enIyıSonuc = populasyon.get(i);
            }
            if (d < mevcutHesaplananUzaklik) {
                mevcutHesaplananUzaklik = d;
                mevcutEnIyıSonuc = populasyon.get(i);
            }

            uygunluk[i] = (float)(1/(Math.pow(d,10))); // Üstel fonksiyonun artış hızı kullanılarak distancea göre paydanın
            //artış hızını arttırıyoruz. böylece daha yüksek uzaklıklarda uygunluk daha düşük oluyor.
        }
    }

    //Normalizasyon ile tüm uygunluk dizisine toplamı 1 olacak şekilde yeniden değer atanıyor.
    void uygunluguNormalizeEt() {
        float toplam = 0;
        for (int i = 0; i < uygunluk.length; i++) {
            toplam += uygunluk[i];
        }
        for (int i = 0; i < uygunluk.length; i++) {
            uygunluk[i] = uygunluk[i] / toplam;
        }
    }


    //Cross-over ve mutasyon ile bir sonraki nesil dizisi oluşturuluyor.
    void sonrakiNesliOlustur() {
        List<List<Integer>> yeniPopulasyon = new ArrayList<List<Integer>>();
        for (int i = 0; i < populasyon.size(); i++) {
            List<Integer> siraliGirdiA = biriniSec(populasyon, uygunluk);
            List<Integer> siraliGirdiB = biriniSec(populasyon, uygunluk);

            //cross-over
            List<Integer> siraliGirdi = carprazla(siraliGirdiA, siraliGirdiB);

            //mutasyon
            mutasyon(siraliGirdi, 0.01f);
            yeniPopulasyon.add(siraliGirdi);
        }
        populasyon = yeniPopulasyon;

    }

    //Cross-over yardımcı fonksiyonu popülasyon listesinden uygunluğa göre seçim yapıyor.
    List<Integer> biriniSec(List<List<Integer>> liste, float[] uygunluk) {
        int index = 0;
        float r = (float)Math.random();

        while (r > 0 && (index < populasyonBuyuklugu -1 || index > 0)) {
            r = r - uygunluk[index % populasyonBuyuklugu];
            index++;
        }
        if(index >= 1){
            index--;
        }

        List<Integer> kopyaListe = new ArrayList<Integer>();
        kopyaListe.addAll(liste.get(index % populasyonBuyuklugu));
        return kopyaListe;
    }

    //Cross-over fonksiyonu
    List<Integer> carprazla(List<Integer> siraliGirdiA, List<Integer> siraliGirdiB) {
        int baslangic = (int)Math.floor(r.nextInt(siraliGirdiA.size()));
        int alt = baslangic+1;
        int ust = siraliGirdiA.size();

        int bitis = (int)Math.floor((Math.random() * (ust - alt)) + alt);
        List<Integer> yeniSiraliGirdi = new ArrayList<Integer>();
        for (int i = baslangic; i < bitis; i++){
            yeniSiraliGirdi.add(siraliGirdiA.get(i));
        }

        for (int i = 0; i < siraliGirdiB.size(); i++) {
            int sehir = siraliGirdiB.get(i);
            if (!yeniSiraliGirdi.contains(sehir)) {
                yeniSiraliGirdi.add(sehir);
            }
        }
        return yeniSiraliGirdi;
    }


    void mutasyon(List<Integer> siraliGirdi, float mutasyonSikligi) {
        for (int i = 0; i < toplamSehirler; i++) {
            if (Math.random() < mutasyonSikligi) {
                int indexA = (int)Math.floor(r.nextInt(siraliGirdi.size()));
                int indexB = (indexA + 1) % toplamSehirler;
                degistir(siraliGirdi, indexA, indexB);
            }
        }
    }

    void degistir(List<Integer> a, int i, int j) {
        int geciciDeger = a.get(i);
        a.set(i, a.get(j));
        a.set(j, geciciDeger);
    }


    //Dijkstra ile hesaplanan uzaklıkları döndürüyor.
    float mesafeHesapla(Sehir[] noktalar, List<Integer> siraliGirdi) {
        float toplam = 0;
        for (int i = 0; i < siraliGirdi.size() - 1; i++) {
            int sehirAIndex = siraliGirdi.get(i);
            int sehirBIndex = siraliGirdi.get(i + 1);

            toplam += Veriler.tumKisaYollar.get(sehirAIndex).get(sehirBIndex).get( Veriler.tumKisaYollar.get(sehirAIndex).get(sehirBIndex).size()-1).uzaklik;

        }
        //Son şehirden başa olan uzaklığı da katıp cycle yaptık.
        int birinciSehirIndex = siraliGirdi.get(0);
        int sonSehirIndex = siraliGirdi.get(siraliGirdi.size()-1);

        toplam += Veriler.tumKisaYollar.get(birinciSehirIndex).get(sonSehirIndex).get( Veriler.tumKisaYollar.get(birinciSehirIndex).get(sonSehirIndex).size()-1).uzaklik;

        return toplam;
    }

}