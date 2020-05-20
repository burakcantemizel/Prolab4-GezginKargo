/*
Dijkstra için oluşturulmuş graf modeli düğümleri tutuyor.
 */

import java.util.HashSet;

class Graf{
    HashSet<Dugum> dugumler = new HashSet<Dugum>();

    void dugumEkle(Dugum dugumA){
        dugumler.add(dugumA);
    }
}