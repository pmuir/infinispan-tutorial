package uk.org.bleepbleep.infinispan.tutorial;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import uk.org.bleepbleep.infinispan.tutorial.util.ClusterValidation;

public class Node1 {
   
   public static void main(String[] args) throws Exception {

      // Create the configuration, and set to replication
      GlobalConfiguration gc = GlobalConfiguration.getClusteredDefault();
      Configuration c = new Configuration();
      c.setCacheMode(Configuration.CacheMode.REPL_SYNC);
       
      // Create the cache manager and get a handle to the cache we will use
      EmbeddedCacheManager cm = new DefaultCacheManager(gc, c);
      Cache<String, String> cache = cm.getCache("Demo");
      
      // Wait for the cluster to form, erroring if it doesn't form after the timeout
      if (!ClusterValidation.waitForClusterToForm(cm, 1, 2))
      {
	 throw new IllegalStateException("Error forming cluster, check the log");
      }
      
      // Put some information in the cache that we can display on the other node
      cache.put("key", "value");
   }

}
