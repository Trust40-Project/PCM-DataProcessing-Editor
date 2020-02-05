package org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.data.ParameterBasedData;

//org.palladiosimulator.editors.sirius.repository.dataprocessingextension.custom.SelectParameterForParameterBasedData
public class SelectParameterForParameterBasedData implements IExternalJavaAction{

	@Override
	public boolean canExecute(Collection<? extends EObject> arg0) {
		return true;
	}

	@Override
	public void execute(Collection<? extends EObject> arg0, Map<String, Object> arg1) {
		((ParameterBasedData)(arg1.get("instance"))).setEntityName("testName");
	}

}
