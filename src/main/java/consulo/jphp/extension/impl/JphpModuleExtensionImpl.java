package consulo.jphp.extension.impl;

import com.intellij.openapi.projectRoots.SdkType;
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
	protected LanguageLevelModuleInheritableNamedPointerImpl myLanguageLevel;

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

	@Override
	protected void getStateImpl(@Nonnull Element element)
	{
		super.getStateImpl(element);
	}

	@RequiredReadAction
	@Override
	protected void loadStateImpl(@Nonnull Element element)
	{
		super.loadStateImpl(element);
	}
}