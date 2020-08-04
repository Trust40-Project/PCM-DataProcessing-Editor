package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.service.Services;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ResultBasedData;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.impl.DataFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.repository.OperationSignatureDataRefinement;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;


public class SetOperationSignatureForResultbasedData implements IExternalJavaAction {

	public SetOperationSignatureForResultbasedData() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		OperationSignature opSig = Services.getCorrectOperationSignature(((Repository)((DSemanticDecorator)((DNodeList) arg1.get("containerView")).getParentDiagram()).getTarget()).getInterfaces__Repository(), (OperationSignatureDataRefinement) arg1.get("container"));
		if(opSig.getReturnType__OperationSignature() != null) {
			ResultBasedData rsData = (ResultBasedData) DataFactoryImpl.init().createResultBasedData();
			rsData.setOperation(opSig);
			OperationSignatureDataRefinement opSigDataRef = (OperationSignatureDataRefinement) arg1.get("container");
			opSigDataRef.getResultRefinements().add(rsData);
			rsData.setEntityName("ResultBasedData:: " + opSig.getEntityName());
		}

	}
	

}
