package com.javahotel.db.hotelbase.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;

@MappedSuperclass
public abstract class AbstractIID implements IId {
	
    @Id
    @GeneratedValue
    private Long id;
            
    public HId getId() {
        return new HId(id);
    }

    public void setId(HId id) {
        this.id = id.getL();
    }

}
