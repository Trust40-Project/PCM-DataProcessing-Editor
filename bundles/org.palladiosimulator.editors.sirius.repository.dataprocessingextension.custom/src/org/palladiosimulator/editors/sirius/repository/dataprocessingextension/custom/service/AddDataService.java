package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.Data;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.DataFactory;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.DataPackage;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataOperation;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;

public class AddDataService {
	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	
	public static EReference selectEReference(DataOperation dataOp) {
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(DataSpecification.class);
		filterList.add(DataProcessingContainer.class);
		filterList.add(DataOperation.class);
		
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filterList, dataOp.eResource().getResourceSet());
		dialog.setProvidedService(EReference.class);
		
		filterOtherElements(dialog, dataOp);
		
		Collection<EStructuralFeature> containmentReferences = dataOp.eClass().getEAllStructuralFeatures().stream().filter(structur -> structur instanceof EReference).filter(reference -> ((EReference)reference).isContainment()).filter(reference -> DataPackage.Literals.DATA.isSuperTypeOf(((EClass)((EReference)reference).getEType()))).collect(Collectors.toList());
		
		
		for (Object object : dialog.getTreeViewer().getExpandedElements()) {
			if(object instanceof DataSpecification) {
				for (EStructuralFeature eReference : containmentReferences) {
							dialog.getTreeViewer().add(object, (EReference) eReference);
				}
				break;
			}
		}
		
		dialog.open();
		return (EReference) dialog.getResult();
	}
	
	public static EClass selectClassifier(EObject resource, DataOperation dataOp) {
		Collection<Object> filterList = new ArrayList<Object>();
		filterList.add(DataSpecification.class);
		filterList.add(DataProcessingContainer.class);
		filterList.add(DataOperation.class);
		
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filterList, resource.eResource().getResourceSet());
		dialog.setProvidedService(EClass.class);
	
		filterOtherElements(dialog, dataOp);		
		
		Collection<EClass> classifierCollection = new HashSet<>();
		for (EClassifier eClassifier : DataPackage.eINSTANCE.getEClassifiers()) {
			if(eClassifier instanceof EClass) {
				if(((EClass)eClassifier).isAbstract() || !DataPackage.Literals.DATA.isSuperTypeOf((EClass)eClassifier)) {
				}
				else {
					classifierCollection.add((EClass)eClassifier);				
				}
			}
		}
		for (Object object : dialog.getTreeViewer().getExpandedElements()) {
			if((object instanceof DataProcessingContainer)) {
				for (EClass eClassifier : classifierCollection) {
							dialog.getTreeViewer().add(object, eClassifier);			
				}
			}
		}
		
		dialog.open();
		return (EClass) dialog.getResult();
	}
	
	
	public static void addDataElement(EReference eref, DataOperation dataOp, EClass selectedDataType) {
		Data data = (Data) DataFactory.eINSTANCE.create(selectedDataType);
		if(eref.getUpperBound() == 1) {
			dataOp.eSet(eref, data);
		} else {
			EList<Data> dataList = ((EList<Data>) dataOp.eGet(eref));
			dataList.add(data);
			dataOp.eSet(eref, dataList);
		}
	}
	
	private static void filterOtherElements(PalladioSelectEObjectDialog dialog, DataOperation dataOp) {
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, DataSpecification.class, Services.getParentOfType(dataOp, DataSpecification.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, DataProcessingContainer.class, Services.getParentOfType(dataOp, DataProcessingContainer.class));
		Services.removeUnrelatedElementsFromTreeDiagramm(dialog, DataOperation.class, Services.getParentOfType(dataOp, DataOperation.class));
		
		for (DataOperation containedDataOp : dataOp.getContainer().getOperations()) {
			if(!containedDataOp.equals(dataOp)) {
				dialog.getTreeViewer().remove(containedDataOp);
			}
		}
	}
	
}
