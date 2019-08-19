package consulo.jphp.jppm;

import com.intellij.openapi.fileTypes.ExactFileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

import javax.annotation.Nonnull;

/**
 * Created by
 * mwguy on 19.08.19.
 */
public class JPPMFileTypeFactory extends FileTypeFactory
{
	public static final String PACKAGE_YAML = "package.php.yml";
	public static final String PACKAGE_LOCK = "package-lock.php.yml";

	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer)
	{
		fileTypeConsumer.consume(JPPMFileType.INSTANCE, new ExactFileNameMatcher(PACKAGE_YAML));
		fileTypeConsumer.consume(JPPMFileType.INSTANCE, new ExactFileNameMatcher(PACKAGE_LOCK));
	}
}
