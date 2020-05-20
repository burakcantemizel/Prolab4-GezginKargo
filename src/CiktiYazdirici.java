/*
Çıktı işlemlerini gerçekleştiren sınıf
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class CiktiYazdirici {

    Dijkstra dijkstra;
    static LinkedList< LinkedList< LinkedList < Dugum > > > tumYollar;
    boolean hesaplamaYapildi = false;
    //String ciktiYolu = "ciktilar//";

    public CiktiYazdirici() throws IOException {
        dijkstra = new Dijkstra();
        tumYollar = new LinkedList<LinkedList <LinkedList<Dugum>>>();

    }

    void tumSehirlerinMesafeleriniHesapla(){
        for(int i = 0; i < Veriler.sehirler.size(); i++){
            tumYollar.add(new LinkedList< LinkedList<Dugum> >() );
            for(int j = 0; j < Veriler.sehirler.size(); j++){
                if(Veriler.sehirler.get(i).komsuSehirler.contains(Veriler.sehirler.get(j))){
                    //Eğer komşuysa
                    tumYollar.get(i).add(dijkstra.dijkstraHesapla(Veriler.sehirler,Veriler.sehirler.get(i).plaka, Veriler.sehirler.get(j).plaka));
                }else{
                    //Değilse
                    tumYollar.get(i).add(null);
                }

            }

        }

        hesaplamaYapildi = true;
    }

    void ciktilariYazdir() throws IOException {
        File matrisDosya = new File("cikti_matris.txt");
        FileWriter matrisYazdirici = new FileWriter("cikti_matris.txt");
        BufferedWriter tamponMatrisYazdirici = new BufferedWriter(matrisYazdirici);
        try {
            for(int i = 0; i < Veriler.sehirler.size(); i++){
                for(int j = 0; j < Veriler.sehirler.size(); j++) {
                    if(Veriler.sehirler.get(i).komsuSehirler.contains(Veriler.sehirler.get(j))){
                        int uzaklik = (int)tumYollar.get(i).get(j).get( tumYollar.get(i).get(j).size()-1).uzaklik;
                        tamponMatrisYazdirici.write(String.format("%03d  ", uzaklik));
                    }else{
                        tamponMatrisYazdirici.write( String.format("%03d  ", 0));
                    }
                }
                tamponMatrisYazdirici.write("\n");
            }
            tumYollar = null;
            tamponMatrisYazdirici.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ciktilariYazdir2() throws IOException {
        File yolDosya = new File( "cikti_yollar.txt");
        FileWriter yolYazdirici = new FileWriter( "cikti_yollar.txt");
        BufferedWriter tamponYolYazdirici = new BufferedWriter(yolYazdirici);
        try {
            for(int i = 0; i < SunumEkrani.hesaplananTumCiktiYollari.size(); i++){
                Yol y = SunumEkrani.hesaplananTumCiktiYollari.get(i);
                tamponYolYazdirici.write(Float.toString(y.yolUzunlugu) + "  ");

                String yol = "";
                ArrayList<String> yolListe = SunumEkrani.yoluNormalizeEt(SunumEkrani.hesaplananTumCiktiYollari.get(i).yolListesi);
                for(String s : yolListe){
                    yol += s;
                    yol += ",";
                }
                yol += "\n";
                tamponYolYazdirici.write(yol);

            }
            tamponYolYazdirici.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
