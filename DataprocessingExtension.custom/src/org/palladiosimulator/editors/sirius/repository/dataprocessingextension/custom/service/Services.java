package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.impl.CharacteristicsFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;

public class Services {
	
	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	
	 public static OperationSignature getCorrectOperationSignature(EList<Interface> interfaceList, OperationSignatureDataRefinement targetRefinement) {
			for (Interface interfaceElement : interfaceList) {
				if(interfaceElement instanceof OperationInterface) {
					for (OperationSignature operationSignature : ((OperationInterface)interfaceElement).getSignatures__OperationInterface() ) {
						if(StereotypeAPI.isStereotypeApplied(operationSignature, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
							OperationSignatureDataRefinement returnedRef= StereotypeAPI.getTaggedValue(operationSignature, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT);
							if(returnedRef.equals(targetRefinement)) {
								return operationSignature;
							}
						}
					}
				}
			}
			return null;
		}
	 
		
		
	public static OperationSignature getCorrectOperationSignature(EList<Interface> interfaceList, OperationSignature targetSignature) {
			for (Interface interfaceElement : interfaceList) {
				if(interfaceElement instanceof OperationInterface) {
					for (OperationSignature operationSignature : ((OperationInterface)interfaceElement).getSignatures__OperationInterface() ) {
						if(operationSignature.equals(targetSignature)) {
							return operationSignature;
						}
					}
				}
			}
			return null;
		}
	
//	TODO rm
	public static Repository getRepo(EObject eObject) {
		try {
		while( ! (eObject.eContainer() instanceof Repository)) {
			eObject = eObject.eContainer();
		}
		return (Repository) eObject.eContainer();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
//	TODO effizienter
	public static DataSpecification getCorrespondingDataspecification(Iterator<EObject> treeIt, EObject parent) {
		EObject available = null;
		
		
		while(treeIt.hasNext()) {
			EObject object = (EObject) treeIt.next();
			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE)) {
				available = (StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE));
			}
			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING)) {
				available = (StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_DATA_PROCESSING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_DATA_PROCESSING));
			}
			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT)) {
				available = (StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT, ProfileConstants.STEREOTYPE_NAME_OPERATION_SIGNATURE_DATA_REFINEMENT));
			}
			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_STORE_CHARACTERIZATION)) {
				available = (StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_STORE_CHARACTERIZATION, ProfileConstants.STEREOTYPE_NAME_STORE_CHARACTERIZATION));
			}
			if(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)) {
				available = (StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING));
			}
				if(available != null) {
					return getParentOfType(available, DataSpecification.class);
				}else {
				}
		} 
		return (DataSpecification) loadResourceFromXMI(parent);
	}
	
	public static void addCharacteristicToElement(DataSpecification dataSpec, EObject object){
		if(dataSpec != null) {
			CharacteristicContainer characContainer;
			if(StereotypeAPI.isStereotypeApplicable(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) || 
					(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) && 
					StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE).equals(null))) {
				characContainer = CharacteristicsFactoryImpl.init().createCharacteristicContainer();
				dataSpec.getCharacteristicContainer().add(characContainer);
				StereotypeAPI.applyStereotype(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				StereotypeAPI.setTaggedValue(object, characContainer, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER);
				characContainer.setEntityName(((Entity)object).getEntityName() + "Container");
			}else {
				characContainer = StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
			}
			addCharacteristic(dataSpec, characContainer);
		}
	}
	
	private static void addCharacteristic(DataSpecification dataSpec, CharacteristicContainer characContainer) {
		Characteristic newCharac = CharacteristicsFactoryImpl.init().createEnumCharacteristic();
		newCharac.setCharacteristicType(getCharacteristic(dataSpec));
		characContainer.getOwnedCharacteristics().add(newCharac);
	}
	
	
	
	
	private static EObject loadResourceFromXMI(EObject self) {
		Shell shell = Display.getCurrent().getActiveShell();
		
		shell = getShell();
		shell.open();
		ResourceDialog dia = new ResourceDialog(shell, "open a resource", SWT.OPEN);
		dia.open();
		String uri = dia.getURIText();
		
		System.out.println ("Selected: "+uri);
        ResourceSet resSet = self.eResource().getResourceSet();
        Resource resource = resSet.getResource(URI.createURI(uri), true);
		EObject obj = resource.getContents().get(0);
		
		System.out.println("loaded resource: "+obj.toString());
		return obj;
	}
	
	
	private static Shell getShell() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		  if (window == null) {
		    IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		    if (windows.length > 0) {
		       return windows[0].getShell();
		    }
		  }
		  else {
		    return window.getShell();
		  }
		  return null;
		}

	
    public static <T> T getParentOfType(EObject object, Class<T> parentClass) {
        EObject parent = object;
        while (parent != null && !parentClass.isInstance(parent)) {
               parent = parent.eContainer();
        }
        return (T) parent;
  }
    
	public static CharacteristicType getCharacteristic(DataSpecification dataSpec) {
		Collection<Object> filter = new ArrayList<Object>();

//		filter.add(DataSpecification.class);
//		filter.add(CharacteristicContainer.class);
//		filter.add(Characteristic.class);
		filter.add(CharacteristicType.class);
		filter.add(CharacteristicTypeContainer.class);

		Collection<EReference> additionalChildReferences = new ArrayList<EReference>();
		PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filter, additionalChildReferences,
				dataSpec.eResource().getResourceSet());
		dialog.setProvidedService(CharacteristicType.class);
		
		filterCharacteristic(dialog);
		
		dialog.open();

		return (CharacteristicType) dialog.getResult();
	}
	
	private static void filterCharacteristic(PalladioSelectEObjectDialog dialog) {
		Collection<CharacteristicTypeContainer> types = new HashSet<CharacteristicTypeContainer>();
		for (Object o: dialog.getTreeViewer().getExpandedElements()) {
			if(o instanceof CharacteristicTypeContainer) {
				if(isInCollection((CharacteristicTypeContainer) o, types)) {
					dialog.getTreeViewer().remove(o);
				} else {
					types.add((CharacteristicTypeContainer)o);
				}
			}
		}
	}
	
	private static boolean isInCollection(CharacteristicTypeContainer lookUpObject, Collection<CharacteristicTypeContainer> collection) {
		for (CharacteristicTypeContainer eObject : collection) {
			if(eObject.getId().equals(lookUpObject.getId())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	public static void removeCharacteristic(EObject appliedObject, Characteristic characteristic) {
		if(StereotypeAPI.isStereotypeApplied(appliedObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE)) {
			CharacteristicContainer characContainer = StereotypeAPI.getTaggedValue(appliedObject, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
			if(characContainer.getOwnedCharacteristics().contains(characteristic)) {
				if(characContainer.getOwnedCharacteristics().size() > 1) {
					characContainer.getOwnedCharacteristics().remove(characteristic);
				}else {
					((DataSpecification) characContainer.eContainer()).getCharacteristicContainer().remove(characContainer);
					StereotypeAPI.unapplyStereotype(appliedObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				}
				return;
			}
		}
	}
    

}


