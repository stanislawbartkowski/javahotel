Open Source project, J2EE application, hotel management software


Demo version:

Administrator: http://testjavahotel.appspot.com/?start=admin.xml  (U/P admin/admin)

User : http://testjavahotel.appspot.com/?hotel=hotel (U/P user/user)

Blog: http://hoteljavaopensource.blogspot.com/

---
# 2015/07/14 : new verion deployed #

Nothing specific to JavaHotel, some goodies related the latest version of MVP Jython framework
---

# 2015/02/01: new version deployed #
More info : http://hoteljavaopensource.blogspot.com/2015/02/new-version-of-javahotel-taxation.html

Taxation added

---

# 2014/11/16 : new version deployed #
More info: http://hoteljavaopensource.blogspot.com/2014/11/new-version-of-javahotel-application.html

Advance deposit for guaranteed booking, status visualization and more.

---

# 2014/09/24 : new version deployed #
More info : http://hoteljavaopensource.blogspot.com/2014/09/new-version-of-javahotel-mailing.html

New feature is mailing, sending a confirmation note after booking.

---

# 2014/07/20 : new version deployed #
More info: http://hoteljavaopensource.blogspot.com/2014/07/new-version-of-javahotel-application.html
# 2014/06/29 : new version deployed #
More info: http://hoteljavaopensource.blogspot.com/2014/06/javahotel-new-version.html

---

Polish 	<img src='http://darmowegrafiki.5m.pl/flagi/gify_ikony/pl.gif' alt='pl.gif'> : Applikacja webowa dla zarządzania hotelem zrealizowana w modelu Open Source. Jest to przedsięwzięcie niekomercyjne, ma także nie zawierać żadnych ukrytych kosztów (np, kosztów licencji). Oczywiście taki model nie wyklucza zastosowania komercyjnego.<br>
<hr />
<h2>Aktualna wersja demonstracyjna</h2>

Administrator <a href='http://testjavahotel.appspot.com/?start=admin.xml&locale=pl'>http://testjavahotel.appspot.com/?start=admin.xml&amp;locale=pl</a> (Użytkownika/Hasło admin/admin)<br>
<br>
Użytkownik: <a href='http://testjavahotel.appspot.com/?hotel=hotel&locale=pl'>http://testjavahotel.appspot.com/?hotel=hotel&amp;locale=pl</a> (Użytkownik/hasło user/user)<br>
<br>
<h2>Dodatkowe informacje</h2>

Wersja demo działa w technologii Google App Engine aby skorzystać z bezpłatnego hostingu jaki oferuje Google. Nie jest to jednak docelowy model ze względu na liczne ograniczenia związane z tą platformą.<br>
Docelowo aplikacja ma działać w sieci lokalnej lub na pojednyczym komputerze.<br>
<br>
<h2>Technologia</h2>

Jest to webowa aplikacja zrobiona w technologii AJAX. Strona kliencka jest wykonana przy użyciu GWT (Google Web Toolkit). Strona serwerowa (warstwa biznesowa) działa w oparciu o Java EE 5, udostępniona jako "stateless beany" EJB3 dostępne zdalnie. Dostęp może być także lokalny. Warstwa "persystencji" (baza danych) została zrealizowana za pomocą JPA (Java Persistence API).<br>
Do tej pory aplikacja była testowana na serweracj aplikacyjnych (oprócz Google App Engine) : Tomcat 7.0 Glassfish (4.2). Bazy danych testowane do tej pory to: Derby, Postgress oraz DB2.<br>
Aplikacja może działać lokalnie (na jednym komputerze) oraz w wersji rozproszonej.<br>
Instalacja minimalna to Tomcat 7.0, Derby, dostęp do wartwy biznesowej lokalny. Wersja maksymalna, rozproszona, to warstwa kliencka, osobno warstwa bizensowa (np. Glassfish) dostępna zdalnie oraz odrębna instalacja bazy danych (np. komercyjna DB2).
