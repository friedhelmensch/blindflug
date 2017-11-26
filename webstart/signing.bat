jarsigner -storetype pkcs12 -keystore yourPathHere/dist/Blindflug.jar yourAliasHere

jarsigner -storetype pkcs12 -keystore yourPathHere/resources/htmlcleaner-2.7.jar yourAliasHere

jarsigner -storetype pkcs12 -keystore yourPathHere/jcalendar-1.4.jar yourAliasHere

jarsigner -storetype pkcs12 -keystore yourPathHere/jgoodies-common-1.6.0.jar yourAliasHere

jarsigner -storetype pkcs12 -keystore yourPathHere/jgoodies-looks-2.5.3.jar yourAliasHere

jarsigner -storetype pkcs12 -keystore yourPathHere/joda-time-2.1.jar yourAliasHere

jarsigner -storetype pkcs12 -keystore yourPathHere/selenium-server-standalone-2.45.0.jar yourAliasHere

jarsigner -verify -verbose -certs yourPathHere/resources/selenium-server-standalone-2.45.0.jar

keytool -list -v -storetype pkcs12 -keystore yourPathHere/YourCertificateHere.p12

