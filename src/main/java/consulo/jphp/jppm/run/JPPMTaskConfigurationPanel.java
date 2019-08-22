package consulo.jphp.jppm.run;

import com.intellij.application.options.ModuleListCellRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import consulo.jphp.extension.impl.JphpModuleExtensionImpl;

import javax.swing.*;

/**
 * Created by mwguy
 * on 22.08.19.
 */
public class JPPMTaskConfigurationPanel extends JPanel
{
	private LabeledComponent<ComboBox> myModule;
	private LabeledComponent<JTextField> myComamndline;

	public JPPMTaskConfigurationPanel(Project project) {
		super(new VerticalFlowLayout());

		myModule = LabeledComponent.create(new ComboBox(), "Module");
		myComamndline = LabeledComponent.create(new JTextField(), "Comandline");

		myModule.getComponent().setRenderer(new ModuleListCellRenderer());

		add(myModule);
		add(myComamndline);
	}

	public void reset(JPPMTaskConfiguration configuration)
	{
		for(Module module : ModuleManager.getInstance(configuration.getProject()).getModules())
		{
			JphpModuleExtensionImpl extension = ModuleUtilCore.getExtension(module, JphpModuleExtensionImpl.class);
			if(extension != null)
			{
				myModule.getComponent().addItem(module);
			}
		}

		myComamndline.getComponent().setText(configuration.getProgramParameters());
	}

	public void applyTo(JPPMTaskConfiguration configuration)
	{
		configuration.setProgramParameters(myComamndline.getComponent().getText());

		Module module = (Module) myModule.getComponent().getSelectedItem();
		configuration.setModule(module);
	}
}
