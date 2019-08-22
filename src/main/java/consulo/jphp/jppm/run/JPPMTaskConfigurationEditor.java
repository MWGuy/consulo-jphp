package consulo.jphp.jppm.run;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * Created by mwguy
 * on 22.08.19.
 */
public class JPPMTaskConfigurationEditor extends SettingsEditor<JPPMTaskConfiguration>
{
	private Project myProject;
	private JPPMTaskConfigurationPanel myPanel;

	public JPPMTaskConfigurationEditor(Project project)
	{
		myProject = project;
	}

	@Nonnull
	@Override
	protected JComponent createEditor()
	{
		return myPanel = new JPPMTaskConfigurationPanel(myProject);
	}

	@Override
	protected void resetEditorFrom(JPPMTaskConfiguration configuration)
	{
		myPanel.reset(configuration);
	}

	@Override
	protected void applyEditorTo(JPPMTaskConfiguration configuration) throws ConfigurationException
	{
		myPanel.applyTo(configuration);
	}
}
