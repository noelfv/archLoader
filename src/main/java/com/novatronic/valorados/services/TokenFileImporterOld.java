package com.novatronic.valorados.services;

import com.novatronic.valorados.services.db.NovaOracleConnectionInfo;
import com.novatronic.valorados.services.db.NovaOracleConnectionPool;
import com.novatronic.valorados.services.work.FileProcessor;
import com.novatronic.valorados.services.work.NovaExecutor;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TokenFileImporterOld implements TokenFileImporterMBean {

    private final static Logger logger = Logger.getLogger(TokenFileImporterOld.class);
    private boolean started = false;
    private String pathTrabajo;
    private String pathHistorico;
    private String pathErrores;
    private String estado = "Listo";
    private FileObject fileObject;
    DefaultFileMonitor fm;
    private final static int PATH_NOT_EXISTS = 1;
    private final static String PATH_NOT_EXISTS_STR = "El directorio de %s no existe";
    private final static int PATH_NOT_FOLDER = 2;
    private final static String PATH_NOT_FOLDER_STR = "El path de %s no es un directorio";
    private final static int PATH_INVALID_PARAM = 3;
    private final static String PATH_INVALID_PARAM_STR = "El directorio de %s no es valido";
    private final static int PATH_NOT_PERMS = 4;
    private final static String PATH_NOT_PERMS_STR = "El directorio de %s no tiene permisos de lectura y escritura";
    private final static int PATH_ERRORS = 5;
    private final static String PATH_ERRORS_STR = "Error al procesar el directorio de %s: ";
    private final static int PATH_OK = 6;
    NovaExecutor executor = null;

    public static void main(String[] args) throws UnknownHostException {

//        System.out.println("JVM");
//
//        TokenFileImporterMBean tok = new TokenFileImporter();
//        tok.setPathTrabajo("D:\\Temp\\valorados\\SGV\\in");
//        tok.setPathErrores("D:\\Temp\\valorados\\SGV\\err");
//        tok.setPathHistorico("D:\\Temp\\valorados\\SGV\\hist");
//        tok.setURLOracle("jdbc:oracle:thin:@localhost:1521:orcl");
//        tok.setUsuarioOracle("secadv_bn");
//        tok.setPasswordOracle("IQAT66nhSU0=");
//        logger.debug("iniciando");
//        tok.start();

//        while (true) {
//            ;
//        }

        InetAddress addr = InetAddress.getLocalHost();
        String fqName = addr.getCanonicalHostName();
        System.out.println(fqName);
        String hostName = addr.getHostName();
        System.out.println(hostName);
        String hostAddres = addr.getHostAddress();
        System.out.println(hostAddres);
        String osName = System.getProperty("os.name").toLowerCase();
        System.out.println(osName);
        
       
        

    }

    public void start() {

        if (!validarDirectorios() || !validarDatosConexion()) {
            return;
        }
        if (!validarConexion()) {
            return;
        }


        try {
            detener();
            logger.info("paso 1");

            executor = new NovaExecutor(3);
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            logger.info("paso 2");
            fileObject = VFS.getManager().resolveFile(pathTrabajo);
            fm = new DefaultFileMonitor(new NovaFileListener());
            fm.setRecursive(false);
            fm.addFile(fileObject);
            logger.info("paso 3");
            if (fileObject.getChildren().length > 0) {
                logger.info("paso 4");
                //fileObject.getContent().setLastModifiedTime(System.currentTimeMillis() + 1000);

                logger.info(fileObject.getChildren().length);
                for (FileObject fob : fileObject.getChildren()) {
                    executor.submit(new FileProcessor(fob.getName().getBaseName(), pathTrabajo, pathErrores,
                            pathHistorico));
                }
            }
            logger.info("paso 5");

            fm.start();
        } catch (Exception e) {
            logger.debug(e.toString());
            estado = e.toString();
            started = false;
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("Iniciado");
        }
        estado = "Iniciado";
        started = true;
    }

    public void stop() {

        detener();

        if (logger.isInfoEnabled()) {
            logger.info("Detenido");
        }

        started = false;
    }

    private void detener() {

        if (fm != null) {
            fm.stop();
            fm = null;
        }

        if (fileObject != null) {
            try {
                fileObject.close();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        if (executor != null) {
            try {
                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }
            executor = null;
        }

        NovaOracleConnectionPool.reset();
    }

    private boolean validarDatosConexion() {
        if (NovaOracleConnectionInfo.PASSWORD == null || NovaOracleConnectionInfo.PASSWORD.trim().length() == 0) {
            estado = "Debe completar el password de conexion";
            return false;
        }

        if (NovaOracleConnectionInfo.URL == null || NovaOracleConnectionInfo.URL.trim().length() == 0) {
            estado = "Debe completar la URL de conexion";
            return false;
        }

        if (NovaOracleConnectionInfo.USER == null || NovaOracleConnectionInfo.USER.trim().length() == 0) {
            estado = "Debe completar el usuario de conexion";
            return false;
        }
        return true;
    }

    private boolean validarConexion() {
        try {
            return NovaOracleConnectionPool.getConnection() != null;
        } catch (Exception e) {
            logger.error(e.toString());
            estado = e.toString();
            return false;
        }
    }

    private boolean validarDirectorios() {
        int resultValidacion = validarDirectorio(pathTrabajo);
        if (resultValidacion != PATH_OK) {
            started = false;
            estado = getPathMessage(resultValidacion, "trabajo");
            if (logger.isDebugEnabled()) {
                logger.debug(estado);
            }
            return false;
        }
        resultValidacion = validarDirectorio(pathHistorico);
        if (resultValidacion != PATH_OK) {
            started = false;
            estado = getPathMessage(resultValidacion, "historicos");
            if (logger.isDebugEnabled()) {
                logger.debug(estado);
            }
            return false;
        }
        resultValidacion = validarDirectorio(pathErrores);
        if (resultValidacion != PATH_OK) {
            started = false;
            estado = getPathMessage(resultValidacion, "errores");
            if (logger.isDebugEnabled()) {
                logger.debug(estado);
            }
            return false;
        }
        return true;
    }

    private int validarDirectorio(String directorio) {
        if (directorio == null || "".equals(directorio.trim())) {
            return PATH_INVALID_PARAM;
        }

        try {
            File file = new File(directorio);
            if (!file.exists()) {
                return PATH_NOT_EXISTS;
            }
            if (!file.isDirectory()) {
                return PATH_NOT_FOLDER;
            }
            if (!file.canRead() || !file.canWrite()) {
                return PATH_NOT_PERMS;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            estado = e.toString();
            return PATH_ERRORS;
        }
        return PATH_OK;
    }

    private String getPathMessage(int idMessage, String tipoPath) {
        switch (idMessage) {
            case PATH_ERRORS:
                return String.format(PATH_ERRORS_STR, tipoPath);
            case PATH_INVALID_PARAM:
                return String.format(PATH_INVALID_PARAM_STR, tipoPath);
            case PATH_NOT_EXISTS:
                return String.format(PATH_NOT_EXISTS_STR, tipoPath);
            case PATH_NOT_PERMS:
                return String.format(PATH_NOT_PERMS_STR, tipoPath);
            case PATH_NOT_FOLDER:
                return String.format(PATH_NOT_FOLDER_STR, tipoPath);
            default:
                return "Error al obtener el mensaje: " + idMessage;
        }
    }

    public String getPathTrabajo() {
        return pathTrabajo;
    }

    public void setPathTrabajo(String pathTrabajo) {
        if (started) {
            estado = "Para cambiar cualquiera de los paths debe detener el servicio";
            return;
        } else {
            int resultValidacion = validarDirectorio(pathTrabajo);
            if (resultValidacion != PATH_OK) {
                started = false;
                estado = getPathMessage(resultValidacion, "trabajo");
                if (logger.isDebugEnabled()) {
                    logger.debug(estado);
                }
            } else {
                estado = "Listo";
            }
        }
        this.pathTrabajo = pathTrabajo;

    }

    public String getPathHistorico() {
        return pathHistorico;
    }

    public void setPathHistorico(String pathHistorico) {
        if (started) {
            estado = "Para cambiar cualquiera de los paths debe detener el servicio";
            return;
        } else {
            int resultValidacion = validarDirectorio(pathHistorico);
            if (resultValidacion != PATH_OK) {
                started = false;
                estado = getPathMessage(resultValidacion, "historicos");
                if (logger.isDebugEnabled()) {
                    logger.debug(estado);
                }
            }
        }
        this.pathHistorico = pathHistorico;
    }

    public String getPathErrores() {
        return pathErrores;
    }

    public void setPathErrores(String pathErrores) {
        if (started) {
            estado = "Para cambiar cualquiera de los paths debe detener el servicio";
            return;
        } else {
            int resultValidacion = validarDirectorio(pathErrores);
            if (resultValidacion != PATH_OK) {
                started = false;
                estado = getPathMessage(resultValidacion, "errores");
                if (logger.isDebugEnabled()) {
                    logger.debug(estado);
                }
            }
        }
        this.pathErrores = pathErrores;
    }

    public String getEstado() {
        if ("Listo".equalsIgnoreCase(estado) && !started) {
            if (validarDirectorios()) {
                validarDatosConexion();
            }
        }
        return estado;
    }

    public String getURLOracle() {
        return NovaOracleConnectionInfo.URL;
    }

    public void setURLOracle(String newURL) {
        if (started) {
            estado = "Para cambiar cualquiera de los datos de conexion debe detener el servicio";
            return;
        }
        NovaOracleConnectionInfo.URL = newURL;

    }

    public String getUsuarioOracle() {
        return NovaOracleConnectionInfo.USER;
    }

    public void setUsuarioOracle(String newUsuario) {
        if (started) {
            estado = "Para cambiar cualquiera de los datos de conexion debe detener el servicio";
            return;
        }
        NovaOracleConnectionInfo.USER = newUsuario;

    }

    public String getPasswordOracle() {
        return NovaOracleConnectionInfo.PASSWORD;
    }

    public void setPasswordOracle(String newPassword) {
        if (started) {
            estado = "Para cambiar cualquiera de los datos de conexion debe detener el servicio";
            return;
        }
        NovaOracleConnectionInfo.PASSWORD = newPassword;

    }

    public boolean isStarted() {
        return started;
    }

    private class NovaFileListener implements FileListener {

        public void fileChanged(FileChangeEvent arg0) throws Exception {
            logger.debug("Archivo modificado: " + arg0.getFile().getName());
        }

        public void fileCreated(FileChangeEvent arg0) throws Exception {
            executor.submit(new FileProcessor(arg0.getFile().getName().getBaseName(), pathTrabajo, pathErrores,
                    pathHistorico));

        }

        public void fileDeleted(FileChangeEvent arg0) throws Exception {
            logger.debug("Archivo eliminado: " + arg0.getFile().getName());
        }
    }
}
