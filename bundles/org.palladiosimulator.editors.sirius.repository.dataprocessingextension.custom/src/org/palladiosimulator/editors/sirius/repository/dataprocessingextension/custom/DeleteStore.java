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
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.Store;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.StoreContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

public class DeleteStore implements IExternalJavaAction {

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		Store store = (Store) arg1.get("element");
		
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		Repository repo = (Repository) semanticDiagram.getTarget();
		for (RepositoryComponent repoComponent : repo.getComponents__Repository()) {
			if(StereotypeAPI.isStereotypeApplied(repoComponent, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)) {
				StoreContainer storeContainer = StereotypeAPI.getTaggedValue(repoComponent, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
				if(storeContainer.getStores().contains(store)) {
					if(storeContainer.getStores().size()>1) {
						storeContainer.getStores().remove(store);
					} else {
						((DataSpecification) storeContainer.eContainer()).getStoreContainers().remove(storeContainer);
						StereotypeAPI.unapplyStereotype(repoComponent, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
					}
					
				}
			}
		}
	}

}
