package org.drools.compiler.builder.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.drools.compiler.assembler.KieAssemblerContext;
import org.drools.compiler.assembler.KieAssemblerContextImpl;
import org.drools.compiler.assembler.KieAssemblerFactory;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.ClassLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieAssemblerRegistry {

    private static final Logger log = LoggerFactory.getLogger(KieAssemblerRegistry.class);

    public static String ASSEMBLY_FILE_NAME = "kassembly.properties";
    public static String ASSEMBLY_PATH      = "META-INF/" + ASSEMBLY_FILE_NAME;

    private static final KieAssemblerRegistry INSTANCE = new KieAssemblerRegistry();

    private Map<ResourceType, KieAssemblerFactory> registry;

    public static KieAssemblerRegistry getInstance() {
        return INSTANCE;
    }

    private KieAssemblerRegistry() {

    }

    public void register(ResourceType resourceType, KieAssemblerFactory factory) {
        log.info("Registering Kie Assembly name = {}, factory = {}", resourceType.getName(), factory.getClass().getName());
        getRegistry().put(resourceType, factory);
    }

    public KieAssemblerFactory getKieAssemblerFactory(ResourceType resourceType) {
        return getRegistry().get(resourceType);
    }

    private Map<ResourceType, KieAssemblerFactory> getRegistry() {
        if (registry == null) {
            initAssemblers();
        }

        return registry;
    }

    public synchronized void initAssemblers() {
        // ensures only one thread initializes he registry
        if ( registry != null ) {
            return;
        }

        this.registry = new HashMap<ResourceType, KieAssemblerFactory>();

        List<KieAssemblerFactory> list = discoverAssemblers();

        KieAssemblerContext ctx = new KieAssemblerContextImpl(list);
        for ( KieAssemblerFactory factory : list ) {
            factory.preInit(ctx);
        }

        for ( KieAssemblerFactory factory : list ) {
            factory.init(ctx);
        }

        for ( KieAssemblerFactory factory : list ) {
            factory.postInit(ctx);
        }
    }

    public List<KieAssemblerFactory> discoverAssemblers() {


        ClassLoader classLoader = getClassLoader();

        List<KieAssemblerFactory> list = new ArrayList<KieAssemblerFactory>();

        final Enumeration<URL> e;
        try {
            e = classLoader.getResources( ASSEMBLY_PATH );
        } catch ( IOException exc ) {
            log.error( "Unable to find and build assembly of {}\n {} ", ASSEMBLY_PATH, exc.getMessage() );
            return list;
        }

        // Map of kmodule urls
        while ( e.hasMoreElements() ) {
            URL url = e.nextElement();
            try {
                Properties properties = new Properties();

                java.io.InputStream is = url.openStream();
                properties.load( is );
                is.close();

                String factoryName = (String) properties.get( "factory" );
                KieAssemblerFactory factory = (KieAssemblerFactory) Class.forName(factoryName, true, classLoader).newInstance();
                list.add( factory );
                register(factory.getResourceType(), factory);
            } catch ( Exception exc ) {
                log.error( "Unable to build assembly url={}\n", url.toExternalForm(), exc.getMessage() );
            }
        }

        log.info( "{} Kie Assemblies discovered {}", list.size(), list.toString() );

        return list;

    }

    public ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        if (cl == null) {
            cl = ClassLoaderUtil.class.getClassLoader();
        }
        return cl;
    }
}
