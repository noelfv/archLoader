package com.novatronic.valorados.services.util;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

/**
 * Varios ejemplos sobre la clase @see org.apache.commons.io.FileUtils
 * @author Carlos García. Autentia
 */
public class FileUtilsExamples {
    /**
     * Punto de inicio del ejemplo
     */
    public static void main(String[] arg){

        InputStream   input1  = null;
        InputStream   input2  = null;
        OutputStream output1 = null;
        Reader reader1 = null;
        final File	 testDir   = new File(System.getProperty("java.io.tmpdir"));
        final File	 testDir2  = new File("E:\\temp\\hist");
       // final File	 testDir2  = new File(System.getProperty("java.home"));
        final File	 testFile1 = new File("E:\\temp\\hist\\a.txt");
        final File	 testFile2 = new File("E:\\temp\\hist\\b.txt");


        try {
            // Averiguamos el tamaño total de un directorio.
            System.out.println("Nº bytes del directorio: " + FileUtils.sizeOfDirectory(testDir2));

            // Imprime un tamaño en un formato más legible para las personas.
            System.out.println( FileUtils.byteCountToDisplaySize(FileUtils.sizeOfDirectory(testDir2))); // Por ejemplo: 80 MB

            //  Elimina el contenido de un directorio sin eliminar el directorio.
            //FileUtils.cleanDirectory(testDir2);

            // Elimina un fichero cuando finalize la máquina virtual que ejecuta la aplicación.
            // En caso de especificar un directorio, borra todos los ficheros de todos
            // los subdirectorios, pero sin eliminar los subdirectorios
            // FileUtils.forceDeleteOnExit(testFile);

            // Compara la fecha de creación de dos ficheros
            if (FileUtils.isFileNewer(testFile1, testFile2)){
                System.out.println(testFile1.toString() + " es más reciente");
            } else {
                System.out.println(testFile2.toString() + " es más reciente");
            }

            // Leemos el contenido de un fichero de texto con codificación iso-8859-1
            String content = FileUtils.readFileToString(testFile1, "iso-8859-1");
            System.out.println(content);

            // Obtenemos una colección (de objetos File) de todas las imágenes
            // un directorio y subdirectorios
            String[]   exts	  = new String[] {"jpg", "gif", "png"};
            Collection images = FileUtils.listFiles(testDir2, exts, true);
            System.out.println("Existen " + images.size() + " imágenes");

            // Implementa el comportamiento del comando 'touch' de Unix.
            // a) Si el fichero no existe lo crea con tamaño 0.
            // b) Si el fichero existe actualiza su fecha de modificación a la hora actual
            //  sin modificar su contenido.
            FileUtils.touch(testFile2);


            // Escribe una cadena con codificación UTF-8 en un fichero.
            // Si el fichero no existiese lo crea.
            FileUtils.writeStringToFile(testFile2, "Carlos García Pérez", "utf-8");
        } catch (Exception ex){
            System.out.println(ex);
        } finally {
            // Cerramos los Stream de prueba.
            // Nota, podemos olvidándonos de tener que capturar CUALQUIER excepción. ;-)
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(input2);
            IOUtils.closeQuietly(output1);
            IOUtils.closeQuietly(reader1);
        }
    }
}