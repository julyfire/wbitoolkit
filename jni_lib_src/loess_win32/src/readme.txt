loess depends on GNU Scientific Library (gsl) for the gamma function
install gsl on windows:
http://blog.csdn.net/augusdi/article/details/8803099
从http://gnuwin32.sourceforge.net/packages/gsl.htm下载Complete package, except sources和Sources两个exe文件。
首先安装从http://gnuwin32.sourceforge.net/packages/gsl.htm下载的两个文件gsl-1.8.exe和gsl-1.8-src.exe
进入gsl库的lib目录下依次输入以下两条语句：（每输入一行回车）
lib /machine:i386 /def:libgsl.def
lib /machine:i386 /def:libgslcblas.def
再看lib目录下，发现有了libgsl.lib,libgslcblas.lib这两个文件。
2、将C:\Program Files\GnuWin32l\bin中的libgsl.dll和libgslcblas.dll复制到D:\Program Files\Microsoft Visual Studio 10.0\VC\Bin；将\include整个Gsl目录复制到D:\Program Files\Microsoft Visual Studio 10.0\VC\include下；\lib目录下的所有.lib文件全部复制到D:\Program Files\Microsoft Visual Studio 10.0\VC\Lib下。

http://www.netllib.org
f2c.exe translate fortran code to c code

build:

cl -I"C:\Program Files\Java\jdk1.7.0_21\include" -I"C:\Program Files\Java\jdk1.7.0_21\include\win32" -LD *.c -FeLoessSmooth.dll /link libgsl.lib

java jni need to load three library:
libgslcblas.dll
libgsl.dll
LoessSmooth.dll


2013/04/23