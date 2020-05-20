/*
Ana Sınıf
OZGE POYRAZ - 180202025
BURAK CAN TEMIZEL - 180202024
 */

import javax.swing.*;
import com.bulenkov.darcula.DarculaLaf;

public class Main {
    static final String PENCERE_BASLIK = "Geldik Yoktunuz - Kargo Rota Hesaplama"; // Pencere başlığı.
    static JFrame pencere = new JFrame(PENCERE_BASLIK); // Pencere oluşturulması.

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        ImageIcon pencereIcon = new ImageIcon("kaynaklar/icon.png");

        try {
            //UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        Veriler veriler = new Veriler();
        AyarEkrani ayarEkrani = new AyarEkrani(); // Ayar ekranı için panel oluşturulması.
        pencere.add(ayarEkrani); // Ayar ekranını pencereye ekledik.
        pencere.pack(); // Boyutu en fazla olan panele göre pencerenin boyutlandırılması.
        pencere.setResizable(false); // Yeniden boyutlandırmanın engellenmesi.
        pencere.setVisible(true); // Pencerenin görünür hale getirilmesi.
        pencere.setIconImage(pencereIcon.getImage());
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Varsayılan kapatma işlemi.
        pencere.setLocationRelativeTo(null); // Pencerenin ortalanması.
    }


}
