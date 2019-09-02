package com.novatronic.valorados.services.util;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

/**
 * Varios ejemplos sobre las clases del paquete @see org.apache.commons.io.filefilters
 * @author Carlos García. Autentia
 */
public class FileFiltersExamples {
    /**
     * Punto de inicio del ejemplo
     */
    public static void main(String[] args){
        File	 testDir	= new File(System.getProperty("java.io.tmpdir"));
        String[] files		= null;
        IOFileFilter filter	= null;
        String[] cads		= null;

        //  Mostramos los archivos del directorio que comienzan por 'a' o 'A'
        cads   = new String[] {"a", "A"};
        filter = new PrefixFileFilter(cads);
        files  = testDir.list(filter);
        for (int i = 0, nFiles = files.length; i < nFiles; i++ ) {
            System.out.println(files[i]);
        }

        // Combinamos dos filtros para mostramos las imágenes de más de 10Kb.
        cads	= new String[] {".jpg", ".png", ".gif", ".bmp"};
        filter = new AndFileFilter(new SizeFileFilter(1024 * 10), new SuffixFileFilter(cads));
        files  = testDir.list(filter);
        for (int i = 0, nFiles = files.length; i < nFiles; i++ ) {
            System.out.println(files[i]);
        }
    }
}