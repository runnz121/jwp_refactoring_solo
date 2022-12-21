package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    @Column(name = "empty")
    private boolean isEmpty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

/*
    public void addTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
*/

    public boolean isNotNull() {
        return this.tableGroupId != null;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }


    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }
}
