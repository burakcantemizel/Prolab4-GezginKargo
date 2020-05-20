/*
Programın kulanacağı dosyaları yükleyen sınıf
 */

import java.io.*;
import java.util.LinkedList;

public class DosyaYukleyici {
    String dosyaYolu = "kaynaklar//"; // Kaynaklar için dosya yolu.
    String sehirDosyasiYolu = dosyaYolu + "sehirler.txt";
    String uzaklikDosyasiYolu = dosyaYolu + "uzakliklar.txt";
    File sehirDosyasi;
    File uzaklikDosyasi;
    FileReader dosyaOkuyucu;
    FileReader uzaklikOkuyucu;
    FileReader komsuOkuyucu;
    BufferedReader sehirTamponOkuyucu;
    BufferedReader uzaklikTamponOkuyucu;
    BufferedReader komsuTamponOkuyucu;
    String sehirSatir;
    String uzaklikSatir;

    public DosyaYukleyici(){
        sehirDosyasi = new File(sehirDosyasiYolu);
        uzaklikDosyasi = new File(uzaklikDosyasiYolu);

        try {
            dosyaOkuyucu = new FileReader(sehirDosyasi);
            uzaklikOkuyucu = new FileReader(uzaklikDosyasi);
            komsuOkuyucu = new FileReader(sehirDosyasi);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        sehirTamponOkuyucu = new BufferedReader(dosyaOkuyucu);
        uzaklikTamponOkuyucu = new BufferedReader(uzaklikOkuyucu);
        komsuTamponOkuyucu = new BufferedReader(komsuOkuyucu);
    }

    public LinkedList<Sehir> sehirleriDosyadanYukle() {
        LinkedList<Sehir> sehirler = new LinkedList<Sehir>();

        while(true){
            try {
                sehirSatir = sehirTamponOkuyucu.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                sehirSatir = null;
            }

            if(sehirSatir == null){
                break;
            }else{
                String[] kelimeler = sehirSatir.split(",");
                int plaka = Integer.parseInt(kelimeler[0]);
                float x = Float.parseFloat(kelimeler[1]);
                float y = Float.parseFloat(kelimeler[2]);
                String isim = kelimeler[3];
                sehirler.add(new Sehir(plaka, x, y, isim));
            }
        }

        return sehirler;
    }

    void komsuSehirleriDosyadanYukle(){
        int sehirNumarasi = 0;


        while(true){
            try{
                sehirSatir = komsuTamponOkuyucu.readLine();
            }catch(IOException e){
                e.printStackTrace();
                sehirSatir = null;
            }

            if(sehirSatir == null){
                return;
            }else{
                String[] kelimeler = sehirSatir.split(",");
                int plaka = Integer.parseInt(kelimeler[0]);
                String isim = kelimeler[3];

                for(int i = 5; i < kelimeler.length; i++){
                    String komsuIsim = kelimeler[i];
                    Veriler.sehirler.get(sehirNumarasi).komsuSehirler.add(Veriler.sehirBul(komsuIsim));
                }

                sehirNumarasi++;
            }

        }

    }


    int[][]  uzakliklariDosyadanYukle(){
        int[][] uzakliklar;
        uzakliklar = new int[81][81];

        int sehirNumarasi = 0;
        String uzaklikSatir;

        while(sehirNumarasi < 81){
            try{
                uzaklikSatir = uzaklikTamponOkuyucu.readLine();
            }catch(IOException e){
                e.printStackTrace();
                uzaklikSatir = null;
            }

            if(uzaklikSatir == null){
                return null;
            }else{
                String[] kelimeler = uzaklikSatir.split("\t");

                for(int i = 0; i < 81; i++){
                    if(i == sehirNumarasi){
                        uzakliklar[sehirNumarasi][i] = -1;
                    }else{
                        uzakliklar[sehirNumarasi][i] = Integer.parseInt(kelimeler[i]);
                    }
                }

            }

            sehirNumarasi++;
        }

        return uzakliklar;
    }

}
