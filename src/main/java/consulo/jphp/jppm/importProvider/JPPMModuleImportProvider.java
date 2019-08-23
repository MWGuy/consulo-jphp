package consulo.jphp.jppm.importProvider;

import com.intellij.ide.util.newProjectWizard.ProjectNameStep;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import consulo.jphp.JPHPIcons;
import consulo.jphp.extension.JphpMutableModuleExtension;
import consulo.jphp.jppm.JPPMFileTypeFactory;
import consulo.logging.Logger;
import consulo.moduleImport.ModuleImportContext;
import consulo.moduleImport.ModuleImportProvider;
import consulo.roots.impl.ProductionContentFolderTypeProvider;
import consulo.ui.image.Image;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mwguy
 * on 19.08.19.
 */
public class JPPMModuleImportProvider implements ModuleImportProvider<ModuleImportContext>
{
	private final Logger log = Logger.getInstance(getClass());

	@Nonnull
	@Override
	public String getName()
	{
		return "jPHP Package manager";
	}

	@Nonnull
	@Override
	public String getFileSample()
	{
		return "<b>JPPM</b> project";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return JPHPIcons.LOGO;
	}

	@Override
	public boolean canImport(@Nonnull File file)
	{
		return new File(file, JPPMFileTypeFactory.PACKAGE_YAML).exists();
	}


	@Override
	public ModuleWizardStep[] createSteps(@Nonnull WizardContext context, @Nonnull ModuleImportContext moduleImportContext)
	{
		return new ModuleWizardStep[]{new ProjectNameStep(context)};
	}

	@Nonnull
	@Override
	public List<Module> commit(@Nonnull ModuleImportContext moduleImportContext,
							   @Nonnull Project project,
							   @Nullable ModifiableModuleModel modifiableModuleModel,
							   @Nonnull ModulesProvider modulesProvider,
							   @Nullable ModifiableArtifactModel modifiableArtifactModel)
	{
		ModifiableModuleModel targetModuleModel = modifiableModuleModel == null ? ModuleManager.getInstance(project).getModifiableModel() : modifiableModuleModel;
		List<Module> modules = new ArrayList<>();

		String fileToImport = moduleImportContext.getFileToImport();
		File targetDirectory = new File(fileToImport);
		File packageFile = new File(targetDirectory, JPPMFileTypeFactory.PACKAGE_YAML);
		VirtualFile targetVFile = LocalFileSystem.getInstance().findFileByIoFile(targetDirectory);
		VirtualFile packageVFile = LocalFileSystem.getInstance().findFileByIoFile(packageFile);

		assert targetVFile != null;

		Module rootModule = targetModuleModel.newModule(targetDirectory.getName(), targetDirectory.getPath());
		modules.add(rootModule);

		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(rootModule);
		ModifiableRootModel modifiableModel = moduleRootManager.getModifiableModel();

		ContentEntry contentEntry = modifiableModel.addContentEntry(targetVFile);

		JphpMutableModuleExtension phpModuleExtension = modifiableModel.getExtensionWithoutCheck(JphpMutableModuleExtension.class);
		assert phpModuleExtension != null;
		phpModuleExtension.setEnabled(true);

		modifiableModel.addModuleExtensionSdkEntry(phpModuleExtension);

		try (InputStream in = packageVFile.getInputStream())
		{
			Yaml yaml = new Yaml();
			Map<String, Object> packageMap = yaml.load(in);

			if(packageMap.containsKey("sources"))
			{
				List<String> sourcesFromPackage = (List<String>) packageMap.get("sources");

				for(String source : sourcesFromPackage)
				{
					contentEntry.addFolder(targetVFile.getUrl() + "/" + source, ProductionContentFolderTypeProvider.getInstance());
				}
			}
		}
		catch(IOException e)
		{
			log.error(e);
		}

		WriteAction.run(modifiableModel::commit);

		if(modifiableModuleModel == null)
		{
			WriteAction.run(targetModuleModel::commit);
		}

		return modules;
	}
}
