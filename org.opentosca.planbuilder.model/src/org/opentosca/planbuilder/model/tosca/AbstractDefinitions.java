package org.opentosca.planbuilder.model.tosca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;

/**
 * <p>
 * This class represents a TOSCA Definitions Document
 * </p>
 * Copyright 2013 IAAS University of Stuttgart <br>
 * <br>
 *
 * @author Kalman Kepes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public abstract class AbstractDefinitions {

	/**
	 * Returns the id of this TOSCA Definitions
	 *
	 * @return a String containing the id
	 */
	public abstract String getId();

	/**
	 * Returns the name of this TOSCA Definitions
	 *
	 * @return a String containing the name, if not set null
	 */
	public abstract String getName();

	/**
	 * Returns a List of the ServiceTemplates this TOSCA Definitions has declared
	 *
	 * @return a List of AbstractServiceTemplates
	 */
	public abstract List<AbstractServiceTemplate> getServiceTemplates();

	/**
	 * Returns a List of the NodeTypes this TOSCA Definitions has declared
	 *
	 * @return a List of AbstractNodeType
	 */
	public abstract List<AbstractNodeType> getNodeTypes();

	/**
	 * Returns a List of the NodeTypeImplementation this TOSCA Definitions has
	 * declared
	 *
	 * @return a List of AbstractNodeTypeImplementation
	 */
	public abstract List<AbstractNodeTypeImplementation> getNodeTypeImplementations();

	/**
	 * Returns a List of the RelationshipTypes this TOSCA Definitions has declared
	 *
	 * @return a List of AbstractRelationshipType
	 */
	public abstract List<AbstractRelationshipType> getRelationshipTypes();

	/**
	 * Return a List of the ArtifactTemplates this TOSCA Definitions has declared
	 *
	 * @return a List of AbstractArtifactTemplates
	 */
	public abstract List<AbstractArtifactTemplate> getArtifactTemplates();

	/**
	 * Returns the targetNamespace of this TOSCA Definitions
	 *
	 * @return a String containing the targetNamespace
	 */
	public abstract String getTargetNamespace();

	/**
	 * Returns a List of all Definitions this TOSCA Definitions has imported
	 *
	 * @return a List of AbstractDefinitions
	 */
	public abstract List<AbstractDefinitions> getImportedDefinitions();

	/**
	 * Returns an absolute Path for the given AbstractArtifactReference
	 *
	 * @param ref
	 *            an AbstractArtifactReference
	 * @return a File containing an absolute path to the given ArtifactReference
	 */
	public abstract File getAbsolutePathOfArtifactReference(AbstractArtifactReference ref);

	/**
	 * Returns a RelationshipType for the given QName. This method looks trough the
	 * whole Definitions space, which means the search looks trough the imported
	 * Definitions of this Definitions.
	 *
	 * @param relationshipTypeId
	 *            a QName
	 * @return an AbstractRelationshipType, if nothing was found null
	 */
	public AbstractRelationshipType getRelationshipType(QName relationshipTypeId) {
		for (AbstractRelationshipType relationshipType : this.getRelationshipTypes()) {
			// info: at this moment i have no idea why it doesn't work using the
			// QName.equals() method..
			if (relationshipType.getId().equals(relationshipTypeId)) {
				return relationshipType;
			}
		}
		return null;
	}

	/**
	 * Returns a NodeType for the given QName, This method looks trough the whole
	 * Definitions space, which means the search looks trough the imported
	 * Definitions of this Definitions
	 *
	 * @param nodeTypeId
	 *            a QName
	 * @return an AbstractNodeType, if nothing was found null
	 */
	public AbstractNodeType getNodeType(QName nodeTypeId) {
		for (AbstractNodeType nodeType : this.getNodeTypes()) {
			if (nodeType.getId().equals(nodeTypeId)) {
				return nodeType;
			}
		}
		return null;
	}

	/**
	 * Returns a ArtifactTemplate for the given QName, This method looks trough the
	 * whole Definitions space, which means the search looks trough the imported
	 * Definitions of this Definitions
	 *
	 * @param qname
	 *            a QName
	 * @return an AbstractArtifactTemplate, if nothing was found null
	 */
	public AbstractArtifactTemplate getArtifactTemplate(QName qname) {
		for (AbstractArtifactTemplate template : this.getArtifactTemplates()) {
			if (template.getId().equals(qname.getLocalPart())) {
				return template;
			}
		}
		for (AbstractDefinitions def : this.getImportedDefinitions()) {
			if (def.getArtifactTemplate(qname) != null) {
				return def.getArtifactTemplate(qname);
			}
		}
		return null;
	}

	/**
	 * Returns a List of all RelationshipTypeImplemenations this TOSCA Definitions
	 * has defined
	 *
	 * @return a List of AbstractRelationshipTypeImplementation
	 */
	public abstract List<AbstractRelationshipTypeImplementation> getRelationshipTypeImplementations();

	/**
	 * Returns all {@link AbstractArtifactType} objects of this
	 * {@link AbstractDefinitions} obj.
	 * 
	 * @return a {@link List} of {@link AbstractArtifactType}
	 */
	public abstract List<AbstractArtifactType> getArtifactTypes();
}
