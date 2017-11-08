.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Delivery
========

- |  **MariaDB Image**
  |  Create a container using the Docker MariaDB image.
  
- |  **VID Image**
  |  Create a Docker image which extends the Tomcat Docker image, and linked to the MariaDB container created earlier. 
  |  Configuration of the Docker container will be customized by providing environment variables to the "docker run" command.


.. blockdiag::

   blockdiag layers {
    node_width = 200;
    default_fontsize = 24;
    node_height = 100;
    orientation = portrait
    VID -> MariaDb [dir = both];

    group l1 {
 	 VID; MariaDb [shape = flowchart.database];
 	}
   }


Recommended Rackspace VM Flavor
--------------------------------
+------------+------------------------+--------+------+-----------+-------+-------------+
| ID         | Flavor name            | Memory | Disk | Ephemeral | VCPUs | RTTX factor |
+============+========================+========+======+===========+=======+=============+
| general1-2 | 2GB General Purpose v1 | 2048   | 40   | 0         | 2     | 400.0       |
+------------+------------------------+--------+------+-----------+-------+-------------+

