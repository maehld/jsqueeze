package com.dsnine.jsqueeze.album2mp3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exec {

	public static void exec(File dir, String[] command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		builder.command(command);
		builder.directory(dir);
		Process process = builder.start();
		BufferedReader inReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(process.getInputStream())));
		while (inReader.ready()) {
			System.out.println(inReader.readLine());
		}
		inReader.close();
		process.waitFor();
		return;
	}

	public static void exec(File dir, String command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		builder.command(command.split(" "));
		builder.directory(dir);
		Process process = builder.start();
		BufferedReader inReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(process.getInputStream())));
		while (inReader.ready()) {
			System.out.println(inReader.readLine());
		}
		inReader.close();
		process.waitFor();
		return;
	}
	
}
