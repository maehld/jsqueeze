/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007 Dominik Maehl and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dominik Maehl - initial API and implementation
 * 
 * $Id$
 *******************************************************************************/

package com.dsnine.jsqueeze;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Server;
import com.dsnine.jsqueeze.model.Song;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mullassery.imaging.Imaging;
import com.mullassery.imaging.ImagingFactory;

public class ExporterStressTest {

	// Command line parameters
	private static String serverAddress;
	private static String outputFilename;
	private static String httpPort;
	private static String cliPort;
	
	// Statistical fields
	private static long startTime;
	private static long elapsedTime;

	public static void main(String[] args) throws Exception {
		System.out.println("Exporter stress test v3");
		System.out.println("Server address: " + args[0]);
		serverAddress = args[0];
		System.out.println("CLI port:       " + args[1]);
		cliPort = args[1];
		System.out.println("HTTP port:      " + args[2]);
		httpPort = args[2];
		System.out.println("Output file:    " + args[3]);
		outputFilename = args[3];
		new ExporterStressTest().run();
		System.gc();
		printInfo("Cleaning 2/2...");
		printInfo("Done!");
	}

	private Font titleFont;
	private Font albumInfoFont;
	@SuppressWarnings("unused")
	private Font songInfoFont;
	@SuppressWarnings("unused")
	private Font songInfoHeaderFont;
	Imaging imaging;


	private void run() throws Exception {
		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
		PdfWriter.getInstance(document, new FileOutputStream(outputFilename));
		document.open();
		
		imaging = ImagingFactory.createImagingInstance();
		
		Configuration configuration = Configuration.create();
		configuration.setServerAddress(serverAddress);
		configuration.setCLIPort(Integer.parseInt(cliPort));
		configuration.setHTTPPort(Integer.parseInt(httpPort));
		Server server = Server.create(configuration);
		
		System.out.println("Server version: " + server.getVersion());

		printInfo("Exporter is starting...");
		
		BaseFont baseFont = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		for (String cp : baseFont.getCodePagesSupported()) {
			System.err.println(cp);
		}
		titleFont = new Font(baseFont, 10, Font.BOLD);
		albumInfoFont = new Font(baseFont, 8, Font.ITALIC);
		songInfoFont = new Font(baseFont, 6);		
		songInfoHeaderFont = new Font(baseFont, 6, Font.BOLD);		

		Paragraph titleParagraph = new Paragraph();
		titleParagraph.setAlignment(Element.ALIGN_CENTER);
		titleParagraph.add(new Chunk("SlimServer Inventory (" + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()) + ")", titleFont));
		titleParagraph.setSpacingAfter(30);
		document.add(titleParagraph);

		PdfPTable table = null;
		boolean tableDirty = false;
		boolean newRow =  true;
		
		printInfo("Getting albums,..");
		List<Album> albums = server.getAllAlbums();
		printInfo("Albums: " + albums.size());
		for (int i = 0; i<albums.size(); i++) {
			if (i % 20 == 0) {
				if (table != null) {
					document.add(table);
				}
				table = new PdfPTable(2);
				table.setWidthPercentage(100);
			}

			Album album = albums.get(i);
			printInfo("Processing album #" + (i + 1) + " (" + album.getName() + ")...");
			PdfPCell cell = new PdfPCell();
			cell.disableBorderSide(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
			printAlbum(cell, album);
			table.addCell(cell);
			tableDirty = true;
			newRow = !newRow;
		}

		if (!newRow) {
			PdfPCell cell = new PdfPCell();
			cell.disableBorderSide(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
			table.addCell(cell);
		}
		
		if (tableDirty) {
			document.add(table);
		}
		printInfo("Completing PDF...");
		server.disconnect();
		document.close();
		printInfo("Almost done...trying to clean memory...");
		System.gc();
		printInfo("Cleaning 1/2...");
	}

	private void printAlbum(PdfPCell cell, Album album) throws Exception {
		
		List<Artist> artists = album.getArtists();
		Song song = album.getArtworkSong();
		
		// Handle albums without an artwork
		if (song == null) {
			song = album.getSongs().get(0);
		}
		
		String albumName = album.getName();
		String genre = song.getGenre().getName();
		int year = album.getYear();
		int discs  = album.getDiscs();
		
		String artistsLine = "";
		
		for (int i=0;i<3;i++) {
			if (artists.size() > i) {
				artistsLine = artistsLine + artists.get(i).getName() + ", ";
			} else {
				break;
			}
		}
		
		if (artists.size() > 3) {
			artistsLine = artistsLine + "etc.";
		} else {
			if (artistsLine.endsWith(", ")) {
					artistsLine = artistsLine.substring(0, artistsLine.length() - 2);
			}
		}
		
		PdfPCell albumCell = new PdfPCell();
		albumCell.disableBorderSide(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
		
		{
			Paragraph p = new Paragraph();
			p.setLeading(titleFont.getCalculatedSize());
			p.add(new Chunk(albumName, titleFont));
			p.add(Chunk.NEWLINE);
			p.setLeading(titleFont.getCalculatedSize());
			p.add(new Chunk(artistsLine, albumInfoFont));
			p.add(Chunk.NEWLINE);
			p.add(new Chunk("Genre: " + genre + ", Year: " + year + ", Discs: " + discs, albumInfoFont));
			albumCell.addElement(p);
		}
		
		PdfPCell songsCell = new PdfPCell();
		songsCell.disableBorderSide(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
		songsCell.setColspan(2);

		PdfPCell imageCell = new PdfPCell();
		imageCell.disableBorderSide(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.TOP | PdfPCell.BOTTOM);
		BufferedImage image = imaging.read(new URL("http://" + serverAddress + ":" + httpPort + "/music/" + song.getId() + "/cover.jpg"));
		imaging.setImagingQuality(75);
		image = imaging.resize(image, 100, 100);
		Image coverImage = Image.getInstance(image, null);
		
		imageCell.addElement(coverImage);
		
		PdfPTable table = new PdfPTable(new float[]{20,80});
		table.setWidthPercentage(100);
		table.addCell(imageCell);
		table.addCell(albumCell);
		table.addCell(songsCell);
		cell.addElement(table);
	}

	private static void printInfo(String message) {
		startTime = (startTime == 0L) ? System.currentTimeMillis() : startTime;
		elapsedTime = System.currentTimeMillis() - startTime;
		MemoryMXBean memoryMXB = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapMemoryUsage = memoryMXB.getHeapMemoryUsage();
		MemoryUsage nonHeapMemoryUsage = memoryMXB.getNonHeapMemoryUsage();
		
		String output = message;
		output += " (";
		output += "h:" + getFormattedSize(heapMemoryUsage.getUsed()) + "/" + getFormattedSize(heapMemoryUsage.getMax()) + "KB";
		output += " nh:" + getFormattedSize(nonHeapMemoryUsage.getUsed()) + "/" + getFormattedSize(nonHeapMemoryUsage.getMax()) + "KB";
		output += " time:" + elapsedTime + "ms";
		output += ")";
		
		System.out.println(output);
	}
	
	private static String getFormattedSize(long size) {
		long kbytes = size / 1024;
		return NumberFormat.getNumberInstance().format(kbytes); 
	}
}
