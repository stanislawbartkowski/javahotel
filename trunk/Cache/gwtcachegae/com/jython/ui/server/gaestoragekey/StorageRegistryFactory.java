package com.jython.ui.server.gaestoragekey;

import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.registry.IStorageRegistryFactory;

public class StorageRegistryFactory implements IStorageRegistryFactory {

    @Override
    public IStorageRegistry construct(String realm) {
        return new GaeStorageRegistry(realm);
    }

}