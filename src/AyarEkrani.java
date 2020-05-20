/*
Ayar Ekranı
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AyarEkrani extends JPanel implements ListSelectionListener, ActionListener{
    String dosyaYolu = "kaynaklar//"; // Kaynaklar için dosya yolu.
    ImageIcon pencereIcon = new ImageIcon("kaynaklar/butonIcon3.png");

    // Pencere sabitleri.
    int PENCERE_GENISLIK = 1280;
    int PENCERE_YUKSEKLIK = 528;

    // Resim dosyaları.
    BufferedImage haritaAyarResim;
    BufferedImage evResim;
    BufferedImage bayrakResim;

    // Arayüz elemanlarının tanımlanması.
    DefaultListModel<String> varsayilanListeModeli;
    JList<String> secimListesi;
    JLabel haritaBasligi;
    JLabel secilenSehirAdeti;
    JScrollPane secimListesiPencere;
    ListSelectionModel secimListesiSecimModeli;
    JButton butonYolHesapla;

    //JSlider hizSlider;
    // Veri yapıları.
    ArrayList<Sehir> secilenSehirler;

    public AyarEkrani(){
        this.setPreferredSize(new Dimension(PENCERE_GENISLIK, PENCERE_YUKSEKLIK)); // Panel boyutunun ayarlanması.
        // Arayüz elemanlarının ayarlanması.
        varsayilanListeModeli = new DefaultListModel<String>();
        haritaBasligi = new JLabel("Harita"); // Program Basligi.
        secilenSehirAdeti = new JLabel("Seçilen şehir sayısı: 0 / 81");
        secimListesi = new JList(varsayilanListeModeli){
        };
        //butonYolHesapla = new JButton("En Kısa Yolu Hesapla");
        butonYolHesapla = new JButton("En Kısa Yolu Hesapla",pencereIcon);
        //secimListesi.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // Çoklu seçim ayarlandı.
        //secimListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Çoklu seçim ayarlandı.


        /*
        https://stackoverflow.com/questions/2404546/select-multiple-items-in-jlist-without-using-the-ctrl-command-key
        kaynağından JList için selection model kodu alınmıştır.
         */
        secimListesi.setSelectionModel(new DefaultListSelectionModel() {
            private int i0 = -1;
            private int i1 = -1;

            public void setSelectionInterval(int index0, int index1) {
                if(i0 == index0 && i1 == index1){
                    if(getValueIsAdjusting()){
                        setValueIsAdjusting(false);
                        setSelection(index0, index1);
                    }
                }else{
                    i0 = index0;
                    i1 = index1;
                    setValueIsAdjusting(false);
                    setSelection(index0, index1);
                }
            }
            private void setSelection(int index0, int index1){
                if(super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                }else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });

        secimListesi.setLayoutOrientation(JList.VERTICAL); // Dikey görünüm ayarlandı.
        secimListesiPencere = new JScrollPane(secimListesi);
        secimListesiSecimModeli = secimListesi.getSelectionModel();

        secimListesiSecimModeli.addListSelectionListener(this);

        // Arayüz elemanlarının konumlandırılması.
        haritaBasligi.setBounds(12,4,100,20);
        secilenSehirAdeti.setBounds(PENCERE_GENISLIK - 290 - 10, 4, 290, 20);
        secimListesiPencere.setBounds(PENCERE_GENISLIK - 290 - 10 ,28, 290 , 435 );
        butonYolHesapla.setBounds(PENCERE_GENISLIK - 290 - 10 ,465,290,60);
        // Seçim Listesine Elemanların eklenmesi.
        secimListesineElemanlariEkle();

        // Arayüz elemanlarının panele eklenmesi.
        this.add(haritaBasligi);
        this.add(secimListesiPencere);
        this.add(butonYolHesapla);
        this.add(secilenSehirAdeti);
        //this.add(hizSlider);
        this.setLayout(null); // Koordinat bazında düzenleme.

        butonYolHesapla.addActionListener(this);

        JTextArea bilgiKutusu = new JTextArea();
        bilgiKutusu.setBounds(12, 475, 958, 40);
        bilgiKutusu.setLineWrap(true);
        bilgiKutusu.setWrapStyleWord(true);
        bilgiKutusu.setText("       Listeden hedef şehirlerin seçimini yapınız. Rota hesaplamak için en az bir şehir seçmeniz gereklidir. Genetik algoritma ile yüksek sayıda girdi için sonuç bulabildiğimiz için herhangi bir üst şehir limiti bulunmamaktadır. Elli girdiden fazlası optimum süreyi geçebilir ve yoğun işlemci tüketimi isteyebilir.");
        bilgiKutusu.setEditable(false);
        this.add(bilgiKutusu);

        // Görsellerin yüklenmesi.
        try{
            haritaAyarResim = ImageIO.read(new File(dosyaYolu + "haritaAyar3.png"));
            evResim = ImageIO.read(new File(dosyaYolu + "ev.png"));
            bayrakResim = ImageIO.read(new File(dosyaYolu + "bayrak.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

        // Veri yapılarının oluşturulması.
        secilenSehirler = new ArrayList<Sehir>();
    }


    // paint metodu override edilerek tuval çizimleri yapılıyor.
    @Override
    public void paint(Graphics g){
        super.paint(g);
        //this.setBackground(Color.LIGHT_GRAY);

        //Tuval çizimlerinde kenar yumuşatmanın aktifleştirilmesi.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Çizim İşlemleri
        g.drawImage(haritaAyarResim, 10, 28, null); //Haritanın çizdirilmesi
        g.drawImage(evResim, 225, 98, null);


        // Secilen sehirlerin cizdirilmesi
        if(secilenSehirler.size() > 0) {
            for (Sehir sehir : secilenSehirler) {
                //System.out.println(sehir.isim);
                int kucultulmusX = (int) (sehir.x * 0.75) + 10 - 5;
                int kucultulmusY = (int) (sehir.y * 0.75) + 28 - 5;

                g.setColor(Color.BLACK);
                //g.fillOval(kucultulmusX, kucultulmusY, 10, 10);
                g.drawImage(bayrakResim, kucultulmusX+3, kucultulmusY-13, null);
            }
        }


    }

    // Secim listesine elemanların eklenmesi.
    void secimListesineElemanlariEkle(){
        for(int i=0; i < 81; i++){
            varsayilanListeModeli.addElement(Veriler.sehirler.get(i).isim);
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (!e.getValueIsAdjusting()) {//Eventin 2 kere gerçekleşmesini engelliyoruz.

            int secilenSehirSayisi = 0;
            secilenSehirAdeti.setText("Secilen sehir sayisi 0 / 81");

            if (lsm.isSelectionEmpty()) {
                secilenSehirler.removeAll(secilenSehirler);
                this.repaint();
            } else {
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();

                // Oncelikle secilen sehirler listesi bosaltiliyor.
                secilenSehirler.removeAll(secilenSehirler);

                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        secilenSehirler.add(Veriler.sehirler.get(i));
                        secilenSehirSayisi++;
                        //System.out.println(Veriler.sehirler.get(i).isim);
                        this.repaint();
                    }
                }
                secilenSehirAdeti.setText("Seçilen şehirler: " + secilenSehirSayisi + " / 81");

            }

        }

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(secilenSehirler.size() > 0) {
            //Listemizde kocaeli olmasi gerek ama 2 defa da olmamasi gerek
            if(!secilenSehirler.contains(Veriler.sehirBul("Kocaeli"))){
                secilenSehirler.add(0,Veriler.sehirler.get(40));
            }

            SunumEkrani sunumEkrani = null;
            try {
                sunumEkrani = new SunumEkrani(secilenSehirler);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Main.pencere.add(sunumEkrani);
            Main.pencere.pack();
            Main.pencere.setLocationRelativeTo(null); // Pencerenin ortalanması.
            this.hide();
            //Main.pencere.remove(this);
            //System.out.println("Tiklandi!");
        }
    }


}
