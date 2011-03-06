package com.googlecode.propidle.persistence;

public interface Transaction {
    void commit();
    void rollback();
}
