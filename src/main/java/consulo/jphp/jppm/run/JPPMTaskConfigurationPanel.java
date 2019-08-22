package consulo.jphp.jppm.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;
import consulo.jphp.jppm.JPPMFileTypeFactory;
import org.jetbrains.yaml.YAMLFileType;

import javax.swing.*;
import java.io.File;

/**
 * Created by mwguy
 * on 22.08.19.
 */
public class JPPMTaskConfigurationPanel extends JPanel
{
	private LabeledComponent<TextFieldWithBrowseButton> myPackageFilePath;
	private LabeledComponent<JTextField> myComamndline;

	public JPPMTaskConfigurationPanel(Project project) {
		super(new VerticalFlowLayout());

		TextFieldWithBrowseButton component = new TextFieldWithBrowseButton();
		component.addBrowseFolderListener(new TextBrowseFolderListener(
				FileChooserDescriptorFactory
						.createSingleLocalFileDescriptor()
						.withFileFilter(virtualFile ->
							virtualFile.getName().equals(JPPMFileTypeFactory.PACKAGE_YAML))
						.withTitle("JPPM Project")
						.withHideIgnored(true), project));
		myPackageFilePath = LabeledComponent.create(component, "Package path");
		myComamndline = LabeledComponent.create(new JTextField(), "Comandline");

		add(myPackageFilePath);
		add(myComamndline);
	}

	public void reset(JPPMTaskConfiguration configuration)
	{
		myPackageFilePath.getComponent().setText(configuration.PACKAGE_PATH);
		myComamndline.getComponent().setText(configuration.getProgramParameters());
	}

	public void applyTo(JPPMTaskConfiguration configuration)
	{
		File packageFile = new File(
				configuration.PACKAGE_PATH = myPackageFilePath.getComponent().getText());
		configuration.setWorkingDirectory(packageFile.getParent());
		configuration.setProgramParameters(myComamndline.getComponent().getText());
	}
}
