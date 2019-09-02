package com.novatronic.valorados.services;

public interface TokenFileImporterMBean {

	public abstract void start();

	public abstract void stop();

	public abstract boolean isStarted();

	public abstract void setPathTrabajo(String workingPath);

	public abstract String getPathTrabajo();

	public abstract String getPathHistorico();

	public abstract void setPathHistorico(String historicalPath);

	public abstract String getPathErrores();

	public abstract void setPathErrores(String errorPath);

	public abstract String getEstado();
	
	public abstract String getURLOracle();
	
	public abstract void setURLOracle(String newURL);
	
	public abstract String getUsuarioOracle();
	
	public abstract void setUsuarioOracle(String newUsuario);
	
	public abstract String getPasswordOracle();
	
	public abstract void setPasswordOracle(String newPassword);

}
