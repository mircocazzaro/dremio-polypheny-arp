# Dremio ARP module connector for Polypheny

This repository contains the ARP module source code and compiled JAR to connect a Polypheny istance as a Dremio source.

## Compiling Instructions

### Prerequisites

This proof-of-concept project has been built using the following software components:

- #### Runtime Requirements
    - Docker version 1.13.1, build 7d71120/1.13.1
    - Dremio 24.3.2-202401241821100032-d2d8a497
    - Polypheny v0.9.1

More accurate analysis still need to be performed in order to guarantee the whole process to be reproducible with other software versions.

#

### Basic Usage & Installation Procedures

1. ### Docker & Dremio Setup Steps
    - Install the aforementioned docker version;
    - Pull Dremio repository: ```docker pull dremio/dremio-oss```;
    - Run a Dremio container ```docker run -p 9047:9047 -p 31010:31010 -p 32010:32010 -p 45678:45678 dremio/dremio-oss```;
    - Access to the Dremio Web Application available at ```http://localhost:9047/``` and follow the steps in order to create an user.

2. ### Polypheny Setup Steps
    - Download and install Polypheny from ```https://github.com/polypheny/Polypheny-DB/releases/tag/v0.9.1``` (choose the corresponding installer w.r.t. your OS).

3. ### ARP Module Injection Steps
    - Clone the repository: ```git clone https://github.com/mircocazzaro/dremio-polypheny-arp.git```;
    - Copy the plugin into the Docker container: ```docker cp /path/to/dremio-polypheny-arp/target/dremio-polypheny-plugin-24.3.2-202401241821100032-d2d8a497.jar <CONTAINER_NAME>:/opt/dremio/jars/```;
    - Copy the Polypheny JDBC driver into the Docker container: ```docker cp /path/to/dremio-polypheny-arp/jdbc/polypheny-jdbc-driver-1.5.3.jar <CONTAINER_NAME>:/opt/dremio/jars/3rdparty/```;
    - Restart the Docker container.

    Note: if you don't know your container name, run in a separate shell ```docker ps``` in order to get it.

4. ### Usage Steps
    - After restarting Dremio's Docker container, log in with the previously setted credentials, click on "Add source" and select Polypheny;
    - Insert ```<HOST:PORT>``` where your Polypheny istance is running (N.B. If Polypheny is running in the same host of Docker, don't use ```localhost``` neither ```127.0.0.1``` as host, but rather ```host.docker.internal```);
    - Use ```pa``` as user, leave blank the password field.

    Note: By default Polypheny DB port is ```20591```, but it may change depending on your local configuration.

#

5. ### Plugin Compilation Steps
    #### Build / Compile Requirements
    - Apache Maven 3.0.5 (Red Hat 3.0.5-17)
    - Java version: 17.0.2, vendor: Oracle Corporation
    
    If you want to customize the plugin behaviour, you will have to rebuild it with maven through: ```mvn clean install```; you can then follow back the instructions starting from point 3.

#

#### Done! Your Polypheny istance now will act as a Dremio source!



## Contact

This project is currently mantained by Mirco Cazzaro as part of his Master Thesis project in Computer Engineering at the University of Padua ([mirco.cazzaro@studenti.unipd.it](mailto:mirco.cazzaro@studenti.unipd.it)).
