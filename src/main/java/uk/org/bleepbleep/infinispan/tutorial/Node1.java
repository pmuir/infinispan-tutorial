package uk.org.bleepbleep.infinispan.tutorial;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import uk.org.bleepbleep.infinispan.tutorial.util.ClusterValidation;

public class Node1 {
   
   public static void main(String[] args) throws Exception {
      GlobalConfiguration gc = GlobalConfiguration.getClusteredDefault();
      Configuration c = new Configuration();
      c.setCacheMode(Configuration.CacheMode.REPL_SYNC);
       
      EmbeddedCacheManager cm = new DefaultCacheManager(gc, c);
      
      Cache<String, String> cache = cm.getCache("Demo");
      
      ClusterValidation.isClusterFormed(cm, 1, 2);
      
      cache.put("key", "value");
   }

}
