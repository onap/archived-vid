package org.onap.vid.model;

/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Modifications Copyright 2018 Nokia
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vid_category_parameter", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CategoryParameter extends VidBaseEntity {

    public enum Family {
        PARAMETER_STANDARDIZATION,
        TENANT_ISOLATION
    }

    private String name;
    private boolean idSupported;

    @Column(name = "FAMILY")
    @Enumerated(EnumType.STRING)
    private String family;

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    private Set<CategoryParameterOption> options = new HashSet<>(0);

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    public Long getId() {
        return super.getId();
    }

    @Column(name = "NAME", unique = true, nullable = false, length=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "categoryParameter")
    public Set<CategoryParameterOption> getOptions() {
        return options;
    }

    public void setOptions(Set<CategoryParameterOption> options) {
        this.options = options;
    }

    public boolean addOption(CategoryParameterOption option) {
        return options.add(option);
    }

    @Column(name = "ID_SUPPORTED")
    public boolean isIdSupported() {
        return idSupported;
    }

    public void setIdSupported(boolean idSupported) {
        this.idSupported = idSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        CategoryParameter that = (CategoryParameter) o;
        return this.idSupported == that.idSupported &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.family, that.family) &&
                Objects.equals(this.options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.idSupported, this.family, this.options);
    }
}
