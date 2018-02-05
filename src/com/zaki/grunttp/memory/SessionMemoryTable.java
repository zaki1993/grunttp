package com.zaki.grunttp.memory;

import com.zaki.grunttp.server.session.Session;

import java.util.Hashtable;
import java.util.Map;

final class SessionMemoryTable {

	private static Map<String, Session> virtualSessionMap;
	private static SessionMemoryTable reference;
	
	private SessionMemoryTable() {
		virtualSessionMap = new Hashtable<>();
	}

	/**
	 * @return SessionMemoryTable reference
	 */
	static SessionMemoryTable getVirtualMemoryTable() {
		
		if (SessionMemoryTable.reference == null) {
			synchronized (SessionMemoryTable.class) {
				if (SessionMemoryTable.reference == null) {
					SessionMemoryTable.reference = new SessionMemoryTable();
				}
			}
		}
		return SessionMemoryTable.reference;
	}

	/**
	 * @return the server session map
	 */
	Map<String, Session> getSessionMap() {
		return SessionMemoryTable.virtualSessionMap;
	}
}
