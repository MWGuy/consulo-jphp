package consulo.jphp.jppm.run;

import com.intellij.compiler.options.CompileStepBeforeRun;
import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import consulo.annotations.RequiredReadAction;
import consulo.container.plugin.PluginManager;
import consulo.jphp.extension.impl.JphpModuleExtensionImpl;
import consulo.php.module.extension.PhpModuleExtension;
import org.jdom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

/**
 * Created by mwguy
 * on 22.08.19.
 */
public class JPPMTaskConfiguration extends ModuleBasedConfiguration<RunConfigurationModule> implements CommonProgramRunConfigurationParameters, CompileStepBeforeRun.Suppressor
{
	public String PACKAGE_PATH;
	public String JPPM_COMMANDLINE;
	public String WORKING_DIRECTORY;

	public Map<String, String> ENVS = new HashMap<>();
	public boolean PASS_PARENT_ENVS = true;

	public JPPMTaskConfiguration(Project project, ConfigurationFactory factory)
	{
		super(new RunConfigurationModule(project), factory);
	}

	@Override
	@RequiredReadAction
	public Collection<Module> getValidModules()
	{
		List<Module> result = new ArrayList<>();
		Module[] modules = ModuleManager.getInstance(getProject()).getModules();
		for(Module module : modules)
		{
			PhpModuleExtension<?> moduleExtension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);

			if(moduleExtension != null && moduleExtension instanceof JphpModuleExtensionImpl)
			{
				result.add(module);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new JPPMTaskConfigurationEditor(getProject());
	}

	@Override
	public void readExternal(Element element)
	{
		super.readExternal(element);

		if(element.getAttribute("package-path") != null)
		{
			PACKAGE_PATH = element.getAttribute("package-path").getValue();
		}

		if(element.getAttribute("jppm-commandline") != null)
		{
			JPPM_COMMANDLINE = element.getAttribute("jppm-commandline").getValue();
		}

		if(element.getAttribute("directory-path") != null)
		{
			WORKING_DIRECTORY = element.getAttribute("directory-path").getValue();
		}
	}

	@Override
	public void writeExternal(Element element)
	{
		super.writeExternal(element);

		if(PACKAGE_PATH != null)
		{
			element.setAttribute("package-path", PACKAGE_PATH);

			File packageFile = new File(PACKAGE_PATH);
			element.setAttribute("directory-path", packageFile.getParent());
		}

		if(JPPM_COMMANDLINE != null)
		{
			element.setAttribute("jppm-commandline", JPPM_COMMANDLINE);
		}
	}

	@Nullable
	@Override
	public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment executionEnvironment) throws ExecutionException
	{
		return new RunProfileState()
		{
			@Nullable
			@Override
			public ExecutionResult execute(Executor executor, @Nonnull ProgramRunner programRunner) throws ExecutionException
			{
				JPPMTaskConfiguration conf = (JPPMTaskConfiguration) executionEnvironment.getRunProfile();

				File pluginPath = PluginManager.getPluginPath(JPPMTaskConfiguration.class);
				String executableFile = "";

				if(SystemInfo.isWindows)
				{
					executableFile = (new File(pluginPath, "/bundles/jppm/jppm.bat")).getAbsolutePath();
				}
				else
				{
					File file = new File(pluginPath, "/bundles/jppm/jppm");
					file.setExecutable(true);

					executableFile = file.getAbsolutePath();
				}

				Map<String, String> env = conf.getEnvs();
				// TODO: make custom JDKs

				GeneralCommandLine commandLine = new GeneralCommandLine();
				commandLine.withExePath(FileUtil.toSystemDependentName(executableFile));
				commandLine.withWorkDirectory(StringUtil.nullize(conf.getWorkingDirectory()));
				commandLine.withEnvironment(env);
				List<String> args = new ArrayList<>();
				args.addAll(StringUtil.split(StringUtil.notNullize(conf.getProgramParameters()), " "));
				commandLine.withParameters(args);
				commandLine.withParentEnvironmentType(conf.isPassParentEnvs() ? GeneralCommandLine.ParentEnvironmentType.SYSTEM : GeneralCommandLine.ParentEnvironmentType.NONE);

				TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(executionEnvironment.getProject());
				ConsoleView console = consoleBuilder.getConsole();
				OSProcessHandler processHandler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);
				console.attachToProcess(processHandler);
				return new DefaultExecutionResult(console, processHandler);
			}
		};
	}

	@Override
	public void setProgramParameters(@Nullable String s)
	{
		JPPM_COMMANDLINE = s;
	}

	@Nullable
	@Override
	public String getProgramParameters()
	{
		return JPPM_COMMANDLINE;
	}

	@Override
	public void setWorkingDirectory(@Nullable String s)
	{
		WORKING_DIRECTORY = s;
	}

	@Nullable
	@Override
	public String getWorkingDirectory()
	{
		return WORKING_DIRECTORY;
	}

	@Override
	public void setEnvs(@Nonnull Map<String, String> map)
	{
		ENVS = map;
	}

	@Nonnull
	@Override
	public Map<String, String> getEnvs()
	{
		return ENVS;
	}

	@Override
	public void setPassParentEnvs(boolean b)
	{
		PASS_PARENT_ENVS = b;
	}

	@Override
	public boolean isPassParentEnvs()
	{
		return PASS_PARENT_ENVS;
	}
}
