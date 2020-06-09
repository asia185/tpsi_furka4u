package wizut.tpsi.furka4u.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(min = 5, max = 255)
    @Column(name = "title")
    @NotNull
    private String title;

    @JoinColumn(name = "model_id", referencedColumnName = "id")
    @ManyToOne
    @NotNull
    private CarModel model;

    @JoinColumn(name = "body_style_id", referencedColumnName = "id")
    @ManyToOne
    @NotNull
    private BodyStyle bodyStyle;

    @JoinColumn(name = "fuel_type_id", referencedColumnName = "id")
    @ManyToOne
    @NotNull
    private FuelType fuelType;

    @Column(name = "year")
    @Min(1990)
    @NotNull
    private Integer year;

    @Column(name = "mileage")
    @Min(0)
    @NotNull
    private Integer mileage;

    @Column(name = "engine_size")
    @Min(0)
    @NotNull
    private BigDecimal engineSize;

    @Column(name = "engine_power")
    @Min(0)
    @NotNull
    private Integer enginePower;

    @Column(name = "doors")
    @Min(1)
    @Max(5)
    @NotNull
    private Integer doors;

    @Size(min = 3, max = 30)
    @Column(name = "colour")
    @NotNull
    private String color;

    @Lob
    @Size(min = 5, max = 65535)
    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "price")
    @Min(0)
    @NotNull
    private Long price;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAtPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CarModel getModel() {
        return model;
    }

    public void setModel(CarModel model) {
        this.model = model;
    }

    public BodyStyle getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(BodyStyle bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(BigDecimal engineSize) {
        this.engineSize = engineSize;
    }

    public Integer getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(Integer enginePower) {
        this.enginePower = enginePower;
    }

    public Integer getDoors() {
        return doors;
    }

    public void setDoors(Integer doors) {
        this.doors = doors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
