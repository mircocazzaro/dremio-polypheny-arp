# Dremio ARP module connector for Polypheny

This project aims to deliver an ARP module that can be integrated into Dremio in order to add a Polypheny istance as a source.

## Compiling Instructions

### Prerequisites

This proof-of-concept project has been built using the following software components:

- #### Build / Compile Requirements
    - Apache Maven 3.0.5 (Red Hat 3.0.5-17)
    - Java version: 17.0.2, vendor: Oracle Corporation

- #### Runtime Requirements
    - Docker version 1.13.1, build 7d71120/1.13.1
    - Dremio 24.3.2-202401241821100032-d2d8a497
    - Polypheny v0.9.1

More accurate analysis still need to be performed in order to guarantee the whole process to be reproducible with other software versions.

#

### Docker & Dremio Setup Steps
1. Install the aforementioned docker version;
2. Pull Dremio repository: ```docker pull dremio/dremio-oss```;
3. Run a Dremio container ```docker run -p 9047:9047 -p 31010:31010 -p 32010:32010 -p 45678:45678 dremio/dremio-oss```;
4. Access to the Dremio Web Application available at ```http://localhost:9047/``` and follow the steps in order to create an user.

### Polypheny Setup Steps
1. Download and install Polypheny from ```https://github.com/polypheny/Polypheny-DB/releases/tag/v0.9.1``` (choose the corresponding installer w.r.t. your OS).

### Plugin Compilation Steps
1. Clone the repository: ```git clone https://github.com/mircocazzaro/dremio-polypheny-arp.git```
2. Compile and pack the plugin: ```mvn clean install```

### ARP Module Injection Steps
1. After you compiled the plugin, it will be available at ```/path/to/dremio-polypheny-arp/target/dremio-polypheny-plugin-24.3.2-202401241821100032-d2d8a497.jar```;
2. Copy the plugin into the Docker container: ```docker cp /path/to/dremio-polypheny-arp/target/dremio-polypheny-plugin-24.3.2-202401241821100032-d2d8a497.jar <CONTAINER_NAME>:/opt/dremio/jars/```;
3. Copy the Polypheny JDBC driver into the Docker container: ```docker cp /path/to/dremio-polypheny-arp/jdbc/polypheny-jdbc-driver-1.5.3.jar```;
4. Restart the Docker container.

### Usage Steps
1. After restarting Dremio's Docker container, log in with the previously setted credentials, click on "Add source" and select Polypheny;
2. Insert ```<HOST:PORT>``` where your Polypheny istance is running (N.B. If Polypheny is running in the same host of Docker, don't use ```localhost``` neither ```127.0.0.1``` as host, but rather ```host.docker.internal```!);
3. Use ```pa``` as user, leave blank the password field.

#

#### Done! Your Polypheny istance now will act as a Dremio source!



## Contact

This project is currently mantained by Mirco Cazzaro as part of his Master Thesis project in Computer Engineering at the University of Padua ([mirco.cazzaro@studenti.unipd.it](mailto:mirco.cazzaro@studenti.unipd.it)).
