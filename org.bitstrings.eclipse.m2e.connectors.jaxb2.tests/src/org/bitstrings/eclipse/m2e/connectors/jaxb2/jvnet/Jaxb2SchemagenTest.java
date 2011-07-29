/*
 *
 *    Copyright (c) 2011 bitstrings.org - Pino Silvaggio
 *
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *
 */
package org.bitstrings.eclipse.m2e.connectors.jaxb2.jvnet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings( "restriction" )
public class Jaxb2SchemagenTest extends AbstractMavenProjectTestCase
{
    public void testSchemagen() throws Exception
    {
        IProject project = importProject("projects/codehaus-schemagen-test/pom.xml");
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertEquals(new Path("/codehaus-schemagen-test/target/generated-resources/schemagen"), cp[3].getPath());

        assertTrue(
                project
                    .getFile("target/generated-resources/schemagen/test/Bean.class")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-resources/schemagen/test/Bean.class")
                        .isAccessible());

        assertTrue(
                project
                    .getFile("target/generated-resources/schemagen/schema1.xsd")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-resources/schemagen/schema1.xsd")
                        .isAccessible());
    }
}
