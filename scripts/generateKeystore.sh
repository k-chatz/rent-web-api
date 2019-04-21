#!/usr/bin/env bash
cd ../
keytool -genkeypair -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -storepass rent_project -keystore ./src/main/resources/keystore.p12 -validity 9999 -dname "EMAILADDRESS=rent@di.uoa.gr, CN=Rent, O=UOA, OU=TL, L=Athens, S=Greece, C=GR" -ext san=dns:localhost
keytool -export -keystore keystore.p12 -alias tomcat -file rentCertificate.crt -storepass rent_project
keytool -importcert -file rentCertificate.crt -alias tomcat -storepass rent_project -keystore %JRE_HOME%\lib\security\cacerts
