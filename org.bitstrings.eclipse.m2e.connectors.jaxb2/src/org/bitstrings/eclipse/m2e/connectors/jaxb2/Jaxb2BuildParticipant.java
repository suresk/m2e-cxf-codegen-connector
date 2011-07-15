package org.bitstrings.eclipse.m2e.connectors.jaxb2;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class Jaxb2BuildParticipant extends MojoExecutionBuildParticipant
{
    public Jaxb2BuildParticipant(MojoExecution execution)
    {
        super(execution, true);
    }

    @Override
    public Set<IProject> build(int kind, IProgressMonitor monitor) throws Exception
    {
        final IMaven maven = MavenPlugin.getMaven();
        final BuildContext buildContext = getBuildContext();

        File source = maven.getMojoParameterValue(getSession(), getMojoExecution(), "schemaDirectory", File.class);

        String[] modifiedSchemaFiles;
        if (source != null)
        {
            Scanner ds = buildContext.newScanner(source);

            String[] schemaIncludes =
                    maven.getMojoParameterValue(
                            getSession(),
                            getMojoExecution(),
                            "schemaIncludes",
                            String[].class);

            if ((schemaIncludes) != null && (schemaIncludes.length > 0))
            {
                ds.setIncludes(schemaIncludes);
            }

            String[] schemaExcludes =
                    maven.getMojoParameterValue(
                            getSession(),
                            getMojoExecution(),
                            "schemaExcludes",
                            String[].class);

            if ((schemaExcludes) != null && (schemaExcludes.length > 0))
            {
                ds.setExcludes(schemaExcludes);
            }

            ds.scan();
            modifiedSchemaFiles = ds.getIncludedFiles();
        }
        else
        {
            modifiedSchemaFiles = null;
        }

        source = maven.getMojoParameterValue(getSession(), getMojoExecution(), "bindingDirectory", File.class);

        String[] modifiedBindingFiles;
        if (source != null)
        {
            Scanner ds = buildContext.newScanner(source);

            String[] schemaIncludes =
                    maven.getMojoParameterValue(
                            getSession(),
                            getMojoExecution(),
                            "bindingIncludes",
                            String[].class);

            if (schemaIncludes != null && schemaIncludes.length > 0)
            {
                ds.setIncludes(schemaIncludes);
            }

            String[] schemaExcludes =
                    maven.getMojoParameterValue(
                            getSession(),
                            getMojoExecution(),
                            "bindingExcludes",
                            String[].class);

            ds.setExcludes(schemaExcludes);

            ds.scan();
            modifiedBindingFiles = ds.getIncludedFiles();
        }
        else
        {
            modifiedBindingFiles = null;
        }

        if (((modifiedSchemaFiles == null) || (modifiedSchemaFiles.length == 0))
                        && ((source == null) || (modifiedBindingFiles == null) || (modifiedBindingFiles.length == 0)))
        {
            return null;
        }

        Set<IProject> result = super.build(kind, monitor);

        File generated =
                maven.getMojoParameterValue(
                        getSession(),
                        getMojoExecution(),
                        "generateDirectory",
                        File.class);
        if (generated != null)
        {
            buildContext.refresh(generated);
        }

        return result;
    }
}
