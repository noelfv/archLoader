package com.novatronic.valorados.services.work;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.digester.Digester;
import com.novatronic.valorados.services.util.NovaDateConverter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Archivo implements Serializable {

    private final static Logger logger = Logger.getLogger(FileProcessor.class);
    private static final long serialVersionUID = 7287592617756146673L;
    private Map<String, Token> tokens = new HashMap<String, Token>();
    private List<Token> tokensMalFormados = new ArrayList<Token>();
    private List<Token> tokensRepetidos = new ArrayList<Token>();
    private int version;
    private String origin;
    private String dest;
    private String name;
    private String firstToken;
    private String lastToken;
    private int numTokens;
    private String secret;
    private int defDigits;
    private int defInterval;
    private int defAlg;
    private int defMode;
    private int defPrecision;
    private int defSmallWin;
    private int defMediumWin;
    private int defLargeWin;
    private int defAddPIN;
    private int defLocalPIN;
    private int defCopyProtection;
    private int defKeypad;
    private int defProtLevel;
    private int defRevision;
    private int defTimeDerivedSeeds;
    private int defAppDerivedSeeds;
    private int defFormFactor;
    private String headerMAC;
    private List<String> listaTramas=new ArrayList<String>();




    public Archivo(String nombreArchivo) throws Exception {
        logger.info("paso x");
        File archivo = null;

        archivo = new File(nombreArchivo);

        ConvertUtils.register(new NovaDateConverter(), Date.class);

        this.listaTramas=FileUtils.readLines(archivo, Charset.defaultCharset());
    }

    public List<String> getListaTramas() {
        return listaTramas;
    }

    public void setListaTramas(List<String> listaTramas) {
        this.listaTramas = listaTramas;
    }

    private boolean camposCompletos(Token token) {
        boolean salida = false;
        bloque:
        {
            if (token.getSn() == null || token.getSn().trim().length() == 0) {
                token.setError("SSN invalido");
                break bloque;
            }
            if (token.getBirth() == null) {
                token.setError("Fecha de nacimiento invalida");
                break bloque;
            }
            if (token.getDeath() == null) {
                token.setError("Fecha de expiracion invalida");
                break bloque;
            }
            if (token.getSeed() == null) {
                token.setError("Semilla invalida");
                break bloque;
            }
            if (token.getTokenMAC() == null) {
                token.setError("Token MAC invalido");
                break bloque;
            }
            salida = true;
        }
        return salida;
    }

    public Map<String, Token> getTokens() {
        return tokens;
    }

    public void setTokens(Map<String, Token> tokens) {
        this.tokens = tokens;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstToken() {
        return firstToken;
    }

    public void setFirstToken(String firstToken) {
        this.firstToken = firstToken;
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    public int getNumTokens() {
        return numTokens;
    }

    public void setNumTokens(int numTokens) {
        this.numTokens = numTokens;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getDefDigits() {
        return defDigits;
    }

    public void setDefDigits(int defDigits) {
        this.defDigits = defDigits;
    }

    public int getDefInterval() {
        return defInterval;
    }

    public void setDefInterval(int defInterval) {
        this.defInterval = defInterval;
    }

    public int getDefAlg() {
        return defAlg;
    }

    public void setDefAlg(int defAlg) {
        this.defAlg = defAlg;
    }

    public int getDefMode() {
        return defMode;
    }

    public void setDefMode(int defMode) {
        this.defMode = defMode;
    }

    public int getDefPrecision() {
        return defPrecision;
    }

    public void setDefPrecision(int defPrecision) {
        this.defPrecision = defPrecision;
    }

    public int getDefSmallWin() {
        return defSmallWin;
    }

    public void setDefSmallWin(int defSmallWin) {
        this.defSmallWin = defSmallWin;
    }

    public int getDefMediumWin() {
        return defMediumWin;
    }

    public void setDefMediumWin(int defMediumWin) {
        this.defMediumWin = defMediumWin;
    }

    public int getDefLargeWin() {
        return defLargeWin;
    }

    public void setDefLargeWin(int defLargeWin) {
        this.defLargeWin = defLargeWin;
    }

    public int getDefAddPIN() {
        return defAddPIN;
    }

    public void setDefAddPIN(int defAddPIN) {
        this.defAddPIN = defAddPIN;
    }

    public int getDefLocalPIN() {
        return defLocalPIN;
    }

    public void setDefLocalPIN(int defLocalPIN) {
        this.defLocalPIN = defLocalPIN;
    }

    public int getDefCopyProtection() {
        return defCopyProtection;
    }

    public void setDefCopyProtection(int defCopyProtection) {
        this.defCopyProtection = defCopyProtection;
    }

    public int getDefKeypad() {
        return defKeypad;
    }

    public void setDefKeypad(int defKeypad) {
        this.defKeypad = defKeypad;
    }

    public int getDefProtLevel() {
        return defProtLevel;
    }

    public void setDefProtLevel(int defProtLevel) {
        this.defProtLevel = defProtLevel;
    }

    public int getDefRevision() {
        return defRevision;
    }

    public void setDefRevision(int defRevision) {
        this.defRevision = defRevision;
    }

    public int getDefTimeDerivedSeeds() {
        return defTimeDerivedSeeds;
    }

    public void setDefTimeDerivedSeeds(int defTimeDerivedSeeds) {
        this.defTimeDerivedSeeds = defTimeDerivedSeeds;
    }

    public int getDefAppDerivedSeeds() {
        return defAppDerivedSeeds;
    }

    public void setDefAppDerivedSeeds(int defAppDerivedSeeds) {
        this.defAppDerivedSeeds = defAppDerivedSeeds;
    }

    public int getDefFormFactor() {
        return defFormFactor;
    }

    public void setDefFormFactor(int defFormFactor) {
        this.defFormFactor = defFormFactor;
    }

    public String getHeaderMAC() {
        return headerMAC;
    }

    public void setHeaderMAC(String headerMAC) {
        this.headerMAC = headerMAC;
    }

    public List<Token> getTokensMalFormados() {
        return tokensMalFormados;
    }

    public void setTokensMalFormados(List<Token> tokensMalFormados) {
        this.tokensMalFormados = tokensMalFormados;
    }

    public List<Token> getTokensRepetidos() {
        return tokensRepetidos;
    }

    public void setTokensRepetidos(List<Token> tokensRepetidos) {
        this.tokensRepetidos = tokensRepetidos;
    }
}
