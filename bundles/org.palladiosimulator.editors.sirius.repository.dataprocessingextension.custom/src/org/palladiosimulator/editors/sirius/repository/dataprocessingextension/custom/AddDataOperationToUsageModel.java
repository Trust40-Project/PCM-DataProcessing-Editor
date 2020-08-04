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
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

public class AddDataOperationToUsageModel implements IExternalJavaAction, DataopSelectionFilter {
	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		DataProcessingContainer dpContainer = (DataProcessingContainer) arg1.get("container");
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		UsageModel usageModel = (UsageModel) semanticDiagram.getTarget();
		
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(UsageModel.class);
		filterList.add(UsageScenario.class);
		filterList.add(ScenarioBehaviour.class);
		filterList.add(AbstractUserAction.class);
		filterList.add(EClass.class);
		
		AddDataOperationService.selectClassifier(usageModel, dpContainer,  filterList, "UsageScenario", (DataopSelectionFilter) this);
	}
	
	public void filterOtherModels(PalladioSelectEObjectDialog dialog, EObject resource, DataProcessingContainer dpContainer) {
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, UsageModel.class, ((UsageModel)resource));
		AbstractUserAction userAction = getExpectedUserAction(dialog, dpContainer);
		if(userAction != null) {
			Services.removeUnrelatedElementsFromTreeDiagramm(dialog, UsageScenario.class, Services.getParentOfType(userAction, UsageScenario.class));
			Services.removeUnrelatedElementsFromTreeDiagramm(dialog, ScenarioBehaviour.class, Services.getParentOfType(userAction, ScenarioBehaviour.class));
		}
	}
	
	public void removeUnapplyableElements(PalladioSelectEObjectDialog dialog, DataProcessingContainer dpContainer) {
		 for (Object o : dialog.getTreeViewer().getExpandedElements()) {
			 if(o instanceof ScenarioBehaviour) {
				for(AbstractUserAction aa : ((ScenarioBehaviour) o).getActions_ScenarioBehaviour()) {
					if((aa instanceof Start || aa instanceof Stop)) {
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
	
	
	private static AbstractUserAction getExpectedUserAction(PalladioSelectEObjectDialog dialog, DataProcessingContainer dpContainer) {
		for(Object o : dialog.getTreeViewer().getExpandedElements()) {
			 if(o instanceof ScenarioBehaviour) {
				for (AbstractUserAction  userAction : ((ScenarioBehaviour)o).getActions_ScenarioBehaviour()) {
					if(StereotypeAPI.isStereotypeApplied(userAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
						if(StereotypeAPI.getTaggedValue(userAction, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING).equals(dpContainer)) {
							return userAction;
						}
					}
				}
			}
		}
		return null;
	}
	
	

}
