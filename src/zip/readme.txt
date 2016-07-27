=======================================================
Barbecue - @PROJECT.VERSION@
=======================================================

--------------------------
i. Contents of this readme
--------------------------

1. License
2. What is Barbecue?
3. Features
4. API
5. Limitations
6. Known Bugs
7. Future Work
8. Using Barbecue
9. Contributing


----------
1. License
----------

Copyright (c) 2003-2005, International Barcode Consortium
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    * Neither the name of Ian Bourke nor the names of any contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


--------------------
2. What is Barbecue?
--------------------

Barbecue is an open-source, Java, barcode drawing library. Best of all - it's completely
FREE! You can use, and extend, Barbecue for whatever reasons you like, and distribute it
in your application, at no cost. Have fun with it.


-----------
3. Features
-----------

Barbecue has been written in an open ended way in order to enable the easy addition of new
barcode formats without having to changes the underlying API for the user. Currently, Barbecue
supports the following barcode formats:

- Code 128
- EAN/UCC 128
- Codabar
- Code 39
- 2 of 5
- UPCA
- PostNet
- PDF417 (experimental implementation)


--------------
4. API
--------------

Barbecue 1.5 has API changes that are incompatible with the Barbecue 1.0 API. 
If you plan to use Barbecue 1.5, you'll need recompile your application source code.


--------------
5. Limitations
--------------

Barbecue only supports the barcode formats listed in the Features section.
Also, Barbecue will only work with J2SE 1.4 and above.


--------------
6. Known Bugs
--------------

- If the text label to be shown underneath the barcode is wider than the barcode itself,
  the text does not wrap to the next line (problem particularly with PDF barcodes)


------------------
7. Using Barbecue
------------------

Barbecue can be used both as a Swing/AWT component, and as a standalone "drawable" object that
can paint itself straight onto a Graphics(2D) - this is useful for printing barcodes in Java.
Take a look at the example Java code in the examples directory for more information on how to
use Barbecue in code. The API documentation is also included in this release and is available
at http://barbecue.sourceforge.net

Barbecue also includes a barcode generating servlet (BarcodeServlet). It requires a "data"
parameter that specifies the data to encode. Please see the Javadocs and the examples folder
for more information on optional parameters and using the servlet.


--------------
8. Future Work
--------------

There are lots of things that can be done to improve Barbecue:

- More supported barcode formats
- Better documentation
- Optimized drawing


---------------
9. Contributing
---------------

If Barbecue doesn't do what you need, then change it! Go to barbecue.sourceforge.net for
more information.
