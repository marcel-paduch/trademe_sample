package nz.co.trademe.techtest.data.offline.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * POJO for Category data
 */
@Entity
public class Category {
    @PrimaryKey
    @NonNull
    private String id = "";
    private String name;
    private Boolean isMainCategory;
    private String parentId;
    /**
     * IF true, category has no subcategories
     */
    private Boolean isLeaf;

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMainCategory() {
        return isMainCategory;
    }

    public void setMainCategory(Boolean mainCategory) {
        isMainCategory = mainCategory;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
