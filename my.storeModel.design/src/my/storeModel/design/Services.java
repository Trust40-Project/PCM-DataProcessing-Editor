package my.storeModel.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.Store;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;
import org.palladiosimulator.pcm.repository.BasicComponent;

/**
 * The services class used by VSM.
 */
public class Services {

	/**
	 * See
	 * http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24
	 * for documentation on how to write service methods.
	 */
	public EObject myService(EObject self, String arg) {
		// TODO Auto-generated code
		return self;
	}

	public EObject getStoreContainer(EObject component) {

		if (!StereotypeAPI.getStereotypeApplications(component, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)
				.isEmpty()) {
			EList<StereotypeApplication> pcmEntityStereotypeApplications = StereotypeAPI
					.getStereotypeApplications(component, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
			StereotypeApplication stereotypeApplication = pcmEntityStereotypeApplications.get(0);
			Stereotype stereotype = stereotypeApplication.getStereotype();
			EStructuralFeature taggedValue = stereotype.getTaggedValue(ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER);
			EObject container = (EObject) stereotypeApplication.eGet(taggedValue);
			return container;
		}
		return null;

	}

	public List<EObject> getAllStoreContainers(EObject repo) {

		EList<EObject> componentList = repo.eContents();
		List<EObject> containers = new ArrayList<EObject>();

		for (int i = 0; i < componentList.size(); i++) {
			if (!StereotypeAPI
					.getStereotypeApplications(componentList.get(i), ProfileConstants.STEREOTYPE_NAME_STORE_HAVING)
					.isEmpty()) {
				final EList<StereotypeApplication> pcmEntityStereotypeApplications = StereotypeAPI
						.getStereotypeApplications(componentList.get(i), ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
				final StereotypeApplication stereotypeApplication = pcmEntityStereotypeApplications.get(0);
				final Stereotype stereotype = stereotypeApplication.getStereotype();
				final EStructuralFeature taggedValue = stereotype.getTaggedValue(ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER);
				EObject container = (EObject) stereotypeApplication.eGet(taggedValue);
				containers.add(container);
			}
		}

		return containers;

	}
	
	public EObject getComponent(EObject repo) {
		
		
		
		return repo.eContents().get(0);
	}

}
