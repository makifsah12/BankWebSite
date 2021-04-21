
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "newClass")

public class NewClass {

    Connection connection;

    //----------------------------  Tanımlamalar
    private int radio1 = 1;
    private int radio2 = 1;

    private double tl;
    private double dolar;
    private double euro;
    private double sterlin;

    public double alis1 = 7;
    public double satis1 = 6.8;
    public double alis2 = 7;
    public double satis2 = 6.8;

    public double girilen1 = 0;
    public double girilen2 = 0;

    private String islemSonucu;
    private String isim_soyisim;
    private String hesap_numarasi;
    private String sifre;
    private String signValue;
    private String iban;
    private String islem;
    private double mevcut_para = 300; //bakiye
    private double max_kredi;
    private double kullanilabilir_bakiye;
    private double borc;
    private double toplam_odenecek;
    private double taksit_tutari;
    private int vade;

    //Para yatirma degiskenler
    private String girilenIban;
    private double girilenPara;
    //

    //Fatura
    private double elektrik, su, dogalgaz, internet;
    //

    public NewClass() {
        try {
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/addressbook", "APP", "APP");
            double bakiye = doubleVeriGetir(connection, "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'", "bakiye");
            veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = ?", bakiye + "", getHesap_numarasi());
        } catch (Exception e) {
        }
    }
    
    public void islemEkle(String islem_, String hedef_iban){ 
        veritabaninaEkle(connection, "insert into sonislemler (hesap_numarasi, hedef_numara, islem) values (?,?,?)", getHesap_numarasi(), hedef_iban, islem_);
    }

    //------------------------- FONKSİYONLAR
    public void radioDegis1() {
        switch (radio1) {
            case 1:
                alis1 = 7;
                satis1 = 6.8;
                break;
            case 2:
                alis1 = 7.5;
                satis1 = 7.3;
                break;
            case 3:
                alis1 = 8.5;
                satis1 = 8.3;
                break;
            default:
                break;
        }
    }

    public void radioDegis2() {
        switch (radio2) {
            case 1:
                alis2 = 7;
                satis2 = 6.8;
                break;
            case 2:
                alis2 = 7.5;
                satis2 = 7.3;
                break;
            case 3:
                alis2 = 8.5;
                satis2 = 8.3;
                break;
            default:
                break;
        }
    }

