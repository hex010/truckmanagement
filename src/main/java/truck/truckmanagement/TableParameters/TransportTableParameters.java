package truck.truckmanagement.TableParameters;

import javafx.beans.property.SimpleStringProperty;

public class TransportTableParameters {
    private SimpleStringProperty truckId = new SimpleStringProperty();
    private SimpleStringProperty truckModel = new SimpleStringProperty();
    private SimpleStringProperty truckMaxSpeed = new SimpleStringProperty();
    private SimpleStringProperty truckMileage = new SimpleStringProperty();
    private SimpleStringProperty truckManufacturedDate = new SimpleStringProperty();
    private SimpleStringProperty truckColor = new SimpleStringProperty();
    private SimpleStringProperty truckFuelType = new SimpleStringProperty();
    private SimpleStringProperty truckTransmissionType = new SimpleStringProperty();

    public TransportTableParameters(){

    }

    public TransportTableParameters(SimpleStringProperty truckId, SimpleStringProperty truckModel, SimpleStringProperty truckMaxSpeed, SimpleStringProperty truckMileage, SimpleStringProperty truckManufacturedDate, SimpleStringProperty truckColor, SimpleStringProperty truckFuelType, SimpleStringProperty truckTransmissionType) {
        this.truckId = truckId;
        this.truckModel = truckModel;
        this.truckMaxSpeed = truckMaxSpeed;
        this.truckMileage = truckMileage;
        this.truckManufacturedDate = truckManufacturedDate;
        this.truckColor = truckColor;
        this.truckFuelType = truckFuelType;
        this.truckTransmissionType = truckTransmissionType;
    }

    public void setTruckId(String truckId) {
        this.truckId.set(truckId);
    }

    public void setTruckModel(String truckModel) {
        this.truckModel.set(truckModel);
    }

    public void setTruckMaxSpeed(String truckMaxSpeed) {
        this.truckMaxSpeed.set(truckMaxSpeed);
    }

    public void setTruckMileage(String truckMileage) {
        this.truckMileage.set(truckMileage);
    }

    public void setTruckManufacturedDate(String truckManufacturedDate) {
        this.truckManufacturedDate.set(truckManufacturedDate);
    }

    public void setTruckColor(String truckColor) {
        this.truckColor.set(truckColor);
    }

    public void setTruckFuelType(String truckFuelType) {
        this.truckFuelType.set(truckFuelType);
    }

    public void setTruckTransmissionType(String truckTransmissionType) {
        this.truckTransmissionType.set(truckTransmissionType);
    }


    public String getTruckId() {
        return truckId.get();
    }

    public SimpleStringProperty truckIdProperty() {
        return truckId;
    }

    public String getTruckModel() {
        return truckModel.get();
    }

    public SimpleStringProperty truckModelProperty() {
        return truckModel;
    }

    public String getTruckMaxSpeed() {
        return truckMaxSpeed.get();
    }

    public SimpleStringProperty truckMaxSpeedProperty() {
        return truckMaxSpeed;
    }

    public String getTruckMileage() {
        return truckMileage.get();
    }

    public SimpleStringProperty truckMileageProperty() {
        return truckMileage;
    }

    public String getTruckManufacturedDate() {
        return truckManufacturedDate.get();
    }

    public SimpleStringProperty truckManufacturedDateProperty() {
        return truckManufacturedDate;
    }

    public String getTruckColor() {
        return truckColor.get();
    }

    public SimpleStringProperty truckColorProperty() {
        return truckColor;
    }

    public String getTruckFuelType() {
        return truckFuelType.get();
    }

    public SimpleStringProperty truckFuelTypeProperty() {
        return truckFuelType;
    }

    public String getTruckTransmissionType() {
        return truckTransmissionType.get();
    }

    public SimpleStringProperty truckTransmissionTypeProperty() {
        return truckTransmissionType;
    }

}
