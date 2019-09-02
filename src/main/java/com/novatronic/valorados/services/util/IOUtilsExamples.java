package com.novatronic.valorados.services.util;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

/**
 * Varios ejemplos sobre la clase @see org.apache.commons.io.IOUtils
 * @author Carlos García. Autentia
 */
public class IOUtilsExamples {

    /**
     * Ficheros con los que realizamos las pruebas.
     */
    private static String TestFile1 = "E:\\temp\\hist\\a.txt";
    private static String TestFile2 = "E:\\temp\\hist\\b.txt";

    /**
     * Punto de inicio del ejemplo
     */
    public static void main(String[] args){
        InputStream    input1   = null;
        InputStream    input2   = null;
        OutputStream output1 = null;
        Reader reader1 = null;

        try {

            // Copiamos el contenido de un InputStream en un OutputStream
            // (Todos los métodos están sobrecargados para usar Readers y Writers)
            input1  = new FileInputStream(TestFile1);
            output1 = new FileOutputStream(TestFile2);
            IOUtils.copy(input1, output1);

            // Cerramos los Stream de la prueba
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(output1);



            // Comparamos dos InputStream
            input1  = new FileInputStream(TestFile1);
            input2  = new FileInputStream(TestFile2);

            if (IOUtils.contentEquals(input1, input2)){
                System.out.println("Los ficheros son iguales");
            } else {
                System.out.println("Los ficheros son distintos");
            }

            // Cerramos los ficheros de la prueba. No lanza excepciones aunque sean null.
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(input2);


            // Convertimos una cadena a un byte[] con codificación de caracteres UTF-8
            String	name  = "Cañón";
            byte[]	bytes = IOUtils.toByteArray(new StringReader(name), "UTF-8");
            System.out.println(name.getBytes().length);  // => 5
            System.out.println(bytes.length);	 // => 7

            // Leemos un stream de texto a través de un Iterator
            reader1 = new FileReader(TestFile1);
            LineIterator fileIte = IOUtils.lineIterator(reader1);
            try {
                while (fileIte.hasNext()) {
                    String line = fileIte.nextLine();

                    // Realizamos el tratamiento de la línea
                    System.out.println(line);
                }
            } finally {
                fileIte.close();
                IOUtils.closeQuietly(reader1);
            }

            // Averiguamos el número de líneas de un Stream de texto.
            reader1 = new FileReader(TestFile1);
            BufferedReader x=new BufferedReader(reader1);
            java.util.List list1 = IOUtils.readLines(x);
            IOUtils.closeQuietly(reader1);
            System.out.println("El fichero tiene " + list1.size() + " lineas.");


            // Leemos un recurso a través de su URL. En este caso una página web
            try {
                input1 = new java.net.URL("http://www.google.es").openStream();
                System.out.println(IOUtils.toString(input1));
            } finally {
                IOUtils.closeQuietly(input1);
            }

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
    } // FIN main
} // FIN class