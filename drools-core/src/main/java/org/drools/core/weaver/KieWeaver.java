package org.drools.core.weaver;

import org.drools.core.definitions.impl.ResourceTypePackage;
import org.kie.api.definition.KiePackage;

public interface KieWeaver<P extends ResourceTypePackage> {
    public void weave(KiePackage kiePkg, P rtPkg);
}
