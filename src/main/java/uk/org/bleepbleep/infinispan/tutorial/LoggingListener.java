package uk.org.bleepbleep.infinispan.tutorial;

import java.util.logging.Logger;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

@Listener
public class LoggingListener {

   private transient Logger log = Logger.getLogger("tutorial");
   
   @CacheEntryCreated
   public void observeAdd(CacheEntryCreatedEvent<?, ?> event) {
      log.info("Cache entry with key " + event.getKey() + " added in cache " + event.getCache());
   }
   
   @CacheEntryRemoved
   public void observeRemove(CacheEntryRemovedEvent<?, ?> event) {
      log.info("Cache entry with key " + event.getKey() + " removed in cache " + event.getCache());
   }
   
}
