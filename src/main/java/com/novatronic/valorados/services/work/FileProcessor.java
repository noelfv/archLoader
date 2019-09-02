package com.novatronic.valorados.services.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.novatronic.valorados.services.db.NovaOracleConnectionInfo;
import com.novatronic.valorados.services.db.NovaOracleConnectionPool;
import com.novatronic.valorados.services.util.LoggerHelper;

public class FileProcessor implements Callable<Integer> {

    private final static Logger logger = Logger.getLogger(FileProcessor.class);
    private final String fileName;
    private final String pathTrabajo;
    private final String pathErrores;
    private final String pathHistorico;
    private final static String SQL_INSERT_FILE = "INSERT INTO TP_CARGA_VALORADO (ID, FILENAME, NOMBRE, TKN_INICIO, TKN_FIN, VAL_INFORMADO,"
            + " VAL_DUPLICADO, VAL_ERROR, VAL_REALES, VAL_FALTANTES) VALUES ( SEQ_TP_CARGA_VALORADO.NEXTVAL, "
            + " ?,?,?,?,?,?,?,?,?)";
    private final static String SQL_INSERT_TOKEN = "INSERT INTO TP_TOKEN ("
            + "COD_EMPRESA, BIN_TOKEN, ID,"
            + "FECH_BIRTH, FECH_DEAD, ESTADO,"
            + "USER_ADD)"
            + "VALUES (?,?,?,?,?,?,?)";
    private final static String SQL_SELECT_FILE = "SELECT TKN_INICIO, TKN_FIN AS CUENTA FROM TP_CARGA_VALORADO WHERE UPPER(FILENAME)  = UPPER(?)";
    private static final String SQL_DELETE_FILE = "DELETE FROM TP_CARGA_VALORADO WHERE UPPER(FILENAME)  = UPPER(?)";
    private static final String SQL_DELETE_TOKEN = "DELETE FROM TP_TOKEN WHERE UPPER(ID) BETWEEN UPPER(?) AND UPPER(?)";

    public FileProcessor(final String nombreArchivo, final String pathTrabajo, final String pathErrores,
            final String pathHistorico) {
        this.fileName = nombreArchivo;
        this.pathErrores = pathErrores;
        this.pathHistorico = pathHistorico;
        this.pathTrabajo = pathTrabajo;
    }

    public Integer call() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("procesando: " + fileName);
        }

        try {
            logger.info("paso a");
            Archivo archivo = new Archivo(resolverNombre(pathTrabajo, fileName));
            for (int i = 0; i <archivo.getListaTramas().size() ; i++) {

                System.out.println(archivo.getListaTramas().get(i));
            }
            System.out.println( FileUtils.byteCountToDisplaySize(archivo.getListaTramas().size()));
/*
            Connection connection = NovaOracleConnectionPool.getConnection();
            connection.setAutoCommit(false);


            boolean isBatch = connection.getMetaData().supportsBatchUpdates();
            //insertamos carga valorados
            PreparedStatement cargaValorados = connection.prepareStatement(SQL_INSERT_FILE);
            cargaValorados.setString(1, fileName);
            cargaValorados.setString(2, archivo.getName());
            cargaValorados.setString(3, archivo.getFirstToken());
            cargaValorados.setString(4, archivo.getLastToken());
            cargaValorados.setInt(5, archivo.getNumTokens());
            cargaValorados.setInt(6, archivo.getTokensRepetidos().size());
            cargaValorados.setInt(7, archivo.getTokensMalFormados().size());
            cargaValorados.setInt(8, archivo.getTokens().size());
            int faltantes = archivo.getNumTokens()
                    - (archivo.getTokensRepetidos().size() + archivo.getTokensMalFormados().size() + archivo.getTokens()
                    .size());
            cargaValorados.setInt(9, faltantes);
            cargaValorados.execute();
            cargaValorados.close();

logger.info("paso 2");
            //insertamos en tp_token y tp_token_bin
            PreparedStatement tp_token = connection.prepareStatement(SQL_INSERT_TOKEN);
            int contador = 1;
            for (Token token : archivo.getTokens().values()) {
                //"COD_EMPRESA
                tp_token.setString(1, "0001");
                //BIN_TOKEN, 
                tp_token.setString(2, "999999");
                //ID,"+ 
                tp_token.setString(3, token.getSn());
                //"FECH_BIRTH, 
                tp_token.setDate(4, new java.sql.Date(token.getBirth().getTime()));
                //FECH_DEAD, 
                tp_token.setDate(5, new java.sql.Date(token.getDeath().getTime()));
                //ESTADO,"
                tp_token.setString(6, "0");
                //USER_ADD
                tp_token.setString(7, NovaOracleConnectionInfo.USER);

                if (isBatch) {
                    tp_token.addBatch();

                    if ((contador % 50) == 0) {
                        tp_token.executeBatch();
                        contador = 1;
                    } else {
                        contador += 1;
                    }
                } else {
                    tp_token.execute();
                }
            }

            if (contador > 1) {
                //EJECUTAMOS LOS QUE HAYAN QUEDADO
                tp_token.executeBatch();
            }
            tp_token.close();
logger.info("paso 3");

            connection.commit();
            connection.close();

            */
            if (archivo.getTokensMalFormados().size() > 0 || archivo.getTokensRepetidos().size() > 0) {
                StringBuilder buffer = new StringBuilder("Informe de errores para el archivo " + fileName + "\n\n");
                for (Token token : archivo.getTokensMalFormados()) {
                    buffer.append("token: ").append(token.getSn()).append(". Error: ").append(token.getError())
                            .append("\n");
                }

                for (Token token : archivo.getTokensRepetidos()) {
                    buffer.append("token: ").append(token.getSn()).append(". Error: ").append(token.getError())
                            .append("\n");
                }
                File errores = new File(resolverNombre(pathErrores, fileName + ".err"));

                if (errores.exists()) {
                    errores.delete();
                    errores.createNewFile();
                }

                FileWriter writer = new FileWriter(errores);

                writer.write(buffer.toString());
                writer.flush();
                writer.close();
            }
logger.info("paso 4");
            moveFile(new File(resolverNombre(pathTrabajo, fileName)), new File(resolverNombre(pathHistorico, fileName)));

            logger.debug("tokens en archivo " + fileName + ": " + archivo.getTokens().size());

            return archivo.getTokens().size();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void moveFile(File sourceFile, File destFile) throws IOException {

        if (destFile.exists()) {
            destFile.delete();
        }

        destFile.createNewFile();

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();

            long count = 0;
            long size = source.size();
            while ((count += destination.transferFrom(source, 0, size - count)) < size)
				;
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
            sourceFile.delete();
        }
    }

    private String resolverNombre(String path, String nombre) {
        String nombreArchivo = null;
        if (path.endsWith("\\") || path.endsWith("/")) {
            nombreArchivo = path + nombre;
        } else {
            if (path.contains("/")) {
                nombreArchivo = path + "/" + nombre;
            } else if (path.contains("\\")) {
                nombreArchivo = path + "\\" + nombre;
            } else {
                nombreArchivo = path + System.getProperty("file.separator") + nombre;
            }
        }
        return nombreArchivo;
    }
}
