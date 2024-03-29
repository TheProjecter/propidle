package com.googlecode.propidle.util.tinytype;

import java.util.UUID;

public abstract class UuidTinyType<T extends TinyType<UUID,?>> extends TinyType<UUID,T>{
    protected UuidTinyType(UUID value) {
        super(value);
    }
}
