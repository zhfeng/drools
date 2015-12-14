/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.drools.compiler.integrationtests;

import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FromWithoutFactHandleTest {

    @Test
    public void testAlphaFrom() {
        String drl =
                "import " + FromWithoutFactHandleTest.class.getCanonicalName() + ";\n" +
                "global java.util.List list;\n" +
                "rule R1 when\n" +
                "    $s : String( this str[startsWith] \"t\" ) from FromWithoutFactHandleTest.getStrings()\n" +
                "then \n" +
                "    list.add($s);\n" +
                "end\n";

        KieSession ksession = new KieHelper().addContent( drl, ResourceType.DRL )
                                             .build()
                                             .newKieSession();

        List<String> list = new ArrayList<String>();
        ksession.setGlobal( "list", list );
        ksession.fireAllRules();

        System.out.println(list);
        for (String s : list) {
            assertTrue( s.startsWith( "t" ) );
        }
    }

    @Test
    public void testBetaFrom() {
        String drl =
                "import " + FromWithoutFactHandleTest.class.getCanonicalName() + ";\n" +
                "global java.util.List list;\n" +
                "rule R1 when\n" +
                "    $start : String()" +
                "    $s : String( this str[startsWith] $start ) from FromWithoutFactHandleTest.getStrings()\n" +
                "then \n" +
                "    list.add($s);\n" +
                "end\n";

        KieSession ksession = new KieHelper().addContent( drl, ResourceType.DRL )
                                             .build()
                                             .newKieSession();

        List<String> list = new ArrayList<String>();
        ksession.setGlobal( "list", list );
        ksession.insert( "t" );
        ksession.fireAllRules();

        System.out.println(list);
        for (String s : list) {
            assertTrue( s.startsWith( "t" ) );
        }
    }

    public static List<String> getStrings() {
        return Arrays.asList( "one", "two", "three" );
    }
}
