package org.bitstrings.eclipse.m2e.connectors.jaxb2;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;

public class Jaxb2ProjectConfigurator extends AbstractJavaProjectConfigurator
{
    @Override
    protected String getOutputFolderParameterName()
    {
        return "generateDirectory";
    }

    @Override
    public AbstractBuildParticipant getBuildParticipant(
                    IMavenProjectFacade projectFacade,
                    MojoExecution execution,
                    IPluginExecutionMetadata executionMetadata)
    {
        return new Jaxb2BuildParticipant(execution);
    }
}
