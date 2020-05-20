/*
https://www.baeldung.com/java-dijkstra burada verilen dijkstra algoritması referans alınarak 81x81 matriste
tüm şehirler arasındaki en kısa uzaklığı bulabilecek şekilde düzenlenerek tekrar oluşturulmuştur. Dijkstra
implementasyonunda matrisler kullanılmayıp diğer sistemlerle uyumlu olması için collection nesneleri kullanılmıştır.

Dijkstra Temel Sınıfı - Dijkstra algoritmasının temel fonksiyonlarını barındırır.
*/

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class Dijkstra {

    public  Dijkstra(){

    }

    Graf kaynaktanEnKisaYoluHesapla(Graf graf, Dugum kaynak){
        kaynak.uzaklik = 0;
        HashSet<Dugum> gezilmisDugumler = new HashSet<Dugum>();
        HashSet<Dugum> gezilmemisDugumler = new HashSet<Dugum>();

        gezilmemisDugumler.add(kaynak);

        while(gezilmemisDugumler.size() != 0){
            Dugum mevcutDugum = enDusukUzaklikliDugumuGetir(gezilmemisDugumler);
            gezilmemisDugumler.remove(mevcutDugum);

            for(Map.Entry<Dugum, Integer> bitisiklikCifti: mevcutDugum.komsuDugumler.entrySet()){
                Dugum komsuDugum = bitisiklikCifti.getKey();
                Integer kenarAgirligi = bitisiklikCifti.getValue();

                if(!gezilmisDugumler.contains(komsuDugum)){
                    minimumUzakligiHesapla(komsuDugum, kenarAgirligi, mevcutDugum);
                    gezilmemisDugumler.add(komsuDugum);
                }
            }

            gezilmisDugumler.add(mevcutDugum);
        }


        return graf;
    }

    Dugum enDusukUzaklikliDugumuGetir(HashSet<Dugum> gezilmemisDugumler){
        Dugum enDusukUzaklikliDugum = null;
        int enDusukUzaklik = Integer.MAX_VALUE;

        for(Dugum dugum : gezilmemisDugumler){
            int dugumUzakligi = dugum.uzaklik;

            if(dugumUzakligi < enDusukUzaklik){
                enDusukUzaklik = dugumUzakligi;
                enDusukUzaklikliDugum = dugum;
            }
        }

        return enDusukUzaklikliDugum;
    }

    void  minimumUzakligiHesapla(Dugum degerlendirmeDugumu, Integer kenarAgirligi_, Dugum kaynakDugum){
        Integer kaynakUzakligi = kaynakDugum.uzaklik;

        if(kaynakUzakligi + kenarAgirligi_ < degerlendirmeDugumu.uzaklik){
            degerlendirmeDugumu.uzaklik = kaynakUzakligi + kenarAgirligi_;
            LinkedList<Dugum> enKisaYol = new LinkedList<Dugum>(kaynakDugum.enKisaYol);
            enKisaYol.add(kaynakDugum);
            degerlendirmeDugumu.enKisaYol = enKisaYol;
        }

    }

    LinkedList<Dugum> dijkstraHesapla(LinkedList<Sehir> sehirler, int baslangicSehir, int hedefSehir){
        LinkedList<Dugum> dugumler = new LinkedList<Dugum>();


        for(Sehir s : sehirler){
            dugumler.add(new Dugum(s.isim, s.plaka, s.x, s.y));
        }



        for(int i = 0; i < sehirler.size(); i++){
            Sehir s = sehirler.get(i);

            for(int j = 0; j < s.komsuSehirler.size(); j++){
                Sehir k = s.komsuSehirler.get(j);
                dugumler.get(i).hedefEkle(dugumler.get(k.plaka-1), Veriler.uzakliklar[s.plaka-1][k.plaka-1]);
            }
        }

        Graf graf = new Graf();

        for(int i = 0; i < sehirler.size(); i++){
            graf.dugumEkle(dugumler.get(i));
        }

        graf = kaynaktanEnKisaYoluHesapla(graf, dugumler.get(baslangicSehir-1));

        //Dijkstraya hedef şehiri de gönderiyoruz.
        dugumler.get(hedefSehir-1).enKisaYol.add(dugumler.get(hedefSehir-1));

        return dugumler.get(hedefSehir-1).enKisaYol;
    }

}