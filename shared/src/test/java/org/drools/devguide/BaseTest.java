/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.devguide;

import java.util.Collection;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 *
 * @author esteban
 */
public class BaseTest {

    protected KieSession createDefaultSession() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        return kContainer.newKieSession();
    }

    protected KieSession createSession(String name) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        return kContainer.newKieSession(name);
    }

    protected <T> Collection<T> getFactsFromKieSession(KieSession ksession, Class<T> classType) {
        return (Collection<T>) ksession.getObjects(new ClassObjectFilter(classType));
    }

}
