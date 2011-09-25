package com.javahotel.db.context;

import java.util.List;
import java.util.Set;

import com.javahotel.db.hotelbase.jpa.ParamRegistry;
import com.javahotel.db.jtypes.IId;

public interface IPersistCache {

    void clear();

    void put(Class<?> cla, String name, IId id, boolean persist);

    IId get(Class<?> cla, String name);

    void put(Class<?> parent_cla, String parentname, Class<?> cla, String name,
            IId id, boolean persist);

    IId get(Class<?> parent_cla, String parentname, Class<?> cla, String name);

    void persistRecords(ICommandContext iC);
    
    // used only for getting res object status
    // keeps set of services connected with booking
    
    /**
     * Get set of booking services
     * @return Set of services (strings)
     */
    Set<String> getBookingServices();
    
    /**
     * Set set of booking services
     * @param set Set of strings (booking services)
     */
    void setBookingServices(Set<String> set);

    // solution specific to Google App Engine
    // to avoid reading the list in one transaction
    // and refreshing the content    
    
    /**
     * Get the list stored in the cache
     * @return The list or null if not stored already
     */
    List<ParamRegistry> getCacheList();

    /**
     * Set the list into the cache
     * @param li List to be stored in the cache
     */
    void setCacheList(List<ParamRegistry> li);

}
