package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.editors.commons.dialogs.selection.PalladioSelectEObjectDialog;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.RepositoryFactory;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.Store;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.StoreContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.impl.RepositoryFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.CollectionDataType;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.Repository;

public class AddStore implements IExternalJavaAction {

	public static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();


	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		for(EObject eObject : arg0){
			return 	(StereotypeAPI.isStereotypeApplicable(eObject, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)|| 
					StereotypeAPI.isStereotypeApplied(eObject, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING));
	}
	return false;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		BasicComponent basicComponent = (BasicComponent) arg1.get("container");
		Repository repo = Services.getParentOfType(basicComponent, Repository.class);
		StoreContainer storeContainer;
		if(StereotypeAPI.isStereotypeApplied(basicComponent, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)) {
			 storeContainer = StereotypeAPI.getTaggedValue(basicComponent, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
		}else {
			DataSpecification dataSpec = Services.getCorrespondingDataspecification(repo);
			storeContainer =  RepositoryFactoryImpl.init().createStoreContainer();
			storeContainer.setEntityName(basicComponent.getEntityName());
			dataSpec.getStoreContainers().add(storeContainer);
			StereotypeAPI.applyStereotype(basicComponent, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
			StereotypeAPI.setTaggedValue(basicComponent, storeContainer, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING, ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER);
		}
		Store newStore = RepositoryFactoryImpl.init().createStore();
		newStore.setEntityName(storeContainer.getEntityName()+"Store"+storeContainer.getStores().size());
		storeContainer.getStores().add(newStore);
		newStore.setDataType(selectDataType(repo));
	}
	
	public DataType selectDataType(Repository repo){
			Collection<Object> filter = new ArrayList<Object>();
			filter.add(Repository.class);
			filter.add(DataType.class);
			Collection<EReference> additionalChildReferences = new ArrayList<EReference>();
			PalladioSelectEObjectDialog dialog = new PalladioSelectEObjectDialog(SHELL, filter, additionalChildReferences,
					repo.eResource().getResourceSet());
			dialog.setProvidedService(DataType.class);
			dialog.open();
			return (DataType) dialog.getResult();
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
