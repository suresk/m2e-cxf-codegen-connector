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
package org.bitstrings.eclipse.m2e.connectors.jaxb2;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings( "restriction" )
public class CodehausJaxb2GenerationTest extends AbstractMavenProjectTestCase
{
    public void testSchema() throws Exception
    {
        IProject project = importProject("projects/codehaus-test/pom.xml");
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertEquals(new Path("/codehaus-test/target/generated-sources/jaxb"), cp[3].getPath());

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/jaxb/generated/Items.java")
                        .isAccessible());
    }
}
