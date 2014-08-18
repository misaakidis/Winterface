Winterface seperates logic and template of Freenet's web interface, enforces a better modularization and makes theming easier.

For a more comprehensive description see the [Wiki page](https://wiki.freenetproject.org/User:Pausb/Winterface) at [FreenetProject.org](https://freenetproject.org)

Get the latest released version of Winterface, along with instructions on how to load it from the [Winterface Updates](http://127.0.0.1:8888/USK@it0CEEZMIjspaDLopVr7QRPEat7GzbXJMX-OpiVDEhM,OOL0jKyXVaH400BPebAPu4dsDDDgogpAkWUnyqJgu88,AQACAAE/winterface-updates/4/) freesite.

# Rationale
Current Freenet's web interface FProxy uses <tt>HTMLNodes</tt> in combination with <tt>ToadletServer</tt> to deliver HTML-Pages. This has the disadvantage of mixed template and logic which makes it hard to separately make changes to each of them. Moreover debugging and understanding of code can be very exhausting.

# Overview
Winterface is delivered as a Fred plugin:

* It uses Apache Velocity as its component-based web-framework to generate HTML files from templates
* Jetty (embedded) as a serlvet-container is used to deliver Velocity generated servlets.
* It should completely replace replace FProxy,ToadletServer and associated Toadlets
* It ''should'' make it possible to override Templates (HTML files) and design (CSS+JS)

What is to do:

* Create HTML templates and corresponding servlet logic for each existing Toadlet
* Make reusable components (e.g. Panels) for reusable templates (e.g. Alerts)
* Eventually add new functionalities

# Prerequisites

Requires `freenet` and `freenet-ext`. As these are not currently available in Maven repositories,
after [downloading](https://downloads.freenetproject.org/alpha/freenet-build01465.jar)
[them](https://downloads.freenetproject.org/alpha/freenet-ext.jar) or building them from source, install them:

    mvn install:install-file -Dfile=freenet.jar -DgroupId=org.freenetproject -DartifactId=fred -Dversion=0.7.5.1465 -Dpackaging=jar
    mvn install:install-file -Dfile=freenet-ext.jar -DgroupId=org.freenetproject -DartifactId=freenet-ext -Dversion=29 -Dpackaging=jar

# Building

This will download the remaining dependencies from the Internet.

    mvn package

# Using

Enter the path to the file with a name ending in `jar-with-dependencies.jar` under the `target` directory into the "Add an Unofficial Plugin" field on the Configuration > Plugins page and click "Load."

# Licences
Icons used for the interface are created by [Mark James](http://www.famfamfam.com/lab/icons/silk/) and released under [Creative Commons Attribution 3.0 License](http://creativecommons.org/licenses/by/3.0/) 

