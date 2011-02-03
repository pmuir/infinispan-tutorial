package uk.org.bleepbleep.infinispan.tutorial.util;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

public class ClusterValidation {
   
   public static int REPLICATION_TRY_COUNT = 60;
   public static int REPLICATION_TIME_SLEEP = 2000;
   private static final String CACHE = "clusterValidation";
   private static final String KEY = "clusterValidation";
   
   public static boolean isClusterFormed(EmbeddedCacheManager cacheManager, int nodeId, int clusterSize) {
      return new ClusterValidation(cacheManager.getCache(CACHE), nodeId, clusterSize).checkReplicationSeveralTimes() > 0;
   }
   
   private final Cache<Object, Object> cache;
   private final int clusterSize;
   private final int nodeId;

   public ClusterValidation(Cache<Object, Object> cache, int nodeId, int clusterSize) {
      this.cache = cache;
      this.clusterSize = clusterSize;
      this.nodeId = nodeId;
   }

   private int checkReplicationSeveralTimes() {
      tryToPut();
      int replCount = 0;
      for (int i = 0; i < REPLICATION_TRY_COUNT; i++) {
         replCount = replicationCount(clusterSize);
         System.out.println("Replication Count: " + replCount);
         if (replCount == clusterSize - 1) {
            System.out.println("Cluster formed successfully!");
            try {
	       Thread.sleep(2000);
            } catch (InterruptedException e) {
	       Thread.currentThread().interrupt();
            }
            return replCount;
         }
         //adding our stuff one more time
         tryToPut();
         try {
	    Thread.sleep(REPLICATION_TIME_SLEEP);
	} catch (InterruptedException e) {
	    Thread.currentThread().interrupt();
	}
      }
      System.out.println("Cluster failed to form!");
      return -1;
   }

   private void tryToPut() {
      int tryCount = 0;
      while (tryCount < 5) {
         try {
            cache.put(key(nodeId), "true");
            System.out.println("Put true to cache for node " + nodeId);
            return;
         }
         catch (Throwable e) {
            tryCount++;
         }
      }
      throw new IllegalStateException("Couldn't accomplish addition before replication!");
   }
   
   private int replicationCount(int clusterSize) {
      int replicaCount = 0;
      for (int i = 0; i < clusterSize; i++) {
         if (i == nodeId) {
            continue;
         }
         Object data = tryGet(i);
         System.out.println("Got " + data + " for node " + i);
         if (data == null || !"true".equals(data)) {
         } else {
            replicaCount++;
         }
      }
      return replicaCount;
   }
   
   private Object tryGet(int i) {
      int tryCont = 0;
      while (tryCont < 5) {
         try {
            return cache.get(key(i));
         }
         catch (Throwable e) {
            tryCont++;
         }
      }
      return null;
   }
   
   private String key(int slaveIndex) {
      return KEY + slaveIndex;
   }
   
}
