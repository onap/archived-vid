package org.onap.vid.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vid_name_counter")
public class NameCounter {

    private String name;
    private Integer counter;

    public NameCounter() {
    }

    public NameCounter(String name) {
        this.name = name;
        this.counter = 1;
    }

    @Id
    @Column(name = "name", columnDefinition = "VARCHAR(100)")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "counter", columnDefinition = "INT")
    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
