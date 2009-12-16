package com.dsnine.jsqueeze.album2mp3;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Server;
import com.dsnine.jsqueeze.model.Song;

public class Album2Mp3 {

	private Server server;
		
	public Album2Mp3(int albumId) throws Exception {
		this();
		Album album = getAlbumById(albumId);
		exportAlbumToMp3(album);
	}
	
	public Album2Mp3(String albumName) throws Exception {
		this();
		Album album = getAlbumByName(albumName);
		exportAlbumToMp3(album);
	}

	private void exportAlbumToMp3(Album album) throws SqueezeException, InterruptedException, IOException {
		System.out.print("Exporting <" + album.getName() + "> by <" + album.getArtists().get(0).getName() + ">");
		for (int i = 0; i<3; i++) {
			Thread.sleep(1000);
			System.out.print(".");
		}
		System.out.println("");
		
		File albumDir = new File (album.getArtists().get(0).getName().replace("/", "_") + " - " + album.getName().replace("/", "_"));
		albumDir.mkdir();
		
		//Cover brauchen wir nicht mehr!: Exec.exec(albumDir, "wget http://192.168.0.3:9000/music/" + album.getArtworkSong().getId() + "/thumb_100x100_f_000000.jpg -q -O cover.jpg");

		for (Song song : album.getSongs()) {
			System.out.print(song.getTitle());
			System.out.print(".");

			Exec.exec(albumDir, "wget http://192.168.0.3:9000/music/" + song.getId() + "/download -q -O " + song.getId() + " ");
			
			System.out.print(".");
			Exec.exec(albumDir, "flac -s -d " + song.getId());
			new File(albumDir, "" + song.getId()).delete();
		
			
			System.out.print(".");
			Exec.exec(albumDir, "lame --quiet -b 256 " + song.getId() + ".wav " + song.getId() + ".mp3");
			new File(albumDir, "" + song.getId() + ".wav").delete();
			
			System.out.print(".");
			/*
			Runtime.getRuntime().exec(new String[] {
					"eyeD3",
					"-t",
					song.getTitle(),
					"-n",
					"" + song.getTrack(),
					"-A",
					album.getName(),
					"-a",
					song.getArtist().getName(),
					"--add-image=cover.jpg:FRONT_COVER",
					song.getId() + ".mp3"
			},null, albumDir).waitFor();
			*/
			
			File mp3File = new File(albumDir, song.getId() + ".mp3");
			
			mp3File.renameTo(new File(albumDir, trackFilename(album, song)));
			
			System.out.println("Done!");
			//break;
		}
		
		new File(albumDir, "cover.jpg").delete();
	}

	private String trackFilename(Album album, Song song) {
		String filename = "";
		
		if (album.getDiscs() > 1) {
			filename += song.getDisc() + "-";
		}
		
		filename += String.format("%02d", song.getTrack()) + "_";
		filename += song.getTitle().replace('/', '_');
		filename += ".mp3";
		return filename;
	}
	
	private Album getAlbumById(int id) throws SqueezeException {
		List<Album> allAlbums = server.getAllAlbums();
		for (Album album : allAlbums) {
			if (album.getId() == id) {
				return album;
			}
		}
		
		return null;
	}

	private Album getAlbumByName(String albumName) throws SqueezeException {
		List<Album> albums = server.searchAlbums(albumName);
		return (albums.size() != 0) ? albums.get(0) : null;
	}

	private Album2Mp3() throws Exception {
		initializeConnection();
	}

	private void initializeConnection() throws UnknownHostException, SqueezeException {
		Configuration configuration = Configuration.create();
		configuration.setServerAddress("192.168.0.3");
		server = new Server(configuration);
	}
	
}
