package models;

import java.util.Map;

import net.sf.ehcache.store.chm.ConcurrentHashMap;
import play.libs.F.ArchivedEventStream;
import play.libs.F.EventStream;

public class LiveBidding {

    private static final ConcurrentHashMap<Long, ArchivedEventStream<String>> bidEvents = new ConcurrentHashMap<Long, ArchivedEventStream<String>>();

    public static ArchivedEventStream<String> getEventStream(Long id) {
        bidEvents.putIfAbsent(id, new ArchivedEventStream<String>(10));
        return bidEvents.get(id);
    }
}
