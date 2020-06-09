package wizut.tpsi.furka4u.entity;

import javax.persistence.*;

@Entity
@Table(name = "car_model")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id")
    @ManyToOne
    private CarManufacturer manufacturer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarManufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(CarManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
