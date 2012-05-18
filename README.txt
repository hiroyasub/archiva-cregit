Apache Archiva
==============

To get involved in Archiva development, contact dev@archiva.apache.org.

Running from Source Code
========================

With maven 3 and the tomcat-maven-plugin, you will be able to run the webapp from the top
and include all the other modules in the webapp classloader.
No more need to install everything to run the jetty plugin.
So just use : mvn tomcat6:run -Pdev -Ptomcat or mvn tomcat7:run -Pdev -Ptomcat
and hit in your browser : http://localhost:9091/archiva

note with dev profile admin account is automatically created with password admin123
see file : archiva-modules/archiva-web/archiva-webapp/src/test/tomcat/auto-admin-creation.properties

NOTE: you will need a MAVEN_OPTS with some memory setup as sample :
export MAVEN_OPTS="-Xmx768m -Xms768m -XX:MaxPermSize=256m"


Running webapp full js
========================
As webapp js is in dev and won't probably be released soon, the module is not activated by default and it's included only in a profile
mvn tomcat7:run -pl :archiva-webapp-js -am -Pdev
or
mvn tomcat6:run -pl :archiva-webapp-js -am -Pdev

hit your browser: http://localhost:9091/archiva/index.html

Test Registration email
========================
Redback can send email on registration by default the mail jndi si configured to use localhost.
You can use your gmail accout for testing purpose
In your ~/.m2/settings.xml add a property with a path to a tomcat context file:
<tomcatContextXml>/Users/olamy/dev/tomcat-context-archiva-gmail.xml</tomcatContextXml>

This file must contains:

<Context path="/archiva">
  <Resource name="jdbc/users" auth="Container" type="javax.sql.DataSource"
            username="sa"
            password=""
            driverClassName="org.apache.derby.jdbc.EmbeddedDriver"
            url="jdbc:derby:${catalina.base}/target/database/users;create=true"
  />
  <Resource name="mail/Session" auth="Container"
          type="javax.mail.Session"
          mail.smtp.host="smtp.gmail.com"
          mail.smtp.port="465"
          mail.smtp.auth="true"
          mail.smtp.user="your gmail account"
          password="your gmail password"
          mail.smtp.starttls.enable="true"
          mail.smtp.socketFactory.class="javax.net.ssl.SSLSocketFactory"/>

</Context>

jrebel
generate files:  mvn org.zeroturnaround:jrebel-maven-plugin:1.1.3:generate -Pjs

