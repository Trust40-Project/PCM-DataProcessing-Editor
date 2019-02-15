package my.storeModel.design;
 
import java.util.ArrayList;
import java.util.List;

//org.eclipse.emf.edit.ui.action
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.internal.session.SessionTransientAttachment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.impl.DataSpecificationImpl;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.StoreContainer;
import org.palladiosimulator.pcm.dataprocessing.profile.api.ProfileConstants;

/**
 * The services class used by VSM.
 */
public class Services {
	

	/**
	 * if the Component has the Stereotype StoreHaving, return the connected StoreContainer
	 */
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
	
	public EObject tesd(EObject self, EObject cont) {
		System.out.println("the mysteriuos self object: "+self.toString() + " and the cont: "+cont.toString());
		return null;
	}
	
	public boolean hasAStoreContainer(EObject self) {
		EObject potentialContainer = getStoreContainer(self);
		if(potentialContainer == null) {
			return true;
		}
		return false;
	}

	/**
	 * return all the storeContainers of the BasicComponents in the current repository
	 */
	public List<EObject> getAllStoreContainers(EObject repo) {

		EList<EObject> componentList = repo.eContents();
		List<EObject> containers = new ArrayList<EObject>();

		for (int i = 0; i < componentList.size(); i++) {
			
			EObject component = componentList.get(i);
			
			containers.add(getStoreContainer(component));
		}
		
		return containers;

	}
	
	public EObject openDialogForDataSpecification(EObject self) {
		
		Shell shell = Display.getCurrent().getActiveShell();
		shell.open();
		
		ResourceDialog dia = new ResourceDialog(shell, "open a resource", SWT.OPEN);
		
		dia.open();
		
		String uri = dia.getURIText();
		
		System.out.println ("Selected: "+uri);
		
        ResourceSet resSet = new ResourceSetImpl();

        Resource resource = resSet.getResource(URI.createURI(uri), true);
        
		EObject obj = resource.getContents().get(0);
		
		System.out.println("loaded resource: "+obj.toString());
		
		return obj;
	}
	
	public EObject connectComponentWithStoreContainer(EObject target, EObject source) {
		
		// var source is the basic component without a connection
		// var target is the storeContainer to connect with the basic component
		
		StereotypeAPI.applyStereotype(source, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
		
		final EList<Stereotype> applicableStereotypes = StereotypeAPI.getApplicableStereotypes(source, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
        if (applicableStereotypes.size() != 1) {
            throw new RuntimeException("ApplyStereotype based on name failed: name \"" + ProfileConstants.STEREOTYPE_NAME_STORE_HAVING
                    + "\" not uniquely found (" + applicableStereotypes.size() + " times)!");
        }
        
        Stereotype stType = applicableStereotypes.get(0);
        
        StereotypeApplication compStTypeApp = StereotypeAPI.getStereotypeApplication(source, stType);
        
        EStructuralFeature taggedValue = stType.getTaggedValue(ProfileConstants.TAGGED_VALUE_NAME_STORE_HAVING_CONTAINER);
        
        compStTypeApp.eSet(taggedValue, target);
        
        Session session = SessionManager.INSTANCE.getSession(target.eContainer());
        Session sessiont = SessionManager.INSTANCE.getSession(target);
        
        System.out.println("session of data spec: "+session.toString());
        
        System.out.println("session of storeC: "+sessiont.toString());
        
        //target.eAdapters().add(new SessionTransientAttachment(session));
        
		
		return null;
	}
	
	// TODO
	public EObject deleteBasicComponent(EObject self) {

		// check whether the basic component has a store container
		EObject potentialStoreContainer = getStoreContainer(self);
		if(potentialStoreContainer != null) {
			disconnectCompAndStoreContainer(self, potentialStoreContainer);
		}
		return self;
	}
	
	public void disconnectCompAndStoreContainer(EObject comp, EObject storeCont) {
		
		StoreContainer givenStoreCont = (StoreContainer) storeCont;
		
		// unapply stereotype
		StereotypeAPI.unapplyStereotype(comp, ProfileConstants.STEREOTYPE_NAME_STORE_HAVING);
		
		// ecoreUtil remove/delete
		
		// delete storeContainer from data spec
		Shell shell = Display.getCurrent().getActiveShell();
		shell.open();
		ResourceDialog dia = new ResourceDialog(shell, "open a resource", SWT.OPEN);
		dia.open();
		String uri = dia.getURIText();
		
        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.getResource(URI.createURI(uri), true);
		DataSpecificationImpl obj = (DataSpecificationImpl) resource.getContents().get(0);
		
		if(obj.getClass() == DataSpecificationImpl.class) {
			DataSpecification dataSpec = (DataSpecification) obj;
			EList<StoreContainer> storeContList = dataSpec.getStoreContainers();
			for(StoreContainer x: storeContList){
				boolean sameId = x.getId().equals(givenStoreCont.getId());
				if(sameId) {
					storeContList.remove(x);
					EStructuralFeature feature = dataSpec.eClass().getEStructuralFeature("storeContainers");
					dataSpec.eSet(feature, storeContList);
				}
			}
		}
		
	}
	
	
	
}
