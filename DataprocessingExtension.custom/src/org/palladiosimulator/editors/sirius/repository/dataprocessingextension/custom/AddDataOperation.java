package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.EditorSelectionDialog;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.DataPackage;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.ProcessingPackage;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.impl.DataProcessingContainerImpl;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.impl.ProcessingFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.impl.DelayImpl;
import org.palladiosimulator.pcm.usagemodel.impl.EntryLevelSystemCallImpl;
import org.palladiosimulator.pcm.usagemodel.impl.ScenarioBehaviourImpl;
import org.palladiosimulator.pcm.usagemodel.impl.UsageModelImpl;
import org.palladiosimulator.pcm.usagemodel.impl.UsageScenarioImpl;


public class AddDataOperation implements IExternalJavaAction {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		Collection<EClass> classifierCollection = new HashSet<>();
		
		for (EClassifier eClassifier : ProcessingPackage.eINSTANCE.getEClassifiers()) {
			if(eClassifier instanceof EClass) {
				if(((EClass)eClassifier).isAbstract()) {
				}
				else {
					classifierCollection.add((EClass)eClassifier);				
				}
			}
		}
		
		DataProcessingContainer dpContainer = (DataProcessingContainer) arg1.get("container");
		
		DSemanticDiagram semanticDiagram = Services.getParentOfType((EObject) arg1.get("containerView"), DSemanticDiagram.class);
		ResourceDemandingSEFF seff = (ResourceDemandingSEFF) semanticDiagram.getTarget();
		
		EClass selectedClass = selectClassifier(classifierCollection, seff, dpContainer);
		DataOperation dataop = (DataOperation) ProcessingFactoryImpl.init().create(selectedClass);
		
		dataop.setEntityName("Generated DataOperation: "+ dataop.getClass().getSimpleName());
		
		dpContainer.getOperations().add(dataop);
	}
	
	
	
	
	
	
	
	private EClass selectClassifier(Collection<EClass> classifierCollection, EObject resource, DataProcessingContainer dpContainer) {
		
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(ResourceDemandingSEFF.class);
		filterList.add(AbstractAction.class);
		filterList.add(BasicComponent.class);
		filterList.add(Repository.class);
		filterList.add(EClass.class);
		
		
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filterList, resource.eResource().getResourceSet());
		dialog.setProvidedService(EClass.class);
		
		filterOtherModels(dialog, resource);
		removeUnapplyableElements(dialog, dpContainer);
		
		for (Object object : dialog.getTreeViewer().getExpandedElements()) {
			if(object instanceof ResourceDemandingSEFF) {
				for (EClass eClassifier : classifierCollection) {
							dialog.getTreeViewer().add(object, eClassifier);
					
				}
			}
		}
		dialog.open();
		return  (EClass) dialog.getResult();
	}
	
	private void filterOtherModels(PalladioSelectEObjectDialog dialog, EObject resource) {
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, Repository.class, Services.getParentOfType(resource, Repository.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, BasicComponent.class, Services.getParentOfType(resource, BasicComponent.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, ResourceDemandingSEFF.class, ((ResourceDemandingSEFF)resource));
	}
	
	
	public static void removeUnapplyableElements(PalladioSelectEObjectDialog dialog, DataProcessingContainer dpContainer) {
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
