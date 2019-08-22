package consulo.jphp.extension.impl;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.projectRoots.JavaSdk;
import consulo.roots.ui.configuration.SdkComboBox;
import consulo.ui.RequiredUIAccess;

import javax.swing.*;
import java.util.Objects;

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

		ProjectSdksModel sdksModel = new ProjectSdksModel();
		sdksModel.reset();

		SdkComboBox box = new SdkComboBox(sdksModel, Conditions.instanceOf(JavaSdk.class), false);

		if(mutableModuleExtension.getJavaHome() != null)
		{
			for(Sdk sdk : sdksModel.getSdks())
			{
				if(Objects.equals(sdk.getHomePath(), mutableModuleExtension.getJavaHome()))
				{
					box.setSelectedSdk(sdk);
					break;
				}

			}
		}

		box.addActionListener(e -> {
			mutableModuleExtension.setJavaHomePath(Objects.requireNonNull(box.getSelectedSdk()).getHomePath());
		});

		add(LabeledComponent.create(box, "SDK"));
	}
}
