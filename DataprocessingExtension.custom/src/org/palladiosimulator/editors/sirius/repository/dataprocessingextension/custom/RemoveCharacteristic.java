package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

public class RemoveCharacteristic implements IExternalJavaAction {

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		Characteristic characteristic = (Characteristic)arg1.get("element");

		//wenn der container leer ist
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
//		repo ist das gesamtmodel ohne detaflow
		Repository repo = (Repository) semanticDiagram.getTarget();
		for (RepositoryComponent repoComponent : repo.getComponents__Repository()) {
//		repoComponent: basic model an dem dings applied ist
			if(StereotypeAPI.isStereotypeApplied(repoComponent, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE)) {
//				Stereotyped value bekommen
				CharacteristicContainer characContainer = StereotypeAPI.getTaggedValue(repoComponent, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				if(characContainer.getOwnedCharacteristics().contains(characteristic)) {
					if(characContainer.getOwnedCharacteristics().size() > 1) {
						characContainer.getOwnedCharacteristics().remove(characteristic);
					}else {
//						container Dataspecification, daraus löschen
						((DataSpecification) characContainer.eContainer()).getCharacteristicContainer().remove(characContainer);
						StereotypeAPI.unapplyStereotype(repoComponent, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
					}
					return;
				}
			}
		}
	}

}
