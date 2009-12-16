package com.dsnine.jsqueeze.album2mp3;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Album ID or name must be specified");
			System.exit(1);
		}

 		int id = -1;

		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
		}
		
		if (id != -1) {
			new Album2Mp3(id);
		} else {
			new Album2Mp3(args[0]);
		}
	}
}
