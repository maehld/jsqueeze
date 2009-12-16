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
 * $Id: CachingRepositoryFactoryEnhancer.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Entity;
import com.dsnine.jsqueeze.model.Genre;
import com.dsnine.jsqueeze.model.Song;

public class CachingRepositoryFactoryEnhancer extends RepositoryFactory {

	private static Object enhanceRepository(Object targetRepository, Class<?> targetInterface, Class<? extends Entity> entityClass) {
		return Proxy.newProxyInstance(targetRepository.getClass().getClassLoader(), new Class[]{targetInterface}, new CachingRepository(targetRepository, entityClass));
	}

	public static void enhance(RepositoryFactory factoryToEnhance) {
		ISongRepository songRepository = factoryToEnhance.getSongRepository();
		IArtistRepository artistRepository = factoryToEnhance.getArtistRepository();
		IAlbumRepository albumRepository = factoryToEnhance.getAlbumRepository();
		IGenreRepository genreRepository = factoryToEnhance.getGenreRepository();
		
		songRepository = (ISongRepository) enhanceRepository(songRepository, ISongRepository.class, Song.class);
		artistRepository = (IArtistRepository) enhanceRepository(artistRepository, IArtistRepository.class, Artist.class);
		albumRepository = (IAlbumRepository) enhanceRepository(albumRepository, IAlbumRepository.class, Album.class);
		genreRepository = (IGenreRepository) enhanceRepository(genreRepository, IGenreRepository.class, Genre.class);
		
		factoryToEnhance.setSongRepository(songRepository);
		factoryToEnhance.setAlbumRepository(albumRepository);
		factoryToEnhance.setArtistRepository(artistRepository);
		factoryToEnhance.setGenreRepository(genreRepository);
	}
	
	private static class CachingRepository implements InvocationHandler {

		private Object targetRepository;
		//TODO: A better way to efficiently cache objects would be to store only id's in the first cache and have an id-based list for
		//TODO: the real objects
		private Map<String, Object> entityCache;
		private Class<? extends Entity> entityClass;
		private Logger log = LoggerFactory.getLogger(CachingRepository.class);
		
		private int cacheHits = 0;
		private int cacheMisses = 0;
		
		public CachingRepository(Object targetRepository, Class<? extends Entity> entityClass) {
			this.targetRepository = targetRepository;
			this.entityClass = entityClass;
			entityCache = new HashMap<String, Object>();
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String cacheKey = null;
			Object returnValue = null;
			
			if (method.getReturnType().equals(entityClass) || method.getReturnType().equals(List.class)) {
				StringBuilder builder = new StringBuilder();
				builder.append(method.getName());
				
				if (args != null) {
					for (Object argument : args) {
						builder.append(argument);
					}
				}
				
				cacheKey = builder.toString();
				
				if (entityCache.containsKey(cacheKey)) {
					returnValue = entityCache.get(cacheKey);
					cacheHits++;
				}
			}
		
			if (method.getName().equals("dispose")) {
				log.debug("Cache statistics for Cache(" + entityClass.getName()+ "): Hits: " + cacheHits + " Misses: " + cacheMisses + " Cachesize:" + entityCache.size());
			}
			
			if (returnValue == null) {
				returnValue = method.invoke(targetRepository, args);
				if (cacheKey != null) {
					entityCache.put(cacheKey, returnValue);
					cacheMisses++;
				}
			}
			 
			return returnValue;
		}
		
	}

}
