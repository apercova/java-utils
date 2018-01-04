package net.apercova.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Obtains JVM's Process ID assuming that {@link RuntimeMXBean#getName()} is 
 * of the form of: {@code PID@HOST}. <br/> 
 * Example: {@code 10368@github.com}
 *
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2018.01
 *
 */
public class ProcessId {
	
	private String pid;
	
	public ProcessId() {
		parsePid();
	}
	
	/**
	 * Get JVM Process ID from {@link RuntimeMXBean}'s name.<br/>
	 * <b>Note:</b> <i>NOt all JVM implementations assure that PID is within JVM's name</i>
	 */
	private void parsePid() {
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		pid = runtimeBean.getName();
		Pattern pattern = Pattern.compile("(@.*)$");
		Matcher matcher = pattern.matcher(pid);
		
		matcher.find();
		pid = pid.substring(0, matcher.start());
	}
	
	/**
	 * Retrieves JVM's Process ID. 
	 * @return JVM's Process ID or {@code null} if JVM´s PID is not within JVM's name
	 */
	public String getPid() {
		return pid;
	}
	
	public static void main(String[] args) {
		String pid = new ProcessId().getPid();
		System.out.println(pid);
	}

}
