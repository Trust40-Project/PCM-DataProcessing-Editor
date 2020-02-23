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
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.impl.ProcessingFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.impl.DelayImpl;
import org.palladiosimulator.pcm.usagemodel.impl.EntryLevelSystemCallImpl;
import org.palladiosimulator.pcm.usagemodel.impl.ScenarioBehaviourImpl;
import org.palladiosimulator.pcm.usagemodel.impl.UsageModelImpl;
import org.palladiosimulator.pcm.usagemodel.impl.UsageScenarioImpl;


public class AddDataProcessingContainerToUsageModel implements IExternalJavaAction, DataSelectionFilter {

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for (EObject usageModel : arg0) {
			for (UsageScenario usageScenario : ((UsageModel) usageModel).getUsageScenario_UsageModel()) {
				for(AbstractUserAction scenBehaviour : usageScenario.getScenarioBehaviour_UsageScenario().getActions_ScenarioBehaviour()) {
					if(scenBehaviour instanceof EntryLevelSystemCall || scenBehaviour instanceof Delay) {
						if(StereotypeAPI.isStereotypeApplicable(scenBehaviour, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
							return true;
						}else {
						}
					}
				}
			}
		}	
		
		
		
	return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		UsageModel uModel = (UsageModel) arg1.get("container");
		DataSpecification dataSpec = Services.getCorrespondingDataspecification(uModel.eAllContents(), uModel);
		
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(UsageModelImpl.class);
		filterList.add(UsageScenarioImpl.class);
		filterList.add(ScenarioBehaviourImpl.class);
		filterList.add(EntryLevelSystemCallImpl.class);
		filterList.add(DelayImpl.class);
		
		AbstractUserAction aUserAction = DataSpecificationServices.getCharacteristic(uModel, filterList, AbstractUserAction.class, this);
		DataProcessingContainer dpContainer = ProcessingFactoryImpl.init().createDataProcessingContainer();

		dataSpec.getDataProcessingContainers().add(dpContainer);
		StereotypeAPI.applyStereotype(aUserAction, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING);
		StereotypeAPI.setTaggedValue(aUserAction, dpContainer, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER);
		
		dpContainer.setEntityName("UsageModel_"+aUserAction.getEntityName());
	}
	
	public void filterDialog(PalladioSelectEObjectDialog dialog, EObject resource) {
		filterUnapplyableAbstractUserActions(dialog);
		filterOtherModels(dialog, resource);
	}
	
	private static void filterUnapplyableAbstractUserActions(PalladioSelectEObjectDialog dialog) {
		for(Object o : dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof ScenarioBehaviour) {
					for (EObject object : ((ScenarioBehaviour) o).getActions_ScenarioBehaviour()) {
						boolean show = false;
						if(object instanceof EntryLevelSystemCall || object instanceof Delay) {
							if(!StereotypeAPI.isStereotypeApplicable((object), ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
								dialog.getTreeViewer().remove(object);
							}else {
								show = true;
							}
						}
					}
				
			}
		}
	}
	
	private static void filterOtherModels(PalladioSelectEObjectDialog dialog, EObject uModel) {
//		System.out.println(dialog.getTreeViewer().getExpandedElements().length);
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, UsageModel.class, ((UsageModel)uModel));
//		System.out.println(dialog.getTreeViewer().getExpandedElements().length);
	}

}