    public void cevir1() {
        String temp99;
        if (getTl() >= girilen1) {
            switch (radio1) {
                case 1:
                    tl = getTl() - girilen1;
                    dolar = getDolar() + (girilen1 / 7);
                    temp99 = String.format("%.2f", dolar);
                    dolar = Double.parseDouble(temp99.replaceAll(",", "."));
                    veritabaninaEkle(connection, "update donusumler set dolar = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + dolar);
                    veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    islemEkle(girilen1 + "₺  " + String.format("%.2f", (girilen1/7)) + "$'a çevrilmiştir. ", "");
                    break;
                case 2:
                    tl = getTl() - girilen1;
                    euro = getEuro() + (girilen1 / 7.5);
                    temp99 = String.format("%.2f", euro);
                    euro = Double.parseDouble(temp99.replaceAll(",", "."));
                    veritabaninaEkle(connection, "update donusumler set euro = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + euro);
                    veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    islemEkle(girilen1 + "₺  " + String.format("%.2f", (girilen1/7.5)) + "€'ya çevrilmiştir. ", "");
                    break;
                case 3:
                    tl = getTl() - girilen1;
                    sterlin = getSterlin() + (girilen1 / 8.5);
                    temp99 = String.format("%.2f", sterlin);
                    sterlin = Double.parseDouble(temp99.replaceAll(",", "."));
                    veritabaninaEkle(connection, "update donusumler set sterlin = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + sterlin);
                    veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    islemEkle(girilen1 + "₺  " + String.format("%.2f", (girilen1/8.5)) + "£'e çevrilmiştir. ", "");
                    break;
                default:
                    break;
            }
        }
    }

    public void cevir2() {
        switch (radio2) {
            case 1:
                if (getDolar() >= girilen2) {
                    dolar = getDolar() - girilen2;
                    tl = getTl() + (girilen2 * 6.8);
                    veritabaninaEkle(connection, "update donusumler set dolar = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + dolar);
                    veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    islemEkle(girilen1 + "$  " + String.format("%.2f", (girilen2*6.8)) + "₺'ye çevrilmiştir. ", "");
                }
                break;
            case 2:
                if (getEuro() >= girilen2) {
                    euro = getEuro() - girilen2;
                    tl = getTl() + (girilen2 * 7.3);
                    veritabaninaEkle(connection, "update donusumler set euro = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + euro);
                    veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    islemEkle(girilen1 + "€  " + String.format("%.2f", (girilen2*7.3)) + "₺'ye çevrilmiştir. ", "");
                }
                break;
            case 3:
                if (getSterlin() >= girilen2) {
                    sterlin = getSterlin() - girilen2;
                    tl = getTl() + (girilen2 * 8.3);
                    veritabaninaEkle(connection, "update donusumler set sterlin = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + sterlin);
                    veritabaninaEkle(connection, "update donusumler set turk_lirasi = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = '" + getHesap_numarasi() + "'", "" + tl);
                    islemEkle(girilen1 + "£  " + String.format("%.2f", (girilen2*8.3)) + "₺'ye çevrilmiştir. ", "");
                }
                break;
            default:
                break;
        }
    }

    public String faturaOde(String faturaTuru) {
        //Anlık Bakiye
        double bakiye = doubleVeriGetir(connection, "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'", "bakiye");
        double faturaTutari = doubleVeriGetir(connection, "select * from faturalar where hesap_numarasi = '" + getHesap_numarasi() + "'", faturaTuru);

        if (bakiye >= faturaTutari) {
            switch (faturaTuru) {
                case "elektrik":
                    veritabaninaEkle(connection, "update faturalar set elektrik = 0 where hesap_numarasi = ?", getHesap_numarasi());
                    break;
                case "su_faturasi":
                    veritabaninaEkle(connection, "update faturalar set su_faturasi = 0 where hesap_numarasi = ?", getHesap_numarasi());
                    break;
                case "internet":
                    veritabaninaEkle(connection, "update faturalar set internet = 0 where hesap_numarasi = ?", getHesap_numarasi());
                    break;
                case "dogalgaz":
                    veritabaninaEkle(connection, "update faturalar set dogalgaz = 0 where hesap_numarasi = ?", getHesap_numarasi());
                    break;
            }
            veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?", "" + (bakiye - faturaTutari), getHesap_numarasi());
            setIslemSonucu(faturaTutari + "TL tutarındaki " + faturaTuru + " faturası başarıyla ödenmiştir");
        }

        return "islembasarili.xhtml";
    }

    public String ekle() { //sign up yaptırıyor
        String sql = "insert into login (hesap_no, isim_soyisim, sifre) values (?,?,?)";
        String sql2 = "insert into kisiselbilgiler (hesap_numarasi, bakiye, borc) values (?,?,?)";
        System.out.println("hesap no: " + hesap_numarasi);
        veritabaninaEkle(connection, sql, hesap_numarasi, isim_soyisim, sifre);
        veritabaninaEkle(connection, sql2, hesap_numarasi, "10000.0", "1000");
        double bakiye = doubleVeriGetir(connection, "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'", "bakiye");
        veritabaninaEkle(connection, "insert into donusumler (hesap_numarasi, turk_lirasi,dolar, sterlin, euro) values (?,?,0,0,0)", getHesap_numarasi(), bakiye + "");
        veritabaninaEkle(connection, "insert into faturalar (hesap_numarasi, elektrik,su_faturasi, internet, dogalgaz) values (?,170,92.25,115,600)", getHesap_numarasi());
        return "giris.xhtml";
    }

    public String girisYap() {
        String sql = "select * from login where hesap_no = '" + hesap_numarasi + "'";
        if (hesap_numarasi.equals(stringVeriGetir(connection, sql, "hesap_no")) && sifre.equals(stringVeriGetir(connection, sql, "sifre"))) {
            return "sube_main.xhtml";
        }
        return "giris.xhtml";
    }

    public String cikisYap() {
        System.out.println("Çağırıldı");
        veritabaninaEkle(connection, "delete from currentuser");
        return "giris.xhtml";
    }

    public void veritabaninaEkle(Connection con, String sql, String... values) {
        try {
            PreparedStatement st = con.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                st.setString(i + 1, values[i]);
            }
            st.executeUpdate();
        } catch (Exception e) {
        }
    }

    public String stringVeriGetir(Connection con, String sql, String alinacakVeri) {
        String veri = null;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                veri = rs.getString(alinacakVeri);
            }
        } catch (Exception e) {
        }
        return veri;
    }

    public ArrayList<String> stringVerileriGetir(Connection con, String sql, String alinacakVeri) {//son islemler
        ArrayList<String> veriler = new ArrayList<String>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                veriler.add(rs.getString(alinacakVeri));
            }
        } catch (Exception e) {
        }
        return veriler;
    }

    public Double doubleVeriGetir(Connection con, String sql, String alinacakVeri) {//sayısal veri çekme
        Double veri = 0.0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                veri = rs.getDouble(alinacakVeri);
            }
        } catch (Exception e) {

        }

