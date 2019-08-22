package consulo.jphp.extension.impl;

import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import consulo.annotations.RequiredReadAction;
import consulo.module.extension.ModuleInheritableNamedPointer;
import consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.module.extension.impl.LanguageLevelModuleInheritableNamedPointerImpl;
import consulo.php.sdk.PhpSdkType;
import consulo.roots.ModuleRootLayer;
import org.jdom.Element;

import javax.annotation.Nonnull;

/**
 * Created by mwguy
 * on 19.08.19.
 */
public class JphpModuleExtensionImpl extends ModuleExtensionWithSdkImpl<JphpModuleExtensionImpl> implements PhpModuleExtension<JphpModuleExtensionImpl>
{
	public static final String JAVA_SDK_NAME = "java-sdk-name";

	protected LanguageLevelModuleInheritableNamedPointerImpl myLanguageLevel;
	protected String myJavaSdkName;

	public JphpModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer layer)
	{
		super(id, layer);
		myLanguageLevel = new LanguageLevelModuleInheritableNamedPointerImpl(layer, id);
	}

	@RequiredReadAction
	@Override
	public void commit(@Nonnull JphpModuleExtensionImpl mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		myJavaSdkName = mutableModuleExtension.getJavaSdkName();
	}

	@Nonnull
	public ModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel()
	{
		return myLanguageLevel;
	}

	@Override
	@Nonnull
	public PhpLanguageLevel getLanguageLevel()
	{
		return PhpLanguageLevel.PHP_7_3; // Just hard-code it
	}

	@Nonnull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return PhpSdkType.class;
	}

	public void setJavaSdkName(String javaSdkName)
	{
		myJavaSdkName = javaSdkName;
	}

	public String getJavaSdkName()
	{
		return myJavaSdkName;
	}

	@Override
	protected void getStateImpl(@Nonnull Element element)
	{
		super.getStateImpl(element);

		if(myJavaSdkName != null)
		{
			element.setAttribute(JAVA_SDK_NAME, myJavaSdkName);
		}
	}

	@RequiredReadAction
	@Override
	protected void loadStateImpl(@Nonnull Element element)
	{
		super.loadStateImpl(element);

		if(element.getAttribute(JAVA_SDK_NAME) != null)
		{
			myJavaSdkName = element.getAttribute(JAVA_SDK_NAME).getValue();
		}
		else
		{
			ProjectSdksModel sdksModel = new ProjectSdksModel();
			sdksModel.reset();

			myJavaSdkName = sdksModel.getSdks()[sdksModel.getSdks().length - 1].getHomePath();
		}
	}
}