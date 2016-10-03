#Projenin Amacı

Bu proje VOIP teknolojisini kullanarak Muğla Üniversitesi içerisindeki öğrencilerin birbiriyle ücretsiz konuşmalarını amaçlayan kurumsal bir projedir.

#Başlangıç

Projeyi forklayıp cloneladıktan sonra Android Studio içerisine import edebilirsiniz. Başlamadan önce lütfen https://github.com/DoubangoTelecom/imsdroid/ adresindeki dökümentasyonu inceleyiniz.

Test etmek için https://mdns.sipthor.net/register_sip_account.phtml adresinden bir profil oluşturunuz.

TODO: Mesajlaşma için XmPP server url'si gir.

#Proje İçeriği

Bu proje sip2sip.info adresinin sağladığı Asterisk Server üzerinden konuşmayı gerçekleştiriyor.

Profil bilgilerinizi bir Obje içerisinde toplayıp Register etmek için server'a gönderiyor. Server'dan success cevabıyla birlikte sipSession initialize olmuş oluyor. Artık başka bir Sip adresinden gelen aramaları karşılayabilir durumda olmuş olacaksınız.

#Yapılacaklar

> sip2sip.info ile bağımsız çalışabilmek için bir Server oluşturmak ve Muğla Üniversitelerindeki her öğrenci için bir sip adresi profili oluşturmak.

>Xmpp Server ile mesajlaşma bağımlılıklarından kurtulmak için bir Xmpp Server oluşturmak ve sip profilleri ile uyumlu profiller oluşturmak.

>Rehber oluşturmak

>GPRS ile konum bilgileri alınma.


# AndroVoip
This is the Simple Softphone aplication based on android-ngn-stack library.
> **Project Owners:**
> - Hamdi Burak Dilek ---> https://github.com/hamdiburakdilek
> - Erkan Çetinkaya ---> https://github.com/erkanderon
> - Elif Türkay ---> https://github.com/cengelif
