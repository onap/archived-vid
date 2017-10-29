.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0

Delivery
========

VID is delivered as a docker image, and uses MariaDb which also delivered as a docker image. The two images are linked.

.. blockdiag::
   

   blockdiag layers {
   orientation = portrait
   VID -> MariaDb;
   MariaDb -> VID;
   group l1 {
	color = blue;
	VID; MariaDb;
	}
   }



