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
package org.bitstrings.eclipse.m2e.connectors.cxf;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.maven_plugin.WsdlOption;
import org.apache.cxf.maven_plugin.WsdlOptionLoader;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.bitstrings.eclipse.m2e.common.BuildHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class CxfWsdl2JavaBuildParticipant extends MojoExecutionBuildParticipant {

	public CxfWsdl2JavaBuildParticipant(MojoExecution execution) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {
		
		final IMaven maven = MavenPlugin.getMaven();
		final BuildContext buildContext = getBuildContext();
		final MavenSession mavenSession = getSession();
		final MojoExecution mojoExecution = getMojoExecution();

		File sourceRoot = maven.getMojoParameterValue(mavenSession,
				mojoExecution, "sourceRoot", File.class);

		boolean filesModified = false;

		if (sourceRoot != null && !sourceRoot.exists()) {
			filesModified = true;
		} else {

			final List wsdlOptions = maven.getMojoParameterValue(mavenSession, mojoExecution, "wsdlOptions", List.class);

			// getMojoParameterValue returns an instance of WsdlOption from a different classloader, so casting doesn't work.
			for (Object obj : wsdlOptions) {
				Class k = obj.getClass();
				Method getWsdl = k.getMethod("getWsdl");
				Method getBindingFiles = k.getMethod("getBindingFiles");
				
				String wsdl = getWsdl.invoke(obj).toString();

				filesModified = (!StringUtils.isEmpty(wsdl) && !ArrayUtils
						.isEmpty(BuildHelper.getModifiedFiles(buildContext,
								new File(wsdl))));

				if (!filesModified) {
					String[] bindingFiles = (String[])getBindingFiles.invoke(obj);

					for (String bindingFile : bindingFiles) {
						filesModified = (!StringUtils.isEmpty(bindingFile) && !ArrayUtils
								.isEmpty(BuildHelper.getModifiedFiles(
										buildContext, new File(bindingFile))));

						if (filesModified) {
							break;
						}
					}
				}

				if (filesModified) {
					break;
				}
			}

		}

		if (!filesModified) {
			return null;
		}

		final Set<IProject> result = super.build(kind, monitor);

		if (sourceRoot != null) {
			buildContext.refresh(sourceRoot);
		}

		return result;
	}
}
