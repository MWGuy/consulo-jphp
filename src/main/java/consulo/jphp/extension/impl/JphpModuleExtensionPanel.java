package consulo.jphp.extension.impl;

import com.intellij.openapi.ui.VerticalFlowLayout;
import consulo.ui.RequiredUIAccess;

import javax.swing.*;

/**
 * Created by mwguy
 * on 19.08.19.
 */
public class JphpModuleExtensionPanel extends JPanel
{
	@RequiredUIAccess
	public JphpModuleExtensionPanel(JphpMutableModuleExtensionImpl mutableModuleExtension, Runnable runnable)
	{
		super(new VerticalFlowLayout());

		// TODO: make module extension panel for jPHP
	}
}
