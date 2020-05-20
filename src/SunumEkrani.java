/*
Sunum Ekranı - Sunum penceresi
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;




public class SunumEkrani extends JPanel implements Runnable {
    String dosyaYolu = "kaynaklar//"; // Kaynaklar için dosya yolu.
    int sayac = 0;
    // Pencere sabitleri.
    int PENCERE_GENISLIK = 1280;
    int PENCERE_YUKSEKLIK = 664;
    int bulunanYol = 0;
    int seciliYol = 1;

    String gosterilecekYol = "";
    float gosterilecekUzaklik = 0;

    // Resim dosyaları.
    BufferedImage haritaSunumResim;
    BufferedImage evResim;
    BufferedImage bayrakResim;

    ArrayList<Sehir> kargoSehirleri;

    Dijkstra dijkstra;
    Genetik genetik;

    Thread thread = new Thread(this);
    Font haritaIndex = new Font("Courier New", 1, 32);

    LinkedList<LinkedList<Dugum>> enKisaToplamYol;

    LinkedList<LinkedList<Dugum>> hesaplananYolGenetik = new LinkedList<LinkedList<Dugum>>();

    LinkedList<LinkedList<Dugum>> eskiHesaplananYolGenetik = new LinkedList<LinkedList<Dugum>>();
    LinkedList<LinkedList<Dugum>> yeniHesaplananYolGenetik = new LinkedList<LinkedList<Dugum>>();

    LinkedList<LinkedList<Dugum>> enKisaToplamYol2;
    int enKisaToplamUzunluk = Integer.MAX_VALUE;

    ArrayList<Yol> top5Yol = new ArrayList<Yol>();

    JComboBox yolSecim;
    JTextArea yolGosterici;
    JLabel toplamBulunanYolSayisi;
    JLabel bilgilendirmeYazisi;

    int threadBekleme = 60;
    Yol enSonYol;

    ArrayList<ArrayList<String>> hesaplananKisaYollar = new ArrayList<ArrayList<String>>();
    ArrayList<Yol> hesaplananTumYollar = new ArrayList<Yol>(); // En iyi 5 yolu tutuyor

    //HARD SINIR - 200 Yol
    static ArrayList<Yol> hesaplananTumCiktiYollari = new ArrayList<Yol>(); // Tüm yolları tutacak çıktıda yazdırılacak.
    CiktiYazdirici ciktiYazdirici;
    Random random;
    ImageIcon ciktiIcon = new ImageIcon("kaynaklar/kagitIcon2.png");
    JButton ciktiButon = new JButton("Tüm Yolların ve Matrisin Çıktısını Al", ciktiIcon);

    String aramaDurumu = "Canlı Önizleme\nArama Durumu : Devam Ediyor(Değişiklikler Saplandı).";
    int ayniYol = 0;

    public SunumEkrani(ArrayList<Sehir> secilenSehirler) throws IOException {
        for(int i = 0; i < 5; i++){
            hesaplananKisaYollar.add(null);
            hesaplananTumYollar.add(null);
        }

        //Thread bekleme süresinin ayarlanması
        //int durmaSaniyesi = 10;

        if(secilenSehirler.size() > 19){
            threadBekleme = 1;
        }else if(secilenSehirler.size() > 14){
            threadBekleme = 10;
        }else if(secilenSehirler.size() > 12){
            threadBekleme = 40;
        }else if(secilenSehirler.size() > 10){
            threadBekleme = 60;
        }else if(secilenSehirler.size() > 4){
            threadBekleme = 80;
        }else if(secilenSehirler.size() > 0){
            threadBekleme = 100;
        }

        enSonYol = new Yol(null, 0);

        enKisaToplamYol = new LinkedList<LinkedList<Dugum>>();
        enKisaToplamYol2 = new LinkedList<LinkedList<Dugum>>();

       yolSecim=new JComboBox();
       yolGosterici = new JTextArea();
       yolGosterici.setBounds(470, 650, 600, 60);
       JTextArea yolUzunlukGosterici = new JTextArea();
       toplamBulunanYolSayisi = new JLabel();
       yolUzunlukGosterici.setBounds(1100,650,150,30);
       yolUzunlukGosterici.setEditable(false);
       this.add(yolUzunlukGosterici);

       yolGosterici.setLineWrap(true);
       yolGosterici.setWrapStyleWord(true);
       yolGosterici.setEditable(false);
       //this.add(yolGosterici);
        JLabel enIyi5YolYazi = new JLabel("En İyi Yollar");
        enIyi5YolYazi.setBounds(1280-100-10-600-10-110-60,580,160,20);
        this.add(enIyi5YolYazi);
        yolSecim.setBounds(1280-100-10-600-10-110-60, 580+20+4,160,20);
        //yolGostericiScroll.setBounds(570,650,600,600);
        toplamBulunanYolSayisi.setBounds(1280-100-10-600-10-110-60,580+20+4+28,170,20);
        this.add(toplamBulunanYolSayisi);

        JScrollPane yolGostericiScroll = new JScrollPane(yolGosterici);
        yolGostericiScroll.setBounds(1280-100-10-600-10,580+20+4,600,50);
        JLabel yolBilgisi = new JLabel("Yol Bilgisi");
        yolBilgisi.setBounds(1280-100-10-600-10, 580, 100, 20);
        this.add(yolBilgisi);
        yolGostericiScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //yolGostericiScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(yolGostericiScroll);

        JScrollPane yolUzunlukGostericiScroll = new JScrollPane(yolUzunlukGosterici);
        JLabel yolUzunlukBilgisi = new JLabel("Yol Uzunluğu");
        yolUzunlukBilgisi.setBounds(1280-10-100,580,100,20);
        this.add(yolUzunlukBilgisi);
        yolUzunlukGostericiScroll.setBounds(1280-10-100,580+20+4,100,20);
        this.add(yolUzunlukGostericiScroll);


        ciktiButon.setBounds(31,590,349,70);
        ciktiButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ciktiYazdirici = new CiktiYazdirici();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                //Matris Yazdırılması
                if(ciktiYazdirici.hesaplamaYapildi == false) {
                    ciktiYazdirici.tumSehirlerinMesafeleriniHesapla();
                    try {
                        ciktiYazdirici.ciktilariYazdir();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                //Tüm Yolların Yazdırılması
                try {
                    ciktiYazdirici.ciktilariYazdir2();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                ciktiYazdirici = null;

                System.gc();

            }
        });
        this.add(ciktiButon);


        yolSecim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(yolSecim.getSelectedItem().toString() != null) {
                    seciliYol = Integer.parseInt(yolSecim.getSelectedItem().toString());
                }


                gosterilecekYol = "";
                for(String s : hesaplananKisaYollar.get(seciliYol-1)) {
                    gosterilecekYol += s;
                    gosterilecekYol += " ";
                }
                yolGosterici.setText(gosterilecekYol);

                gosterilecekUzaklik = hesaplananTumYollar.get(seciliYol-1).yolUzunlugu;
                yolUzunlukGosterici.setText(Float.toString(gosterilecekUzaklik) + " km");

            }
        });


        random = new Random();

        this.setPreferredSize(new Dimension(PENCERE_GENISLIK, PENCERE_YUKSEKLIK)); // Panel boyutunun ayarlanması.
        this.setLayout(null);
        //Main.pencere.pack();
        this.add(yolSecim);

        // Görsellerin yüklenmesi.
        try{
            haritaSunumResim = ImageIO.read(new File(dosyaYolu + "haritaSunum3.png"));
            evResim = ImageIO.read(new File(dosyaYolu + "ev2.png"));
            bayrakResim = ImageIO.read(new File(dosyaYolu + "bayrak2.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

        dijkstra = new Dijkstra();

        kargoSehirleri = secilenSehirler;
        kargoSehirlerininYollariniHesapla();

        genetik = new Genetik(kargoSehirleri);

        thread.start();

    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Tuval çizimlerinde kenar yumuşatmanın aktifleştirilmesi.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Çizim İşlemleri
        g.drawImage(haritaSunumResim, 0, 0, null); //Haritanın çizdirilmesi

        g.setColor(Color.black);
        //Yollarin Cizdirilmesi
        for(Sehir s : Veriler.sehirler){
            for(Sehir k : s.komsuSehirler){
                g.drawLine((int)s.x, (int)s.y, (int)k.x, (int)k.y);
            }
        }

        //Sehirlerin Cizdirilmesi
        for(Sehir s : Veriler.sehirler){
            g.fillOval((int)s.x-3, (int)s.y-3, 6, 6);
        }

        //Kargo Sehirlerinin Cizdirilmesi
        for(Sehir s : kargoSehirleri){
            g.setColor(Color.RED);
            g.fillOval((int)s.x-5, (int)s.y-5, 10, 10);
        }

        for(int i = 0; i < genetik.enIyıSonuc.size(); i++){
            g.setColor(Color.BLACK);
            g.setFont(haritaIndex);
        }


        eskiHesaplananYolGenetik.removeAll(eskiHesaplananYolGenetik);
        eskiHesaplananYolGenetik.addAll(hesaplananYolGenetik);

        hesaplananYolGenetik.removeAll(hesaplananYolGenetik);
        for(int i = 0; i < genetik.mevcutEnIyıSonuc.size()-1; i++){
            hesaplananYolGenetik.add(Veriler.tumKisaYollar.get(genetik.mevcutEnIyıSonuc.get(i)).get(genetik.mevcutEnIyıSonuc.get(i+1)));
        }
        hesaplananYolGenetik.add(Veriler.tumKisaYollar.get(genetik.mevcutEnIyıSonuc.get(genetik.mevcutEnIyıSonuc.size()-1)).get(genetik.mevcutEnIyıSonuc.get(0)));

        //Burda bu yolu da normalize etmem lazim

        yeniHesaplananYolGenetik.removeAll(yeniHesaplananYolGenetik);
        yeniHesaplananYolGenetik.addAll(hesaplananYolGenetik);

        enKisaToplamYol.removeAll(enKisaToplamYol);
        for(int i = 0; i < genetik.enIyıSonuc.size()-1; i++){
            enKisaToplamYol.add(Veriler.tumKisaYollar.get(genetik.enIyıSonuc.get(i)).get(genetik.enIyıSonuc.get(i+1)));
        }
        enKisaToplamYol.add(Veriler.tumKisaYollar.get(genetik.enIyıSonuc.get(genetik.enIyıSonuc.size()-1)).get(genetik.enIyıSonuc.get(0)));

        ArrayList<String> normalizeEnKisaToplamYol = yoluNormalizeEt(enKisaToplamYol);


        if(hesaplananKisaYollar.get(0) == null || !hesaplananKisaYollar.get(0).equals(normalizeEnKisaToplamYol)){
            hesaplananKisaYollar.add(0, normalizeEnKisaToplamYol);
            hesaplananTumYollar.add(0, new Yol(new LinkedList<LinkedList<Dugum>>(enKisaToplamYol), genetik.mevcutHesaplananUzaklik));
            hesaplananKisaYollar.remove(5);
            hesaplananTumYollar.remove(5);

            if(bulunanYol < 100){
                hesaplananTumCiktiYollari.add(0, new Yol(new LinkedList<LinkedList<Dugum>>(enKisaToplamYol), genetik.mevcutHesaplananUzaklik));
            }else{
                hesaplananTumCiktiYollari.add(0, new Yol(new LinkedList<LinkedList<Dugum>>(enKisaToplamYol), genetik.mevcutHesaplananUzaklik));
                hesaplananTumCiktiYollari.remove(hesaplananTumCiktiYollari.size()-1);
            }

            bulunanYol++;
            toplamBulunanYolSayisi.setText("Toplam Bulunan Yol: " + Integer.toString(bulunanYol));

            //Yeni bir yol bulunduğunda buraya giriyor
            //yolSecim.s
            yolSecim.setSelectedItem(Integer.toString(seciliYol));

            aramaDurumu = "Arama Durumu : Devam Ediyor Lütfen Bekleyiniz... (Değişiklikler Saptandı).";

            if(bulunanYol <= 5){
                yolSecim.addItem(Integer.toString(bulunanYol));
            }
            //System.out.println("Çalıştı!");
            ayniYol = 0;
        }else{
            //Aynı yolu buluyor.
            ayniYol++;

            if(ayniYol > 10 * (kargoSehirleri.size()/2)){
                aramaDurumu = "Arama Durumu : Yollar Gösteriliyor.";
            }
        }


        //for(int k = 0; k < hesaplananTumYollar.size(); k++) {
            int k = seciliYol-1;
            if(hesaplananTumYollar.get(k) != null) {
                for (LinkedList<Dugum> liste : hesaplananTumYollar.get(k).yolListesi) {
                    for (int i = 0; i < liste.size(); i++) {
                        //System.out.println(liste.get(i).isim);
                        if (i < liste.size() - 1) {
                            Dugum d1 = liste.get(i);
                            Dugum d2 = liste.get(i + 1);

                            if(k == 0){
                                g.setColor(Color.BLUE);
                            }else if(k == 1){
                                g.setColor(Color.RED);
                            }else if(k == 2){
                                g.setColor(Color.CYAN);
                            }else if(k == 3){
                                g.setColor(Color.magenta);
                            }else if(k == 4){
                                g.setColor(Color.orange);
                            }
                            ((Graphics2D) g).setStroke(new BasicStroke(10f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                            //((Graphics2D) g).setStroke(new BasicStroke(8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0f,new float[] {12.0f,12.0f},0.0f));
                            //g.setColor(Color.BLUE);

                            g.drawLine((int) d1.x, (int) d1.y, (int) d2.x, (int) d2.y);
                        }
                    }
                }
            }
        //}

        g.drawImage(evResim, (int)Veriler.sehirler.get(40).x-12, (int)Veriler.sehirler.get(40).y-50, null);

        for(Sehir s: kargoSehirleri){
            g.drawImage(bayrakResim, (int)s.x-17, (int)s.y-30, null);
        }



        int rX = 16;
        int rY = 602;

        g.setColor(Color.BLUE);
        g.fillOval(rX-5, rY-5 + 11*0, 10, 10);
        g.setColor(Color.RED);
        g.fillOval(rX-5, rY-5 + 11*1, 10, 10);
        g.setColor(Color.CYAN);
        g.fillOval(rX-5, rY-5 + 11*2, 10, 10);
        g.setColor(Color.magenta);
        g.fillOval(rX-5, rY-5 + 11*3, 10, 10);
        g.setColor(Color.orange);
        g.fillOval(rX-5, rY-5 + 11*4, 10, 10);

        g.setColor(Color.black);
        g.setFont(new Font("Courier New", 1, 12));
        g.drawString("Canlı Önizlemeli Harita", 4,12);
        g.drawString(aramaDurumu, 4,24);
        g.drawString("  Genetik algoritma popülasyon üzerinden \"en iyi sonucu\" bulmaya odaklı oldu-",732,510);
        g.drawString("ğundan alternatif yol elde etmek için istenen girdi aralıklarında populasyon",732,525);
        g.drawString("çeşitliliği ve çalışma hızı düşürüldü.Yirmi girdi üzerinde limit bulunmamakta.",732,540);
        g.drawString("  Mevcut arama durumu sol üstte, genel bilgiler alttaki panelde bulunmaktadır.",732,555);
        g.drawString("  Çıktı dosyaları uygulamanın ana dizininde oluşmaktadır.",732,570);
    }

    void kargoSehirlerininYollariniHesapla(){
        for(int i = 0; i < kargoSehirleri.size(); i++){
            Veriler.tumKisaYollar.add(new LinkedList <LinkedList<Dugum>>());
            for(int j = 0; j < kargoSehirleri.size(); j++){
                Veriler.tumKisaYollar.get(i).add(dijkstra.dijkstraHesapla(
                        Veriler.sehirler, kargoSehirleri.get(i).plaka, kargoSehirleri.get(j).plaka)
                );
            }
        }
    }

    public void guncelle(){
        //Burada dongusel islemler yapilacak.
            genetik.uygunluguHesapla();
            genetik.uygunluguNormalizeEt();
            genetik.sonrakiNesliOlustur();
    }

    @Override
    public void run() {
        while(true){
            guncelle();
            repaint();
            System.gc();
            try {
                thread.sleep(threadBekleme);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static ArrayList<String> yoluNormalizeEt(LinkedList<LinkedList<Dugum>> enKisaToplamYol){
        ArrayList<String> normalizeYol = new ArrayList<String>();
        ArrayList<String> normalizeYol2 = new ArrayList<String>();

        //Listeki tüm elemanlari bir string arraylistine atiyoruz.
        for(LinkedList<Dugum> liste: enKisaToplamYol){
            for(int i = 0; i < liste.size(); i++){
                normalizeYol.add(liste.get(i).isim);
            }
        }

        //Eğer ilk elemanım kocaeli ise ve son eleman kocaeli ise sadece peşpeşe gelenleri kaldırıcam
        int hizaIndexi = 0;
        for(int i = 0; i < normalizeYol.size()-1; i++){
            if(normalizeYol.get(i).equals("Kocaeli") && normalizeYol.get(i+1).equals("Kocaeli")){
                hizaIndexi = i+1;
                break;
            }
        }

        for(int i = hizaIndexi; i < normalizeYol.size(); i++){
            normalizeYol2.add(normalizeYol.get(i));
        }

        for(int i = 0; i < hizaIndexi; i++ ){
            normalizeYol2.add(normalizeYol.get(i));
        }

        //Artık her seferinde Kocaeli en başta olacak.

        //
        boolean ciftDurumu = true;
        while(ciftDurumu){
            ciftDurumu = false;
            for(int i = 0; i < normalizeYol2.size()-1; i++){
                if(normalizeYol2.get(i).equals(normalizeYol2.get(i+1))){
                    ciftDurumu = true;
                }
            }
            if(ciftDurumu == true){
                for(int i = 0; i < normalizeYol2.size()-1; i++){
                    if(normalizeYol2.get(i).equals(normalizeYol2.get(i+1))){
                        normalizeYol2.remove(i);
                        break;
                    }
                }
            }
        }

        //Burada arraylisti gezeceğiz ve kocaeliyi tespit edeceğiz.
        return normalizeYol2;
    }

}
