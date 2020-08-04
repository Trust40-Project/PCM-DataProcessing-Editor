package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.AddDataOperationService;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.DataopSelectionFilter;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.ProcessingPackage;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.impl.ProcessingFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;

public class AddDataOperationToSeff implements IExternalJavaAction, DataopSelectionFilter {
	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}
		
	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		DataProcessingContainer dpContainer = (DataProcessingContainer) arg1.get("container");
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) semanticDiagram.getTarget();
		
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(ResourceDemandingSEFF.class);
		filterList.add(AbstractAction.class);
		filterList.add(BasicComponent.class);
		filterList.add(Repository.class);
		filterList.add(EClass.class);
		
		AddDataOperationService.selectClassifier(seff, dpContainer, filterList, "ResourceDemandingSEFF", (DataopSelectionFilter) this);
	}
	
	
	
	public void filterOtherModels(PalladioSelectEObjectDialog dialog, EObject resource, DataProcessingContainer dpContainer) {
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, Repository.class, Services.getParentOfType(resource, Repository.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, BasicComponent.class, Services.getParentOfType(resource, BasicComponent.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, ResourceDemandingSEFF.class, ((ResourceDemandingSEFF)resource));
	}
	
	
	public void removeUnapplyableElements(PalladioSelectEObjectDialog dialog, DataProcessingContainer dpContainer) {
		 for (Object o : dialog.getTreeViewer().getExpandedElements()) {
			 if(o instanceof ResourceDemandingSEFF) {
				for(AbstractAction aa : ((ResourceDemandingSEFF) o).getSteps_Behaviour()) {
					if((aa instanceof StartAction || aa instanceof StopAction)) {
						dialog.getTreeViewer().remove(aa);
					}else if(StereotypeAPI.isStereotypeApplicable((EObject)aa, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
						dialog.getTreeViewer().remove(aa);
					}else if(!StereotypeAPI.getTaggedValue(aa, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING).equals(dpContainer)){
						dialog.getTreeViewer().remove(aa);
					}				
					
				}
			}
		}  	
	}
	
}
