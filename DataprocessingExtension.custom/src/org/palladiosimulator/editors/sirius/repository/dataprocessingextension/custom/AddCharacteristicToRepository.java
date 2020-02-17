package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.CharacteristicServices;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Repository;


public class AddCharacteristicToRepository implements IExternalJavaAction {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	
	
	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for(EObject eObject : arg0){
				return 	(StereotypeAPI.isStereotypeApplicable(eObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) || 
						StereotypeAPI.isStereotypeApplied(eObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE));
		}
		return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		EObject object = (EObject)arg1.get("container");
		Repository repo = Services.getRepo(object);				
		DataSpecification dataSpec = Services.getCorrespondingDataspecification(repo.eAllContents(), repo);
		CharacteristicServices.addCharacteristicToElement(dataSpec, object);
	}
	
	

	

}
