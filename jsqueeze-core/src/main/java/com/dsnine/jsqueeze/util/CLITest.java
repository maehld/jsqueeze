package com.dsnine.jsqueeze.util;

import java.net.UnknownHostException;
import java.util.List;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Server;

public class CLITest {

	public static void main(String[] args) throws SqueezeException, UnknownHostException {
		Configuration cfg = Configuration.create();
		cfg.setServerAddress("192.168.0.3");
		Server server = new Server(cfg);
		System.out.println(server.getVersion());

		for (Album album : server.getAllAlbums()) {
			// Album album = server.searchAlbums("Top Gun").get(0);
			List<Artist> artists = album.getArtists();
			if (!artists.isEmpty()) {
				// System.out.println(album.getId() + " " + album.getName() +
				// " " + artists.size());
			} else {
				System.err.println(album.getId() + " " + album.getName());
			}
		}
	}

}
