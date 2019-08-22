package consulo.jphp.jppm.run;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import consulo.jphp.JPHPIcons;
import consulo.jphp.extension.impl.JphpModuleExtensionImpl;
import consulo.module.extension.ModuleExtensionHelper;

import javax.annotation.Nonnull;

/**
 * Created by mwguy
 * on 22.08.19.
 */
public class JPPMTaskConfigrationType extends ConfigurationTypeBase
{
	public JPPMTaskConfigrationType() {
		super("JPPMTaskConfigrationType", "JPPM Task", "", JPHPIcons.LOGO);

		addFactory(new ConfigurationFactoryEx(this)
		{
			@Override
			public RunConfiguration createTemplateConfiguration(Project project)
			{
				return new JPPMTaskConfiguration(project, this);
			}

			@Override
			public boolean isApplicable(@Nonnull Project project)
			{
				return ModuleExtensionHelper.getInstance(project).hasModuleExtension(JphpModuleExtensionImpl.class);
			}
		});
	}
}
