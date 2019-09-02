package com.novatronic.valorados.services.util;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

/**
 * Varios ejemplos sobre la clase @see org.apache.commons.io.FilenameUtils
 * @author Carlos García. Autentia
 */
public class FilenameUtilsExamples {
    /**
     * Punto de inicio del ejemplo
     */
    public static void main(String[] args){
        System.out.println(FilenameUtils.concat("c:/windows", "c:/pepe/a")); // c:\pepe\a
        System.out.println(FilenameUtils.concat("c:/windows", "pepe/a"));    // c:\windows\pepe\a
        System.out.println(FilenameUtils.normalize("c:/windows/.//"));       //  c:\windows\
        System.out.println(FilenameUtils.getBaseName("c:/windows/a.bmp"));   // a
        System.out.println(FilenameUtils.getExtension("c:/windows/a.bmp"));  // bmp
        System.out.println(FilenameUtils.getFullPath("c:/windows/a.bmp"));   // c:/windows/
        System.out.println(FilenameUtils.getPath("c:/windows/a.bmp"));       // windows/
        System.out.println(FilenameUtils.separatorsToSystem("c:/windows/a.bmp"));// c:\windows\a.bmp
        System.out.println(FilenameUtils.wildcardMatch("a.bmp", "*.bmp"));   // true
        System.out.println(FilenameUtils.wildcardMatch("a.bmp", "*.jpg"));   // false
        System.out.println(FilenameUtils.wildcardMatch("a/b/a.bmp", "a/b/*")); // true
        System.out.println(FilenameUtils.wildcardMatch("a.bmp", "*.???"));   // true
        System.out.println(FilenameUtils.wildcardMatch("a.bmp", "*.????"));  // false
    }
}