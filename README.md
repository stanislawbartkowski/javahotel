http://www.mf.gov.pl/kontrola-skarbowa/dzialalnosc/jednolity-plik-kontrolny/-/asset_publisher/2NoO/content/struktury-jpk

Jednolity Plik Kontrolny - Standard Audit File

It is a new requirement of Polish Ministry of Finance to improve and extend areas in tax reporting.

The taxpayer should send tax reports to the gateway as XML file using a specified procedure. Unfortunately, although the procedure comprises well-known encrypting methods, Ministry of Finance does not provide any sample code how to accomplish the task.

This JPK Open Source project is a ready to use implementation of the problem in Java. It contains:
* Initialization, InitUploadSigned method. Important: qualified electronic signature (XADES-BES) should be covered by external program, it is not the part of the solution.
* PutBlob method, sending zipped and encoded data files to the server
* FinishUpload method.
* GetStatus method. Obtain UPO, Urzędowe Potwierdzenie Odbioru, Official Receipt Confirmation

----------
Build.

Prerequisities. 
* JDK 8.0
* Ant and Ivy plugin

* Download 
* ant
* dist/JPK.jar
* sample/sh : sample script files for every step: 
  * InitUploadSigned : runtransform.sh
  * PutBlob and FinishUpload : runupload.sh
  * GetStatus : rungetupo.sh
* sample/conf : sample configuration files



