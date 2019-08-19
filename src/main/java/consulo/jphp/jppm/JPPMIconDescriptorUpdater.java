package consulo.jphp.jppm;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import consulo.annotations.RequiredReadAction;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;
import org.jetbrains.yaml.psi.YAMLFile;

import javax.annotation.Nonnull;

/**
 * Created by mwguy
 * on 19.08.19.
 */
public class JPPMIconDescriptorUpdater implements IconDescriptorUpdater
{
	@RequiredReadAction
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement psiElement, int i)
	{
		if(psiElement instanceof YAMLFile) {
			final VirtualFile vf = ((YAMLFile) psiElement).getVirtualFile();
			assert vf != null;

			if(vf.getName().equals(JPPMFileTypeFactory.PACKAGE_YAML)
			|| vf.getName().equals(JPPMFileTypeFactory.PACKAGE_LOCK)) {
				iconDescriptor.setMainIcon(JPPMFileType.INSTANCE.getIcon());
			}
		}
	}
}
