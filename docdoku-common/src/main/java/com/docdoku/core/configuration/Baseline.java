/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2013 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.docdoku.core.configuration;

import com.docdoku.core.product.ConfigurationItem;
import com.docdoku.core.product.PartIteration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Baseline refers to a specific configuration, it could be seen as
 * "snapshots in time" of configurations. More concretely, baselines are collections
 * of items (like parts) at a specified iteration.
 * Within a baseline, there must not be two different iterations of the same part.
 * 
 * @author Florent Garin
 * @version 2.0, 15/05/13
 * @since   V2.0
 */
@Table(name="BASELINE")
@Entity
@NamedQueries({
        @NamedQuery(name="Baseline.findByConfigurationItemId", query="SELECT b FROM Baseline b WHERE b.configurationItem.id = :ciId AND b.configurationItem.workspace.id = :workspaceId"),
        @NamedQuery(name="Baseline.getBaselinesForPartRevision", query="SELECT b FROM Baseline b WHERE b.partCollection IN (SELECT bl.partCollection FROM BaselinedPart bl WHERE bl.targetPart.partRevision = :partRevision)")
})
public class Baseline implements Serializable {


    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "CONFIGURATIONITEM_ID", referencedColumnName = "ID"),
            @JoinColumn(name = "CONFIGURATIONITEM_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private ConfigurationItem configurationItem;

    @Column(nullable = false)
    private String name;

    private BaselineType type=BaselineType.LATEST;

    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date creationDate;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    private PartCollection partCollection=new PartCollection();

    public enum BaselineType {LATEST, RELEASED}

    public Baseline() {
    }

    public Baseline(ConfigurationItem configurationItem, String name, BaselineType type, String description) {
        this.configurationItem = configurationItem;
        this.name = name;
        this.type = type;
        this.description = description;
        this.creationDate = new Date();
    }

    public Map<BaselinedPartKey, BaselinedPart> getBaselinedParts() {
        return partCollection.getBaselinedParts();
    }
    public void removeAllBaselinedParts() {
        partCollection.removeAllBaselinedParts();
    }

    public void addBaselinedPart(PartIteration targetPart){
        partCollection.addBaselinedPart(targetPart);
    }
    public boolean hasBasedLinedPart(String targetPartWorkspaceId, String targetPartNumber){
        return partCollection.hasBasedLinedPart(new BaselinedPartKey(partCollection.getId(),targetPartWorkspaceId,targetPartNumber));
    }
    public BaselinedPart getBaselinedPart(BaselinedPartKey baselinedPartKey){
        return partCollection.getBaselinedPart(baselinedPartKey);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BaselineType getType() {return type;}
    public void setType(BaselineType type) {this.type = type;}

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public PartCollection getPartCollection() {
        return partCollection;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }
    public void setConfigurationItem(ConfigurationItem configurationItem) {
        this.configurationItem = configurationItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Baseline)) return false;

        Baseline baseline = (Baseline) o;
        return id == baseline.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
