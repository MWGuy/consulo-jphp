package consulo.jphp.jppm;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileType;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * Created by mwguy
 * on 19.08.19.
 */
public class JPPMFileType implements FileType
{
	public static final JPPMFileType INSTANCE = new JPPMFileType();

	@Nonnull
	@Override
	public String getId()
	{
		return "YAML";
	}

	@Nonnull
	@Override
	public String getDescription()
	{
		return "jPHP Package manager file";
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "";
	}

	@Override
	public Image getIcon()
	{
		return AllIcons.Modules.Library;
	}
}
