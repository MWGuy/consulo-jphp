package consulo.jphp.jppm;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.ExactFileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import consulo.ui.image.Image;
import org.jetbrains.yaml.YAMLFileType;

import javax.annotation.Nonnull;

/**
 * Created by
 * mwguy on 19.08.19.
 */
public class JPPMFileTypeFactory extends FileTypeFactory
{
	public static final String PACKAGE_YAML = "package.php.yml";
	public static final String PACKAGE_LOCK = "package-lock.php.yml";

	public static final Image JPPM_ICON = AllIcons.Modules.Library;

	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer)
	{
		fileTypeConsumer.consume(YAMLFileType.YML, new ExactFileNameMatcher(PACKAGE_YAML));
		fileTypeConsumer.consume(YAMLFileType.YML, new ExactFileNameMatcher(PACKAGE_LOCK));
	}
}
