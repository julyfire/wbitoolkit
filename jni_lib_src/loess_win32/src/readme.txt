loess depends on GNU Scientific Library (gsl) for the gamma function
install gsl on windows:
http://blog.csdn.net/augusdi/article/details/8803099
��http://gnuwin32.sourceforge.net/packages/gsl.htm����Complete package, except sources��Sources����exe�ļ���
���Ȱ�װ��http://gnuwin32.sourceforge.net/packages/gsl.htm���ص������ļ�gsl-1.8.exe��gsl-1.8-src.exe
����gsl���libĿ¼��������������������䣺��ÿ����һ�лس���
lib /machine:i386 /def:libgsl.def
lib /machine:i386 /def:libgslcblas.def
�ٿ�libĿ¼�£���������libgsl.lib,libgslcblas.lib�������ļ���
2����C:\Program Files\GnuWin32l\bin�е�libgsl.dll��libgslcblas.dll���Ƶ�D:\Program Files\Microsoft Visual Studio 10.0\VC\Bin����\include����GslĿ¼���Ƶ�D:\Program Files\Microsoft Visual Studio 10.0\VC\include�£�\libĿ¼�µ�����.lib�ļ�ȫ�����Ƶ�D:\Program Files\Microsoft Visual Studio 10.0\VC\Lib�¡�

http://www.netllib.org
f2c.exe translate fortran code to c code

build:

cl -I"C:\Program Files\Java\jdk1.7.0_21\include" -I"C:\Program Files\Java\jdk1.7.0_21\include\win32" -LD *.c -FeLoessSmooth.dll /link libgsl.lib

java jni need to load three library:
libgslcblas.dll
libgsl.dll
LoessSmooth.dll


2013/04/23