package org.onap.sdc.ci.tests.datatypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.yaml.snakeyaml.Yaml;

public class Configuration {
	
	private String beHost;
	private String feHost;
	private int bePort;
	private int fePort;
	private String url;
	private String remoteTestingMachineIP;
	private int remoteTestingMachinePort;
	private boolean remoteTesting;
	private String browser;
	private String systemUnderDebug;
	private boolean captureTraffic;
	private boolean useBrowserMobProxy;
	private String stopOnClassFailure;
	private String reportFileName;
	private String reportFolder;
	private int numOfAttemptsToRefresh;
	private boolean rerun;
	private String windowsDownloadDirectory; 
	private String screenshotFolder;
	private String harFilesFolder;
	private boolean useCustomLogin;
	private String geckoDriverPath;
	
	public Configuration(String url) {
		super();
		basicInit(url, false);
	}
	
	public Configuration(String url, boolean useCustomLogin) {
		super();
		basicInit(url, useCustomLogin);
	}
	
	private void basicInit(String url, boolean useCustomLogin) {
		this.remoteTesting = false;
		this.captureTraffic = false;
		this.useBrowserMobProxy = false;
		this.reportFolder = "." + File.separator + "ExtentReport" + File.separator;
		this.reportFileName = "UI_Extent_Report.html";
		this.screenshotFolder = reportFolder + "screenshots" + File.separator;
		this.harFilesFolder = reportFolder + "har_files" + File.separator;
		this.browser = "firefox";
		this.url = url;
		this.numOfAttemptsToRefresh = 2;
		this.useCustomLogin = useCustomLogin;
		this.geckoDriverPath = null;
	}

	public String getGeckoDriverPath(){
		return this.geckoDriverPath; }

	public void setGeckoDriverPath(String geckoDriverPath){ this.geckoDriverPath = geckoDriverPath; }

	public String getBeHost() {
		return beHost;
	}

	public void setBeHost(String beHost) {
		this.beHost = beHost;
	}


	public String getFeHost() {
		return feHost;
	}


	public void setFeHost(String feHost) {
		this.feHost = feHost;
	}


	public int getBePort() {
		return bePort;
	}


	public void setBePort(int bePort) {
		this.bePort = bePort;
	}


	public int getFePort() {
		return fePort;
	}


	public void setFePort(int fePort) {
		this.fePort = fePort;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getRemoteTestingMachineIP() {
		return remoteTestingMachineIP;
	}


	public void setRemoteTestingMachineIP(String remoteTestingMachineIP) {
		this.remoteTestingMachineIP = remoteTestingMachineIP;
	}


	public int getRemoteTestingMachinePort() {
		return remoteTestingMachinePort;
	}


	public void setRemoteTestingMachinePort(int remoteTestingMachinePort) {
		this.remoteTestingMachinePort = remoteTestingMachinePort;
	}


	public boolean isRemoteTesting() {
		return remoteTesting;
	}


	public void setRemoteTesting(boolean remoteTesting) {
		this.remoteTesting = remoteTesting;
	}


	public String getBrowser() {
		return browser;
	}


	public void setBrowser(String browser) {
		this.browser = browser;
	}


	public String getSystemUnderDebug() {
		return systemUnderDebug;
	}


	public void setSystemUnderDebug(String systemUnderDebug) {
		this.systemUnderDebug = systemUnderDebug;
	}


	public boolean isCaptureTraffic() {
		return captureTraffic;
	}


	public void setCaptureTraffic(boolean captureTraffic) {
		this.captureTraffic = captureTraffic;
	}


	public boolean isUseBrowserMobProxy() {
		return useBrowserMobProxy;
	}


	public void setUseBrowserMobProxy(boolean useBrowserMobProxy) {
		this.useBrowserMobProxy = useBrowserMobProxy;
	}


	public String getStopOnClassFailure() {
		return stopOnClassFailure;
	}


	public void setStopOnClassFailure(String stopOnClassFailure) {
		this.stopOnClassFailure = stopOnClassFailure;
	}


	public String getReportFileName() {
		return reportFileName;
	}


	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}


	public String getReportFolder() {
		return reportFolder;
	}


	public void setReportFolder(String reportFolder) {
		this.reportFolder = reportFolder;
	}


	public int getNumOfAttemptsToRefresh() {
		return numOfAttemptsToRefresh;
	}


	public void setNumOfAttemptsToRefresh(int numOfAttemptsToRefresh) {
		this.numOfAttemptsToRefresh = numOfAttemptsToRefresh;
	}


	public boolean isRerun() {
		return rerun;
	}


	public void setRerun(boolean rerun) {
		this.rerun = rerun;
	}




	public String getWindowsDownloadDirectory() {
		return windowsDownloadDirectory;
	}

	public void setWindowsDownloadDirectory(String windowsDownloadDirectory) {
		this.windowsDownloadDirectory = windowsDownloadDirectory;
	}

	public String getScreenshotFolder() {
		return screenshotFolder;
	}

	public void setScreenshotFolder(String screenshotFolder) {
		this.screenshotFolder = screenshotFolder;
	}

	public String getHarFilesFolder() {
		return harFilesFolder;
	}

	public void setHarFilesFolder(String harFilesFolder) {
		this.harFilesFolder = harFilesFolder;
	}

	public boolean isUseCustomLogin() {
		return useCustomLogin;
	}

	public void setUseCustomLogin(boolean useCustomLogin) {
		this.useCustomLogin = useCustomLogin;
	}
	
	public synchronized static Configuration loadConfigFile(File configFile) {	
		return loadConfigFile(configFile, Configuration.class);
	}
	
	public Configuration(){
		super();
	}
	
	public synchronized static <T> T loadConfigFile(File configFile, Class<T> clazz){
		InputStream in = null;
		T config = null;
		try {
			String absolutePath = configFile.getAbsolutePath();
			in = Files.newInputStream(Paths.get(absolutePath));
			Yaml yaml = new Yaml();
			config = yaml.loadAs(in, clazz);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return config;
	}
		
		
		
}
