package com.tuan.exercise.sprdict.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "type_detail")
public class TransTypeDetail {
    public TransTypeDetail() {
        super();
    }

    @Id
    @Column(name = "trans_type")
    private int type;

    @Column(name = "type_value")
    private String typeValue;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    @Override
    public String toString() {
        return "TransTypeDetail [type=" + type + ", typeValue=" + typeValue + "]";
    }
}
