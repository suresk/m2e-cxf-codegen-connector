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
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;

@SuppressWarnings( "restriction" )
public class Jaxb2GenerationTest extends AbstractMavenProjectTestCase
{
    public void testSchemaJaxb2Version074() throws Exception
    {
        commonSchemaTest("jaxb2-plugin, jaxb2-version-0.7.4");
    }

    public void testSchemaJaxb2Version075() throws Exception
    {
        commonSchemaTest("jaxb2-plugin, jaxb2-version-0.7.5");
    }

    public void testSchemaJaxb2Version080() throws Exception
    {
        commonSchemaTest("jaxb2-plugin, jaxb2-version-0.8.0");
    }

    public void testSchemaJaxb20Version075() throws Exception
    {
        commonSchemaTest("jaxb20-plugin, jaxb2-version-0.7.5");
    }

    public void testSchemaJaxb20Version080() throws Exception
    {
        commonSchemaTest("jaxb20-plugin, jaxb2-version-0.8.0");
    }

    public void testSchemaJaxb21Version075() throws Exception
    {
        commonSchemaTest("jaxb21-plugin, jaxb2-version-0.7.5");
    }

    public void testSchemaJaxb21Version080() throws Exception
    {
        commonSchemaTest("jaxb21-plugin, jaxb2-version-0.8.0");
    }

    public void testSchemaJaxb22Version075() throws Exception
    {
        commonSchemaTest("jaxb22-plugin, jaxb2-version-0.7.5");
    }

    public void testSchemaJaxb22Version080() throws Exception
    {
        commonSchemaTest("jaxb22-plugin, jaxb2-version-0.8.0");
    }

    protected void commonSchemaTest(String profiles) throws Exception
    {
        ResolverConfiguration configuration = new ResolverConfiguration();
        configuration.setActiveProfiles(profiles);
        IProject project = importProject("projects/test/pom.xml", configuration);
        waitForJobsToComplete();

        project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
        project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        waitForJobsToComplete();

        assertNoErrors(project);

        IClasspathEntry[] cp = JavaCore.create(project).getRawClasspath();

        assertEquals(new Path("/test/target/generated-sources/xjc"), cp[4].getPath());

        assertTrue(
                project
                    .getFile("target/generated-sources/xjc/generated/Items.java")
                        .isSynchronized(IResource.DEPTH_ZERO));

        assertTrue(
                project
                    .getFile("target/generated-sources/xjc/generated/Items.java")
                        .isAccessible());
    }
}
