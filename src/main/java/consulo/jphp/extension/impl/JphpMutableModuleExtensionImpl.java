package consulo.jphp.extension.impl;

import com.intellij.openapi.projectRoots.Sdk;
import consulo.jphp.extension.JphpMutableModuleExtension;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.php.PhpLanguageLevel;
import consulo.roots.ModuleRootLayer;
import consulo.ui.RequiredUIAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

/**
 * Created by mwguy
 * on 19.08.19.
 */
public class JphpMutableModuleExtensionImpl extends JphpModuleExtensionImpl implements JphpMutableModuleExtension
{
	public JphpMutableModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Override
	@Nonnull
	public MutableModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel()
	{
		return myLanguageLevel;
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		return new JphpModuleExtensionPanel(this, runnable);
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@Nonnull JphpModuleExtensionImpl extension)
	{
		return isModifiedImpl(extension) || !extension.getInheritableLanguageLevel().equals(getInheritableLanguageLevel()) || !(extension.getJavaSdkName() == getJavaSdkName());
	}
}
