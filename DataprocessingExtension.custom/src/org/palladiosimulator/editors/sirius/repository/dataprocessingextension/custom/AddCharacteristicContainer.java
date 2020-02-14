package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ocl.pivot.Stereotype;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.EnumCharacteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.EnumCharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.impl.CharacteristicsFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;


public class AddCharacteristicContainer implements IExternalJavaAction {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	
	
	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for(EObject eObject : arg0){
				return 	(StereotypeAPI.isStereotypeApplicable(eObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) || 
						StereotypeAPI.isStereotypeApplied(eObject, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE));
		}
		return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		EObject object = (EObject)arg1.get("container");
		Repository repo = Services.getRepo(object);				
		DataSpecification dataSpec = Services.getCorrespondingDataspecification(repo);
		if(dataSpec != null) {
			CharacteristicContainer characContainer;
			if(StereotypeAPI.isStereotypeApplicable(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) || 
					(StereotypeAPI.isStereotypeApplied(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE) && 
					StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE).equals(null))) {
				characContainer = CharacteristicsFactoryImpl.init().createCharacteristicContainer();
				dataSpec.getCharacteristicContainer().add(characContainer);
				StereotypeAPI.applyStereotype(object, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
				StereotypeAPI.setTaggedValue(object, characContainer, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER);
				characContainer.setEntityName(((BasicComponent)object).getEntityName() + "Container");
			}else {
				characContainer = StereotypeAPI.getTaggedValue(object, ProfileConstants.TAGGED_VALUE_NAME_CHARACTERIZABLE_CONTAINER, ProfileConstants.STEREOTYPE_NAME_CHARACTERIZABLE);
			}
			addCharacteristic(dataSpec, characContainer);
		}
	}
	
	public void addCharacteristic(DataSpecification dataSpec, CharacteristicContainer characContainer) {
		Characteristic newCharac = CharacteristicsFactoryImpl.init().createEnumCharacteristic();
		newCharac.setCharacteristicType(getCharacteristic(dataSpec));
		characContainer.getOwnedCharacteristics().add(newCharac);
	}
	
	
	private CharacteristicType getCharacteristic(DataSpecification dataSpec) {
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
		
//		filterDialogTree(dialog, opSig);
		
		dialog.open();

		return (CharacteristicType) dialog.getResult();
	}
	

}
