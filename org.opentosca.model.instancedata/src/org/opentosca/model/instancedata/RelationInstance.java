package org.opentosca.model.instancedata;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.namespace.QName;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Converters;
import org.opentosca.settings.Settings;
import org.w3c.dom.Document;

/**
 *
 * Model class representing a NodeInstance
 *
 * @author Marcus Eisele - marcus.eisele@gmail.com
 */

@Entity
@Converters({ @Converter(name = "QNameConverter", converterClass = org.opentosca.util.jpa.converters.QNameConverter.class), @Converter(name = "DOMDocumentConverter", converterClass = org.opentosca.util.jpa.converters.DOMDocumentConverter.class) })
@NamedQueries({
	@NamedQuery(name = RelationInstance.getRelationInstances, query = RelationInstance.getRelationInstancesQuery) })
public class RelationInstance {
	
	// Query to retrieve NodeInstances identified by some parameters
	public final static String getRelationInstances = "RelationInstance.getRelationInstancesQuery";
	protected final static String getRelationInstancesQuery = "select n from RelationInstance n where"
			+ " n.id = COALESCE(:internalID, n.id) AND"
			+ " n.relationshipTemplateName = COALESCE(:relationshipTemplateName, n.relationshipTemplateName) AND"
			+ " n.serviceInstance.id = COALESCE(:internalServiceInstanceID, n.serviceInstance.id) AND"
			+ " n.relationshipTemplateID = COALESCE(:relationshipTemplateID, n.relationshipTemplateID)";
	
	// the internal ID (Database) of the NodeInstance
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// the external ID (used in all contexts BUT in the Database)
	// it is separated because there is no need to save BOTH into the DB!
	@Transient
	private URI relationInstanceID;
	
	@Convert("QNameConverter")
	private QName relationshipTemplateID;
	
	// the name of the corresponding NodeTemplate
	private String relationshipTemplateName;
	
	@Temporal(TemporalType.TIMESTAMP)
	// the creation date of a nodeInstance
	private Date created;
	
	// foreign key relationship to serviceInstance
	@ManyToOne
	@JoinColumn(name = "serviceInstance")
	ServiceInstance serviceInstance;
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "id")
	NodeInstance sourceInstance;
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "id")
	NodeInstance targetInstance;
	
	@Column(name = "properties", columnDefinition = "VARCHAR(8192)")
	@Convert("DOMDocumentConverter")
	Document properties;
	
	// "TOSCA"State
	@Convert("QNameConverter")
	private QName state;
	
	//nodeType of the nodeTemplate which this nodeInstance depends on
	@Convert("QNameConverter")
	private QName relationshipType;
	
	// This empty constructor is required by JPA
	@SuppressWarnings("unused")
	private RelationInstance() {
	}
	
	/**
	 * Creates a new instance of a NodeTemplate. ID and creation date will be
	 * set automatically.
	 *
	 * @param relationshipTemplateID
	 *            - the relationshipTemplateID specified by the Namespace and the ID
	 *            value of the RelationshipTemplate
	 * @param relationshipTemplateName
	 *            - the name of the nodeTemplate
	 */
	public RelationInstance(QName relationshipTemplateID, String relationshipTemplateName, QName relationshipTypeOfRelationshipTemplate,
			ServiceInstance serviceInstance, NodeInstance sourceInstanceID, NodeInstance targetInstanceID) {
		super();
		this.relationshipTemplateID = relationshipTemplateID;
		this.relationshipTemplateName = relationshipTemplateName;
		this.serviceInstance = serviceInstance;
		this.sourceInstance = sourceInstanceID;
		this.targetInstance = targetInstanceID;
		created = new Date();
		properties = null;
		relationshipType = relationshipTypeOfRelationshipTemplate;
	}
	
	public QName getRelationshipType() {
		return relationshipType;
	}
	
	public String getRelationshipTemplateName() {
		return relationshipTemplateName;
	}
	
	public int getId() {
		return id;
	}
	
	public URI getRelationInstanceID() {
		return relationInstanceID;
	}
	
	public QName getRelationshipTemplateID() {
		return relationshipTemplateID;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public QName getState() {
		return state;
	}
	
	public void setState(QName state) {
		this.state = state;
	}
	
	public void setProperties(Document props) {
		properties = props;
	}
	
	public Document getProperties() {
		return properties;
	}
	
	public ServiceInstance getServiceInstance() {
		return serviceInstance;
	}
	
	public NodeInstance getSourceInstance(){
		return sourceInstance;
	}
	
	public NodeInstance getTargetInstance(){
		return targetInstance;
	}
	
	/**
	 * The ID persisted in the database is "only" an integer. To the outside, we
	 * need the ID to be an URI. To avoid storing two IDs in the database we
	 * generate the URI ID out of the integer ID. Therefore, when reading a
	 * NodeInstance object from the database we need to set the URI ID
	 * accordingly.
	 */
	@PostLoad
	@PostPersist
	private void setRelationInstanceID() {
		try {
			relationInstanceID = new URI(Settings.CONTAINER_API + "/CSARs/" + serviceInstance.getCSAR_ID() + "/ServiceTemplates/" + URLEncoder.encode(URLEncoder.encode(serviceInstance.getServiceTemplateID().toString(), "UTF-8"), "UTF-8") + "/Instances/" + this.serviceInstance.getDBId() + "/RelationshipTemplates/" + relationshipTemplateName + "/Instances/" + id);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RelationInstance [id=" + id + ", relationInstanceID=" + relationInstanceID + ", relationshipTemplateID=" + relationshipTemplateID + ", relationshipTemplateName=" + relationshipTemplateName + ", created=" + created + ", serviceInstance=" + serviceInstance + ", properties=" + properties + "]";
	}
	
}