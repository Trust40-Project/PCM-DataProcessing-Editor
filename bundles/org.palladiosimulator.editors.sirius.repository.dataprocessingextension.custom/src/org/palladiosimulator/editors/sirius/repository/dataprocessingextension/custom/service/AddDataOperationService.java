package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.ProcessingPackage;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.impl.ProcessingFactoryImpl;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;


public class AddDataOperationService {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	
	public static void selectClassifier(EObject resource, DataProcessingContainer dpContainer, Collection<Object> filterList, String className, DataopSelectionFilter filterClass) {
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filterList, resource.eResource().getResourceSet());
		dialog.setProvidedService(EClass.class);
		
		filterClass.filterOtherModels(dialog, resource, dpContainer);
		filterClass.removeUnapplyableElements(dialog, dpContainer);
		
		Collection<EClass> classifierCollection = new HashSet<>();
		
		for (EClassifier eClassifier : ProcessingPackage.eINSTANCE.getEClassifiers()) {
			if(eClassifier instanceof EClass) {
				if(!((EClass)eClassifier).isAbstract()) {
					classifierCollection.add((EClass)eClassifier);				
				}
			}
		}
		
		for (Object object : dialog.getTreeViewer().getExpandedElements()) {
			if((className.equals("UsageScenario") && object instanceof UsageScenario) || ((className.equals("ResourceDemandingSEFF") && object instanceof ResourceDemandingSEFF))) {
				for (EClass eClassifier : classifierCollection) {
							dialog.getTreeViewer().add(object, eClassifier);			
				}
			}
		}
		dialog.open();
		EClass selectedClass = (EClass) dialog.getResult();
		
		DataOperation dataop = (DataOperation) ProcessingFactoryImpl.init().create(selectedClass);
		dataop.setEntityName("Generated DataOperation: "+ dataop.getClass().getSimpleName());
		dpContainer.getOperations().add(dataop);
	}
	
}