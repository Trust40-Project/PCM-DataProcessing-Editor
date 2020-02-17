package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.CharacteristicServices;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class AddCharacteristicToResourceEnvironment implements IExternalJavaAction {

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for (EObject eObject : arg0) {
			return (StereotypeAPI.isStereotypeApplicable(eObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) ||
					StereotypeAPI.isStereotypeApplied(eObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE));
		}
		return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		EObject container = (EObject)arg1.get("container");
		ResourceEnvironment resEnv = Services.getParentOfType((EObject)arg1.get("container"), ResourceEnvironment.class);
		DataSpecification dataSpec = Services.getCorrespondingDataspecification(resEnv.eAllContents(), resEnv);
		
		CharacteristicServices.addCharacteristicToElement(dataSpec, container);
		
	}

}
