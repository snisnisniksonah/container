package org.opentosca.container.core.model.deployment.ia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.opentosca.container.core.model.csar.id.CSARID;
import org.opentosca.container.core.model.deployment.AbstractFileDeploymentInfo;

/**
 * Deployment information of a Implementation Artifact inside a CSAR file. It is
 * used for tracking its deploy progress.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({@NamedQuery(name = IADeploymentInfo.getIADeploymentInfoByCSARIDAndRelPath, query = IADeploymentInfo.getIADeploymentInfoByCSARIDAndRelPathQuery), @NamedQuery(name = IADeploymentInfo.getIADeploymentInfoByCSARID, query = IADeploymentInfo.getIADeploymentInfoByCSARIDQuery)})
@Table(name = IADeploymentInfo.tableName)
@PrimaryKey(columns = {@Column(name = "csarID"), @Column(name = "RelPath")})
public class IADeploymentInfo extends AbstractFileDeploymentInfo {
	
	protected static final String tableName = "IADeploymentInfo";
	
	/*
	 * JPQL Queries
	 */
	public static final String getIADeploymentInfoByCSARIDAndRelPath = "IADeploymentInfo.ByCSARIDAndRelPath";
	protected static final String getIADeploymentInfoByCSARIDAndRelPathQuery = "select t from " + IADeploymentInfo.tableName + " t where t.relPath = :iaRelPath and t.csarID = :csarID";
	
	public static final String getIADeploymentInfoByCSARID = "IADeploymentInfo.ByCSARID";
	protected static final String getIADeploymentInfoByCSARIDQuery = "select t from " + IADeploymentInfo.tableName + " t where t.csarID = :csarID";
	
	/**
	 * Deployment state of this IA.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "DeploymentState")
	private IADeploymentState deploymentState;
	
	
	protected IADeploymentInfo() {

	}
	
	public IADeploymentInfo(final CSARID csarID, final String relPath, final IADeploymentState deploymentState) {
		super(csarID, relPath);
		this.deploymentState = deploymentState;
	}

	public IADeploymentState getDeploymentState() {
		return this.deploymentState;
	}

	public void setDeploymentState(final IADeploymentState deploymentState) {
		this.deploymentState = deploymentState;
	}
}
