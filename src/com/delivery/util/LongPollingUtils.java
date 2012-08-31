package com.delivery.util;

import java.util.HashSet;


public class LongPollingUtils {
	private static HashSet<LongPollingLock> sOrderManagerLPHandlersLocks = new HashSet<LongPollingLock>();
	private static Object sOrderManagerLPLock = new Object();

	public static void registerOrderLPLock(LongPollingLock lock) {
		synchronized(sOrderManagerLPLock) {
			sOrderManagerLPHandlersLocks.add(lock);
		}
	}

	public static void unregisterOrderLPLock(LongPollingLock lock) {
		synchronized(sOrderManagerLPLock) {
			sOrderManagerLPHandlersLocks.remove(lock);
		}
	}

	public static void notifyOrderChange() {
		synchronized(sOrderManagerLPLock) {
			for (LongPollingLock lock : sOrderManagerLPHandlersLocks) {
				synchronized(lock) {
					lock.timedOut = false;
					lock.notifyAll();
				}
			}
		}
	}

	public static class LongPollingLock {
		public boolean timedOut = true;
	}
}
