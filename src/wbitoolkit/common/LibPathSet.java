/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.common;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 *
 * @author wb
 */
public class LibPathSet {
    public static String getWorkingDir(){
        return System.getProperty("user.dir");
    }
    public static String getPackageDir(){
        String dir=LibPathSet.class.getResource("").getPath();
        //System.out.println("dir"+dir);
        // file:/home/wb/NetBeansProjects/GRecScan/dist/GRecScan.jar!/grecscan/
        int pos=dir.indexOf("file:");
        if(pos>-1) dir=dir.substring(pos+5);
        pos=dir.indexOf("!");
        if(pos>-1) dir=dir.substring(0, dir.lastIndexOf("/",pos));
        //return PathSet.class.getPackage().getName();
        return dir;
    }
    /**
     * add program library to java.library.path,
     * construct a temp directory to store the custom java class path
     * @param String s: the directory of the library path to be added
     * @throws IOException
     */
    public static void setLibDir(String s) throws IOException {
	try {
		Field field = ClassLoader.class.getDeclaredField("usr_paths");
		field.setAccessible(true);
		String[] paths = (String[])field.get(null);
		for (int i = 0; i < paths.length; i++) {
			if (s.equals(paths[i])) {
				return;
			}
		}
		String[] tmp = new String[paths.length+1];
		System.arraycopy(paths,0,tmp,0,paths.length);
		tmp[paths.length] = s;
		field.set(null,tmp);



	} catch (IllegalAccessException e) {
		throw new IOException("Failed to get permissions to set library path");
	} catch (NoSuchFieldException e) {
		throw new IOException("Failed to get field handle to set library path");
	}

        /*** cannot use the following way for the java.library.path is readonly, it is final (readonly) ***/

        //System.getProperty("java.library.path");
//        System.setProperty("java.libaray.path", s);
//        System.out.println(System.getProperty("java.library.path"));

   }
}
