package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.DataSelectionFilter;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.DataSpecificationServices;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.DataPackage;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.impl.ProcessingFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;

public class AddDataProcessingContainerToSeff implements IExternalJavaAction, DataSelectionFilter {

	public AddDataProcessingContainerToSeff() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for (EObject eObject : arg0) {
			for (EObject eObject2 : ((ResourceDemandingSEFF) eObject).getSteps_Behaviour()) {
				if(!(eObject2 instanceof StartAction || eObject2 instanceof StopAction))
					if(StereotypeAPI.isStereotypeApplicable(eObject2, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
						return true;
				}
			}
		}
		return  false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) arg1.get("container");
		DataSpecification dataSpec = Services.getCorrespondingDataspecification(seff.eAllContents(), seff);
		
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(ResourceDemandingSEFF.class);
		filterList.add(AbstractAction.class);
		filterList.add(BasicComponent.class);
		filterList.add(Repository.class);
		
		AbstractAction aAction = DataSpecificationServices.getCharacteristic(seff, filterList, AbstractAction.class, this);
		
		DataProcessingContainer dpContainer = ProcessingFactoryImpl.init().createDataProcessingContainer();

		dataSpec.getDataProcessingContainers().add(dpContainer);
		StereotypeAPI.applyStereotype(aAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING);
		StereotypeAPI.setTaggedValue(aAction, dpContainer, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER);
		
		dpContainer.setEntityName("Seff_"+aAction.getEntityName());
		
	}

	@Override
	public void filterDialog(PalladioSelectEObjectDialog dialog, EObject resource) {
		//filter other models
		filterOtherModels(dialog, resource);
		//filter unapplyable Elements
		removeUnapplyableElements(dialog);
	}
	
	
	private void filterOtherModels(PalladioSelectEObjectDialog dialog, EObject resource) {
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, Repository.class, Services.getParentOfType(resource, Repository.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, BasicComponent.class, Services.getParentOfType(resource, BasicComponent.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, ResourceDemandingSEFF.class, ((ResourceDemandingSEFF)resource));
	}
	
	public static void removeUnapplyableElements(PalladioSelectEObjectDialog dialog) {
		 for (Object o : dialog.getTreeViewer().getExpandedElements()) {
			 if(o instanceof ResourceDemandingSEFF) {
				for(AbstractAction aa : ((ResourceDemandingSEFF) o).getSteps_Behaviour()) {
					if((aa instanceof StartAction || aa instanceof StopAction)) {
						dialog.getTreeViewer().remove(aa);
					}else if(!StereotypeAPI.isStereotypeApplicable((EObject)aa, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
						dialog.getTreeViewer().remove(aa);
					}
				}
			}
		}  
	
	}

}