        return veri;
    }

    public int intVeriGetir(Connection con, String sql, String alinacakVeri) {//vade
        int veri = 0;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                veri = rs.getInt(alinacakVeri);
            }
        } catch (Exception e) {

        }

        return veri;
    }

    public boolean veritabanindaVarMi(Connection con, String veritabani, String hesapNo) {//hesap no kontrolu
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from " + veritabani + " where hesap_numarasi = '" + hesapNo + "'");
            if (!rs.next()) {
                return false;
            }
        } catch (Exception e) {

        }
        return true;
    }

    public String paraEkle() { // para gonderme islemi yapiyor
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + girilenIban + "'";
        String sql2 = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        double eskibakiye = doubleVeriGetir(connection, sql, "bakiye");
        double eskibakiyeGonderen = doubleVeriGetir(connection, sql2, "bakiye");
        if (eskibakiyeGonderen >= girilenPara && veritabanindaVarMi(connection, "kisiselbilgiler", girilenIban) && !girilenIban.equals(getHesap_numarasi())) {
            veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?", "" + (eskibakiyeGonderen - girilenPara), getHesap_numarasi());
            veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?", "" + (eskibakiye + girilenPara), girilenIban);
            setIslemSonucu(stringVeriGetir(connection, "select * from login where hesap_no = '" + girilenIban + "'", "isim_soyisim") + " Adlı Kişiye " + girilenPara + "TL Gönderildi");
            String gonderen = stringVeriGetir(connection, "select * from login where hesap_no = '" + getHesap_numarasi() + "'", "isim_soyisim");
            String alan = stringVeriGetir(connection, "select * from login where hesap_no = '" + girilenIban + "'", "isim_soyisim");
            String islem_ = "Hesaptan " + alan + " adlı kişiye " + girilenPara + "TL gönderilmiştir.";
            veritabaninaEkle(connection, "insert into sonislemler (hesap_numarasi, hedef_numara, islem) values (?,?,?)", getHesap_numarasi(), girilenIban, islem_);
        } else {
            setIslemSonucu("İşlem Başarısız");
        }
        return "islembasarili.xhtml";
    }

    public String paraCek() {
        System.out.println("girdi");
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        double eskibakiye = doubleVeriGetir(connection, sql, "bakiye");
        System.out.println("eski bakiye: " + eskibakiye);
        if (eskibakiye >= girilenPara) {
            System.out.println("buraya girdi");
            veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?", "+" + (eskibakiye - girilenPara), getHesap_numarasi());
            setIslemSonucu("Hesabınızdan " + girilenPara + "TL Tutarında Para Çekilmiştir");
            String islem_ = "Hesaptan " + girilenPara + "TL çekilmiştir.";
            veritabaninaEkle(connection, "insert into sonislemler (hesap_numarasi, hedef_numara, islem) values (?,?,?)", getHesap_numarasi(), girilenIban, islem_);
        } else {
            setIslemSonucu("İşlem Başarısız");
        }
        return "islembasarili.xhtml";
    }

    public String borcOde() {
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        double borcMiktari = doubleVeriGetir(connection, sql, "borc");
        double bakiye = doubleVeriGetir(connection, sql, "bakiye");
        if (girilenPara <= bakiye && borcMiktari > 0) {
            if (girilenPara >= borcMiktari) {
                girilenPara = borcMiktari;
                setIslemSonucu(borcMiktari + " Değerinde Borcunuz Ödenmiştir");
            } else {
                setIslemSonucu(girilenPara + " Değerinde Borcunuz Ödenmiştir");
            }
            veritabaninaEkle(connection, "update kisiselbilgiler set borc = ? where hesap_numarasi = ?", String.valueOf(borcMiktari - girilenPara), getHesap_numarasi());
            veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?", String.valueOf(bakiye - girilenPara), getHesap_numarasi());
            String islem_ = girilenPara + "TL değerinde borcunuz ödenmiştir.";
            veritabaninaEkle(connection, "insert into sonislemler (hesap_numarasi, hedef_numara, islem) values (?,?,?)", getHesap_numarasi(), girilenIban, islem_);
        } else {
            setIslemSonucu("Borcunuz Yoktur");
        }
        return "faces/islembasarili.xhtml";
    }

    public String krediBorcOde() {
        String sql = "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'";
        String sql2 = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        double borcMiktari = doubleVeriGetir(connection, sql, "toplam_odenecek");
        double bakiye = doubleVeriGetir(connection, sql2, "bakiye");
        if (getTaksit_tutari() <= bakiye && borcMiktari > 0) {
            veritabaninaEkle(connection, "update kredibasvurusu set toplam_odenecek = ? where hesap_numarasi = ?", "" + (getToplam_odenecek() - getTaksit_tutari()), getHesap_numarasi());
            veritabaninaEkle(connection, "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?", "" + (bakiye - getTaksit_tutari()), getHesap_numarasi());
            veritabaninaEkle(connection, "update kredibasvurusu set vade = ? where hesap_numarasi = ?", "" + (getVade() - 1), getHesap_numarasi());
            setIslemSonucu("Ödemeniz Gerçekleşmiştir. Kalan Taksit Sayısı " + getVade());
            String islem_ = "Kredi taksidi ödenmiştir";
            veritabaninaEkle(connection, "insert into sonislemler (hesap_numarasi, hedef_numara, islem) values (?,?,?)", getHesap_numarasi(), girilenIban, islem_);
        } else {
            setIslemSonucu("Kredi Borcu Öderken Bir Hata İle Karşılaşıldı");
        }
        return "faces/islembasarili.xhtml";
    }

    public void krediBasvurusu() {
        System.out.println("toplam: " + (toplam_odenecek + toplam_odenecek * (vade * 0.01)));
        System.out.println("vade: " + vade);
        String sql = "insert into kredibasvurusu (hesap_numarasi,toplam_odenecek,vade,taksit_tutari,kredi_miktari) values (?,?,?,?,?)";
        String sql2 = "update kisiselbilgiler set bakiye = ? where hesap_numarasi = ?";
        double bakiye = doubleVeriGetir(connection, "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'", "bakiye");
        if (doubleVeriGetir(connection, "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'", "toplam_odenecek") <= 0) {
            bakiye += toplam_odenecek;
            veritabaninaEkle(connection, sql2, "" + bakiye, getHesap_numarasi());
            veritabaninaEkle(connection, sql, getHesap_numarasi(), (toplam_odenecek + toplam_odenecek * (vade * 0.01)) + "", vade + "", "" + ((toplam_odenecek + toplam_odenecek * (vade * 0.01)) / vade), toplam_odenecek + "");
            Double kredi = doubleVeriGetir(connection, "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'", "kredi_miktari");
            String islem_ = kredi + "TL tutarında kredi çekilmiştir.";
            veritabaninaEkle(connection, "insert into sonislemler (hesap_numarasi, hedef_numara, islem) values (?,?,?)", getHesap_numarasi(), girilenIban, islem_);
        }
    }

    //---------------------------------------------------GETTER VE SETTER LAR----------------------------------------------------------
    public double getElektrik() {
        elektrik = doubleVeriGetir(connection, "select * from faturalar where hesap_numarasi = '" + getHesap_numarasi() + "'", "elektrik");
        return elektrik;
    }

    public double getSu() {
        su = doubleVeriGetir(connection, "select * from faturalar where hesap_numarasi = '" + getHesap_numarasi() + "'", "su_faturasi");
        return su;
    }

    public double getInternet() {
        internet = doubleVeriGetir(connection, "select * from faturalar where hesap_numarasi = '" + getHesap_numarasi() + "'", "internet");
        return internet;
    }

    public double getDogalgaz() {
        dogalgaz = doubleVeriGetir(connection, "select * from faturalar where hesap_numarasi = '" + getHesap_numarasi() + "'", "dogalgaz");
        return dogalgaz;
    }

    public String getIslemSonucu() {
        return islemSonucu;
    }

    public void setIslemSonucu(String islemSonucu) {
        this.islemSonucu = islemSonucu;
    }

    public ArrayList<String> getIslem() {
        ArrayList<String> islemler = stringVerileriGetir(connection, "select * from sonislemler where hesap_numarasi = '" + getHesap_numarasi() + "'", "islem");
        ArrayList<String> reverse = new ArrayList<String>();
        for (int i = islemler.size() - 1; i >= 0; i--) {
            reverse.add(islemler.get(i));
        }
        return reverse;
    }

    public void setIslem(String islem) {
        this.islem = islem;
    }

    public String getIsim_soyisim() {
        String sql = "select * from login where hesap_no = '" + getHesap_numarasi() + "'";
        return stringVeriGetir(connection, sql, "isim_soyisim");
    }

    public void setIsim_soyisim(String isim_soyisim) {
        this.isim_soyisim = isim_soyisim;
    }

    public String getIban() {
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        iban = stringVeriGetir(connection, sql, "iban");
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getKullanilabilir_bakiye() {
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        kullanilabilir_bakiye = doubleVeriGetir(connection, sql, "kullanilabilir_bakiye");
        return kullanilabilir_bakiye;
    }

    public void setKullanilabilir_bakiye() {
        this.kullanilabilir_bakiye = kullanilabilir_bakiye;
    }

    public Double getBorc() {
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        borc = doubleVeriGetir(connection, sql, "borc");
        return borc;
    }

    public void setBorc() {
        this.borc = borc;
    }

    public String getSignValue() {
        if (getHesap_numarasi() == null) {
            return "Giriş Yap";
        }
        return "Çıkış Yap";
    }

    public String getSifre() {
        String sql = "select * from login where hesap_no = '" + hesap_numarasi + "'";
        return stringVeriGetir(connection, sql, "sifre");
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getHesap_numarasi() {
        if (hesap_numarasi == null) {
            return stringVeriGetir(connection, "select * from currentuser", "hesap_no");
        }
        return hesap_numarasi;
    }

    public void setHesap_numarasi(String hesap_numarasi) {
        if (stringVeriGetir(connection, "select * from currentuser", "hesap_no") == null) {
            veritabaninaEkle(connection, "insert into currentuser (hesap_no) values (?)", hesap_numarasi);
        } else {
            veritabaninaEkle(connection, "update currentuser set hesap_no = ?", hesap_numarasi);
        }
        this.hesap_numarasi = hesap_numarasi;
    }

    public Double getMevcut_para() {
        String sql = "select * from kisiselbilgiler where hesap_numarasi = '" + getHesap_numarasi() + "'";
        mevcut_para = doubleVeriGetir(connection, sql, "bakiye");
        String deneme = String.format("%.2f", mevcut_para).replaceAll(",", ".");
        mevcut_para = Double.parseDouble(deneme);
        return mevcut_para;
    }

    public void setMevcut_para(Double mevcut_para) {
        this.mevcut_para = mevcut_para;
    }

    public Double getMax_kredi() {
        return max_kredi;
    }

    public void setMax_kredi(Double max_kredi) {
        this.max_kredi = max_kredi;
    }

    public String getGirilenIban() {
        return girilenIban;
    }

    public void setGirilenIban(String girilenIban) {
        this.girilenIban = girilenIban;
    }

    public double getGirilenPara() {
        return girilenPara;
    }

    public void setGirilenPara(double girilenPara) {
        this.girilenPara = girilenPara;
    }

    public int getVade() {
        vade = intVeriGetir(connection, "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'", "vade");
        return vade;
    }

    public void setVade(int vade) {
        this.vade = vade;
    }

    public double getToplam_odenecek() {
        toplam_odenecek = doubleVeriGetir(connection, "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'", "toplam_odenecek");
        return Double.parseDouble(String.valueOf(toplam_odenecek).format("%.2f", toplam_odenecek).replaceAll(",", "."));
    }

    public void setToplam_odenecek(double toplam_odenecek) {
        this.toplam_odenecek = toplam_odenecek;
    }

    public double getTaksit_tutari() {
        if (intVeriGetir(connection, "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'", "vade") <= 0) {
            veritabaninaEkle(connection, "DELETE FROM kredibasvurusu where hesap_numarasi = (?)", getHesap_numarasi());
        } else {
            taksit_tutari = doubleVeriGetir(connection, "select * from kredibasvurusu where hesap_numarasi = '" + getHesap_numarasi() + "'", "taksit_tutari");
        }
        return Double.parseDouble(String.valueOf(taksit_tutari).format("%.2f", taksit_tutari).replaceAll(",", "."));
    }

    public void setTaksit_tutari(double taksit_tutari) {
        this.taksit_tutari = taksit_tutari;
    }

    public int getRadio1() {
        return radio1;
    }

    public void setRadio1(int radio1) {
        this.radio1 = radio1;
    }

    public int getRadio2() {
        return radio2;
    }

    public void setRadio2(int radio2) {
        this.radio2 = radio2;
    }

    public double getalis1() {
        return alis1;
    }

    public double getalis2() {
        return alis2;
    }

    public double getsatis1() {
        return satis1;
    }

    public double getsatis2() {
        return satis2;
    }

    public double getGirilen1() {
        return girilen1;
    }

    public void setGirilen1(double girilen1) {
        this.girilen1 = girilen1;
    }

    public double getGirilen2() {
        return girilen2;
    }

    public void setGirilen2(double girilen2) {
        this.girilen2 = girilen2;
    }

    public double getTl() {
        tl = doubleVeriGetir(connection, "select * from donusumler where hesap_numarasi = '" + getHesap_numarasi() + "'", "turk_lirasi");
        return tl;
    }

    public double getDolar() {
        dolar = doubleVeriGetir(connection, "select * from donusumler where hesap_numarasi = '" + getHesap_numarasi() + "'", "dolar");
        return dolar;
    }

    public double getEuro() {
        euro = doubleVeriGetir(connection, "select * from donusumler where hesap_numarasi = '" + getHesap_numarasi() + "'", "euro");
        return euro;
    }

    public double getSterlin() {
        sterlin = doubleVeriGetir(connection, "select * from donusumler where hesap_numarasi = '" + getHesap_numarasi() + "'", "sterlin");
        return sterlin;
    }

    //------------------------- BİDDİ
}
