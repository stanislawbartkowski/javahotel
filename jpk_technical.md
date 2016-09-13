# Configuration file

Sample configuration file is available [here](https://github.com/stanislawbartkowski/javahotel/blob/jpk/sample/conf/jpk.properties). Following properties should be defined.

- conf Directory containing a set of artifacts necessary to run the solution. Sample directory is available [here](https://github.com/stanislawbartkowski/javahotel/tree/jpk/sample/conf).
  * JPKMFTest-klucz publiczny do szyfrowania.pem :  Public key to encode the symmetric key
  * imitupload-enveloped-pattern.xml : Pattern for creating InitUpload XML file used to initiate the data transmition. The file contains a number of position markers to be replaced by current values. I found this solution more applicable then creating XML file on the fly.
  * log.conf : JUL logging configuration. FileHandler is added automatically, only ConsoleLogger should be defined here.
  * test-e-dokumenty.mf.gov.pl_ssl.crt : Certificate used to authorize access to the public gateway.
- workdir : Working directory to keep temporary data between different phases of data transmission. It is  also a place to keep a log file. This directory is cleaned at the beginning of the first phase,  so it is a responsibility of solution user to backup this directory.
- publickey : The name of the file with a public key in conf directory.
- cert : The name of the certificate file (X.509) in conf directory.
- url : The URL to send InitUpload.xml file, transfer initialization.
- finish: The URL to signal the transmission completion.
- get: The URL to receive UPO (Urzędowe Potwierdzenie Odbioru), Official Receipt Confirmation.
