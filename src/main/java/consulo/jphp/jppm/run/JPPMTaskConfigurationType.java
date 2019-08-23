package consulo.jphp.jppm.run;

import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import consulo.jphp.JPHPIcons;
import consulo.jphp.extension.impl.JphpModuleExtensionImpl;
import consulo.module.extension.ModuleExtensionHelper;

import javax.annotation.Nonnull;

/**
 * Created by mwguy
 * on 22.08.19.
 */
public class JPPMTaskConfigurationType extends ConfigurationTypeBase
{
	public JPPMTaskConfigurationType() {
		super("JPPMTaskConfigurationType", "JPPM Task", "", JPHPIcons.LOGO);

		addFactory(new ConfigurationFactoryEx(this)
		{
			@Override
			public RunConfiguration createTemplateConfiguration(Project project)
			{
				return new JPPMTaskConfiguration(project, this);
			}

			@Override
			public void onNewConfigurationCreated(@Nonnull RunConfiguration configuration)
			{
				JPPMTaskConfiguration conf = (JPPMTaskConfiguration) configuration;

				for(Module module : ModuleManager.getInstance(configuration.getProject()).getModules())
				{
					JphpModuleExtensionImpl extension = ModuleUtilCore.getExtension(module, JphpModuleExtensionImpl.class);
					if(extension != null)
					{
						conf.setModule(module);
						break;
					}
				}
			}

			@Override
			public boolean isApplicable(@Nonnull Project project)
			{
				return ModuleExtensionHelper.getInstance(project).hasModuleExtension(JphpModuleExtensionImpl.class);
			}
		});
	}
}
